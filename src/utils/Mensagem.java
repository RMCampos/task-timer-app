package utils;

import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Mensagem {

	public static void informacao(String mensagem, JFrame f) {
		setFont( new Font( "Monospaced", 0, 12 ) );
		JOptionPane.showMessageDialog(f, mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE );
	}

	public static void setFont(Font font) {
		javax.swing.UIManager.put( "OptionPane.messageFont", font );
		javax.swing.UIManager.put( "OptionPane.buttonFont", font );
	}

	public static boolean confirmar( String mensagem, JFrame f ) {
		int resposta;
		resposta = JOptionPane.showConfirmDialog( f, mensagem, "Confirmação" , JOptionPane.YES_NO_OPTION );
		return( resposta == JOptionPane.YES_OPTION );
	}
}
