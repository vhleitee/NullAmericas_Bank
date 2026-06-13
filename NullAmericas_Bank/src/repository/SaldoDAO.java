package repository;
import java.sql.*;

import model.BD;
import model.Cliente;
import model.Conta;
import model.Endereco;
import model.Funcionario;
import model.OperacaoBD;
import model.Transacao;
import enums.TipoOperacaoBD;

public class SaldoDAO{
	private BD bd;
    private Conta conta;
    private Double saldo;

    private PreparedStatement statement;
    private ResultSet resultSet;

    private String sql, msg;

    public SaldoDAO() {
        bd = null;
        this.conta = null;
    }
    
    public void setBd(BD bd) {
		this.bd = bd;
	}

	public Conta getConta() {
		return this.conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}
	
	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	
	public boolean localizar() {
        String sql = "SELECT COALESCE(SUM(valor), 0) AS saldo FROM nullamericas_bank.transacao WHERE idConta = ?";
        
        if (this.conta == null) {
            return false; 
        }
        
        try {
            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, this.conta.getId());

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
            	this.setSaldo(resultSet.getDouble("saldo"));
            	return true;
            } else {
                return false;
            }

        } catch (SQLException erro) {
            erro.printStackTrace(); 
            return false;
        }
	}
}
