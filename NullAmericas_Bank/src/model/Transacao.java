package model;
import java.time.LocalDateTime;
import enums.TipoTransacao;

public class Transacao {

	private int id;
	private int idContaOrigem;
	private int idContaDestino;
	private LocalDateTime data;
	private double valor;
	private TipoTransacao tipoTransacao;
	
	public Transacao() {
		this.id = -1;
		this.idContaOrigem = -1;
		this.idContaDestino = -1;
		this.data = null;
		this.valor = 0;
		this.tipoTransacao = null;
	}
	
	public Transacao(int idContaOrigem, int idContaDestino, LocalDateTime data, double valor, TipoTransacao tipoTransacao) {
		this.id = -1;
		this.idContaOrigem = idContaOrigem;
		this.idContaDestino = idContaDestino;
		this.data = data;
		this.valor = valor;
		this.tipoTransacao = tipoTransacao;
	}
	
	public Transacao(int id, int idContaOrigem, int idContaDestino, LocalDateTime data, double valor, TipoTransacao tipoTransacao) {
		this.id = id;
		this.idContaOrigem = idContaOrigem;
		this.idContaDestino = idContaDestino;
		this.data = data;
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
		return idContaOrigem;
	}

	public void setIdContaOrigem(int idContaOrigem) {
		this.idContaOrigem = idContaOrigem;
	}

	public int getIdContaDestino() {
		return idContaDestino;
	}

	public void setIdContaDestino(int idContaDestino) {
		this.idContaDestino = idContaDestino;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
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
