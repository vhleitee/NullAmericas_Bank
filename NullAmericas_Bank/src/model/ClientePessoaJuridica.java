package model;

public class ClientePessoaJuridica extends Cliente {
	private String cnpj;
	
	public ClientePessoaJuridica(String cnpj){
		super();
		this.cnpj = cnpj;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	
}
