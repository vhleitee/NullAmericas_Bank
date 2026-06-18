package view;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import enums.LoginStatus;
import enums.TipoOperacaoBD;
import model.BD;
import model.Cliente;
import model.Conta;
import model.Funcionario;
import model.Usuario;
import repository.ContaDAO;

public class GuiConta extends JPanel implements Refreshable {

	private static final long serialVersionUID = 1L;

	private JTextField accTxtId, accTxtDocumentoCliente;
	private JTable accTabela;
	private DefaultTableModel accModelo;
	private JTextField accTxtBusca;
	private JButton accBtnBuscar, accBtnCadastrar, accBtnExcluir, accBtnLimpar;

	public GuiConta() {
		inicializarComponentes();
		definirEventos();
		refreshData();
	}

	private void inicializarComponentes() {
		setLayout(new BorderLayout(10, 10));

		JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		painelTopo.add(new JLabel("Buscar por Conta ID/Dono Nome/Documento:"));
		accTxtBusca = new JTextField(20);
		painelTopo.add(accTxtBusca);
		accBtnBuscar = new JButton("Buscar");
		painelTopo.add(accBtnBuscar);

		accModelo = new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID Conta", "Nome do Dono", "Documento" }
		) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		accTabela = new JTable(accModelo);
		accTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollTabela = new JScrollPane(accTabela);

		JPanel painelForm = new JPanel(new GridBagLayout());
		painelForm.setBorder(BorderFactory.createTitledBorder("Gerenciar Conta"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0; gbc.gridy = 0;
		painelForm.add(new JLabel("ID Conta:"), gbc.clone());
		gbc.gridx = 1;
		accTxtId = new JTextField(15);
		accTxtId.setEditable(false);
		painelForm.add(accTxtId, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 1;
		painelForm.add(new JLabel("Documento do Cliente (CPF/CNPJ):"), gbc.clone());
		gbc.gridx = 1;
		accTxtDocumentoCliente = new JTextField(15);
		painelForm.add(accTxtDocumentoCliente, gbc.clone());

		JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		accBtnCadastrar = new JButton("Cadastrar Conta");
		accBtnExcluir = new JButton("Excluir Conta");
		accBtnLimpar = new JButton("Limpar");
		painelBotoes.add(accBtnCadastrar);
		painelBotoes.add(accBtnExcluir);
		painelBotoes.add(accBtnLimpar);

		JPanel painelLeste = new JPanel(new BorderLayout(5, 5));
		painelLeste.add(painelForm, BorderLayout.NORTH);
		painelLeste.add(painelBotoes, BorderLayout.SOUTH);

		add(painelTopo, BorderLayout.NORTH);
		add(scrollTabela, BorderLayout.CENTER);
		add(painelLeste, BorderLayout.EAST);
	}

	private void definirEventos() {
		accTabela.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = accTabela.getSelectedRow();
				if (selectedRow != -1) {
					accTxtId.setText(accModelo.getValueAt(selectedRow, 0).toString());
					accTxtDocumentoCliente.setText(accModelo.getValueAt(selectedRow, 2).toString());
				}
			}
		});

		accBtnBuscar.addActionListener(e -> atualizarTabelaContas(accTxtBusca.getText()));

		accBtnLimpar.addActionListener(e -> {
			accTxtBusca.setText("");
			accTxtId.setText("");
			accTxtDocumentoCliente.setText("");
			atualizarTabelaContas("");
		});

