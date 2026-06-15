package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GuiMenuFuncionario extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTabbedPane abas;

	private JTextField txtNome;
	private JTextField txtDataCadastro;
	private JComboBox<String> cbTipoDocumento;
	private JTextField txtDocumento;

	private JTextField txtPais;
	private JTextField txtEstado;
	private JTextField txtCidade;
	private JTextField txtRua;
	private JTextField txtNumero;
	private JTextField txtComplemento;
	private JTextField txtCep;

	private JButton btnSalvar;
	private JButton btnAtualizar;
	private JButton btnLimpar;

	private JTextField txtBuscaCliente;
	private JButton btnBuscarCliente;
	private JButton btnEditarCliente;
	private JTable tabelaClientes;

	private JTextField txtBuscaConta;
	private JButton btnBuscarConta;

	private JLabel lblIdConta;
	private JLabel lblSaldo;

	private JTable tabelaTransacoes;

	public GuiMenuFuncionario() {
		inicializarComponentes();
		definirEventos();
	}

	private void inicializarComponentes() {

		setTitle("Menu Funcionário");
		setSize(1200, 700);
		setLayout(new BorderLayout());

		abas = new JTabbedPane();

		criarAbaCadastro();
		criarAbaConsultaClientes();
		criarAbaConsultaContas();

		add(abas, BorderLayout.CENTER);
	}

	private void criarAbaCadastro() {

		JPanel painel = new JPanel(new BorderLayout());

		JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));

		txtNome = new JTextField();
		txtDataCadastro = new JTextField();

		cbTipoDocumento = new JComboBox<>(new String[] { "CPF", "CNPJ" });

		txtDocumento = new JTextField();

		txtPais = new JTextField();
		txtEstado = new JTextField();
		txtCidade = new JTextField();
		txtRua = new JTextField();
		txtNumero = new JTextField();
		txtComplemento = new JTextField();
		txtCep = new JTextField();

		formulario.add(new JLabel("Nome Completo:"));
		formulario.add(txtNome);

		formulario.add(new JLabel("Data Cadastro:"));
		formulario.add(txtDataCadastro);

		formulario.add(new JLabel("Tipo Documento:"));
		formulario.add(cbTipoDocumento);

		formulario.add(new JLabel("Documento:"));
		formulario.add(txtDocumento);

		formulario.add(new JLabel("País:"));
		formulario.add(txtPais);

		formulario.add(new JLabel("Estado:"));
		formulario.add(txtEstado);

		formulario.add(new JLabel("Cidade:"));
		formulario.add(txtCidade);

		formulario.add(new JLabel("Rua:"));
		formulario.add(txtRua);

		formulario.add(new JLabel("Número:"));
		formulario.add(txtNumero);

		formulario.add(new JLabel("Complemento:"));
		formulario.add(txtComplemento);

		formulario.add(new JLabel("CEP:"));
		formulario.add(txtCep);

		JPanel painelBotoes = new JPanel();

		btnSalvar = new JButton("Salvar");
		btnAtualizar = new JButton("Atualizar");
		btnLimpar = new JButton("Limpar");

		painelBotoes.add(btnSalvar);
		painelBotoes.add(btnAtualizar);
		painelBotoes.add(btnLimpar);

		painel.add(formulario, BorderLayout.CENTER);
		painel.add(painelBotoes, BorderLayout.SOUTH);

		abas.addTab("Cadastro Cliente", painel);
	}

	private void criarAbaConsultaClientes() {

		JPanel painel = new JPanel(new BorderLayout(10, 10));

		JPanel topo = new JPanel();

		txtBuscaCliente = new JTextField(25);
		btnBuscarCliente = new JButton("Buscar");
		btnEditarCliente = new JButton("Editar Cliente");

		topo.add(new JLabel("CPF/CNPJ:"));
		topo.add(txtBuscaCliente);
		topo.add(btnBuscarCliente);
		topo.add(btnEditarCliente);

		tabelaClientes = new JTable();

		tabelaClientes.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Nome", "Documento", "Tipo", "Cidade", "Estado" }));

		painel.add(topo, BorderLayout.NORTH);
		painel.add(new JScrollPane(tabelaClientes), BorderLayout.CENTER);

		abas.addTab("Consultar Clientes", painel);
	}

	private void criarAbaConsultaContas() {

		JPanel painel = new JPanel(new BorderLayout(10, 10));

		JPanel topo = new JPanel();

		txtBuscaConta = new JTextField(25);
		btnBuscarConta = new JButton("Buscar Conta");

		topo.add(new JLabel("CPF/CNPJ:"));
		topo.add(txtBuscaConta);
		topo.add(btnBuscarConta);

		JPanel dadosConta = new JPanel(new GridLayout(2, 1));

		lblIdConta = new JLabel("Conta: -");
		lblSaldo = new JLabel("Saldo: R$ 0,00");

		lblIdConta.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSaldo.setFont(new Font("Tahoma", Font.BOLD, 16));

		dadosConta.add(lblIdConta);
		dadosConta.add(lblSaldo);

		tabelaTransacoes = new JTable();

		tabelaTransacoes.setModel(
				new DefaultTableModel(new Object[][] {}, new String[] { "Data", "Tipo", "Valor", "Conta Destino" }));

		painel.add(topo, BorderLayout.NORTH);
		painel.add(dadosConta, BorderLayout.CENTER);
		painel.add(new JScrollPane(tabelaTransacoes), BorderLayout.SOUTH);

		abas.addTab("Contas e Transações", painel);
	}

	private void definirEventos() {

		btnSalvar.addActionListener(e -> {
			JOptionPane.showMessageDialog(null, "Salvar cliente (implementar DAO posteriormente)");
		});

		btnAtualizar.addActionListener(e -> {
			JOptionPane.showMessageDialog(null, "Atualizar cliente (implementar DAO posteriormente)");
		});

		btnLimpar.addActionListener(e -> {

			txtNome.setText("");
			txtDataCadastro.setText("");
			txtDocumento.setText("");

			txtPais.setText("");
			txtEstado.setText("");
			txtCidade.setText("");
			txtRua.setText("");
			txtNumero.setText("");
			txtComplemento.setText("");
			txtCep.setText("");
		});

		btnBuscarCliente.addActionListener(e -> {
			JOptionPane.showMessageDialog(null, "Consulta de cliente será implementada posteriormente.");
		});

		btnEditarCliente.addActionListener(e -> {
			JOptionPane.showMessageDialog(null, "Carregar dados para edição.");
		});

		btnBuscarConta.addActionListener(e -> {
			JOptionPane.showMessageDialog(null, "Consulta de conta será implementada posteriormente.");
		});
	}

	public static void abrir() {

		GuiMenuFuncionario frame = new GuiMenuFuncionario();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();

		frame.setLocation((tela.width - frame.getSize().width) / 2, (tela.height - frame.getSize().height) / 2);

		frame.setVisible(true);
	}
}