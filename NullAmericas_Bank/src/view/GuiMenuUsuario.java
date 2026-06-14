package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import main.Main;
import model.Conta;
import model.Transacao;

public class GuiMenuUsuario extends JFrame {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private Container contentPane;

	private JMenuBar mnBarra;
	private JMenu mnArquivo, mnCadastro, mnAjuda;
	private JMenuItem miSair;
	private JMenuItem miPessoaFisica, miPessoaJuridica, miProduto, miCompra;
	private JMenuItem miSobre;

	private JComboBox<Conta> cbContas;
	private JTextArea txtTransacoes;
	private JLabel lblOlaUser;
	private JLabel lblSaldo;

	public GuiMenuUsuario() {
		inicializarComponentes();
		definirEventos();
		carregarDadosIniciais();
	}

	private void inicializarComponentes() {
		setTitle("Menu Usuário");
		setBounds(100, 100, 720, 488);

		// Layout principal
		getContentPane().setLayout(new BorderLayout(10, 10));

		// ==========================
		// TOPO
		// ==========================
		JPanel painelTopo = new JPanel(new BorderLayout(5, 5));

		lblOlaUser = new JLabel("Olá, usuário");
		lblOlaUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblOlaUser.setFont(new Font("Tahoma", Font.BOLD, 20));
		painelTopo.add(lblOlaUser, BorderLayout.NORTH);

		JPanel painelConta = new JPanel(new BorderLayout(5, 0));

		JLabel lblContas = new JLabel("Contas:");
		lblContas.setFont(new Font("Tahoma", Font.PLAIN, 12));
		painelConta.add(lblContas, BorderLayout.WEST);

		cbContas = new JComboBox<>();
		painelConta.add(cbContas, BorderLayout.CENTER);

		painelTopo.add(painelConta, BorderLayout.CENTER);

		getContentPane().add(painelTopo, BorderLayout.NORTH);

		// ==========================
		// CENTRO
		// ==========================
		txtTransacoes = new JTextArea();
		txtTransacoes.setEditable(false);
		txtTransacoes.setFont(new Font("Monospaced", Font.PLAIN, 12));

		JScrollPane scrollTransacoes = new JScrollPane(txtTransacoes);

		// Painel central contendo transações + saldo
		JPanel painelCentral = new JPanel(new BorderLayout());

		painelCentral.add(scrollTransacoes, BorderLayout.CENTER);

		// ==========================
		// SALDO ABAIXO DAS TRANSAÇÕES
		// ==========================
		JPanel painelSaldo = new JPanel();
		painelSaldo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		lblSaldo = new JLabel("Saldo: R$ 0,00");
		lblSaldo.setHorizontalAlignment(SwingConstants.CENTER);
		lblSaldo.setFont(new Font("Tahoma", Font.BOLD, 16));

		painelSaldo.add(lblSaldo);

		painelCentral.add(painelSaldo, BorderLayout.SOUTH);

		getContentPane().add(painelCentral, BorderLayout.CENTER);

		// ==========================
		// LATERAL DIREITA
		// ==========================
		JPanel painelLateral = new JPanel(new BorderLayout());

		JPanel painelBotoes = new JPanel(new GridLayout(3, 1, 0, 10));

		JButton btnDepositar = new JButton("Depositar");
		JButton btnSacar = new JButton("Sacar");
		JButton btnTransferir = new JButton("Transferir");

		painelBotoes.add(btnDepositar);
		painelBotoes.add(btnSacar);
		painelBotoes.add(btnTransferir);

		painelLateral.add(painelBotoes, BorderLayout.NORTH);

		getContentPane().add(painelLateral, BorderLayout.EAST);

		// Margem interna
		((JPanel) getContentPane()).setBorder(
				BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	private void carregarDadosIniciais() {

		if (Main.getUsuarioLogado() != null) {
			lblOlaUser.setText("Olá, " + Main.getUsuarioLogado().getNome());
		} else {
			lblOlaUser.setText("Olá, Visitante");
		}

		if (Main.getContasCarregadas() != null &&
			!Main.getContasCarregadas().isEmpty()) {

			for (Conta conta : Main.getContasCarregadas()) {
				cbContas.addItem(conta);
			}

			atualizarTransacoesDaContaSelecionada();

		} else {

			txtTransacoes.setText(
				"Nenhuma conta encontrada para este usuário.");

			lblSaldo.setText("Saldo: R$ 0,00");
		}
	}

	private void atualizarTransacoesDaContaSelecionada() {

		Conta contaSelecionada =
				(Conta) cbContas.getSelectedItem();

		if (contaSelecionada != null) {

			Main.carregarTransacoes(contaSelecionada);

			double valorSaldo = 0.0;

			if (contaSelecionada.getSaldo() != null) {
			    valorSaldo = contaSelecionada.getSaldo().getSaldo();
			}

			lblSaldo.setText(
			    String.format("Saldo: R$ %.2f", valorSaldo)
			);

			txtTransacoes.setText(
				"--- Histórico de Transações ---\n\n");

			if (contaSelecionada.getTransacoes() != null
					&& !contaSelecionada.getTransacoes().isEmpty()) {

				for (Object t : contaSelecionada.getTransacoes()) {
					txtTransacoes.append(
						t.toString()
						+ "\n---------------------\n");
				}

			} else {

				txtTransacoes.append(
					"Nenhuma transação encontrada para esta conta.");
			}
		}
	}

	private void definirEventos() {

		cbContas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				atualizarTransacoesDaContaSelecionada();
			}
		});
	}

	public static void abrir() {

		GuiMenuUsuario frame = new GuiMenuUsuario();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension tela =
				Toolkit.getDefaultToolkit().getScreenSize();

		frame.setLocation(
				(tela.width - frame.getSize().width) / 2,
				(tela.height - frame.getSize().height) / 2);

		frame.setVisible(true);
	}
}