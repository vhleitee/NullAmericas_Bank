package view;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.BD;

public class GuiConsultaTransacoes extends JPanel implements Refreshable {

	private static final long serialVersionUID = 1L;

	private JTextField qTxtBusca;
	private JButton qBtnBuscar;
	private JLabel qLblClienteNome, qLblClienteDocumento, qLblContaId, qLblSaldo;
	private JTable qTabelaTransacoes;
	private DefaultTableModel qModeloTransacoes;

	public GuiConsultaTransacoes() {
		inicializarComponentes();
		definirEventos();
	}

	private void inicializarComponentes() {
		setLayout(new BorderLayout(10, 10));

		JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		painelTopo.add(new JLabel("Buscar Conta (ID ou CPF/CNPJ do Cliente):"));
		qTxtBusca = new JTextField(20);
		painelTopo.add(qTxtBusca);
		qBtnBuscar = new JButton("Consultar");
		painelTopo.add(qBtnBuscar);

		JPanel painelInfo = new JPanel(new GridLayout(2, 2, 10, 10));
		painelInfo.setBorder(BorderFactory.createTitledBorder("Informações da Conta"));

		qLblClienteNome = new JLabel("Cliente: -");
		qLblClienteNome.setFont(new Font("Tahoma", Font.BOLD, 14));
		qLblClienteDocumento = new JLabel("Documento: -");
		qLblClienteDocumento.setFont(new Font("Tahoma", Font.BOLD, 14));
		qLblContaId = new JLabel("Conta ID: -");
		qLblContaId.setFont(new Font("Tahoma", Font.BOLD, 14));
		qLblSaldo = new JLabel("Saldo: R$ 0,00");
		qLblSaldo.setFont(new Font("Tahoma", Font.BOLD, 20));
		qLblSaldo.setForeground(new Color(0, 128, 0));

		painelInfo.add(qLblClienteNome);
		painelInfo.add(qLblClienteDocumento);
		painelInfo.add(qLblContaId);
		painelInfo.add(qLblSaldo);

		qModeloTransacoes = new DefaultTableModel(
			new Object[][] {},
			new String[] { "ID Transação", "Data", "Tipo", "Valor (R$)", "Conta Destino" }
		) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		qTabelaTransacoes = new JTable(qModeloTransacoes);
		JScrollPane scrollTabela = new JScrollPane(qTabelaTransacoes);
		scrollTabela.setBorder(BorderFactory.createTitledBorder("Histórico de Transações"));
		scrollTabela.setPreferredSize(new Dimension(1100, 380));

		add(painelTopo, BorderLayout.NORTH);
		add(painelInfo, BorderLayout.CENTER);
		add(scrollTabela, BorderLayout.SOUTH);
	}

	private void definirEventos() {
		qBtnBuscar.addActionListener(e -> {
			String busca = qTxtBusca.getText().trim();
			if (busca.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Digite o ID da Conta ou o CPF/CNPJ do Cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
				return;
			}

			BD bd = new BD();
			if (bd.connect()) {
				try {
					String sql = "SELECT c.id AS conta_id, u.nome, u.documento FROM conta c INNER JOIN users u ON c.idUser = u.id " +
								 "WHERE CAST(c.id AS CHAR) = ? OR u.documento = ?";
					PreparedStatement ps = bd.connection.prepareStatement(sql);
					ps.setString(1, busca);
					ps.setString(2, busca);

					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						int contaId = rs.getInt("conta_id");
						String nomeCliente = rs.getString("nome");
						String documentoCliente = rs.getString("documento");

						qLblClienteNome.setText("Cliente: " + nomeCliente);
						qLblClienteDocumento.setText("Documento: " + documentoCliente);
						qLblContaId.setText("Conta ID: " + contaId);

						double saldo = 0.0;
						String saldoSql = "SELECT COALESCE(SUM(valor), 0) AS saldo FROM transacao WHERE idConta = ?";
						try (PreparedStatement sps = bd.connection.prepareStatement(saldoSql)) {
							sps.setInt(1, contaId);
							try (ResultSet srs = sps.executeQuery()) {
								if (srs.next()) {
									saldo = srs.getDouble("saldo");
								}
							}
						}
						qLblSaldo.setText(String.format("Saldo: R$ %.2f", saldo));

						qModeloTransacoes.setRowCount(0);
						String tSql = "SELECT id, dataTransacao, tipoTransacao, valor, idContaCorrespondente FROM transacao WHERE idConta = ? ORDER BY dataTransacao DESC";
						try (PreparedStatement tps = bd.connection.prepareStatement(tSql)) {
							tps.setInt(1, contaId);
							try (ResultSet trs = tps.executeQuery()) {
								while (trs.next()) {
									qModeloTransacoes.addRow(new Object[] {
										trs.getInt("id"),
										trs.getTimestamp("dataTransacao"),
										trs.getString("tipoTransacao"),
										trs.getDouble("valor"),
										trs.getInt("idContaCorrespondente")
									});
								}
							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "Conta não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
						qLblClienteNome.setText("Cliente: -");
						qLblClienteDocumento.setText("Documento: -");
						qLblContaId.setText("Conta ID: -");
						qLblSaldo.setText("Saldo: R$ 0,00");
						qModeloTransacoes.setRowCount(0);
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				} finally {
					bd.close();
				}
			} else {
				JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	@Override
	public void refreshData() {
		// When refresh is triggered, reload the current query if one exists
		String busca = qTxtBusca.getText().trim();
		if (!busca.isEmpty()) {
			qBtnBuscar.doClick();
		}
	}
}
