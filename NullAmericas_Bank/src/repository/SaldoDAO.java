package repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.BD;
import model.Conta;
import model.Saldo;

public class SaldoDAO {

    private BD bd;
    private Conta conta;

    private PreparedStatement statement;
    private ResultSet resultSet;

    public SaldoDAO(Conta conta) {
        this.bd = null;
        this.conta = conta;
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

    public boolean localizar() {

        String sql = """
                SELECT COALESCE(SUM(valor), 0) AS saldo
                FROM nullamericas_bank.transacao
                WHERE idConta = ?
                """;

        if (this.conta == null) {
            return false;
        }

        try {
            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, this.conta.getId());

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double valorCalculado = resultSet.getDouble("saldo");
                
                // Cria o objeto saldo de forma isolada
                Saldo saldoObjeto = new Saldo(valorCalculado);
                
                // Aloca o objeto saldo DENTRO da conta
                this.conta.setSaldo(saldoObjeto);

                return true;
            }

            return false;

        } catch (SQLException erro) {
            erro.printStackTrace();
            return false;
        }
    }
}