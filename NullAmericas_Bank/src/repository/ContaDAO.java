package repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import enums.TipoOperacaoBD;
import model.BD;
import model.Cliente;
import model.Conta;
import model.Usuario;

public class ContaDAO {

    private BD bd;
    private Conta conta;
    private Usuario usuario;
    
    private String sql, msg;

    private PreparedStatement statement;
    private ResultSet resultSet;

    public ContaDAO() {
        this.bd = null;
        this.conta = null;
    }

    public void setBd(BD bd) {
        this.bd = bd;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public List<Conta> localizarContas(Usuario usuarioInstanciado) {

        sql = """
            SELECT
                c.*,
                COALESCE(SUM(t.valor), 0) AS saldo
            FROM conta c
            LEFT JOIN transacao t
                ON t.idConta = c.id
            WHERE c.idUser = ?
            GROUP BY c.id
            """;

        List<Conta> listaContas = new ArrayList<>();
        this.usuario = usuarioInstanciado;

        try {
            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, this.usuario.getId());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Conta contaLocal = new Conta();

                contaLocal.setId(resultSet.getInt("id"));
                
                contaLocal.setSaldo(resultSet.getDouble("saldo"));

                if (this.usuario instanceof Cliente) {
                    contaLocal.setCliente((Cliente) this.usuario);
                }

                if (resultSet.getTimestamp("dataCadastro") != null) {
                    contaLocal.setDataCadastro(
                        resultSet.getTimestamp("dataCadastro")
                                 .toLocalDateTime()
                                 .toLocalDate()
                    );
                }

                listaContas.add(contaLocal);
            }

            return listaContas;

        } catch (SQLException erro) {
            erro.printStackTrace();
            return null;
        }
    }

    public List<Conta> localizarContaPorDocumento(String documento) {
    	
    	sql = """
				SELECT 
					c.id AS conta_id,
					c.dataCadastro AS conta_data,
					u.id AS user_id,
					u.nome,
					u.documento,
					u.cidade,
					u.estado,
					u.tipoDocumento,
					l.tipoUsuario              
				FROM nullamericas_bank.conta c
				LEFT JOIN nullamericas_bank.users u ON c.idUser = u.id
				LEFT JOIN nullamericas_bank.login l ON u.id = l.idUser 
				WHERE u.documento LIKE ? and l.tipoUsuario = "USUARIO";
    		""";
    	
    	List<Conta> listaContas = new ArrayList<>();
    	
		try {
			statement = bd.connection.prepareStatement(sql);
			statement.setString(1, "%" + documento + "%");

			resultSet = statement.executeQuery();

			while (resultSet.next()) {
	            
				Cliente usuarioAtual = new Cliente();
	            usuarioAtual.setId(resultSet.getInt("user_id"));
	            usuarioAtual.setNome(resultSet.getString("nome"));
	            usuarioAtual.setDocumento(resultSet.getString("documento"));
	            usuarioAtual.setTipoDocumento(resultSet.getString("tipoDocumento"));

	            Conta contaAtual = new Conta();
	            contaAtual.setCliente(usuarioAtual);
	            contaAtual.setId(resultSet.getInt("conta_id"));
	            
	            listaContas.add(contaAtual);
	            
	        }
			
			if (listaContas.isEmpty()) {
	            return null; 
	        }

			return listaContas;

		} catch (SQLException erro) {
			erro.printStackTrace();
			return null;
		}
    	
    }
    
    public String atualizar(TipoOperacaoBD operacao) {
        msg = "Operação realizada com sucesso!";
        
        try {
            if (operacao == TipoOperacaoBD.INCLUSAO) {
                
                sql = "INSERT INTO `nullamericas_bank`.`conta` (`idUser`) VALUES (?)";
                  
                statement = bd.connection.prepareStatement(sql);
                statement.setInt(1, conta.getCliente().getId());
                
            } else if (operacao == TipoOperacaoBD.EXCLUSAO) {
                
                sql = "DELETE FROM `nullamericas_bank`.`conta` WHERE `id` = ?";
                
                statement = bd.connection.prepareStatement(sql);
                statement.setInt(1, conta.getId()); 
                
            } else if (operacao == TipoOperacaoBD.ALTERACAO) {
            
                sql = "UPDATE `nullamericas_bank`.`conta` SET `idUser` = ? WHERE `id` = ?";
                
                statement = bd.connection.prepareStatement(sql);
                statement.setInt(1, conta.getCliente().getId());
                statement.setInt(2, conta.getId()); 
                
            }
            
            if (statement.executeUpdate() == 0) {
                msg = "Falha na operação!";
            }
            
        } catch (SQLException erro) {
            msg = "Falha na operação - " + erro.toString();
        }

        return msg;
    }
    

}