package model;

import java.time.LocalDate;

public class ClientePessoaFisica extends Cliente{
	private String cpf;
	private String documento;
	
	public ClientePessoaFisica() {
		super();
		this.cpf = null;
		this.documento = null;
	}
	
	public ClientePessoaFisica(String cpf) {
		super();
		this.cpf = cpf;
		this.documento = null;
	}
	
	public ClientePessoaFisica(int id, String nome, LocalDate data, Endereco endereco, String cpf, String documento) {
		super(id, nome, data, endereco);
		this.cpf = cpf;
		this.documento = documento;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getDocumento() {
		return documento;
	}
	
	public void setDocumento(String documento) {
		this.documento = documento;
	}

}