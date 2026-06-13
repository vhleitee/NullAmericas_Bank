package repository;

import enums.TipoOperacaoBD;
import enums.TipoTransacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.BD;
import model.OperacaoBD;
import model.Transacao;
import model.Conta;

public class TransacaoDAO implements OperacaoBD {
	private BD bd;
	private Conta conta;

	private PreparedStatement statement;
	private ResultSet resultSet;

	private String sql, msg;

	public TransacaoDAO() {
		bd = null;
		conta = null;
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

	public List<Transacao> localizarTransacoes() {
		String sql = "SELECT * FROM transacao WHERE idConta = ?";
		List<Transacao> listaTransacoes = new ArrayList<>();

		try {
			statement = bd.connection.prepareStatement(sql);
			statement.setInt(1, this.getConta().getId());

			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Transacao atual = new Transacao();
				atual.setData(resultSet.getTimestamp("dataTransacao").toLocalDateTime());
				atual.setId(resultSet.getInt("id"));
				atual.setIdConta(resultSet.getInt("idConta"));
				atual.setIdContaCorrespondente(resultSet.getInt("idContaCorrespondente"));
				atual.setTipoTransacao(TipoTransacao.valueOf(resultSet.getString("tipoTransacao")));
				atual.setValor(resultSet.getDouble("valor"));
				listaTransacoes.add(atual);
			}

			return listaTransacoes;

		} catch (SQLException erro) {
			erro.printStackTrace();
			return null;
		}
	}

	/////////// IMPLEMENTAR
	public boolean localizar() {
		return false;
	}

	public String atualizar(TipoOperacaoBD operacao) {
		return "teste";
	}
	//////////////////////////
}