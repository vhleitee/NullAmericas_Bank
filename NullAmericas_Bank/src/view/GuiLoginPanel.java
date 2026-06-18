package view;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import enums.LoginStatus;
import model.BD;
import model.Cliente;
import model.Funcionario;
import model.Usuario;

public class GuiLoginPanel extends JPanel implements Refreshable {

	private static final long serialVersionUID = 1L;

	private JTextField lTxtId, lTxtCodigo, lTxtSenha, lTxtDocumentoUsuario;
	private JComboBox<String> lCbTipoUsuario;
	private JTable lTabela;
	private DefaultTableModel lModelo;
	private JTextField lTxtBusca;
	private JButton lBtnBuscar, lBtnNovo, lBtnSalvar, lBtnExcluir, lBtnLimpar;

	public GuiLoginPanel() {
		inicializarComponentes();
		definirEventos();
		refreshData();
	}

	private void inicializarComponentes() {
		setLayout(new BorderLayout(10, 10));

		JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		painelTopo.add(new JLabel("Buscar Login:"));
		lTxtBusca = new JTextField(20);
		painelTopo.add(lTxtBusca);
		lBtnBuscar = new JButton("Buscar");
		painelTopo.add(lBtnBuscar);

		lModelo = new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID Login", "Usuário ID", "Código (Login)", "Senha", "Tipo Usuário", "Nome Usuário", "Documento" }
		) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		lTabela = new JTable(lModelo);
		lTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollTabela = new JScrollPane(lTabela);

