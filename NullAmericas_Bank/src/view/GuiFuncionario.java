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
import model.Funcionario;
import model.Endereco;
import model.Login;
import model.Usuario;
import repository.LoginDAO;
import repository.UsuarioDAO;

public class GuiFuncionario extends JPanel implements Refreshable {

	private static final long serialVersionUID = 1L;

	private JTextField fTxtId, fTxtNome, fTxtDocumento, fTxtCep, fTxtPais, fTxtEstado, fTxtCidade, fTxtRua, fTxtNumero, fTxtComplemento;
	private JComboBox<String> fCbTipoDocumento;
	private JTable fTabela;
	private DefaultTableModel fModelo;
	private JTextField fTxtBusca;
	private JButton fBtnBuscar, fBtnNovo, fBtnSalvar, fBtnExcluir, fBtnLimpar;

	public GuiFuncionario() {
		inicializarComponentes();
		definirEventos();
		refreshData();
	}

	private void inicializarComponentes() {
		setLayout(new BorderLayout(10, 10));

		JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		painelTopo.add(new JLabel("Buscar por Nome/Documento:"));
		fTxtBusca = new JTextField(20);
		painelTopo.add(fTxtBusca);
		fBtnBuscar = new JButton("Buscar");
		painelTopo.add(fBtnBuscar);

		fModelo = new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID", "Nome", "Documento", "Tipo Doc", "CEP", "Cidade", "Estado", "Rua", "Nº" }
		) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		fTabela = new JTable(fModelo);
		fTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollTabela = new JScrollPane(fTabela);

