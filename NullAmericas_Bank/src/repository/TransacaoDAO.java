package repository;
import java.sql.*;

import model.BD;
import model.Transacao;
import enums.TipoOperacaoBD;

public class TransacaoDAO {
	private BD bd;
    private Transacao transacao;

    private PreparedStatement statement;
    private ResultSet resultSet;

    private String sql, msg;

    public TransacaoDAO() {
        bd = null;
        transacao = null;
    }
    
    public void setBd(BD bd) {
		this.bd = bd;
	}

	public Transacao gettransacao() {
		return transacao;
	}

	public void settransacao(Transacao transacao) {
		this.transacao = transacao;
	}
	
	public boolean localizar() {
        sql = "SELECT * FROM transacao where codigo = ?";
        try {
            statement = bd.connection.prepareStatement(sql);
            /*
            statement.setString(1, transacao.getCodigo());

            resultSet = statement.executeQuery();
            resultSet.next();
            
            transacao.setId( resultSet.getInt(1) );
            transacao.setCodigo( resultSet.getString(2) );
            transacao.setSenha( resultSet.getString(3) );
            
            String statusString = resultSet.getString(4);
            transacao.settransacaoStatus(transacaoStatus.valueOf(statusString));

            */
            return true;
        }
        catch (SQLException erro) {
            return false;
        }
    }

    public String atualizar(TipoOperacaoBD operacao) {
        msg = "Operação realizada com sucesso!";
        try {
            if (operacao == TipoOperacaoBD.INCLUSAO) {
                sql = "INSERT into transacao(codigo,senha) values (?,?)";
                statement = bd.connection.prepareStatement(sql);

                //statement.setString(1, transacao.getCodigo());
                //statement.setString(2, transacao.getSenha());
            }
            else if (operacao == TipoOperacaoBD.ALTERACAO) {
            	sql = "UPDATE transacao SET senha = ? WHERE codigo = ?";
                statement = bd.connection.prepareStatement(sql);

                //statement.setString(1, transacao.getSenha());
                //statement.setString(2, transacao.getCodigo());
            }
            else if (operacao == TipoOperacaoBD.EXCLUSAO) {
                sql = "DELETE FROM transacao WHERE codigo = ?";
                statement = bd.connection.prepareStatement(sql);

                //statement.setString(1, transacao.getCodigo());
            }

            if (statement.executeUpdate() == 0) {
                msg = "Falha na operação!";
            }
        }
        catch (SQLException erro) {
            msg = "Falha na operação - " + erro.toString();
        }

        return msg;
    }
}
