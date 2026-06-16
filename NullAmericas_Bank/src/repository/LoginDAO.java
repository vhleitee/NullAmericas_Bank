package repository;
import enums.LoginStatus;
import enums.TipoOperacaoBD;
import java.sql.*;
import model.BD;
import model.Cliente;
import model.Funcionario;
import model.Login;
import model.OperacaoBD;
import model.Usuario;

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
	        
	        if (!resultSet.next()) {
	            return false; 
	        }
	        
	        login.setId(resultSet.getInt(1));
	        login.setCodigo(resultSet.getString(2));
	        login.setSenha(resultSet.getString(3));
	        
	        String statusString = resultSet.getString(4);
	        login.setLoginStatus(LoginStatus.valueOf(statusString));
	        
	        int idUser = resultSet.getInt("idUser");
	        login.setIdUsuario(idUser);
	        
	        Usuario usuario = null;
	        
	        if (login.getLoginStatus() == LoginStatus.FUNCIONARIO) {
	        	usuario = new Funcionario();
	        } else if (login.getLoginStatus() == LoginStatus.USUARIO) {
	            usuario = new Cliente();
	        }
	        
	        if (usuario != null) {
	            UsuarioDAO usuarioDAO = new UsuarioDAO();
	            usuarioDAO.setBd(this.bd);
	            usuario = usuarioDAO.localizarId(idUser, usuario);
	            
	            if (usuario != null) {
	                login.setUsuario(usuario);
	                return true;
	            }
	        }
	        
	        return false;
	    }
	    catch (SQLException erro) {
	        erro.printStackTrace();
	        return false;
	    }
	}
	
    public String atualizar(TipoOperacaoBD operacao) {
        msg = "Operação realizada com sucesso!";
        try {
        	if (operacao == TipoOperacaoBD.INCLUSAO) {
        	    if (login == null || login.getUsuario() == null) {
        	        throw new IllegalArgumentException("Não é possível incluir o login: O usuário associado não pode ser nulo.");
        	    }
        	    sql = "INSERT into login(codigo,senha,tipoUsuario, idUser) values (?,?,?,?)";
        	    statement = bd.connection.prepareStatement(sql);

        	    statement.setString(1, login.getCodigo());
        	    statement.setString(2, login.getSenha());
        	    statement.setString(3, login.getLoginStatus().name());
        	    statement.setInt(4, login.getUsuario().getId());
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