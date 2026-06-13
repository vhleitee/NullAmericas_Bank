package model;

import java.time.LocalDate;

public class ClientePessoaJuridica extends Cliente {
	private String cnpj;
	private String documento;
	
	public ClientePessoaJuridica(){
		super();
		this.cnpj = null;
		this.documento = null;
	}
	
	public ClientePessoaJuridica(String cnpj){
		super();
		this.cnpj = cnpj;
		this.documento = null;
	}
	
	public ClientePessoaJuridica(int id, String nome, LocalDate data, Endereco endereco, String cnpj, String documento) {
		super(id, nome, data, endereco);
		this.cnpj = cnpj;
		this.documento = documento;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	public String getDocumento() {
		return documento;
	}
	
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	
}
