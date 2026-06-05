package model;

import java.util.List;

import enums.TipoTransacao;

public class ContaSalario extends Conta {

	public ContaSalario(int id, double saldo, Cliente cliente) {
		super(id, saldo, cliente);
	}

	//Gerar os metodos após a criação do BD.
	
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

}
