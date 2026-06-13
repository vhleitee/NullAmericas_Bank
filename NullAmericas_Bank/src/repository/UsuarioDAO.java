package repository;

import enums.TipoOperacaoBD;
import java.sql.*;
import model.BD;
import model.ClientePessoaFisica;
import model.ClientePessoaJuridica;
import model.Endereco;
import model.Funcionario;
import model.OperacaoBD;
import model.Usuario;

public class UsuarioDAO implements OperacaoBD {
    private BD bd;
    private Usuario usuario; // Pode ser um Funcionario ou ClientePessoaFisica devido ao Polimorfismo

    private PreparedStatement statement;
    private ResultSet resultSet;

    private String sql, msg;

    public UsuarioDAO() {
        bd = null;
        usuario = null;
    }

    public void setBd(BD bd) {
        this.bd = bd;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean localizar() {
        return false;
    }
    
    // Mudamos o método para receber o objeto usuário já instanciado (Polimorfismo em ação!)
    public Usuario localizarId(int id, Usuario usuarioInstanciado) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        // Evita o NullPointerException guardando a referência que veio por parâmetro
        this.usuario = usuarioInstanciado;
        
        if (this.usuario == null) {
            // Caso esqueçam de passar o objeto instanciado, não quebra o código
            return null; 
        }
        
        try {
            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                this.usuario.setId(resultSet.getInt("id"));
                this.usuario.setNome(resultSet.getString("nome"));
                this.usuario.setDataCadastro(resultSet.getTimestamp("dataCadastro").toLocalDateTime().toLocalDate());
                
                Endereco endereco = new Endereco();
                endereco.setPais(resultSet.getString("pais"));
                endereco.setEstado(resultSet.getString("estado"));
                endereco.setCidade(resultSet.getString("cidade"));
                endereco.setRua(resultSet.getString("rua"));
                endereco.setNumero(resultSet.getInt("numero"));
                endereco.setComplemento(resultSet.getString("complemento"));
                endereco.setCep(resultSet.getString("cep"));
                
                this.usuario.setEndereco(endereco);
                
                // Se é ClientePessoaFisica, adiciona o documento
                if (this.usuario instanceof ClientePessoaFisica) {
                    ClientePessoaFisica cpf = (ClientePessoaFisica) this.usuario;
                    cpf.setDocumento(resultSet.getString("documento"));
                }
                // Se é ClientePessoaJuridica, adiciona o documento
                else if (this.usuario instanceof ClientePessoaJuridica) {
                    ClientePessoaJuridica cnpj = (ClientePessoaJuridica) this.usuario;
                    cnpj.setDocumento(resultSet.getString("documento"));
                }
                // Se é Funcionario, adiciona cpf e funcao (se houver)
                else if (this.usuario instanceof Funcionario) {
                    Funcionario func = (Funcionario) this.usuario;
                    func.setCpf(resultSet.getString("documento"));
                }
                
                return this.usuario;
            } else {
                return null; // Retorna null se não achou ninguém no banco com esse ID
            }

        } catch (SQLException erro) {
            erro.printStackTrace(); 
            return null;
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public String atualizar(TipoOperacaoBD operacao) {
        return "teste";
    }
}