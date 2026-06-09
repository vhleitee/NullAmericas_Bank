package model;

public class ClientePj extends Cliente {
	private String cnpj;
	
	public ClientePj(String cnpj){
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