		JPanel painelForm = new JPanel(new GridBagLayout());
		painelForm.setBorder(BorderFactory.createTitledBorder("Dados do Funcionário"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0; gbc.gridy = 0;
		painelForm.add(new JLabel("ID:"), gbc.clone());
		gbc.gridx = 1;
		fTxtId = new JTextField(15);
		fTxtId.setEditable(false);
		painelForm.add(fTxtId, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 1;
		painelForm.add(new JLabel("Nome Completo:"), gbc.clone());
		gbc.gridx = 1;
		fTxtNome = new JTextField(15);
		painelForm.add(fTxtNome, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 2;
		painelForm.add(new JLabel("Tipo Documento:"), gbc.clone());
		gbc.gridx = 1;
		fCbTipoDocumento = new JComboBox<>(new String[] { "CPF", "CNPJ" });
		painelForm.add(fCbTipoDocumento, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 3;
		painelForm.add(new JLabel("Documento:"), gbc.clone());
		gbc.gridx = 1;
		fTxtDocumento = new JTextField(15);
		painelForm.add(fTxtDocumento, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 4;
		painelForm.add(new JLabel("CEP:"), gbc.clone());
		gbc.gridx = 1;
		fTxtCep = new JTextField(15);
		painelForm.add(fTxtCep, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 5;
		painelForm.add(new JLabel("País:"), gbc.clone());
		gbc.gridx = 1;
		fTxtPais = new JTextField(15);
		painelForm.add(fTxtPais, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 6;
		painelForm.add(new JLabel("Estado:"), gbc.clone());
		gbc.gridx = 1;
		fTxtEstado = new JTextField(15);
		painelForm.add(fTxtEstado, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 7;
		painelForm.add(new JLabel("Cidade:"), gbc.clone());
		gbc.gridx = 1;
		fTxtCidade = new JTextField(15);
		painelForm.add(fTxtCidade, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 8;
		painelForm.add(new JLabel("Rua:"), gbc.clone());
		gbc.gridx = 1;
		fTxtRua = new JTextField(15);
		painelForm.add(fTxtRua, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 9;
		painelForm.add(new JLabel("Número:"), gbc.clone());
		gbc.gridx = 1;
		fTxtNumero = new JTextField(15);
		painelForm.add(fTxtNumero, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 10;
		painelForm.add(new JLabel("Complemento:"), gbc.clone());
		gbc.gridx = 1;
		fTxtComplemento = new JTextField(15);
		painelForm.add(fTxtComplemento, gbc.clone());

		JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		fBtnNovo = new JButton("Novo");
		fBtnSalvar = new JButton("Salvar");
		fBtnExcluir = new JButton("Excluir");
		fBtnLimpar = new JButton("Limpar");
		painelBotoes.add(fBtnNovo);
		painelBotoes.add(fBtnSalvar);
		painelBotoes.add(fBtnExcluir);
		painelBotoes.add(fBtnLimpar);

		JPanel painelLeste = new JPanel(new BorderLayout(5, 5));
		painelLeste.add(painelForm, BorderLayout.CENTER);
		painelLeste.add(painelBotoes, BorderLayout.SOUTH);

		add(painelTopo, BorderLayout.NORTH);
		add(scrollTabela, BorderLayout.CENTER);
		add(painelLeste, BorderLayout.EAST);
	}

	private void definirEventos() {
		fTabela.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = fTabela.getSelectedRow();
				if (selectedRow != -1) {
					fTxtId.setText(fModelo.getValueAt(selectedRow, 0).toString());
					fTxtNome.setText(fModelo.getValueAt(selectedRow, 1).toString());
					fTxtDocumento.setText(fModelo.getValueAt(selectedRow, 2).toString());
					fCbTipoDocumento.setSelectedItem(fModelo.getValueAt(selectedRow, 3).toString());
					fTxtCep.setText(fModelo.getValueAt(selectedRow, 4).toString());
					fTxtCidade.setText(fModelo.getValueAt(selectedRow, 5).toString());
					fTxtEstado.setText(fModelo.getValueAt(selectedRow, 6).toString());
					fTxtRua.setText(fModelo.getValueAt(selectedRow, 7).toString());
					fTxtNumero.setText(fModelo.getValueAt(selectedRow, 8).toString());

					Usuario u = dbLocalizarUsuarioPorDocumento(fModelo.getValueAt(selectedRow, 2).toString());
					if (u != null && u.getEndereco() != null) {
						fTxtComplemento.setText(u.getEndereco().getComplemento());
						fTxtPais.setText(u.getEndereco().getPais());
					}
				}
			}
		});

		fBtnBuscar.addActionListener(e -> atualizarTabelaFuncionarios(fTxtBusca.getText()));

		fBtnNovo.addActionListener(e -> {
			fTxtId.setText("");
			fTxtNome.setText("");
			fTxtDocumento.setText("");
			fTxtCep.setText("");
			fTxtPais.setText("");
			fTxtEstado.setText("");
			fTxtCidade.setText("");
			fTxtRua.setText("");
			fTxtNumero.setText("");
			fTxtComplemento.setText("");
		});

		fBtnLimpar.addActionListener(e -> {
			fTxtBusca.setText("");
			fBtnNovo.doClick();
			atualizarTabelaFuncionarios("");
		});

		fBtnSalvar.addActionListener(e -> {
			String nome = fTxtNome.getText().trim();
			String doc = fTxtDocumento.getText().trim();
			String cep = fTxtCep.getText().trim();
			String pais = fTxtPais.getText().trim();
			String estado = fTxtEstado.getText().trim();
			String cidade = fTxtCidade.getText().trim();
			String rua = fTxtRua.getText().trim();
			String numStr = fTxtNumero.getText().trim();
			String comp = fTxtComplemento.getText().trim();

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

			Funcionario func = new Funcionario();
			func.setNome(nome);
			func.setDocumento(doc);
			func.setCpf(doc);
			func.setTipoDocumento(fCbTipoDocumento.getSelectedItem().toString());
			func.setEndereco(end);
			func.setFuncao("Funcionário");

			String idStr = fTxtId.getText().trim();
			boolean isNew = idStr.isEmpty();

			if (!isNew) {
				func.setId(Integer.parseInt(idStr));
			} else {
				Usuario exist = dbLocalizarUsuarioPorDocumento(doc);
				if (exist != null) {
					JOptionPane.showMessageDialog(null, "Documento já cadastrado no sistema.", "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			String msg = salvarUsuario(func, isNew);
			JOptionPane.showMessageDialog(null, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);

			if (msg.contains("sucesso") && isNew) {
				int newId = buscarIdUsuarioPorDocumento(doc);
				if (newId != -1) {
					func.setId(newId);
					criarLoginPadrao(func, LoginStatus.FUNCIONARIO);
				}
			}

			atualizarTabelaFuncionarios("");
			fBtnNovo.doClick();
		});

		fBtnExcluir.addActionListener(e -> {
			String idStr = fTxtId.getText().trim();
			if (idStr.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Selecione um funcionário na tabela para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int confirm = JOptionPane.showConfirmDialog(null, "Excluir o funcionário também apagará seus logins associados. Confirmar?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				boolean ok = dbCascataExcluirUsuario(Integer.parseInt(idStr));
				if (ok) {
					JOptionPane.showMessageDialog(null, "Funcionário excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Falha ao excluir funcionário.", "Erro", JOptionPane.ERROR_MESSAGE);
				}
				atualizarTabelaFuncionarios("");
				fBtnNovo.doClick();
			}
		});
	}

	@Override
	public void refreshData() {
		atualizarTabelaFuncionarios(fTxtBusca.getText());
	}

	private void atualizarTabelaFuncionarios(String busca) {
		fModelo.setRowCount(0);
		List<Usuario> funcs = dbListarFuncionarios(busca);
		for (Usuario u : funcs) {
			fModelo.addRow(new Object[] {
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

	private List<Usuario> dbListarFuncionarios(String search) {
		List<Usuario> list = new ArrayList<>();
		BD bd = new BD();
		if (bd.connect()) {
			String sql = "SELECT u.* FROM users u INNER JOIN login l ON u.id = l.idUser " +
						 "WHERE l.tipoUsuario = 'FUNCIONARIO'";
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
						Funcionario f = new Funcionario();
						f.setId(rs.getInt("id"));
						f.setNome(rs.getString("nome"));
						if (rs.getTimestamp("dataCadastro") != null) {
							f.setDataCadastro(rs.getTimestamp("dataCadastro").toLocalDateTime().toLocalDate());
						}
						f.setDocumento(rs.getString("documento"));
						f.setCpf(rs.getString("documento"));
						f.setTipoDocumento(rs.getString("tipoDocumento"));
						Endereco end = new Endereco();
						end.setPais(rs.getString("pais"));
						end.setEstado(rs.getString("estado"));
						end.setCidade(rs.getString("cidade"));
						end.setRua(rs.getString("rua"));
						end.setNumero(rs.getInt("numero"));
						end.setComplemento(rs.getString("complemento"));
						end.setCep(rs.getString("cep"));
						f.setEndereco(end);
						list.add(f);
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
