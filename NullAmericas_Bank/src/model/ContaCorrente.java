package model;

import java.util.List;

import enums.TipoTransacao;

public class ContaCorrente extends Conta{
	private double limiteChequeEspecial;	
	
	public ContaCorrente(int id, double saldo, Cliente cliente, double limiteChequeEspecial) {
        super(id, saldo, cliente); 
        this.limiteChequeEspecial = limiteChequeEspecial;
    }
	
	
	//Gerar os metodos após a criação do BD.
	
	public boolean depositar(double valor) {
		return false;
	}
	
	
	@Override
	public boolean sacar(double valor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean transferir(int contaID, double valor) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Transacao> extrato() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transacao> extrato(TipoTransacao tipoTrasacao) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public double getLimiteChequeEspecial() {
		return limiteChequeEspecial;
	}

	public void setLimiteChequeEspecial(double limiteChequeEspecial) {
		this.limiteChequeEspecial = limiteChequeEspecial;
	}

}