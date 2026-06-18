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
import model.Endereco;
import model.Funcionario;
import model.Login;
import model.Usuario;
import repository.LoginDAO;
import repository.UsuarioDAO;

public class GuiCliente extends JPanel implements Refreshable {

	private static final long serialVersionUID = 1L;

	private JTextField cTxtId, cTxtNome, cTxtDocumento, cTxtCep, cTxtPais, cTxtEstado, cTxtCidade, cTxtRua, cTxtNumero, cTxtComplemento;
	private JComboBox<String> cCbTipoDocumento;
	private JTable cTabela;
	private DefaultTableModel cModelo;
	private JTextField cTxtBusca;
	private JButton cBtnBuscar, cBtnNovo, cBtnSalvar, cBtnExcluir, cBtnLimpar;

	public GuiCliente() {
		inicializarComponentes();
		definirEventos();
		refreshData();
	}

	private void inicializarComponentes() {
		setLayout(new BorderLayout(10, 10));

		JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		painelTopo.add(new JLabel("Buscar por Nome/Documento:"));
		cTxtBusca = new JTextField(20);
		painelTopo.add(cTxtBusca);
		cBtnBuscar = new JButton("Buscar");
		painelTopo.add(cBtnBuscar);

		cModelo = new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID", "Nome", "Documento", "Tipo Doc", "CEP", "Cidade", "Estado", "Rua", "Nº" }
		) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		cTabela = new JTable(cModelo);
		cTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollTabela = new JScrollPane(cTabela);

