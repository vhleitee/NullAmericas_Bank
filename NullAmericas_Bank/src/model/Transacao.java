package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import enums.TipoTransacao;

public class Transacao {

	private int id;
	private int idConta;
	private int idContaCorrespondente;
	private LocalDateTime dataTransacao;
	private double valor;
	private TipoTransacao tipoTransacao;
	
	private static final DateTimeFormatter FORMATTER_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	public Transacao() {
		this.id = -1;
		this.idConta = -1;
		this.idContaCorrespondente = -1;
		this.dataTransacao = null;
		this.valor = 0;
		this.tipoTransacao = null;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getIdConta() {
		return idConta;
	}

	public void setIdConta(int idConta) {
		this.idConta = idConta;
	}

	public int getIdContaCorrespondente() {
		return idContaCorrespondente;
	}

	public void setIdContaCorrespondente(int idContaCorrespondente) {
		this.idContaCorrespondente = idContaCorrespondente;
	}

	public LocalDateTime getData() {
		return dataTransacao;
	}

	public String getDataFormatadaBR() {
		if (this.dataTransacao == null) {
			return "Data não informada";
		}
		return this.dataTransacao.format(FORMATTER_BR);
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
	
	@Override
	public String toString() {
	    return String.format(
	        "Código: %d | %s R$ %.2f às %s",
	        this.getId(),
	        this.getTipoTransacao().getDescricao(),
	        this.getValor(),
	        this.getDataFormatadaBR()
	    );
	}
}