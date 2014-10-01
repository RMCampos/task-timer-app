package contador;

import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Diagrama {
  private String  codigo;
  private String  nome;
  private String  solicitante;
  private String  horaInicio;
  private String  duracao;
  private String  horaTermino;
  private Date    horaIntervalo;
  private boolean emAndamento;
  private String  obs;
  private final Timer timer;
  private String  cronometro;
  
  public Diagrama() {
    this.codigo      = "";
    this.nome        = "";
    this.solicitante = "";
    this.horaInicio  = "";
    this.duracao     = "";
    this.horaTermino = "";
    this.obs         = "";
    
    this.horaIntervalo = new Date();
    this.emAndamento = true;
    
    this.cronometro = "00:00:00";
    
    ActionListener updateClockAction = new ActionListener() {
      @Override
      public void actionPerformed( ActionEvent e ) {
        cronometro = incrementarUmSegundo( cronometro );
      }
    };
    this.timer = new Timer( 1000, updateClockAction );
  }
  
  private String incrementarUmSegundo( String tempo ) {
    try {
      int hora = Integer.parseInt( tempo.substring( 0, 2 ) );
      int min = Integer.parseInt( tempo.substring( 3, 5 ) );
      int seg = Integer.parseInt( tempo.substring( 6, 8 ) );
      
      seg++;
      
      if( seg >= 60 )
      {
        min++;
        seg -= 60;
      }
      
      if( min >= 60 )
      {
        hora++;
        min -= 60;
      }
      
      String horaS = (hora < 10)? "0"+hora+":" : hora+":";
      String minS = (min < 10)? "0"+min+":" : min+":";
      String segS = (seg < 10)? "0"+seg : seg+"";
      
      return( horaS+minS+segS );
    }
    catch( NumberFormatException e ) {
      System.out.println( "NumberFormatException: " + e.getLocalizedMessage() );
      return( "00:00:00" );
    }
  }

  public String getCodigo() {
    return( this.codigo );
  }

  public String getNome() {
    return( this.nome );
  }

  public String getSolicitante() {
    return( this.solicitante );
  }

  public String getHoraInicio() {
    return horaInicio;
  }

  public String getDuracao() {
    return duracao;
  }

  public String getHoraTermino() {
    return horaTermino;
  }

  public char getEmAndamento() {
    return( (this.emAndamento)? 'S' : 'N' );
  }
  public Date getHoraIntervalo() {
    return horaIntervalo;
  }
  public String getObs() {
    return( this.obs );
  }
  public String getCronometro() {
    return( this.cronometro );
  }
  public boolean emAndamento() {
    return( emAndamento );
  }
  public void setCodigo(String pCodigo) {
    this.codigo = pCodigo;
  }
  public void setNome(String pNome) {
    this.nome = pNome;
  }
  public void setSoliciante(String cliente) {
    this.solicitante = cliente;
  }
  public void setHoraInicio(String horaInicio) {
    this.horaInicio = horaInicio;
  }
  public void setDuracao(String duracao) {
    this.duracao = duracao;
  }
  public void setHoraTermino(String horaTermino) {
    this.horaTermino = horaTermino;
  }
  public void setHoraIntervalo( Date hora ) {
    this.horaIntervalo = hora;
  }
  public void setEmAndamento( boolean sim ) {
    this.emAndamento = sim;
  }
  public void setObs( String obs ) {
    this.obs = obs;
  }
  public void iniciarTempo() {
    this.timer.start();
  } 
  public void pararTempo() {
    this.timer.stop();
    this.cronometro = getDuracao();
  }
}