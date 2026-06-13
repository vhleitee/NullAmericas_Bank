package model;
import enums.TipoTransacao;

import java.time.LocalDate;
import java.util.List;

public  class Conta {
	private int id;
	private double saldo;
	private Cliente cliente;
	private LocalDate dataCadastro;

	public Conta(){
		this.id = -1;
		this.saldo = 0;
		this.cliente = null;
		this.dataCadastro = null;
		
	}
	public Conta(int id, double saldo, Cliente cliente) {
		this.id = id;
		this.saldo = saldo;
		this.cliente = cliente;
	}
	
	public boolean sacar(double valor) {
		return false;
	}
	
	public boolean transferir(int contaID, double valor) {
		return false;
	}
	
	public List<Transacao> extrato() {
		return null;
	}
	
	public List<Transacao> extrato(TipoTransacao tipoTrasacao) {
		return null;
	}
	
	public boolean depositar(double valor) {
		return false;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	

}