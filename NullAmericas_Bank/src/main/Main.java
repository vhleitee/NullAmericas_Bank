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
		// Inicializa a interface gráfica na thread correta do Swing
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame = new GuiLogin();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();

				// Centraliza a janela na tela
				frame.setLocation((tela.width - frame.getSize().width) / 2, (tela.height - frame.getSize().height) / 2);

				frame.setVisible(true);
			}
		});
	}

	// Método auxiliar para carregar as contas após o login ser efetuado com sucesso
	public static void carregarContasDoUsuario() {
		if (usuarioLogado == null) {
			System.out.println("Nenhum usuário logado para carregar contas.");
			return;
		}

		BD bd = new BD();
		if (bd.connect()) {
			ContaDAO contaDAO = new ContaDAO();
			contaDAO.setBd(bd);

			// Correção: Chamando o método estático da própria classe sem o 'this'
			List<Conta> listaContas = contaDAO.localizarContas(getUsuarioLogado());

			if (listaContas != null) {
				Main.setContasCarregadas(listaContas);
			}
			// Idealmente aqui você fecharia a conexão: bd.disconnect(); (se existir no seu
			// projeto)
		}
	}

	public static void carregarTransacoes() {
		BD bd = new BD();
		if (bd.connect()) {
			TransacaoDAO transacoes = new TransacaoDAO();
			transacoes.setBd(bd);
			List<Conta> contas = Main.getContasCarregadas();			
			transacoes.setConta(contas.getFirst());
			List<Transacao> transacao = transacoes.localizarTransacoes();
			
			for (int i = 0; i < transacao.size(); i++) {
				System.out.println(transacao.get(i).toString());
			}
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