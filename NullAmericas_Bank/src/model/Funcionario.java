package model;

import java.time.LocalDate;

public class Funcionario extends Usuario {
	private String funcao;
	private String cpf;

	public Funcionario(int id, String nome, LocalDate data, Endereco endereco, String funcao, String cpf) {
		super(id, nome, data, endereco);
		this.funcao = funcao;
		this.cpf = cpf;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

}