		accBtnCadastrar.addActionListener(e -> {
			String doc = accTxtDocumentoCliente.getText().trim();
			if (doc.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Documento do Cliente é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Usuario u = dbLocalizarUsuarioPorDocumento(doc);
			if (u == null) {
				JOptionPane.showMessageDialog(null, "Usuário não encontrado. Cadastre o cliente primeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			BD bd = new BD();
			if (bd.connect()) {
				boolean isEmployee = false;
				String sql = "SELECT tipoUsuario FROM login WHERE idUser = ?";
				try (PreparedStatement ps = bd.connection.prepareStatement(sql)) {
					ps.setInt(1, u.getId());
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next()) {
							if ("FUNCIONARIO".equals(rs.getString("tipoUsuario"))) {
								isEmployee = true;
							}
						}
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}

				if (isEmployee) {
					JOptionPane.showMessageDialog(null, "Erro: Apenas clientes podem possuir contas. Este usuário é um funcionário.", "Erro", JOptionPane.ERROR_MESSAGE);
					bd.close();
					return;
				}

				ContaDAO dao = new ContaDAO();
				dao.setBd(bd);
				Conta c = new Conta();
				c.setCliente((Cliente) u);
				dao.setConta(c);
				String res = dao.atualizar(TipoOperacaoBD.INCLUSAO);
				JOptionPane.showMessageDialog(null, res, "Informação", JOptionPane.INFORMATION_MESSAGE);
				bd.close();
			} else {
				JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
			}

			atualizarTabelaContas("");
			accTxtId.setText("");
			accTxtDocumentoCliente.setText("");
		});

		accBtnExcluir.addActionListener(e -> {
			String idStr = accTxtId.getText().trim();
			if (idStr.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Selecione uma conta na tabela para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int confirm = JOptionPane.showConfirmDialog(null, "Excluir esta conta apagará todo o histórico de transações associado. Confirmar?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				boolean ok = dbCascataExcluirConta(Integer.parseInt(idStr));
				if (ok) {
					JOptionPane.showMessageDialog(null, "Conta excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Falha ao excluir conta.", "Erro", JOptionPane.ERROR_MESSAGE);
				}
				atualizarTabelaContas("");
				accTxtId.setText("");
				accTxtDocumentoCliente.setText("");
			}
		});
	}

	@Override
	public void refreshData() {
		atualizarTabelaContas(accTxtBusca.getText());
	}

	private void atualizarTabelaContas(String busca) {
		accModelo.setRowCount(0);
		List<Object[]> contas = dbListarContas(busca);
		for (Object[] row : contas) {
			accModelo.addRow(row);
		}
	}

	private List<Object[]> dbListarContas(String search) {
		List<Object[]> list = new ArrayList<>();
		BD bd = new BD();
		if (bd.connect()) {
			String sql = "SELECT c.id, u.nome, u.documento FROM conta c INNER JOIN users u ON c.idUser = u.id";
			if (search != null && !search.trim().isEmpty()) {
				sql += " WHERE u.nome LIKE ? OR u.documento LIKE ? OR CAST(c.id AS CHAR) LIKE ?";
			}
			try (PreparedStatement ps = bd.connection.prepareStatement(sql)) {
				if (search != null && !search.trim().isEmpty()) {
					String pattern = "%" + search.trim() + "%";
					ps.setString(1, pattern);
					ps.setString(2, pattern);
					ps.setString(3, pattern);
				}
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						list.add(new Object[] {
							rs.getInt("id"),
							rs.getString("nome"),
							rs.getString("documento")
						});
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				bd.close();
			}
		}
		return list;
	}

	private Usuario dbLocalizarUsuarioPorDocumento(String documento) {
		BD bd = new BD();
		if (bd.connect()) {
			String sql = "SELECT * FROM users WHERE documento = ?";
			try (PreparedStatement ps = bd.connection.prepareStatement(sql)) {
				ps.setString(1, documento);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						LoginStatus role = obterRoleUsuario(bd, rs.getInt("id"));
						Usuario u;
						if (role == LoginStatus.FUNCIONARIO) {
							u = new Funcionario();
						} else {
							u = new Cliente();
						}
						u.setId(rs.getInt("id"));
						u.setNome(rs.getString("nome"));
						if (rs.getTimestamp("dataCadastro") != null) {
							u.setDataCadastro(rs.getTimestamp("dataCadastro").toLocalDateTime().toLocalDate());
						}
						u.setDocumento(rs.getString("documento"));
						u.setTipoDocumento(rs.getString("tipoDocumento"));
						u.setEndereco(null); // Simple loading, Address can be loaded if needed
						return u;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				bd.close();
			}
		}
		return null;
	}

	private LoginStatus obterRoleUsuario(BD bd, int idUsuario) {
		String sql = "SELECT tipoUsuario FROM login WHERE idUser = ?";
		try (PreparedStatement ps = bd.connection.prepareStatement(sql)) {
			ps.setInt(1, idUsuario);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return LoginStatus.valueOf(rs.getString("tipoUsuario"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return LoginStatus.USUARIO;
	}

	private boolean dbCascataExcluirConta(int idConta) {
		BD bd = new BD();
		if (bd.connect()) {
			try {
				bd.connection.setAutoCommit(false);

				try (PreparedStatement ps = bd.connection.prepareStatement(
						"DELETE FROM transacao WHERE idConta = ? OR idContaCorrespondente = ?")) {
					ps.setInt(1, idConta);
					ps.setInt(2, idConta);
					ps.executeUpdate();
				}

				try (PreparedStatement ps = bd.connection.prepareStatement("DELETE FROM conta WHERE id = ?")) {
					ps.setInt(1, idConta);
					ps.executeUpdate();
				}

				bd.connection.commit();
				return true;
			} catch (SQLException e) {
				try {
					bd.connection.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				e.printStackTrace();
				return false;
			} finally {
				bd.close();
			}
		}
		return false;
	}
}
