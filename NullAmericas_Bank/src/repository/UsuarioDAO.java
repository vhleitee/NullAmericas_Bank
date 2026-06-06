package repository;

import java.sql.*;

import model.BD;
import model.Cliente;
import model.OperacaoBD;
import model.Usuario;
import enums.TipoOperacaoBD;

public class UsuarioDAO implements OperacaoBD {
	private BD bd;
	private Usuario usuario;

	private PreparedStatement statement;
	private ResultSet resultSet;

	private String sql, msg;

	public UsuarioDAO() {
		bd = null;
		usuario = null;
	}

	public void setBd(BD bd) {
		this.bd = bd;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean localizar() {
		sql = "SELECT * FROM users where idUser = ?";
		try {
			statement = bd.connection.prepareStatement(sql);
			statement.setInt(1, usuario.getId());

			resultSet = statement.executeQuery();
			resultSet.next();

			return true;
		} catch (SQLException erro) {
			return false;
		}
	}

	public boolean localizarFuncionario() {
		//Implementar
		return false;
	}
	
	private Cliente localizerCliente(int idUsuario){
	    String sql = "SELECT * FROM funcionario WHERE id = ?";
	    try (PreparedStatement stmt = bd.connection.prepareStatement(sql)) {
	        stmt.setInt(1, idUsuario);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	            	
	            	//IMPLEMENTAR 
	                int id = rs.getInt("id");
	                String nome = rs.getString("nome");
	                LocalDate data = rs.getDate("data_cadastro").toLocalDate();

	                // Endereco pode precisar de outra busca ou ser criado aqui
	                Endereco endereco = null; 

	                // Instancia a classe concreta (Funcionario estende Usuario)
	                return new Funcionario(id, nome, data, endereco, rs.getString("cargo"));
	            }
	        }
	    }
	    return null;
	}
	
	public String atualizar(TipoOperacaoBD operacao) {
		msg = "Operação realizada com sucesso!";
		try {
			if (operacao == TipoOperacaoBD.INCLUSAO) {
				sql = "INSERT INTO `users` (`nome`, `dataCadastro`, `pais`, `estado`, `cidade`, `rua`, `numero`, `complemento`, `cep`) VALUES \r\n"
						+ "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				statement = bd.connection.prepareStatement(sql);

				statement.setString(1, usuario.getNome());
				statement.setDate(2, java.sql.Date.valueOf(usuario.getData()));
				// Pegar referencia linha 61 statement.setDate(2,
				// java.sql.Date.valueOf(usuario.getData()));
				//////////////////////////////////////
				statement.setString(3, usuario.getEndereco().getPais());
				statement.setString(4, usuario.getEndereco().getEstado());
				statement.setString(5, usuario.getEndereco().getCidade());
				statement.setString(6, usuario.getEndereco().getRua());
				statement.setInt(7, usuario.getEndereco().getNumero());
				statement.setString(8, usuario.getEndereco().getComplemento());
				statement.setString(9, usuario.getEndereco().getCep());
			} else if (operacao == TipoOperacaoBD.ALTERACAO) {
				sql = "UPDATE `users` SET `nome` = ?, `dataCadastro` = ?, `pais` = ?, `estado` = ?, "
						+ "`cidade` = ?, `rua` = ?, `numero` = ?, `complemento` = ?, `cep` = ? "
						+ "WHERE `idUsers` = ?";
				statement = bd.connection.prepareStatement(sql);

				statement.setString(1, usuario.getNome());
				statement.setDate(2, java.sql.Date.valueOf(usuario.getData()));
				statement.setString(3, usuario.getEndereco().getPais());
				statement.setString(4, usuario.getEndereco().getEstado());
				statement.setString(5, usuario.getEndereco().getCidade());
				statement.setString(6, usuario.getEndereco().getRua());
				statement.setInt(7, usuario.getEndereco().getNumero());
				statement.setString(8, usuario.getEndereco().getComplemento());
				statement.setString(9, usuario.getEndereco().getCep());
				// Verificar o funcionamento do statment abaixo.
				statement.setInt(10, usuario.getId());

			} else if (operacao == TipoOperacaoBD.EXCLUSAO) {
				sql = "DELETE FROM `users` WHERE `idUsers` = ?";
				statement = bd.connection.prepareStatement(sql);

				statement.setInt(1, usuario.getId());

			}

			if (statement.executeUpdate() == 0) {
				msg = "Falha na operação!";
			}
		} catch (SQLException erro) {
			msg = "Falha na operação - " + erro.toString();
		}

		return msg;
	}
}