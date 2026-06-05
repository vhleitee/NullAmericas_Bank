package repository;
import java.sql.*;

import model.BD;
import model.Login;
import model.OperacaoBD;
import enums.LoginStatus;
import enums.TipoOperacaoBD;

public class LoginDAO implements OperacaoBD{
	private BD bd;
    private Login login;

    private PreparedStatement statement;
    private ResultSet resultSet;

    private String sql, msg;

    public LoginDAO() {
        bd = null;
        login = null;
    }
    
    public void setBd(BD bd) {
		this.bd = bd;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}
	
	public boolean localizar() {
        sql = "SELECT * FROM login where codigo = ?";
        try {
            statement = bd.connection.prepareStatement(sql);
            statement.setString(1, login.getCodigo());

            resultSet = statement.executeQuery();
            resultSet.next();
            
            login.setId( resultSet.getInt(1) );
            login.setCodigo( resultSet.getString(2) );
            login.setSenha( resultSet.getString(3) );
            
            String statusString = resultSet.getString(4);
            login.setLoginStatus(LoginStatus.valueOf(statusString));
            //Pegar referencia ************************************************************************
            
            
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
                sql = "INSERT into login(codigo,senha) values (?,?)";
                statement = bd.connection.prepareStatement(sql);

                statement.setString(1, login.getCodigo());
                statement.setString(2, login.getSenha());
            }
            else if (operacao == TipoOperacaoBD.ALTERACAO) {
            	sql = "UPDATE login SET senha = ? WHERE codigo = ?";
                statement = bd.connection.prepareStatement(sql);

                statement.setString(1, login.getSenha());
                statement.setString(2, login.getCodigo());
            }
            else if (operacao == TipoOperacaoBD.EXCLUSAO) {
                sql = "DELETE FROM login WHERE codigo = ?";
                statement = bd.connection.prepareStatement(sql);

                statement.setString(1, login.getCodigo());
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