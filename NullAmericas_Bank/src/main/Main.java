package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import model.BD;
import model.Conta;
import model.Login;
//import model.Usuario;
import repository.ContaDAO;
import view.GuiLogin;

public class Main {

	private static GuiLogin frame;

	private static Login login;
	private static List<Conta> contasCarregadas;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame = new GuiLogin();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();

				frame.setLocation(
						(tela.width - frame.getSize().width) / 2,
						(tela.height - frame.getSize().height) / 2);

				frame.setVisible(true);
			}
		});
	}

	public static void carregarContasDoUsuario() {

		if (getLogin().getUsuario() == null) {
			System.out.println("Nenhum usuário logado para carregar contas.");
			return;
		}

		BD bd = new BD();

		if (bd.connect()) {

			ContaDAO contaDAO = new ContaDAO();
			contaDAO.setBd(bd);

			List<Conta> listaContas =
					contaDAO.localizarContas(getLogin().getUsuario());

			if (listaContas != null) {
				Main.setContasCarregadas(listaContas);
			}

			bd.close();
		}
	}

	public static List<Conta> getContasCarregadas() {
		return contasCarregadas;
	}

	public static void setContasCarregadas(List<Conta> contasCarregadas) {
		Main.contasCarregadas = contasCarregadas;
	}

	public static Login getLogin() {
		return login;
	}

	public static void setLogin(Login login) {
		Main.login = login;
	}
}