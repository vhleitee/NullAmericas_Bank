package model;
import java.time.LocalDate;

public abstract class Usuario {
	private int id;
	private String nome;
	private LocalDate dataCadastro;
	private Endereco endereco;
	private String documento;
	private String tipoDocumento;
	
	public Usuario() {
		this.id = -1;
		this.nome = null;
		this.dataCadastro = null;
		this.endereco = null;
	}
	
	public Usuario(int id, String nome, LocalDate data, Endereco endereco) {
		this.id = id;
		this.nome = nome;
		this.dataCadastro = data;
		this.endereco = endereco;
	}
	
	public String toString() {
		String texto = this.getId() + " - " + this.getNome() + " - " + this.getDataCadastro() +
					   " - " + this.getDocumento() + " - " + this.getTipoDocumento();
		return texto;
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
	
	public LocalDate getDataCadastro() {
		return dataCadastro;
	}
	
	public void setDataCadastro(LocalDate data) {
		this.dataCadastro = data;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}	
}