package model;
import java.time.LocalDate;

public abstract class Usuario {
	private int id;
	private String nome;
	private LocalDate data;
	private Endereco endereco;
	
	public Usuario(int id, String nome, LocalDate data, Endereco endereco) {
		this.id = id;
		this.nome = nome;
		this.data = data;
		this.endereco = endereco;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public Endereco getEndereco() {
		return endereco;
	}
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}	
}