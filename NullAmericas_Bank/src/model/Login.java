package model;
import enums.LoginStatus;

public class Login {
	private int id;
	private String codigo;
	private String senha;
	private LoginStatus loginStatus;
	
	public Login() {
		this.id = -1;
		this.codigo = null;
		this.senha = null;
		this.loginStatus = LoginStatus.NAO_VALIDADO;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}

	public LoginStatus validarLogin(String codigo, String senha) {
		if( this.codigo.equals(codigo) && this.senha.equals(senha)) {
			return this.loginStatus;
		}
		else {
			return LoginStatus.NAO_VALIDADO;
		}
	}
}
