package view.funcionario;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GuiMenuFuncionario extends JFrame {
	private static final long serialVersionUID = 1L;
	private Container contentPane;
    private JMenuBar mnBarra;
    private JMenu mnArquivo, mnCadastro, mnAjuda;
    private JMenuItem miSair;
    private JMenuItem miPessoaFisica, miPessoaJuridica, miProduto, miCompra;
    private JMenuItem miSobre;

    public GuiMenuFuncionario() {
        inicializarComponentes();
        definirEventos();
    }

    private void inicializarComponentes() {
        setTitle("Menu Funcionario");
        setBounds(0, 0, 800, 600);

        contentPane = getContentPane();
        mnBarra = new JMenuBar();

        mnArquivo = new JMenu("Arquivo");
        mnArquivo.setMnemonic('A');
        mnBarra.add(mnArquivo);
        
        mnCadastro = new JMenu("Cadastro");
        mnCadastro.setMnemonic('C');
        mnBarra.add(mnCadastro);
        
        mnAjuda = new JMenu("Ajuda");
        mnAjuda.setMnemonic('A');
        mnBarra.add(mnAjuda);
        
        miSair = new JMenuItem( "Sair" );
        miSair.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.ALT_MASK) );
        mnArquivo.add(miSair);

        miCompra = new JMenuItem("Compra");
        miCompra.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_C, ActionEvent.ALT_MASK) );
        mnCadastro.add(miCompra);
        
        miProduto = new JMenuItem("Produto");
        miProduto.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_P, ActionEvent.ALT_MASK) );
        mnCadastro.add(miProduto);

        miPessoaFisica = new JMenuItem("Pessoa Física");
        miPessoaFisica.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_J, ActionEvent.ALT_MASK) );
        mnCadastro.add(miPessoaFisica);
        
        miPessoaJuridica = new JMenuItem("Pessoa Jurídica");
        miPessoaJuridica.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_J, ActionEvent.ALT_MASK) );
        mnCadastro.add(miPessoaJuridica);
        
        miSobre = new JMenuItem("Sobre...");
        //miSobre.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_H, ActionEvent.ALT_MASK) );
        mnAjuda.add(miSobre);
        
        setJMenuBar(mnBarra);
    }

    private void definirEventos() {
        miSair.addActionListener(
                new ActionListener() {
                	public void actionPerformed( ActionEvent e) {
                		System.exit(0);
                	}
                });

        miPessoaFisica.addActionListener(
                new ActionListener() {
                	public void actionPerformed( ActionEvent e) {
                		// aqui vai o codigo para chamar o exemplo 8.3
                		JOptionPane.showMessageDialog(null, "Ação de cadastro de Pessoa Física",
                				"Informação", JOptionPane.INFORMATION_MESSAGE );
                	}
                });
        
        miPessoaJuridica.addActionListener(
                new ActionListener() {
                	public void actionPerformed( ActionEvent e) {
                		// aqui vai o codigo para chamar o exemplo 8.3
                		JOptionPane.showMessageDialog(null, "Ação de cadastro de Pessoa Jur�dica",
								"Informação", JOptionPane.QUESTION_MESSAGE );
                	}
                });
        
        miCompra.addActionListener(
                new ActionListener() {
                	public void actionPerformed( ActionEvent e) {
                		// aqui vai o codigo para chamar o exemplo 8.3
                		JOptionPane.showMessageDialog(null, "Ação de cadastro de Compra",
								"Informação", JOptionPane.PLAIN_MESSAGE );
                	}
                });
        
        miProduto.addActionListener(
                new ActionListener() {
                	public void actionPerformed( ActionEvent e) {
                		// aqui vai o codigo para chamar o exemplo 8.3
                		JOptionPane.showMessageDialog(null, "Ação de cadastro de Produto",
								"Informação", JOptionPane.WARNING_MESSAGE );
                	}
                });
        
        miSobre.addActionListener(
                new ActionListener() {
                	public void actionPerformed( ActionEvent e) {
                		// aqui vai o codigo para chamar o exemplo 8.3
                		String message = "Desenvolvidos por:\n" +
                						 "KyryIx - https://github.com/KyryIx\n" +
                						 "SrOtimizacao - https://github.com/SrOtimizacao";
                		String title = "Créditos";
                		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE );
                	}
                });
    }

    public static void abrir() {
        GuiMenuFuncionario frame = new GuiMenuFuncionario();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setLocation(
                (tela.width - frame.getSize().width) / 2,
                (tela.height - frame.getSize().height) / 2
        );

        frame.setVisible(true);
    }
}