		JPanel painelForm = new JPanel(new GridBagLayout());
		painelForm.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0; gbc.gridy = 0;
		painelForm.add(new JLabel("ID:"), gbc.clone());
		gbc.gridx = 1;
		cTxtId = new JTextField(15);
		cTxtId.setEditable(false);
		painelForm.add(cTxtId, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 1;
		painelForm.add(new JLabel("Nome Completo:"), gbc.clone());
		gbc.gridx = 1;
		cTxtNome = new JTextField(15);
		painelForm.add(cTxtNome, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 2;
		painelForm.add(new JLabel("Tipo Documento:"), gbc.clone());
		gbc.gridx = 1;
		cCbTipoDocumento = new JComboBox<>(new String[] { "CPF", "CNPJ" });
		painelForm.add(cCbTipoDocumento, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 3;
		painelForm.add(new JLabel("Documento:"), gbc.clone());
		gbc.gridx = 1;
		cTxtDocumento = new JTextField(15);
		painelForm.add(cTxtDocumento, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 4;
		painelForm.add(new JLabel("CEP:"), gbc.clone());
		gbc.gridx = 1;
		cTxtCep = new JTextField(15);
		painelForm.add(cTxtCep, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 5;
		painelForm.add(new JLabel("País:"), gbc.clone());
		gbc.gridx = 1;
		cTxtPais = new JTextField(15);
		painelForm.add(cTxtPais, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 6;
		painelForm.add(new JLabel("Estado:"), gbc.clone());
		gbc.gridx = 1;
		cTxtEstado = new JTextField(15);
		painelForm.add(cTxtEstado, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 7;
		painelForm.add(new JLabel("Cidade:"), gbc.clone());
		gbc.gridx = 1;
		cTxtCidade = new JTextField(15);
		painelForm.add(cTxtCidade, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 8;
		painelForm.add(new JLabel("Rua:"), gbc.clone());
		gbc.gridx = 1;
		cTxtRua = new JTextField(15);
		painelForm.add(cTxtRua, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 9;
		painelForm.add(new JLabel("Número:"), gbc.clone());
		gbc.gridx = 1;
		cTxtNumero = new JTextField(15);
		painelForm.add(cTxtNumero, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 10;
		painelForm.add(new JLabel("Complemento:"), gbc.clone());
		gbc.gridx = 1;
		cTxtComplemento = new JTextField(15);
		painelForm.add(cTxtComplemento, gbc.clone());

		JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		cBtnNovo = new JButton("Novo");
		cBtnSalvar = new JButton("Salvar");
		cBtnExcluir = new JButton("Excluir");
		cBtnLimpar = new JButton("Limpar");
		painelBotoes.add(cBtnNovo);
		painelBotoes.add(cBtnSalvar);
		painelBotoes.add(cBtnExcluir);
		painelBotoes.add(cBtnLimpar);

		JPanel painelLeste = new JPanel(new BorderLayout(5, 5));
		painelLeste.add(painelForm, BorderLayout.CENTER);
		painelLeste.add(painelBotoes, BorderLayout.SOUTH);

		add(painelTopo, BorderLayout.NORTH);
		add(scrollTabela, BorderLayout.CENTER);
		add(painelLeste, BorderLayout.EAST);
	}

	private void definirEventos() {
		cTabela.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = cTabela.getSelectedRow();
				if (selectedRow != -1) {
					cTxtId.setText(cModelo.getValueAt(selectedRow, 0).toString());
					cTxtNome.setText(cModelo.getValueAt(selectedRow, 1).toString());
					cTxtDocumento.setText(cModelo.getValueAt(selectedRow, 2).toString());
					cCbTipoDocumento.setSelectedItem(cModelo.getValueAt(selectedRow, 3).toString());
					cTxtCep.setText(cModelo.getValueAt(selectedRow, 4).toString());
					cTxtCidade.setText(cModelo.getValueAt(selectedRow, 5).toString());
					cTxtEstado.setText(cModelo.getValueAt(selectedRow, 6).toString());
					cTxtRua.setText(cModelo.getValueAt(selectedRow, 7).toString());
					cTxtNumero.setText(cModelo.getValueAt(selectedRow, 8).toString());

					Usuario u = dbLocalizarUsuarioPorDocumento(cModelo.getValueAt(selectedRow, 2).toString());
					if (u != null && u.getEndereco() != null) {
						cTxtComplemento.setText(u.getEndereco().getComplemento());
						cTxtPais.setText(u.getEndereco().getPais());
					}
				}
			}
		});

		cBtnBuscar.addActionListener(e -> atualizarTabelaClientes(cTxtBusca.getText()));

		cBtnNovo.addActionListener(e -> {
			cTxtId.setText("");
			cTxtNome.setText("");
			cTxtDocumento.setText("");
			cTxtCep.setText("");
			cTxtPais.setText("");
			cTxtEstado.setText("");
			cTxtCidade.setText("");
			cTxtRua.setText("");
			cTxtNumero.setText("");
			cTxtComplemento.setText("");
		});

		cBtnLimpar.addActionListener(e -> {
			cTxtBusca.setText("");
			cBtnNovo.doClick();
			atualizarTabelaClientes("");
		});

		cBtnSalvar.addActionListener(e -> {
			String nome = cTxtNome.getText().trim();
			String doc = cTxtDocumento.getText().trim();
			String cep = cTxtCep.getText().trim();
			String pais = cTxtPais.getText().trim();
			String estado = cTxtEstado.getText().trim();
			String cidade = cTxtCidade.getText().trim();
			String rua = cTxtRua.getText().trim();
			String numStr = cTxtNumero.getText().trim();
			String comp = cTxtComplemento.getText().trim();

			if (nome.isEmpty() || doc.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Nome e Documento são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int numero = 0;
			if (!numStr.isEmpty()) {
				try {
					numero = Integer.parseInt(numStr);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Número deve ser um valor inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			Endereco end = new Endereco();
			end.setPais(pais);
			end.setEstado(estado);
			end.setCidade(cidade);
			end.setRua(rua);
			end.setNumero(numero);
			end.setComplemento(comp);
			end.setCep(cep);

			Cliente cli = new Cliente();
			cli.setNome(nome);
			cli.setDocumento(doc);
			cli.setTipoDocumento(cCbTipoDocumento.getSelectedItem().toString());
			cli.setEndereco(end);

			String idStr = cTxtId.getText().trim();
			boolean isNew = idStr.isEmpty();

			if (!isNew) {
				cli.setId(Integer.parseInt(idStr));
			} else {
				Usuario exist = dbLocalizarUsuarioPorDocumento(doc);
				if (exist != null) {
					JOptionPane.showMessageDialog(null, "Documento já cadastrado no sistema.", "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			String msg = salvarUsuario(cli, isNew);
			JOptionPane.showMessageDialog(null, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);

			if (msg.contains("sucesso") && isNew) {
				int newId = buscarIdUsuarioPorDocumento(doc);
				if (newId != -1) {
					cli.setId(newId);
					criarLoginPadrao(cli, LoginStatus.USUARIO);
				}
			}

			atualizarTabelaClientes("");
			cBtnNovo.doClick();
		});

		cBtnExcluir.addActionListener(e -> {
			String idStr = cTxtId.getText().trim();
			if (idStr.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Selecione um cliente na tabela para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int confirm = JOptionPane.showConfirmDialog(null, "Excluir o cliente também apagará suas contas, logins e transações associadas. Confirmar?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				boolean ok = dbCascataExcluirUsuario(Integer.parseInt(idStr));
				if (ok) {
					JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Falha ao excluir cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
				}
				atualizarTabelaClientes("");
				cBtnNovo.doClick();
			}
		});
	}

	@Override
	public void refreshData() {
		atualizarTabelaClientes(cTxtBusca.getText());
	}

	private void atualizarTabelaClientes(String busca) {
		cModelo.setRowCount(0);
		List<Usuario> clientes = dbListarClientes(busca);
		for (Usuario u : clientes) {
			cModelo.addRow(new Object[] {
				u.getId(),
				u.getNome(),
				u.getDocumento(),
				u.getTipoDocumento(),
				u.getEndereco() != null ? u.getEndereco().getCep() : "",
				u.getEndereco() != null ? u.getEndereco().getCidade() : "",
				u.getEndereco() != null ? u.getEndereco().getEstado() : "",
				u.getEndereco() != null ? u.getEndereco().getRua() : "",
				u.getEndereco() != null ? u.getEndereco().getNumero() : ""
			});
		}
	}

	private List<Usuario> dbListarClientes(String search) {
		List<Usuario> list = new ArrayList<>();
		BD bd = new BD();
		if (bd.connect()) {
			String sql = "SELECT u.* FROM users u LEFT JOIN login l ON u.id = l.idUser " +
						 "WHERE (l.tipoUsuario = 'USUARIO' OR l.tipoUsuario IS NULL)";
			if (search != null && !search.trim().isEmpty()) {
				sql += " AND (u.nome LIKE ? OR u.documento LIKE ?)";
			}
			try (PreparedStatement ps = bd.connection.prepareStatement(sql)) {
				if (search != null && !search.trim().isEmpty()) {
					String pat = "%" + search.trim() + "%";
					ps.setString(1, pat);
					ps.setString(2, pat);
				}
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						Cliente c = new Cliente();
						c.setId(rs.getInt("id"));
						c.setNome(rs.getString("nome"));
						if (rs.getTimestamp("dataCadastro") != null) {
							c.setDataCadastro(rs.getTimestamp("dataCadastro").toLocalDateTime().toLocalDate());
						}
						c.setDocumento(rs.getString("documento"));
						c.setTipoDocumento(rs.getString("tipoDocumento"));
						Endereco end = new Endereco();
						end.setPais(rs.getString("pais"));
						end.setEstado(rs.getString("estado"));
						end.setCidade(rs.getString("cidade"));
						end.setRua(rs.getString("rua"));
						end.setNumero(rs.getInt("numero"));
						end.setComplemento(rs.getString("complemento"));
						end.setCep(rs.getString("cep"));
						c.setEndereco(end);
						list.add(c);
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
						Endereco end = new Endereco();
						end.setPais(rs.getString("pais"));
						end.setEstado(rs.getString("estado"));
						end.setCidade(rs.getString("cidade"));
						end.setRua(rs.getString("rua"));
						end.setNumero(rs.getInt("numero"));
						end.setComplemento(rs.getString("complemento"));
						end.setCep(rs.getString("cep"));
						u.setEndereco(end);
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

	private boolean dbCascataExcluirUsuario(int idUsuario) {
		BD bd = new BD();
		if (bd.connect()) {
			try {
				bd.connection.setAutoCommit(false);

				try (PreparedStatement ps = bd.connection.prepareStatement("DELETE FROM login WHERE idUser = ?")) {
					ps.setInt(1, idUsuario);
					ps.executeUpdate();
				}

				List<Integer> accountIds = new ArrayList<>();
				try (PreparedStatement ps = bd.connection.prepareStatement("SELECT id FROM conta WHERE idUser = ?")) {
					ps.setInt(1, idUsuario);
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							accountIds.add(rs.getInt("id"));
						}
					}
				}

				if (!accountIds.isEmpty()) {
					for (int accId : accountIds) {
						try (PreparedStatement ps = bd.connection.prepareStatement(
								"DELETE FROM transacao WHERE idConta = ? OR idContaCorrespondente = ?")) {
							ps.setInt(1, accId);
							ps.setInt(2, accId);
							ps.executeUpdate();
						}
					}
				}

				try (PreparedStatement ps = bd.connection.prepareStatement("DELETE FROM conta WHERE idUser = ?")) {
					ps.setInt(1, idUsuario);
					ps.executeUpdate();
				}

				try (PreparedStatement ps = bd.connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
					ps.setInt(1, idUsuario);
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

	private String salvarUsuario(Usuario u, boolean isNew) {
		BD bd = new BD();
		if (bd.connect()) {
			UsuarioDAO dao = new UsuarioDAO();
			dao.setBd(bd);
			dao.setUsuario(u);
			String res = dao.atualizar(isNew ? TipoOperacaoBD.INCLUSAO : TipoOperacaoBD.ALTERACAO);
			bd.close();
			return res;
		}
		return "Erro de conexão com o banco de dados.";
	}

	private int buscarIdUsuarioPorDocumento(String documento) {
		BD bd = new BD();
		if (bd.connect()) {
			String sql = "SELECT id FROM users WHERE documento = ?";
			try (PreparedStatement ps = bd.connection.prepareStatement(sql)) {
				ps.setString(1, documento);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt("id");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				bd.close();
			}
		}
		return -1;
	}

	private void criarLoginPadrao(Usuario u, LoginStatus status) {
		BD bd = new BD();
		if (bd.connect()) {
			Login login = new Login();
			login.setCodigo(u.getDocumento());
			login.setSenha("123");
			login.setLoginStatus(status);
			login.setUsuario(u);

			LoginDAO loginDao = new LoginDAO();
			loginDao.setBd(bd);
			loginDao.setLogin(login);
			loginDao.atualizar(TipoOperacaoBD.INCLUSAO);
			bd.close();
		}
	}
}
