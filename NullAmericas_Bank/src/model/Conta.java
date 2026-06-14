package model;

import enums.TipoTransacao;

import java.time.LocalDate;
import java.util.ArrayList; // Adicionado
import java.util.List;

public class Conta {

    private int id;
    private Saldo saldo; 
    private Cliente cliente;
    private LocalDate dataCadastro;
    private List<Transacao> transacoes;

    public Conta() {
        this.id = -1;
        this.saldo = new Saldo(0.0);
        this.cliente = null;
        this.dataCadastro = null;
        this.transacoes = new ArrayList<>();
    }

    public Conta(int id, double saldo, Cliente cliente) {
        this.id = id;
        this.saldo = new Saldo(saldo);
        this.cliente = cliente;
        this.dataCadastro = null;
        this.transacoes = new ArrayList<>();
    }

    @Override
    public String toString() {
	    return String.format(
		        "Conta: %d (Saldo: R$ %.2f )",
		        this.getId(),
		        this.getSaldo().getSaldo()
		    );
        
    }

    public boolean sacar(double valor) {
        return false;
    }

    public boolean transferir(int contaID, double valor) {
        return false;
    }

    // Retorna a lista de transações que pertence a ESTA conta
    public List<Transacao> extrato() {
        return this.transacoes;
    }

    public List<Transacao> extrato(TipoTransacao tipoTransacao) {
        if (this.transacoes == null) return null;
        // Filtra a lista pelo tipo se necessário no futuro
        return this.transacoes.stream()
                .filter(t -> t.getTipoTransacao() == tipoTransacao)
                .toList();
    }

    public boolean depositar(double valor) {
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        }
    }

    public Saldo getSaldo() {
        return saldo;
    }

    public void setSaldo(Saldo saldo) {
        this.saldo = saldo;
    }

    public void setSaldo(double valorSaldo) {
        if (this.saldo == null) {
            this.saldo = new Saldo(valorSaldo);
        } else {
            this.saldo.setSaldo(valorSaldo);
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    // Métodos Getter e Setter para a lista de transações
    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }
}