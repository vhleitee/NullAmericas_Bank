package repository;

import enums.TipoOperacaoBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.BD;
import model.Cliente;
import model.Conta;
import model.Usuario;

public class ContaDAO implements model.OperacaoBD {

    private BD bd;
    private Conta conta;
    private Usuario usuario;

    private PreparedStatement statement;
    private ResultSet resultSet;

    public ContaDAO() {
        this.bd = null;
        this.conta = null;
    }

    public void setBd(BD bd) {
        this.bd = bd;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public List<Conta> localizarContas(Usuario usuarioInstanciado) {

        String sql = """
            SELECT
                c.*,
                COALESCE(SUM(t.valor), 0) AS saldo
            FROM conta c
            LEFT JOIN transacao t
                ON t.idConta = c.id
            WHERE c.idUser = ?
            GROUP BY c.id
            """;

        List<Conta> listaContas = new ArrayList<>();
        this.usuario = usuarioInstanciado;

        try {
            statement = bd.connection.prepareStatement(sql);
            statement.setInt(1, this.usuario.getId());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Conta contaLocal = new Conta();

                contaLocal.setId(resultSet.getInt("id"));
                
                contaLocal.setSaldo(resultSet.getDouble("saldo"));

                if (this.usuario instanceof Cliente) {
                    contaLocal.setCliente((Cliente) this.usuario);
                }

                if (resultSet.getTimestamp("dataCadastro") != null) {
                    contaLocal.setDataCadastro(
                        resultSet.getTimestamp("dataCadastro")
                                 .toLocalDateTime()
                                 .toLocalDate()
                    );
                }

                listaContas.add(contaLocal);
            }

            return listaContas;

        } catch (SQLException erro) {
            erro.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean localizar() {
        return false;
    }

    @Override
    public String atualizar(TipoOperacaoBD operacao) {
        return "teste";
    }
}