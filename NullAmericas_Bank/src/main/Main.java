package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import view.GuiLogin;
import model.*;
import repository.ContaDAO;
import repository.TransacaoDAO;

public class Main {
	private static GuiLogin frame;

	private static Usuario usuarioLogado;
	private static List<Conta> contasCarregadas;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame = new GuiLogin();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
				frame.setLocation((tela.width - frame.getSize().width) / 2, (tela.height - frame.getSize().height) / 2);
				frame.setVisible(true);
			}
		});
	}

	public static void carregarContasDoUsuario() {
		if (usuarioLogado == null) {
			System.out.println("Nenhum usuário logado para carregar contas.");
			return;
		}

		BD bd = new BD();
		if (bd.connect()) {
			ContaDAO contaDAO = new ContaDAO();
			contaDAO.setBd(bd);

			List<Conta> listaContas = contaDAO.localizarContas(getUsuarioLogado());

			if (listaContas != null) {
				Main.setContasCarregadas(listaContas);
			}
		}
	}

	// MODIFICADO: Agora aceita uma conta específica por parâmetro para carregar as transações dela
	public static void carregarTransacoes(Conta contaSelecionada) {
		if (contaSelecionada == null) {
			System.out.println("Nenhuma conta selecionada para carregar transações.");
			return;
		}

		BD bd = new BD();
		if (bd.connect()) {
			TransacaoDAO transacoesDAO = new TransacaoDAO();
			transacoesDAO.setBd(bd);
			
			// Associa a conta escolhida ao DAO
			transacoesDAO.setConta(contaSelecionada);
			
			// Executa a busca (que internamente preenche a lista dentro de contaSelecionada)
			transacoesDAO.localizarTransacoes();
			
			// Exibe o extrato tirado de dentro da própria conta selecionada
			System.out.println("Extrato da Conta ID: " + contaSelecionada.getId());
			for (Transacao t : contaSelecionada.extrato()) {
				System.out.println(t.toString());
			}
			System.out.println("---------------------------");
		}
	}

	public static void setUsuarioLogado(Usuario usuario) {
		usuarioLogado = usuario;
	}

	public static Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public static List<Conta> getContasCarregadas() {
		return contasCarregadas;
	}

	public static void setContasCarregadas(List<Conta> contasCarregadas) {
		Main.contasCarregadas = contasCarregadas;
	}
}