import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import view.GuiLogin;

public class Main {
    private static GuiLogin frame;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame = new GuiLogin();
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();

                frame.setLocation(
                        (tela.width - frame.getSize().width) / 2,
                        (tela.height - frame.getSize().height) / 2
                );

                frame.setVisible(true);
            }
        });
    }
}
