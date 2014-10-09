package contador;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

public class Tela extends JFrame {
  private String comandoTela;
  private Timer  relogio;
  private Diagrama diagramaAtual;
  private FrameConsole console;
  
  private JPanel pnlSuperior;
     private JLabel lblData;
     private JLabel lblHora;
     
  private JPanel pnlInferior;
     private JButton btnCancelar;
     private JLabel lblCodigo;
     private JTextField txfCodigo;
     private JLabel lblNome;
     private JTextField txfNome;
     private JLabel lblSolicitante;
     private JTextField txfSolicitante;
     private JLabel lblObs;
     private JTextField txfObs;
     private JButton btnAdd;
     private JButton btnContinuar;
     private JButton btnParar;
     private JButton btnExcluir;
     private JButton btnExportar;
     private JPanel pnlContador;
     private JPanel pnlTempoDecorrido;
        private JLabel lblTempoDecorrido;
        private JLabel lblTotalPr;
        private JLabel lblTempoTodos;
        private JLabel lblTotalTempo;
     private JPanel pnlExportar;
        private JLabel lblExportarPara;
        private JTextField txfDiretorio;
        private JButton btnProcurarDiretorio;
        private JLabel lblNomeArquivo;
        private JTextField txfNomeArquivo;
     
  public JTable contadorTable;
  public DiagramaModel contadorModel;

