package model;
import java.time.LocalDateTime;
import enums.TipoTransacao;

public class Transacao {

	private int id;
	private int idConta;
	private int idContaCorrespondente;
	private LocalDateTime dataTransacao;
	private double valor;
	private TipoTransacao tipoTransacao;
	
	public Transacao() {
		this.id = -1;
		this.idConta = -1;
		this.idContaCorrespondente = -1;
		this.dataTransacao = null;
		this.valor = 0;
		this.tipoTransacao = null;
	}
	
	public Transacao(int idContaOrigem, int idContaDestino, LocalDateTime data, double valor, TipoTransacao tipoTransacao) {
		this.id = -1;
		this.idConta = idContaOrigem;
		this.idContaCorrespondente = idContaDestino;
		this.dataTransacao = data;
		this.valor = valor;
		this.tipoTransacao = tipoTransacao;
	}
	
	public Transacao(int id, int idContaOrigem, int idContaDestino, LocalDateTime data, double valor, TipoTransacao tipoTransacao) {
		this.id = id;
		this.idConta = idContaOrigem;
		this.idContaCorrespondente = idContaDestino;
		this.dataTransacao = data;
		this.valor = valor;
		this.tipoTransacao = tipoTransacao;
	}
	
	public boolean salvarTransacao() {
		
		//Implementar o salvamento usando o TransacaoDAO
		
		return true;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getIdContaOrigem() {
		return idConta;
	}

	public void setIdContaOrigem(int idContaOrigem) {
		this.idConta = idContaOrigem;
	}

	public int getIdContaDestino() {
		return idContaCorrespondente;
	}

	public void setIdContaDestino(int idContaDestino) {
		this.idContaCorrespondente = idContaDestino;
	}

	public LocalDateTime getData() {
		return dataTransacao;
	}

	public void setData(LocalDateTime data) {
		this.dataTransacao = data;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public TipoTransacao getTipoTransacao() {
		return tipoTransacao;
	}

	public void setTipoTransacao(TipoTransacao tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}
}
