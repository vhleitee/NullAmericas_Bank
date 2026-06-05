package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Cliente {
	List<Conta> contas = new ArrayList<>();
	

	public Cliente() {
		
	}
	
	public Cliente(List<Conta> contas) {
		this.setContas(contas);
	}

	public List<Conta> getContas() {
		return contas;
	}

	public void setContas(List<Conta> contas) {
		this.contas = contas;
	}
	
}