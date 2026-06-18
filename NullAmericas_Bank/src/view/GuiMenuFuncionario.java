package view;

import java.awt.*;
import javax.swing.*;

public class GuiMenuFuncionario extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTabbedPane abas;

	public GuiMenuFuncionario() {
		inicializarComponentes();
	}

	private void inicializarComponentes() {
		setTitle("Menu Funcionário");
		setSize(1200, 750);
		setLayout(new BorderLayout());

		abas = new JTabbedPane();

		GuiCliente tabCliente = new GuiCliente();
		GuiFuncionario tabFuncionario = new GuiFuncionario();
		GuiConta tabConta = new GuiConta();
		GuiLoginPanel tabLogin = new GuiLoginPanel();
		GuiConsultaTransacoes tabConsulta = new GuiConsultaTransacoes();

		abas.addTab("Clientes", tabCliente);
		abas.addTab("Funcionários", tabFuncionario);
		abas.addTab("Contas", tabConta);
		abas.addTab("Logins", tabLogin);
		abas.addTab("Consulta Transações e Saldo", tabConsulta);

		abas.addChangeListener(e -> {
			Component selected = abas.getSelectedComponent();
			if (selected instanceof Refreshable) {
				((Refreshable) selected).refreshData();
			}
		});

		add(abas, BorderLayout.CENTER);
	}

	public static void abrir() {
		GuiMenuFuncionario frame = new GuiMenuFuncionario();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((tela.width - frame.getSize().width) / 2, (tela.height - frame.getSize().height) / 2);
		frame.setVisible(true);
	}
}