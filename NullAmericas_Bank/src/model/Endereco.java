package model;

public class Endereco {
	private String pais;
	private String estado;
	private String cidade;
	private String rua;
	private int numero;
	private String complemento;
	private String cep;

	public Endereco() {
		this.pais = null;
		this.estado = null;
		this.cidade = null;
		this.rua = null;
		this.numero = -1;
		this.complemento = null;
		this.cep = null;
	}

	public Endereco(String pais, String estado, String cidade, String rua, int numero, String complemento, String cep) {
		super();
		this.pais = pais;
		this.estado = estado;
		this.cidade = cidade;
		this.rua = rua;
		this.numero = numero;
		this.complemento = complemento;
		this.cep = cep;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}
}