  public Tela( String pNomeCidade ) {
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          iniciarComponentes();
        }
      });
    }
    catch (InterruptedException | InvocationTargetException e) {
      e.printStackTrace();
    }
    this.getContentPane().setLayout( null );
    this.setSize( 800, 600 );
    this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    iniciarPrograma( pNomeCidade );
    criarModel();
    criarListener();
    iniciarConsole();
    setLocationRelativeTo( null );
    setarIcones();
  }
  private void setarIcones() {
    try {
      this.setIconImage( new ImageIcon( getClass().getResource( "clock.png" ) ).getImage() );
      this.btnProcurarDiretorio.setIcon( new ImageIcon( getClass().getResource( "folder.png" ) ) );
    }
    catch( Exception ex ) {
      System.out.println( "Exception: Erro ao setar imagem: " + ex.getMessage() );
    }
  }
  private void criarListener() {
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e) {
        try {
          AOPRESSIONARTecla(e);
        }
        catch( Exception exc ) {
          System.out.println( "Erro ao disparar evento de tecla." );
        }
        return false;
      }
    });
    this.contadorTable.addMouseListener( new MouseAdapter() {
      @Override
      public void mouseClicked( MouseEvent e ) {
        if( e.getClickCount() == 1 ) {
          comandoTela = "CARREGAR_DIAGRAMA";
        } 
      }         
    });
    this.contadorTable.addKeyListener( new KeyAdapter() {
      @Override
      public void keyPressed( KeyEvent ke )
      {
        AOPRESSIONARSetas( ke );
      }
    });
  }
  public void AOPRESSIONARTecla( KeyEvent e ) {
    if( e.isControlDown() && e.getKeyCode() == KeyEvent.VK_I && e.getID() == KeyEvent.KEY_PRESSED ) {
      if( this.console.isVisible() ) {
        this.console.setVisible( false );
      }
      else {
        this.console.setVisible( true );
      }
    }
  }
  private void iniciarConsole() {
    this.console = new FrameConsole();
    this.console.setVisible( false );
    this.console.setTitle( "Console" );
  }
  public void mudarEstado( String pEstado ) {
    boolean estado = true;
    
    if( pEstado.equals( "EDICAO" ) ) {
      estado = false;
    }
    
    this.txfCodigo.setEditable( estado );
    this.txfNome.setEditable( estado );
    this.txfSolicitante.setEnabled( estado );
  }
  private void iniciarComponentes() {
    this.diagramaAtual = null;
    // Fonte: http://stackoverflow.com/questions/2959718/dynamic-clock-in-java
    ActionListener updateClockAction = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Assumes clock is a JLabel
        DateFormat hora = new SimpleDateFormat( "HH:mm:ss" );
        lblHora.setText( hora.format( new Date() ) );
        setDataPainel( obterData( "" ) );
        if( diagramaAtual != null ) {
          setLblTotalPr( diagramaAtual.getCronometro() );
        }
      }
    };
    this.comandoTela = "";
    this.relogio = new Timer( 1000, updateClockAction );
    
    if( OS.isWindows() ) {
      try{
        UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );
      }
      catch( ClassNotFoundException ex ){
        System.out.println( "ClassNotFoundException: " + ex.getLocalizedMessage() );
      }
      catch( InstantiationException ex ){
        System.out.println( "InstantiationException: " + ex.getLocalizedMessage() );
      }
      catch( IllegalAccessException ex ){
        System.out.println( "IllegalAccessException: " + ex.getLocalizedMessage() );
      }
      catch( UnsupportedLookAndFeelException ex ){
        System.out.println( "UnsupportedLookAndFeelException: " + ex.getLocalizedMessage() );
      }
    }
    
    this.pnlSuperior = new JPanel();
    this.pnlSuperior.setBounds( 0, 0, 800, 80 );
    this.pnlSuperior.setLayout( null );
    this.pnlSuperior.setBorder( BorderFactory.createEtchedBorder() );
    
    this.lblData = new JLabel();
    this.lblData.setBounds( 10, 20, 500, 21 );
    this.lblData.setFont( new Font( "Verdana", 0, 18 ) );
    
    this.lblHora = new JLabel();
    this.lblHora.setBounds( 700, 20, 100, 21);
    this.lblHora.setFont( new Font( "Verdana", 0, 18 ) );
    
    this.pnlInferior = new JPanel();
    this.pnlInferior.setBounds( 0, 80, 800, 520 );
    this.pnlInferior.setLayout( null );
    this.pnlInferior.setBorder( BorderFactory.createEtchedBorder() );
    
    this.lblCodigo = new JLabel( "Código:" );
    this.lblCodigo.setHorizontalAlignment( JLabel.RIGHT );
    this.lblCodigo.setBounds( 10, 10, 90, 21 ); // y = 31 + 5 // x = 100
    this.lblCodigo.setFont( new Font( "Verdana", 0, 12 ) );

    this.txfCodigo = new JTextField();
    this.txfCodigo.setBounds( 105, 10, 90, 21);
    this.txfCodigo.setFont( new Font( "Monospaced", 0, 12 ) );
	this.txfCodigo.addKeyListener( new KeyAdapter() {
        @Override
		public void keyPressed( KeyEvent kev ) {
			if( kev.getKeyCode() == KeyEvent.VK_ENTER ) {
				txfNome.requestFocus();
			}
		}
	});
    
    this.lblNome = new JLabel( "Nome:" );
    this.lblNome.setHorizontalAlignment( JLabel.RIGHT );
    this.lblNome.setBounds( 30, 36, 70, 21 ); // y = 57 + 5 // x = 100
    this.lblNome.setFont( new Font( "Verdana", 0, 12 ) );
    
    this.txfNome = new JTextField();
    this.txfNome.setBounds( 105, 36, 300, 21);
    this.txfNome.setFont( new Font( "Monospaced", 0, 12 ) );
	this.txfNome.addKeyListener( new KeyAdapter() {
        @Override
		public void keyPressed( KeyEvent kev ) {
			if( kev.getKeyCode() == KeyEvent.VK_ENTER ) {
				txfSolicitante.requestFocus();
			}
		}
	});
    
    this.lblSolicitante = new JLabel( "Solicitante:" );
    this.lblSolicitante.setHorizontalAlignment( JLabel.RIGHT );
    this.lblSolicitante.setBounds( 0, 62, 100, 21 ); // y = 83 + 5 // x = 100
    this.lblSolicitante.setFont( new Font( "Verdana", 0, 12 ) );
    
    this.txfSolicitante = new JTextField();
    this.txfSolicitante.setBounds( 105, 62, 120, 21);
    this.txfSolicitante.setFont( new Font( "Monospaced", 0, 12 ) );
	this.txfSolicitante.addKeyListener( new KeyAdapter() {
        @Override
		public void keyPressed( KeyEvent kev ) {
			if( kev.getKeyCode() == KeyEvent.VK_ENTER ) {
				txfObs.requestFocus();
			}
		}
	});
    
    this.lblObs = new JLabel( "Obs:" );
    this.lblObs.setHorizontalAlignment( JLabel.RIGHT );
    this.lblObs.setBounds( 60, 88, 40, 21 ); // y = 109 + 5 // x = 100
    this.lblObs.setFont( new Font( "Verdana", 0, 12 ) );
    
    this.txfObs = new JTextField();
    this.txfObs.setBounds( 105, 88, 500, 21);
    this.txfObs.setFont( new Font( "Monospaced", 0, 12 ) );
    this.txfObs.getDocument().addDocumentListener( new DocumentListener() {
      @Override
      public void changedUpdate( DocumentEvent e ) {
        AOMODIFICARtxfObs();
      }
      @Override
      public void removeUpdate( DocumentEvent e ) {
        AOMODIFICARtxfObs();
      }
      @Override
      public void insertUpdate( DocumentEvent e ) {
        AOMODIFICARtxfObs();
      }
    });
	this.txfObs.addKeyListener( new KeyAdapter() {
        @Override
		public void keyPressed( KeyEvent kev ) {
			if( kev.getKeyCode() == KeyEvent.VK_ENTER ) {
				comandoTela = "ADICIONAR_DIAGRAMA";
			}
		}
	});
    
    this.btnAdd = new JButton( "Adicionar" );
    this.btnAdd.setBounds( 650, 83, 100, 30 );
    this.btnAdd.setFont( new Font( "Verdana", 0, 12 ) );
    this.btnAdd.setToolTipText( "Adiciona o diagrama ao GRID." );
    this.btnAdd.addKeyListener( new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if( e.getKeyCode() == KeyEvent.VK_ENTER ) {
          comandoTela = "ADICIONAR_DIAGRAMA";
        }
      }
    });
    this.btnAdd.addActionListener( new ActionListener() {
      @Override
      public void actionPerformed( ActionEvent e ) {
        comandoTela = "ADICIONAR_DIAGRAMA";
      }
    });
    
    this.btnContinuar = new JButton( "Continuar" );
    this.btnContinuar.setBounds( 169, 140, 144, 30); // y = 135 + 5
    this.btnContinuar.setFont( new Font( "Verdana", 0, 12 ) );
    this.btnContinuar.setToolTipText( "Continua a contagem da tarefa selecionada." );
    this.btnContinuar.addActionListener( new ActionListener() {
      @Override
      public void actionPerformed( ActionEvent e ) {
        comandoTela = "CONTINUAR";
      }
    });
    
    this.btnParar = new JButton( "Parar/Terminar" );
    this.btnParar.setBounds( 20, 140, 144, 30 ); // y = 135 + 5
    this.btnParar.setFont( new Font( "Verdana", 0, 12 ) );
    this.btnParar.setToolTipText( "Para a contagem da tarefa selecionada." );
    this.btnParar.addActionListener( new ActionListener() {
      @Override
      public void actionPerformed( ActionEvent e ) {
        comandoTela = "PARAR";
      }
    });
    
    this.btnExcluir = new JButton( "Excluir ");
    this.btnExcluir.setBounds( 318, 140, 144, 30 ); // y = 135 + 5
    this.btnExcluir.setFont( new Font( "Verdana", 0, 12 ) );
    this.btnExcluir.setToolTipText( "Exclui a tarefa selecionada." );
    this.btnExcluir.addActionListener( new ActionListener() {
      @Override
      public void actionPerformed( ActionEvent e ) {
        comandoTela = "EXCLUIR";
      }
    });
    
    this.btnExportar = new JButton( "Exportar" );
    this.btnExportar.setBounds( 616, 140, 144, 30 ); // y = 135 + 5
    this.btnExportar.setFont( new Font( "Verdana", 0, 12 ) );
    this.btnExportar.setToolTipText( "Exporta para um arquivo CSV." );
    this.btnExportar.addActionListener( new ActionListener() {
      @Override
      public void actionPerformed( ActionEvent e ) {
        comandoTela = "EXPORTAR";
      }
    });
    
    this.btnCancelar = new JButton( "Cancelar" );
    this.btnCancelar.setBounds( 467, 140, 144, 30 );
    this.btnCancelar.setFont( new Font( "Verdana", 0, 12 ) );
    this.btnCancelar.addActionListener( new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        comandoTela = "CANCELAR";
      }
    });
    
    this.pnlContador = new JPanel();
    this.pnlContador.setBounds( 20, 170, 740, 120 );
    this.pnlContador.setEnabled( true );
    this.pnlContador.setLayout( null );
    this.pnlContador.setBorder( BorderFactory.createEtchedBorder() );
    
    this.pnlTempoDecorrido = new JPanel();
    this.pnlTempoDecorrido.setBounds( 20, 300, 740, 80 );
    this.pnlTempoDecorrido.setLayout( null );
    this.pnlTempoDecorrido.setBorder( BorderFactory.createEtchedBorder() );
    
    this.lblTempoDecorrido = new JLabel( "Tempo Tarefa:" );
    this.lblTempoDecorrido.setBounds( 10, 15, 115, 21 );
    this.lblTempoDecorrido.setFont( new Font( "Verdana", 0, 12 ) );
    this.lblTempoDecorrido.setHorizontalAlignment( JLabel.RIGHT );
    
    this.lblTotalPr = new JLabel();
    this.lblTotalPr.setBounds( 130, 15, 115, 21 );
    this.lblTotalPr.setFont( new Font( "Verdana", 0, 12 ) );
    
    this.lblTempoTodos = new JLabel( "Tempo Total:" );
    this.lblTempoTodos.setBounds( 10, 45, 115, 21 );
    this.lblTempoTodos.setFont( new Font( "Verdana", 0, 12 ) );
    this.lblTempoTodos.setHorizontalAlignment( JLabel.RIGHT );
    
    this.lblTotalTempo = new JLabel();
    this.lblTotalTempo.setBounds( 130, 45, 115, 21 );
    this.lblTotalTempo.setFont( new Font( "Verdana", 0, 12 ) );
    
    this.pnlExportar = new JPanel();
    this.pnlExportar.setLayout( null );
    this.pnlExportar.setBounds( 20, 390, 740, 80 );
    this.pnlExportar.setBorder( BorderFactory.createEtchedBorder() );
    
    this.lblExportarPara = new JLabel( "Exportar para:" );
    this.lblExportarPara.setBounds( 25, 15, 100, 21 );
    this.lblExportarPara.setHorizontalAlignment( JLabel.RIGHT );
    this.lblExportarPara.setFont( new Font( "Verdana", 0, 12 ) );
    
    this.txfDiretorio = new JTextField();
    this.txfDiretorio.setBounds( 130, 15, 400, 21 );
    this.txfDiretorio.setFont( new Font( "Monospaced", 0, 12 ) );
    
    this.btnProcurarDiretorio = new JButton();
    this.btnProcurarDiretorio.setBounds( 535, 15, 21, 21 );
    this.btnProcurarDiretorio.addActionListener( new ActionListener() {
      @Override
      public void actionPerformed( ActionEvent e ) {
        comandoTela = "PROCURAR_DIRETORIO";
        }
      }
    );
    
    this.lblNomeArquivo = new JLabel( "Nome do arquivo:" );
    this.lblNomeArquivo.setBounds( 10, 45, 115, 21 );
    this.lblNomeArquivo.setHorizontalAlignment( JLabel.RIGHT );
    this.lblNomeArquivo.setFont( new Font( "Verdana", 0, 12 ) );
    
    this.txfNomeArquivo = new JTextField();
    DateFormat df = new SimpleDateFormat( "yyyy_MM_dd" );
    this.txfNomeArquivo.setText( "Tarefas_" + df.format( new Date() ) + ".csv" );
    this.txfNomeArquivo.setText( txfNomeArquivo.getText() );
    this.txfNomeArquivo.setBounds( 130, 45, 200, 21 );
    this.txfNomeArquivo.setFont( new Font( "Monospaced", 0, 12 ) );
    
    this.getContentPane().add( this.pnlSuperior, null );
       this.pnlSuperior.add( this.lblData );
       this.pnlSuperior.add( this.lblHora );
       
    this.getContentPane().add( this.pnlInferior, null );
       this.pnlInferior.add( this.btnCancelar );
       this.pnlInferior.add( this.lblCodigo );
       this.pnlInferior.add( this.txfCodigo );
       this.pnlInferior.add( this.lblNome );
       this.pnlInferior.add( this.txfNome );
       this.pnlInferior.add( this.lblSolicitante );
       this.pnlInferior.add( this.txfSolicitante );
       this.pnlInferior.add( this.lblObs );
       this.pnlInferior.add( this.txfObs );
       this.pnlInferior.add( this.btnAdd );
       this.pnlInferior.add( this.btnContinuar );
       this.pnlInferior.add( this.btnParar );
       this.pnlInferior.add( this.btnExcluir );
       this.pnlInferior.add( this.btnExportar );
       this.pnlInferior.add( this.pnlContador );
       this.pnlInferior.add( this.pnlTempoDecorrido );
          this.pnlTempoDecorrido.add( this.lblTempoDecorrido );
          this.pnlTempoDecorrido.add( this.lblTotalPr );
          this.pnlTempoDecorrido.add( this.lblTempoTodos );
          this.pnlTempoDecorrido.add( this.lblTotalTempo );
       this.pnlInferior.add( this.pnlExportar );
       this.pnlExportar.add( this.lblExportarPara );
       this.pnlExportar.add( this.txfDiretorio );
       this.pnlExportar.add( this.btnProcurarDiretorio );
       this.pnlExportar.add( this.lblNomeArquivo );
       this.pnlExportar.add( this.txfNomeArquivo );
       
    /*   
    Component seqTabOrder[] = {txfCodPr, txfNomePr, cbxNomeCliente, txfObs, btnAdd};
    KugelFocus foco = new KugelFocus(seqTabOrder);
    setFocusTraversalPolicy(foco);
    */
  }
  private void iniciarPrograma( String pNomeCidade ) {
    setDataPainel( obterData( pNomeCidade ) );
    this.relogio.start();
    habilitarBotoes( false );
    
    // coloca o diretorio do desktop no caminho para exportar
    String home = System.getProperty( "user.home" );
    setTxfDiretorio( (OS.isWindows())? home + "\\Desktop\\" : home + "/" );
  }
  private void criarModel() {
    this.contadorTable = new JTable();
    this.contadorModel = new DiagramaModel();
    this.contadorTable.setModel(contadorModel);

    this.contadorTable.setShowGrid(false);
    this.contadorTable.getTableHeader().setReorderingAllowed( false );
    this.contadorTable.setBorder( BorderFactory.createEmptyBorder() );
    this.contadorTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    this.contadorTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
    this.contadorTable.setFont( new Font( "Monospaced", 0, 12 ) );
    
    this.contadorTable.getColumnModel().getColumn(0).setPreferredWidth( 80 );
    this.contadorTable.getColumnModel().getColumn(1).setPreferredWidth( 270 );
    this.contadorTable.getColumnModel().getColumn(2).setPreferredWidth( 110 );
    this.contadorTable.getColumnModel().getColumn(3).setPreferredWidth( 70 );
    this.contadorTable.getColumnModel().getColumn(4).setPreferredWidth( 70 );
    this.contadorTable.getColumnModel().getColumn(5).setPreferredWidth( 70 );
    this.contadorTable.getColumnModel().getColumn(6).setPreferredWidth( 60 );

    this.pnlContador.setLayout( new BorderLayout() );
    this.pnlContador.add( new JScrollPane( contadorTable ) );
    
    class RRenderer extends DefaultTableCellRenderer {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if( row % 2 == 0 ) {
          comp.setBackground(new Color(202, 225, 255));
        }
        else{
          comp.setBackground(new Color(254, 254, 254));
        }
    
        if( isSelected ){
          comp.setBackground( new Color( comp.getBackground().getRed() - 40, comp.getBackground().getGreen() - 40, comp.getBackground().getBlue() - 40 ) );
        }
        return( comp );
      }
    }
    this.contadorTable.setDefaultRenderer( Object.class, new RRenderer() );
    this.contadorTable.setDefaultRenderer( String.class, new RRenderer() );
  }
  public void AOPRESSIONARSetas(KeyEvent ke) {
    if( ke.getKeyCode() == KeyEvent.VK_DOWN ) {
      this.comandoTela = "CARREGAR_PROXIMA_LINHA";
    }
    else if( ke.getKeyCode() == KeyEvent.VK_UP ) {
      this.comandoTela = "CARREGAR_LINHA_ANTERIOR";
    }
  }
  public void habilitarBotoes( boolean sim ) {
    this.btnContinuar.setEnabled( sim );
    this.btnParar.setEnabled( sim );
    this.btnExcluir.setEnabled( sim );
  }
  public void habilitarContinuar( boolean pHab ) {
    this.btnContinuar.setEnabled( !pHab );
    this.btnParar.setEnabled( pHab );
  }
  public void habilitarBotaoInserir( boolean sim ) {
    this.btnAdd.setEnabled( sim );
  }
  public void habilitarExcluir( boolean sim ) {
    this.btnExcluir.setEnabled( sim );
  }
  private String obterData( String pNomeCidade ) {
    String cidade = "";
    
    if( !pNomeCidade.isEmpty() ) {
      cidade = pNomeCidade + ", ";
    }
    
    DateFormat dia = new SimpleDateFormat( "dd" );
    DateFormat mes = new SimpleDateFormat( "MMMM" );
    DateFormat ano = new SimpleDateFormat( "yyyy" );
    DateFormat hora = new SimpleDateFormat( "HH" ); 
    
    Date dataAtual = new Date();
    
    String saudacao = (Integer.parseInt(String.valueOf( hora.format( dataAtual ))) <= 12)? "Bom dia!" : "Boa tarde!"; 
    
    return(
      cidade +
      dia.format( dataAtual ) + " de " + mes.format( dataAtual ) + " de " + ano.format( dataAtual ) + ". " +
      saudacao
    );
  }
  public void acessar() {
    try {
      comandoTela = "";
      
      while( comandoTela.isEmpty() ) {
        Thread.sleep(50);
      }
    }
    catch( InterruptedException e ) {
      e.printStackTrace();
    }
  }
  public void limpar() {
    setTxfCodigo( "" );
    setTxfNome( "" );
    setTxfSolicitante( "" );
    setTxfObs( "" );
  }
  public String getTxfCodigo() {
    return( this.txfCodigo.getText().trim() );
  }
  public String getTxfNome() {
    return( this.txfNome.getText().trim() );
  }
  public String getTxfSolicitante() {
    return( this.txfSolicitante.getText() );
  }
  public String getTxfObs() {
    return( this.txfObs.getText() );
  }
  public void setTxfCodigo( String codParam ) {
    this.txfCodigo.setText( codParam );
  }
  public void setTxfNome( String nomeParam ) {
    this.txfNome.setText( nomeParam );
  }
  public void setTxfSolicitante( String pSolicitante ) {
    this.txfSolicitante.setText( pSolicitante );
  }
  public void setTxfObs( String obsParam ) {
    this.txfObs.setText( obsParam );
  }
  public void setTxfDiretorio( String dirParam ) {
    txfDiretorio.setText( dirParam );
  }
  public String getTxfDiretorio() {
    return( txfDiretorio.getText() );
  }
  public void setTxfNomeArquivo( String nomeParam ) {
    txfNomeArquivo.setText( nomeParam );
  }
  public String getTxfNomeArquivo() {
    return( txfNomeArquivo.getText() );
  }
  public String getComandoTela() {
    return( this.comandoTela );
  }
  public String getLblSuperior() {
    return( this.lblData.getText() );
  }
  public void setDataPainel( String lblParam ) {
    this.lblData.setText( lblParam );
  }
  public void addDiagrama( Diagrama diagramaParam ) {
    this.contadorModel.addLinha( diagramaParam );
  }
  public void limparDiagrama() {
    this.contadorModel.limpar();
  }
  public void setLblTotalPr( String param ) {
    this.lblTotalPr.setText( param );
  }
  public void setLblTotalTempo( String param ) {
    this.lblTotalTempo.setText( param );
  }
  public void limparTempoDecorrido() {
    this.lblTotalPr.setText( "" );
    this.lblTotalTempo.setText( "" );
  }
  public void setDiagrama( Diagrama dParam ) {
    this.diagramaAtual = dParam;
  }
  public Diagrama getDiagrama() {
    return( this.diagramaAtual );
  }
  public void AOMODIFICARtxfObs() {
    this.comandoTela = "ALTERAR_OBS";
  }
  public void carregarDiagramaPosicaoA( Diagrama pDia, char pPosicao ) {
    Collection<Diagrama> dList = this.contadorModel.getLinhas();
    int contador = 0;
    
    if( dList != null && !dList.isEmpty() ) {
      for( Diagrama d : dList ) {
        if( pDia.equals( d ) ) {
          break;
        }
        contador++;
      }
    }
    
    if( pPosicao == '-' ) {
      contador--;
    }
    else {
      contador++;
    }
    
    if( (contador+1) > this.contadorModel.getRowCount() || contador < 0 ) {
      return;
    }
    
    Diagrama antes = this.contadorModel.getLinha( contador );
    
    if( antes != null ) {
      setDiagrama( antes );
      setTxfCodigo( antes.getCodigo() );
      setTxfNome( antes.getNome() );
      setTxfSolicitante( antes.getSolicitante() );
      setTxfObs( antes.getObs() );
    }
  }
  public Diagrama getLinhaSelecionada() {
    int linha = this.contadorTable.getSelectedRow();
    return( this.contadorModel.getLinha( linha ) );
  }
}