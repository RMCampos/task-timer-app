package contador;

import java.awt.*;
import javax.swing.*;

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
    
    resposta = JOptionPane.showConfirmDialog( f, mensagem, "Confirmação" , JOptionPane.QUESTION_MESSAGE );
    
    return( resposta == JOptionPane.YES_OPTION );
  }
}
