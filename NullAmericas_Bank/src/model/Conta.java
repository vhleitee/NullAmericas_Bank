package model;
import enums.TipoTransacao;
import java.util.List;

public abstract class Conta {
	private int id;
	private double saldo;
	private Cliente cliente;

	public Conta(int id, double saldo, Cliente cliente) {
		this.id = id;
		this.saldo = saldo;
		this.cliente = cliente;
	}
	
	public abstract boolean sacar(double valor);
	
	public abstract boolean transferir(int contaID, double valor);
	
	public abstract List<Transacao> extrato();
	
	public abstract List<Transacao> extrato(TipoTransacao tipoTrasacao);
	
	
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

}