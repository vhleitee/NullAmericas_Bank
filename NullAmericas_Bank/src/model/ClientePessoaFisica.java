package model;

public class ClientePessoaFisica extends Cliente{
	private String cpf;
	
	public ClientePessoaFisica(String cpf) {
		super();
		this.cpf = cpf;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

}