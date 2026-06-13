package repository;

import enums.TipoOperacaoBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.BD;
import model.OperacaoBD;
import model.Usuario;
import model.Cliente;
import model.Conta;

public class ContaDAO implements OperacaoBD {
	private BD bd;
	private Conta conta;
	private Usuario usuario;

	private PreparedStatement statement;
	private ResultSet resultSet;

	private String sql, msg;

	public ContaDAO() {
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

	public List<Conta> localizarContas(Usuario usuarioInstanciado) {
		String sql = "SELECT * FROM conta WHERE id = ?";
		List<Conta> listaContas = new ArrayList<>();
		this.usuario = usuarioInstanciado;

		try {
			statement = bd.connection.prepareStatement(sql);
			statement.setInt(1, this.usuario.getId());

			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Conta conta = new Conta();

				conta.setId(resultSet.getInt("id"));
				conta.setSaldo(50); // Deve carregar dinamicamente 
				conta.setCliente(null);
				listaContas.add(conta);
			}

			return listaContas;

		} catch (SQLException erro) {
			erro.printStackTrace();
			return null;
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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