package model;

import java.time.LocalDate;

public abstract class Cliente extends Usuario {

	public Cliente() {
		super();
	}
	
	public Cliente(int id, String nome, LocalDate data, Endereco endereco) {
		super(id, nome, data, endereco);
	}
	
}