		JPanel painelForm = new JPanel(new GridBagLayout());
		painelForm.setBorder(BorderFactory.createTitledBorder("Dados de Login"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0; gbc.gridy = 0;
		painelForm.add(new JLabel("ID Login:"), gbc.clone());
		gbc.gridx = 1;
		lTxtId = new JTextField(15);
		lTxtId.setEditable(false);
		painelForm.add(lTxtId, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 1;
		painelForm.add(new JLabel("Código:"), gbc.clone());
		gbc.gridx = 1;
		lTxtCodigo = new JTextField(15);
		painelForm.add(lTxtCodigo, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 2;
		painelForm.add(new JLabel("Senha:"), gbc.clone());
		gbc.gridx = 1;
		lTxtSenha = new JTextField(15);
		painelForm.add(lTxtSenha, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 3;
		painelForm.add(new JLabel("Tipo Usuário:"), gbc.clone());
		gbc.gridx = 1;
		lCbTipoUsuario = new JComboBox<>(new String[] { "USUARIO", "FUNCIONARIO" });
		painelForm.add(lCbTipoUsuario, gbc.clone());

		gbc.gridx = 0; gbc.gridy = 4;
		painelForm.add(new JLabel("Documento do Usuário:"), gbc.clone());
		gbc.gridx = 1;
		lTxtDocumentoUsuario = new JTextField(15);
		painelForm.add(lTxtDocumentoUsuario, gbc.clone());

		JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lBtnNovo = new JButton("Novo");
		lBtnSalvar = new JButton("Salvar");
		lBtnExcluir = new JButton("Excluir");
		lBtnLimpar = new JButton("Limpar");
		painelBotoes.add(lBtnNovo);
		painelBotoes.add(lBtnSalvar);
		painelBotoes.add(lBtnExcluir);
		painelBotoes.add(lBtnLimpar);

		JPanel painelLeste = new JPanel(new BorderLayout(5, 5));
		painelLeste.add(painelForm, BorderLayout.NORTH);
		painelLeste.add(painelBotoes, BorderLayout.SOUTH);

		add(painelTopo, BorderLayout.NORTH);
		add(scrollTabela, BorderLayout.CENTER);
		add(painelLeste, BorderLayout.EAST);
	}

	private void definirEventos() {
		lTabela.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = lTabela.getSelectedRow();
				if (selectedRow != -1) {
					lTxtId.setText(lModelo.getValueAt(selectedRow, 0).toString());
					lTxtCodigo.setText(lModelo.getValueAt(selectedRow, 2).toString());
					lTxtSenha.setText(lModelo.getValueAt(selectedRow, 3).toString());
					lCbTipoUsuario.setSelectedItem(lModelo.getValueAt(selectedRow, 4).toString());
					lTxtDocumentoUsuario.setText(lModelo.getValueAt(selectedRow, 6).toString());
				}
			}
		});

		lBtnBuscar.addActionListener(e -> atualizarTabelaLogins(lTxtBusca.getText()));

		lBtnNovo.addActionListener(e -> {
			lTxtId.setText("");
			lTxtCodigo.setText("");
			lTxtSenha.setText("");
			lTxtDocumentoUsuario.setText("");
		});

		lBtnLimpar.addActionListener(e -> {
			lTxtBusca.setText("");
			lBtnNovo.doClick();
			atualizarTabelaLogins("");
		});

		lBtnSalvar.addActionListener(e -> {
			String idStr = lTxtId.getText().trim();
			String codigo = lTxtCodigo.getText().trim();
			String senha = lTxtSenha.getText().trim();
			String tipo = lCbTipoUsuario.getSelectedItem().toString();
			String doc = lTxtDocumentoUsuario.getText().trim();

			if (codigo.isEmpty() || senha.isEmpty() || doc.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Código, Senha e Documento do Usuário são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Usuario u = dbLocalizarUsuarioPorDocumento(doc);
			if (u == null) {
				JOptionPane.showMessageDialog(null, "Usuário não encontrado. Cadastre o usuário primeiro.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			BD bd = new BD();
			if (bd.connect()) {
				try {
					if (idStr.isEmpty()) {
						String sql = "INSERT INTO login(codigo, senha, tipoUsuario, idUser) VALUES (?, ?, ?, ?)";
						try (PreparedStatement ps = bd.connection.prepareStatement(sql)) {
							ps.setString(1, codigo);
							ps.setString(2, senha);
							ps.setString(3, tipo);
							ps.setInt(4, u.getId());
							ps.executeUpdate();
							JOptionPane.showMessageDialog(null, "Login cadastrado com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						String sql = "UPDATE login SET codigo = ?, senha = ?, tipoUsuario = ?, idUser = ? WHERE id = ?";
						try (PreparedStatement ps = bd.connection.prepareStatement(sql)) {
							ps.setString(1, codigo);
							ps.setString(2, senha);
							ps.setString(3, tipo);
							ps.setInt(4, u.getId());
							ps.setInt(5, Integer.parseInt(idStr));
							ps.executeUpdate();
							JOptionPane.showMessageDialog(null, "Login atualizado com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, "Erro ao salvar login: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
				} finally {
					bd.close();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
			}

			atualizarTabelaLogins("");
			lBtnNovo.doClick();
		});

		lBtnExcluir.addActionListener(e -> {
			String idStr = lTxtId.getText().trim();
			if (idStr.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Selecione um login na tabela para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			int confirm = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este login?", "Confirmar", JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				BD bd = new BD();
				if (bd.connect()) {
					try {
						String sql = "DELETE FROM login WHERE id = ?";
						try (PreparedStatement ps = bd.connection.prepareStatement(sql)) {
							ps.setInt(1, Integer.parseInt(idStr));
							ps.executeUpdate();
							JOptionPane.showMessageDialog(null, "Login excluído com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (SQLException ex) {
						JOptionPane.showMessageDialog(null, "Erro ao excluir login: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
					} finally {
						bd.close();
					}
				}
				atualizarTabelaLogins("");
				lBtnNovo.doClick();
			}
		});
	}

	@Override
	public void refreshData() {
		atualizarTabelaLogins(lTxtBusca.getText());
	}

	private void atualizarTabelaLogins(String busca) {
		lModelo.setRowCount(0);
		List<Object[]> logins = dbListarLogins(busca);
		for (Object[] row : logins) {
			lModelo.addRow(row);
		}
	}

	private List<Object[]> dbListarLogins(String search) {
		List<Object[]> list = new ArrayList<>();
		BD bd = new BD();
		if (bd.connect()) {
			String sql = "SELECT l.id, l.idUser, l.codigo, l.senha, l.tipoUsuario, u.nome, u.documento " +
						 "FROM login l INNER JOIN users u ON l.idUser = u.id";
			if (search != null && !search.trim().isEmpty()) {
				sql += " WHERE l.codigo LIKE ? OR u.nome LIKE ? OR u.documento LIKE ?";
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
							rs.getInt("idUser"),
							rs.getString("codigo"),
							rs.getString("senha"),
							rs.getString("tipoUsuario"),
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
						u.setEndereco(null);
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
}
