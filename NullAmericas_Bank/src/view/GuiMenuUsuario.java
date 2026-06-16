package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import enums.TipoOperacaoBD;
import enums.TipoTransacao;
import main.Main;
import model.BD;
import model.Conta;
import model.Transacao;
import repository.ContaDAO;
import repository.TransacaoDAO;

public class GuiMenuUsuario extends JFrame {

	private static final long serialVersionUID = 1L;

	private JComboBox<Conta> cbContas;
	private JTextArea txtTransacoes;
	private JLabel lblOlaUser;
	private JLabel lblSaldo;
	
	private JComboBox<String> cbTipoOperacao;
	private JLabel lblDocumentoDestino;
	private JTextField txtDocumentoDestino;
	private JButton btnBuscarContasDestino;
	private JLabel lblContaDestino;
	private JComboBox<Conta> cbContasDestino;
	private JTextField txtValor;
	private JButton btnConfirmar;
	
	private JPanel painelBuscaDocumento;
	private JPanel painelSelecaoDestino;

	private Conta contaSelecionada;

	public GuiMenuUsuario() {
		inicializarComponentes();
		definirEventos();
		carregarDados();
		ajustarCamposPorOperacao();
	}

	private void inicializarComponentes() {
		setTitle("Menu Cliente");
		setBounds(100, 100, 1123, 634);
		getContentPane().setLayout(new BorderLayout(10, 10));

		JPanel painelTopo = new JPanel(new BorderLayout(5, 5));

		lblOlaUser = new JLabel("Olá, usuário");
		lblOlaUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblOlaUser.setFont(new Font("Tahoma", Font.BOLD, 20));
		painelTopo.add(lblOlaUser, BorderLayout.NORTH);

		JPanel painelConta = new JPanel(new BorderLayout(5, 0));
		JLabel lblContas = new JLabel("Sua Conta (Origem):");
		lblContas.setFont(new Font("Tahoma", Font.BOLD, 12));
		painelConta.add(lblContas, BorderLayout.WEST);

		cbContas = new JComboBox<>();
		cbContas.setRenderer(new BasicComboBoxRenderer() {
			private static final long serialVersionUID = 1L;
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Conta) {
					Conta c = (Conta) value;
					setText("Conta ID: " + c.getId());
				}
				return this;
			}
		});
		painelConta.add(cbContas, BorderLayout.CENTER);
		painelTopo.add(painelConta, BorderLayout.CENTER);

		getContentPane().add(painelTopo, BorderLayout.NORTH);

		txtTransacoes = new JTextArea();
		txtTransacoes.setEditable(false);
		txtTransacoes.setFont(new Font("Monospaced", Font.PLAIN, 12));
		JScrollPane scrollTransacoes = new JScrollPane(txtTransacoes);

		JPanel painelCentral = new JPanel(new BorderLayout());
		painelCentral.add(scrollTransacoes, BorderLayout.CENTER);

		JPanel painelSaldo = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		lblSaldo = new JLabel("Saldo: R$ 0,00");
		lblSaldo.setHorizontalAlignment(SwingConstants.CENTER);
		lblSaldo.setFont(new Font("Tahoma", Font.BOLD, 16));
		painelSaldo.add(lblSaldo);
		painelCentral.add(painelSaldo, BorderLayout.SOUTH);

		getContentPane().add(painelCentral, BorderLayout.CENTER);

		JPanel painelLateral = new JPanel(new BorderLayout(10, 10));
		painelLateral.setPreferredSize(new Dimension(340, 0));
		painelLateral.setBorder(BorderFactory.createTitledBorder("Painel de Operações"));

		JPanel painelFormulario = new JPanel(new GridLayout(0, 1, 5, 5));

		painelFormulario.add(new JLabel("Selecione a Operação Desejada:"));
		cbTipoOperacao = new JComboBox<>(new String[]{"DEPÓSITO", "SAQUE", "TRANSFERÊNCIA"});
		painelFormulario.add(cbTipoOperacao);

		painelBuscaDocumento = new JPanel(new GridLayout(2, 1, 2, 2));
		lblDocumentoDestino = new JLabel("CPF/CNPJ do Destinatário:");
		txtDocumentoDestino = new JTextField();
		painelBuscaDocumento.add(lblDocumentoDestino);
		painelBuscaDocumento.add(txtDocumentoDestino);
		painelFormulario.add(painelBuscaDocumento);

		btnBuscarContasDestino = new JButton("Buscar Contas Destino");
		painelFormulario.add(btnBuscarContasDestino);

		painelSelecaoDestino = new JPanel(new GridLayout(2, 1, 2, 2));
		lblContaDestino = new JLabel("Selecione a Conta de Destino:");
		cbContasDestino = new JComboBox<>();
		cbContasDestino.setRenderer(new BasicComboBoxRenderer() {
			private static final long serialVersionUID = 1L;
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Conta) {
					Conta c = (Conta) value;
					String nomeTitular = (c.getCliente() != null) ? c.getCliente().getNome() : "Desconhecido";
					setText("ID: " + c.getId() + " - Titular: " + nomeTitular);
				}
				return this;
			}
		});
		painelSelecaoDestino.add(lblContaDestino);
		painelSelecaoDestino.add(cbContasDestino);
		painelFormulario.add(painelSelecaoDestino);

		painelFormulario.add(new JLabel("Valor da Operação (ex: 150.50):"));
		txtValor = new JTextField();
		painelFormulario.add(txtValor);

		painelLateral.add(painelFormulario, BorderLayout.CENTER);

		btnConfirmar = new JButton("Confirmar Operação");
		btnConfirmar.setFont(new Font("Tahoma", Font.BOLD, 13));
		painelLateral.add(btnConfirmar, BorderLayout.SOUTH);

		getContentPane().add(painelLateral, BorderLayout.EAST);

		((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	private void definirEventos() {
		cbTipoOperacao.addActionListener(e -> ajustarCamposPorOperacao());

		cbContas.addActionListener(e -> atualizarTransacoesDaContaSelecionada());

		btnBuscarContasDestino.addActionListener(e -> {
			String doc = txtDocumentoDestino.getText().trim();
			if (doc.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Digite um documento para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
				return;
			}
			cbContasDestino.removeAllItems();
			
			BD bd = new BD();
			if (bd.connect()) {
				ContaDAO contaDAO = new ContaDAO();
				contaDAO.setBd(bd);
				List<Conta> contasEncontradas = contaDAO.localizarContaPorDocumento(doc);
				bd.close();
				
				if (contasEncontradas != null && !contasEncontradas.isEmpty()) {
					for (Conta c : contasEncontradas) {
						cbContasDestino.addItem(c);
					}
					JOptionPane.showMessageDialog(null, contasEncontradas.size() + " conta(s) encontrada(s)!", "Busca", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Nenhuma conta encontrada para o documento informado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		btnConfirmar.addActionListener(e -> executarOperacaoSelecionada());
	}

	private void ajustarCamposPorOperacao() {
		String operacao = (String) cbTipoOperacao.getSelectedItem();
		boolean ehTransferencia = "TRANSFERÊNCIA".equals(operacao);

		painelBuscaDocumento.setVisible(ehTransferencia);
		btnBuscarContasDestino.setVisible(ehTransferencia);
		painelSelecaoDestino.setVisible(ehTransferencia);

		painelBuscaDocumento.getParent().revalidate();
		painelBuscaDocumento.getParent().repaint();
	}

	private void executarOperacaoSelecionada() {
		Conta contaAtual = getContaSelecionada();
		if (contaAtual == null) {
			JOptionPane.showMessageDialog(null, "Selecione uma conta de origem.", "Aviso", JOptionPane.WARNING_MESSAGE);
			return;
		}

		double valorOp = obterValorValido();
		if (valorOp <= 0) return;

		String operacao = (String) cbTipoOperacao.getSelectedItem();

		if ("DEPÓSITO".equals(operacao)) {
			Transacao t = new Transacao();
			t.setIdConta(contaAtual.getId());
			t.setIdContaCorrespondente(contaAtual.getId());
			t.setTipoTransacao(TipoTransacao.DEPOSITO);
			t.setValor(valorOp);

			processarTransacao(t, valorOp);

		} else if ("SAQUE".equals(operacao)) {
			if (contaAtual.getSaldo() == null || contaAtual.getSaldo().getSaldo() < valorOp) {
				JOptionPane.showMessageDialog(null, "Saldo insuficiente para realizar este saque.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Transacao t = new Transacao();
			t.setIdConta(contaAtual.getId());
			t.setIdContaCorrespondente(contaAtual.getId());
			t.setTipoTransacao(TipoTransacao.SAQUE);
			t.setValor(valorOp);

			processarTransacao(t, -valorOp);

		} else if ("TRANSFERÊNCIA".equals(operacao)) {
			Conta contaDestino = (Conta) cbContasDestino.getSelectedItem();
			if (contaDestino == null) {
				JOptionPane.showMessageDialog(null, "Busque e selecione uma conta de destino válida.", "Aviso", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (contaAtual.getId() == contaDestino.getId()) {
				JOptionPane.showMessageDialog(null, "Operação inválida. Não é possível transferir para si mesmo.", "Aviso", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (contaAtual.getSaldo() == null || contaAtual.getSaldo().getSaldo() < valorOp) {
				JOptionPane.showMessageDialog(null, "Saldo insuficiente para realizar esta transferência.", "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Transacao t = new Transacao();
			t.setIdConta(contaAtual.getId());
			t.setIdContaCorrespondente(contaDestino.getId());
			t.setTipoTransacao(TipoTransacao.TRANSFERENCIA_ENVIADA);
			t.setValor(valorOp);

			processarTransacao(t, -valorOp);
		}
	}

	private double obterValorValido() {
		String valorTexto = txtValor.getText().trim();
		if (!valorTexto.matches("\\d+(\\.\\d+)?")) {
			JOptionPane.showMessageDialog(null, "Valor inválido. Insira apenas números positivos e '.' para os centavos.", "Erro", JOptionPane.ERROR_MESSAGE);
			return -1;
		}
		double valor = Double.parseDouble(valorTexto);
		if (valor <= 0) {
			JOptionPane.showMessageDialog(null, "O valor da operação deve ser maior que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
			return -1;
		}
		return valor;
	}

	private void processarTransacao(Transacao t, double modificacaoSaldo) {
		BD bd = new BD();
		if (bd.connect()) {
			TransacaoDAO dao = new TransacaoDAO();
			dao.setTrasacao(t);
			dao.setBd(bd);

			String retorno = dao.atualizar(TipoOperacaoBD.INCLUSAO);
			bd.close();

			Conta contaAtual = getContaSelecionada();
			double saldoAtual = contaAtual.getSaldo().getSaldo();
			contaAtual.getSaldo().setSaldo(saldoAtual + modificacaoSaldo);

			JOptionPane.showMessageDialog(null, retorno, "Informação", JOptionPane.INFORMATION_MESSAGE);
			
			txtValor.setText("");
			txtDocumentoDestino.setText("");
			cbContasDestino.removeAllItems();
			
			cbContas.repaint();
			atualizarTransacoesDaContaSelecionada();
		} else {
			JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void carregarDados() {
		if (Main.getLogin().getUsuario() != null) {
			lblOlaUser.setText("Olá, " + Main.getLogin().getUsuario().getNome());
		} else {
			lblOlaUser.setText("Olá, Visitante");
		}

		cbContas.removeAllItems();

		if (Main.getContasCarregadas() != null && !Main.getContasCarregadas().isEmpty()) {
			for (Conta conta : Main.getContasCarregadas()) {
				cbContas.addItem(conta);
			}
			atualizarTransacoesDaContaSelecionada();
		} else {
			txtTransacoes.setText("Nenhuma conta encontrada para este usuário.");
			lblSaldo.setText("Saldo: R$ 0,00");
		}
	}

	private void carregarTransacoes() {
		if (this.getContaSelecionada() == null) return;

		BD bd = new BD();
		if (bd.connect()) {
			TransacaoDAO transacaoDAO = new TransacaoDAO();
			transacaoDAO.setBd(bd);
			transacaoDAO.setConta(this.getContaSelecionada());
			transacaoDAO.localizarTransacoes();
			bd.close();
		}
	}

	private void atualizarTransacoesDaContaSelecionada() {
		if (cbContas.getSelectedItem() != null) {
			this.setContaSelecionada((Conta) cbContas.getSelectedItem());
		}
		if (contaSelecionada == null) return;

		carregarTransacoes();
		double valorSaldo = (contaSelecionada.getSaldo() != null) ? contaSelecionada.getSaldo().getSaldo() : 0.0;
		lblSaldo.setText(String.format("Saldo: R$ %.2f", valorSaldo));

		txtTransacoes.setText("--- Histórico de Transações ---\n\n");
		if (contaSelecionada.getTransacoes() != null && !contaSelecionada.getTransacoes().isEmpty()) {
			for (Transacao t : contaSelecionada.getTransacoes()) {
				txtTransacoes.append(t.toString() + "\n---------------------\n");
			}
		} else {
			txtTransacoes.append("Nenhuma transação encontrada para esta conta.");
		}
	}

	public static void abrir() {
		GuiMenuUsuario frame = new GuiMenuUsuario();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((tela.width - frame.getSize().width) / 2, (tela.height - frame.getSize().height) / 2);
		frame.setVisible(true);
	}
	
	public Conta getContaSelecionada() {
		return contaSelecionada;
	}

	public void setContaSelecionada(Conta contaSelecionada) {
		this.contaSelecionada = contaSelecionada;
	}
}