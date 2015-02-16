package view;

import java.awt.geom.AffineTransform;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicLabelUI;
import com.sun.security.auth.module.NTSystem;
import data.Tarefa;
import data.TarefaResultado;
import model.TarefaModel;
import model.TarefaResultadoModel;
import utils.Mensagem;
import utils.OS;
import utils.StatusTarefa;

public class Tela extends JFrame {

	public static int larguraBotao = 92;
	private String comandoTela;
	private Timer  relogio;
	private Tarefa tarefaAtual;
	private FrameConsole console;

	private JPanel pnlSuperior;
	private JLabel lblData;
	private JLabel lblHora;
	private JLabel lblLogin;

	private JTabbedPane tbpPainelAbas;
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
	private JButton btnTray;
	private JButton btnLinxERP;
	private JButton btnMSSQL;
	private JPopupMenu popupSQL;
	private JButton btnERP;
	private JButton btnAnexos;
	private JPopupMenu popupERP;
    private JButton btnAdd;
    private JButton btnContinuar;
    private JButton btnParar;
    private JButton btnExcluir;
    private JButton btnExportar;
	private JButton btnAlterar;
    private JPanel pnlContador;
	private JTextArea txaDetalhesFollowUp;
	private JScrollPane scrollPaneHome;
	private JPanel pnlTempoDecorrido;
    private JLabel lblTempoDecorrido;
    private JLabel lblTotalTarefa;
    private JLabel lblTempoTodos;
    private JLabel lblTotalTempo;
    private JPanel pnlExportar;
    private JLabel lblExportarPara;
    private JTextField txfDiretorio;
    private JButton btnProcurarDiretorio;
	private JPanel pnlEvolucao;
	private JButton btnMiniSQL;
	private JButton btnMiniERP;
    private JLabel lblTarefa;
	private JTextField txfCodTarefa;
	private JLabel lblDescrTarefa;
	private JTextArea pnlParse;
	private JScrollPane scrollPane;
	private JPanel containerCampos;
	private JPanel pnlBuscar;
	private JLabel lblTarefaBuscar;
	private JLabel lblTemposTotalEMedio;
	private JTextField txfTarefaBuscar;
	private JLabel lblPeriodoBuscar;
	private JDateChooser dtcPeriodoInicial;
	private JLabel lblA;
	private JDateChooser dtcPeriodoFinal;
	private JLabel lblStatusBuscar;
	private JRadioButton rbtTodos;
	private JRadioButton rbtFinalizados;
	private JRadioButton rbtNaoFinalizados;
	private JRadioButton rbtEmAndamento;
	private JLabel lblTextoLivre;
	private JTextField txfTextoLivre;
	private JButton btnBuscar;
	private JPanel pnlResultado;
	private JTable resultadoTable;
	private TarefaResultadoModel resultadoModel;
	private JButton btnReativarBusca;
	private JButton btnExcluirBusca;

	private JPanel pnlAnotacoes;
	private JLabel lblCaracteres;
	private JTextArea txaAnotacoes;
	private JScrollPane scrollPaneAnotacoes;

	public JTable contadorTable;
	public TarefaModel contadorModel;
	private TrayIcon trayIcon;
	private SystemTray tray;
	private JPopupMenu popm;
	private JScrollPane scpBusca;

	private String usuarioLogado;

	public Tela( final String pNomeCidade ) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					iniciarComponentes();
				}
			});
		} catch (InterruptedException | InvocationTargetException e) {
			System.out.println(e.getMessage());
		}

		this.getContentPane().setLayout( null );
		this.setSize( 815, 640 );
		this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
		iniciarPrograma( pNomeCidade );
		criarModel();
		criarModelResultado();
		iniciarConsole();
		setLocationRelativeTo( null );
		setarIcones();
		adicionarListener();
		verificarLoginWindows();
	}

	private void verificarLoginWindows() {
		NTSystem s = new NTSystem();

		if( s.getName().isEmpty() ) {
			Mensagem.informacao( "Não foi possível obter suas credenciais!", this );
			System.exit( 0 );
		}

		this.lblLogin.setText( this.lblLogin.getText() + s.getName() );
		this.usuarioLogado = s.getName();
	}

	private void adicionarListener() {
		if( !SystemTray.isSupported() ) {
			return;
		}

		this.tray = SystemTray.getSystemTray();
		Image img = new ImageIcon(getClass().getResource("/images/clock.png")).getImage();
		PopupMenu pop = new PopupMenu();
		final MenuItem defaultItem = new MenuItem( "Restaurar" );
		defaultItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				setVisible( true );
				setExtendedState( JFrame.NORMAL );
			}
		});

		pop.add( defaultItem );

		this.trayIcon = new TrayIcon( img, "Contador", pop );
		this.trayIcon.setImageAutoSize( true );

		this.trayIcon.addMouseListener( new MouseListener() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				if( e.getClickCount() == 2 ) {
					tray.remove( trayIcon );
					setVisible( true );
					setExtendedState( JFrame.NORMAL );
				}
			}

			@Override
			public void mousePressed( MouseEvent e ) {
				if( e.getClickCount() == 2 ) {
					tray.remove( trayIcon );
					setVisible( true );
					setExtendedState( JFrame.NORMAL );
				}
			}

			@Override
			public void mouseReleased( MouseEvent e ) {
				if( e.getClickCount() == 2 ) {
					tray.remove( trayIcon );
					setVisible( true );
					setExtendedState( JFrame.NORMAL );
				}
			}

			@Override
			public void mouseEntered( MouseEvent e ) {
				if( e.getClickCount() == 2 ) {
					tray.remove( trayIcon );
					setVisible( true );
					setExtendedState( JFrame.NORMAL );
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if( e.getClickCount() == 2 ) {
					tray.remove( trayIcon );
					setVisible( true );
					setExtendedState( JFrame.NORMAL );
				}
			}
		});

		addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing( WindowEvent e ) {
				comandoTela = "SAIR";
			}
		});

		addWindowStateListener( new WindowStateListener() {
			@Override
			public void windowStateChanged( WindowEvent e ) {
				if( e.getNewState() == MAXIMIZED_BOTH ) {
					tray.remove( trayIcon );
					setVisible( true );
				}
				if( e.getNewState() == NORMAL ) {
					tray.remove( trayIcon );
					setVisible( true );
				}
			}
		});

		addComponentListener( new ComponentAdapter(){
			@Override
			public void componentResized( ComponentEvent e ){
				ajustarPosicao();
			}
		});

		this.txaAnotacoes.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed( KeyEvent ke ) {
				lblCaracteres.setText( String.valueOf( 5000 - getTxaAnotacoes().length() ) + " Caracteres Restantes" );
			}
		});

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher( new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent( KeyEvent e ) {
				try {
					if( e.isControlDown() && e.getKeyCode() == KeyEvent.VK_I && e.getID() == KeyEvent.KEY_PRESSED ) {
						if( console.isVisible() ) {
							console.setVisible( false );
			} else {
							console.setVisible( true );
						}
					}
		} catch (Exception exc) {
					System.out.println( "Erro ao disparar evento de tecla: " + exc.getMessage() );
				}
				return false;
			}
		});
		this.contadorTable.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				if( e.getClickCount() == 1 ) {
					comandoTela = "CARREGAR_TAREFA";
				}
			}
		});
		this.contadorTable.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed( KeyEvent ke ) {
				if( ke.getKeyCode() == KeyEvent.VK_DOWN ) {
					comandoTela = "CARREGAR_PROXIMA_LINHA";
		} else if (ke.getKeyCode() == KeyEvent.VK_UP) {
					comandoTela = "CARREGAR_LINHA_ANTERIOR";
				}
			}
		});
		this.addWindowListener( new WindowListener(){
	    @Override
			public void windowClosing( WindowEvent we ){
				if( todasTarefasParadas() ){
					System.exit(0);
		} else {
					Mensagem.informacao( "Não é possível sair com tarefas em andamento.", null );
				}
			}

	    @Override
	    public void windowClosed(WindowEvent we) {
	    }

	    @Override
	    public void windowDeactivated(WindowEvent we) {
	    }

	    @Override
	    public void windowDeiconified(WindowEvent we) {
	    }

	    @Override
	    public void windowIconified(WindowEvent we) {
	    }

	    @Override
	    public void windowActivated(WindowEvent we) {
	    }

	    @Override
	    public void windowOpened(WindowEvent we) {
	    }
		});
		this.txfCodigo.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed( KeyEvent kev ) {
				if( kev.getKeyCode() == KeyEvent.VK_ENTER ) {
					setTxfCodTarefa( getTxfCodigo() );
					carregarPaginaEvolucao( 'S' );
					txfNome.requestFocus();
				}
			}
		});
		this.txfCodigo.addFocusListener( new FocusAdapter() {
			@Override
			public void focusGained( FocusEvent fe ) {
				txfCodigo.selectAll();
			}
		});
		this.txfNome.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed( KeyEvent kev ) {
				if( kev.getKeyCode() == KeyEvent.VK_ENTER ) {
					txfSolicitante.requestFocus();
				}
			}
		});
		this.txfNome.addFocusListener( new FocusAdapter() {
		@Override
			public void focusGained( FocusEvent fe ) {
				txfNome.selectAll();
			}
		});
		this.txfSolicitante.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed( KeyEvent kev ) {
				if( kev.getKeyCode() == KeyEvent.VK_ENTER ) {
					txfObs.requestFocus();
				}
			}
		});
		this.txfSolicitante.addFocusListener( new FocusAdapter() {
			@Override
			public void focusGained( FocusEvent fe ) {
				txfSolicitante.selectAll();
			}
		});
		this.txfObs.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed( KeyEvent kev ) {
				if( kev.getKeyCode() == KeyEvent.VK_ENTER ) {
					comandoTela = "ADICIONAR_TAREFA";
				}
			}
		});
		this.txfObs.addFocusListener( new FocusAdapter() {
			@Override
			public void focusGained( FocusEvent fe ) {
				txfObs.selectAll();
			}
		});
		this.btnLinxERP.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				iniciarERPLinx();
			}
		});
		this.btnTray.addActionListener( new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed( java.awt.event.ActionEvent evt ) {
				try {
					tray.add( trayIcon );
					setVisible( false );
					setExtendedState( JFrame.MAXIMIZED_BOTH );
		} catch (AWTException ex) {
					System.out.println( "ERRO: AWTException: " + ex.getLocalizedMessage() );
				}
			}
		});
		this.btnAdd.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				comandoTela = "ADICIONAR_TAREFA";
			}
		});
		this.btnContinuar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				comandoTela = "CONTINUAR";
			}
		});
		this.btnParar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				comandoTela = "PARAR";
			}
		});
		this.btnExcluir.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				comandoTela = "EXCLUIR";
			}
		});
		this.btnAlterar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				comandoTela = "ALTERAR";
			}
		});
		this.btnExportar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				comandoTela = "EXPORTAR";
			}
		});
		this.btnCancelar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comandoTela = "CANCELAR";
			}
		});
		this.btnAnexos.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comandoTela = "DOWNLOAD";
			}
		});
		this.btnProcurarDiretorio.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				comandoTela = "PROCURAR_DIRETORIO";
			}
		});
		this.btnMiniSQL.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				switch( e.getButton() ) {
		    case 1: {
			iniciarSQLServer("SUPORTE");
			break;
		    }
		    case 2: {
			iniciarSQLServer("MIC");
		    }
				}
			}
		});
		this.btnMiniERP.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				switch( e.getButton() ) {
		    case 1: {
			iniciarERP("CORRECAO");
			break;
		    }
		    case 2: {
			iniciarERP("PRODUCAO");
		    }
				}
			}
		});
		this.txfCodTarefa.addKeyListener( new KeyAdapter(){
			@Override
			public void keyPressed( KeyEvent ke ){
				if( ke.getKeyCode() == KeyEvent.VK_ENTER ){
					lblDescrTarefa.setText( "" );
					carregarPaginaEvolucao( 'N' );
				}
			}
		});
		this.txfTarefaBuscar.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed( KeyEvent kev ) {
				if( kev.getKeyCode() == KeyEvent.VK_ENTER ) {
					dtcPeriodoInicial.requestFocus();
				}
			}
		});
		this.txfTarefaBuscar.addFocusListener( new FocusAdapter() {
			@Override
			public void focusGained( FocusEvent fe ) {
				txfTarefaBuscar.selectAll();
			}
		});
		this.dtcPeriodoInicial.addKeyListener( new KeyAdapter() {
			@Override
			public void keyPressed( KeyEvent kev ) {
				if( kev.getKeyCode() == KeyEvent.VK_ENTER ) {
					dtcPeriodoFinal.requestFocus();
				}
			}
		});
		this.btnBuscar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent evt ) {
				comandoTela = "BUSCAR";
			}
		});
		this.btnReativarBusca.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent evt ) {
				comandoTela = "REATIVAR";
			}
		});
		this.btnExcluirBusca.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent evt ) {
				comandoTela = "EXCLUIR_SELECAO";
			}
		});
		this.contadorTable.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				int linha = contadorTable.rowAtPoint( new Point( e.getX(), e.getY() ) );
				int coluna = contadorTable.columnAtPoint( new Point( e.getX(), e.getY() ) );
				String conteudoCelula = (String) contadorTable.getValueAt( linha, coluna );

				if( conteudoCelula == null || conteudoCelula.isEmpty() || coluna > 0 ) {
					return;
				}

				try {
					final URI uri = new URI( "http://a-srv63/suporte/followup_find.asp?OBJETO_ID=" + conteudoCelula );
					if( Desktop.isDesktopSupported() ) {
						try {
							Desktop.getDesktop().browse( uri );
			} catch (IOException ie) {
			}
		    }
		} catch (URISyntaxException ex) {
		}
			}

			@Override
			public void mouseEntered( MouseEvent e ) {
				int coluna = contadorTable.columnAtPoint( new Point( e.getX(), e.getY() ) );
				if( coluna == 0 ) {
					contadorTable.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
				}
			}

			@Override
			public void mouseExited( MouseEvent e ) {
				int coluna = contadorTable.columnAtPoint( new Point( e.getX(), e.getY() ) );
				if( coluna > 0 ) {
					contadorTable.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
				}
			}
		});
		this.btnERP.addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed( MouseEvent e ) {
				popupERP.show( e.getComponent(), e.getX(), e.getY() );
			}
		});
		this.btnMSSQL.addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed( MouseEvent e ) {
				popupSQL.show( e.getComponent(), e.getX(), e.getY() );
			}
		});
	}

	private void carregarDadosLidos( String pTexto ) {
		String tituloTp = "";
		String solicitanteTp = "";
		String obsTp = "";

		for( String linha : pTexto.split( "\n" ) ) {
			if( tituloTp.isEmpty() && linha.contains( "Chamado:" ) ) {
				tituloTp = linha;
			}
			if( solicitanteTp.isEmpty() && linha.contains( "Cadastrado por:" ) ) {
				solicitanteTp = linha.replaceAll( "Cadastrado por:", "" );
			}
			if( obsTp.isEmpty() && linha.contains( "Fim Previsto" ) ) {
				obsTp = linha;
			}

			if( !solicitanteTp.isEmpty() ) {
				break;
			}
		}

		setTxfNome( tituloTp );
		setTxfSolicitante( solicitanteTp );
		setTxfObs( obsTp );
	}

	private void ajustarPosicao() {
		final int largura = this.getWidth();
		final int altura = this.getHeight();
		final int alturaTela = Toolkit.getDefaultToolkit().getScreenSize().height;

		// ajusta a largura do painel superior
		this.pnlSuperior.setBounds( this.pnlSuperior.getX(), this.pnlSuperior.getY(), largura, this.pnlSuperior.getHeight() );

		// ajusta a largura e tamanho do painel inferior (com abas)
		this.tbpPainelAbas.setBounds( this.tbpPainelAbas.getX(), this.tbpPainelAbas.getY(), largura, (altura-this.pnlSuperior.getHeight()) );

		// ajusta a posicao do painel containerCampos
		this.containerCampos.setBounds( ((largura-this.containerCampos.getWidth())/2)-20, this.containerCampos.getY(), this.containerCampos.getWidth(), this.containerCampos.getHeight() );

		// centraliza o painel grid
		this.pnlContador.setBounds( ((largura-this.pnlContador.getWidth())/2)-20, this.pnlContador.getY(), this.pnlContador.getWidth(), this.pnlContador.getHeight() );

		// centraliza o painel followup_home
	int alturaPainel = altura - (this.pnlSuperior.getHeight()
		+ this.containerCampos.getHeight()
		+ this.pnlContador.getHeight()
		+ this.pnlTempoDecorrido.getHeight()
		+ this.pnlExportar.getHeight() + 80);
		this.scrollPaneHome.setBounds( ((largura-this.pnlTempoDecorrido.getWidth())/2)-20, this.pnlContador.getY() + this.pnlContador.getHeight()+10, this.scrollPaneHome.getWidth(), alturaPainel );

		// centraliza o painel de tempo
		this.pnlTempoDecorrido.setBounds( ((largura-this.pnlTempoDecorrido.getWidth())/2)-20, this.scrollPaneHome.getY() + this.scrollPaneHome.getHeight() + 10, this.pnlTempoDecorrido.getWidth(), this.pnlTempoDecorrido.getHeight() );

		// centraliza o painel de exportar
		this.pnlExportar.setBounds( ((largura-this.pnlExportar.getWidth())/2)-20, this.pnlTempoDecorrido.getY() + this.pnlTempoDecorrido.getHeight() + 10, this.pnlExportar.getWidth(), this.pnlExportar.getHeight() );

		// ajusta a largura e altura do painel de parse
		this.pnlParse.setBounds( this.pnlParse.getX(), this.pnlParse.getY(), largura-20, altura-200 );
		this.scrollPane.setBounds( this.scrollPane.getX(), this.scrollPane.getY(), largura-70, altura-180 );

		// ajusta a largura e altura do painel de resultado
		this.pnlResultado.setBounds( 10, 125, pnlBuscar.getWidth()-60, pnlBuscar.getHeight()-195 );
		this.scpBusca.setBounds( 10, 125, pnlBuscar.getWidth()-60, pnlBuscar.getHeight()-195 );

		// ajusta a posicao dos botoes do painel de busca
		this.btnReativarBusca.setBounds( 20, pnlResultado.getHeight()+130, 150, 30 );
		this.btnExcluirBusca.setBounds( 190, pnlResultado.getHeight()+130, 150, 30 );

		// ajusta a largura e altura do painel de anotações
		this.txaAnotacoes.setBounds( this.txaAnotacoes.getX(), this.txaAnotacoes.getY(), largura-20, altura-200 );
		this.scrollPaneAnotacoes.setBounds( this.scrollPaneAnotacoes.getX(), this.scrollPaneAnotacoes.getY(), largura-70, altura-180 );
	}

	private void setarIcones() {
		try {
			this.setIconImage(new ImageIcon(getClass().getResource("/images/clock.png")).getImage());
			this.btnProcurarDiretorio.setIcon(new ImageIcon(getClass().getResource("/images/folder.png")));
			this.btnLinxERP.setIcon(new ImageIcon(getClass().getResource("/images/logolinx.png")));
			this.btnMSSQL.setIcon(new ImageIcon(getClass().getResource("/images/sql.png")));
			this.btnERP.setIcon(new ImageIcon(getClass().getResource("/images/erp.png")));
			this.btnContinuar.setIcon(new ImageIcon(getClass().getResource("/images/play.png")));
			this.btnParar.setIcon(new ImageIcon(getClass().getResource("/images/pause.png")));
			this.btnExcluir.setIcon(new ImageIcon(getClass().getResource("/images/trash.png")));
			this.btnCancelar.setIcon(new ImageIcon(getClass().getResource("/images/cancel.png")));
			this.btnAlterar.setIcon(new ImageIcon(getClass().getResource("/images/edit.png")));
			this.btnAnexos.setIcon(new ImageIcon(getClass().getResource("/images/download.png")));
			this.btnMiniSQL.setIcon(new ImageIcon(getClass().getResource("/images/sql16.png")));
			this.btnMiniERP.setIcon(new ImageIcon(getClass().getResource("/images/erp16.png")));
		} catch (Exception ex) {
			System.out.println( "Exception: " + ex.getMessage() );
			ex.printStackTrace();
		}
	}

	public boolean todasTarefasParadas(){
		Collection<Tarefa> dList = this.contadorModel.getLinhas();

		for( Tarefa t : dList ){
			if( t.emAndamento() ){
				return( false );
			}
		}
		return( true );
	}

	private void iniciarConsole() {
		this.console = new FrameConsole();
		this.console.setVisible( false );
		this.console.setTitle( "Console" );
	}

	public void mudarEstado( final String pEstado ) {
		boolean estado = true;

		if( pEstado.equals( "EDICAO" ) ) {
			estado = false;
		}

		this.txfCodigo.setEditable( estado );
	}

	private void iniciarComponentes() {
		this.tarefaAtual = null;
		// Fonte: http://stackoverflow.com/questions/2959718/dynamic-clock-in-java
		ActionListener updateClockAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Assumes clock is a JLabel
				DateFormat hora = new SimpleDateFormat( "HH:mm:ss" );
				lblHora.setText( hora.format( new Date() ) );
				setDataPainel( obterData( "" ) );
				if( tarefaAtual != null ) {
					setLblTotalTarefa( tarefaAtual.getCronometro() );
				}
			}
		};
		this.comandoTela = "";
		this.relogio = new Timer( 1000, updateClockAction );

		if( OS.isWindows() ) {
			try{
				UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );
	    } catch (ClassNotFoundException ex) {
				System.out.println( "ClassNotFoundException: " + ex.getLocalizedMessage() );
	    } catch (InstantiationException ex) {
				System.out.println( "InstantiationException: " + ex.getLocalizedMessage() );
	    } catch (IllegalAccessException ex) {
				System.out.println( "IllegalAccessException: " + ex.getLocalizedMessage() );
	    } catch (UnsupportedLookAndFeelException ex) {
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

		this.lblLogin = new JLabel( "Logado como: " );
		this.lblLogin.setBounds( 10, 60, 200, 21);
		this.lblLogin.setFont( new Font( "Verdana", Font.ITALIC, 10 ) );

		this.tbpPainelAbas = new JTabbedPane( JTabbedPane.LEFT );
		this.tbpPainelAbas.setBounds( 0,80, 800, 520 );
		this.tbpPainelAbas.setBorder( BorderFactory.createEtchedBorder() );
		this.tbpPainelAbas.setFont( new Font( "Verdana", 0, 12 ) );

		this.pnlInferior = new JPanel();
		this.pnlInferior.setBounds( 0, 0, 800, 520 );
		this.pnlInferior.setLayout( null );
		this.pnlInferior.setName( "Tarefas" );
		this.pnlInferior.setBorder( BorderFactory.createEtchedBorder() );

		this.containerCampos = new JPanel();
		this.containerCampos.setLayout( null );
		this.containerCampos.setBounds( 0, 2, 780, 170 );

		this.lblCodigo = new JLabel( "Tarefa (TP):" );
		this.lblCodigo.setHorizontalAlignment( JLabel.RIGHT );
		this.lblCodigo.setBounds( 10, 10, 90, 21 ); // y = 31 + 5 // x = 100
		this.lblCodigo.setFont( new Font( "Verdana", 0, 12 ) );

		this.txfCodigo = new JTextField();
		this.txfCodigo.setBounds( 105, 10, 90, 21);
		this.txfCodigo.setFont( new Font( "Monospaced", 0, 12 ) );

		this.lblNome = new JLabel( "Descrição:" );
		this.lblNome.setHorizontalAlignment( JLabel.RIGHT );
		this.lblNome.setBounds( 30, 36, 70, 21 ); // y = 57 + 5 // x = 100
		this.lblNome.setFont( new Font( "Verdana", 0, 12 ) );

		this.txfNome = new JTextField();
		this.txfNome.setBounds( 105, 36, 300, 21);
		this.txfNome.setFont( new Font( "Monospaced", 0, 12 ) );

		this.lblSolicitante = new JLabel( "Solicitante:" );
		this.lblSolicitante.setHorizontalAlignment( JLabel.RIGHT );
		this.lblSolicitante.setBounds( 0, 62, 100, 21 ); // y = 83 + 5 // x = 100
		this.lblSolicitante.setFont( new Font( "Verdana", 0, 12 ) );

		this.txfSolicitante = new JTextField();
		this.txfSolicitante.setBounds( 105, 62, 120, 21);
		this.txfSolicitante.setFont( new Font( "Monospaced", 0, 12 ) );

		this.lblObs = new JLabel( "Obs:" );
		this.lblObs.setHorizontalAlignment( JLabel.RIGHT );
		this.lblObs.setBounds( 60, 88, 40, 21 ); // y = 109 + 5 // x = 100
		this.lblObs.setFont( new Font( "Verdana", 0, 12 ) );

		this.txfObs = new JTextField();
		this.txfObs.setBounds( 105, 88, 500, 21);
		this.txfObs.setFont( new Font( "Monospaced", 0, 12 ) );

		this.btnTray = new JButton( "Tray" );
		this.btnTray.setBounds( 650, 45, 100, 30 );
		this.btnTray.setToolTipText( "Enviar para o system tray" );
		this.btnTray.setFocusable( false );

		this.btnLinxERP = new JButton( "ERP" );
		this.btnLinxERP.setBounds( 650, 10, 100, 30 );
		this.btnLinxERP.setToolTipText( "Abrir ERP Linx" );
		this.btnLinxERP.setFocusable( false );

		this.btnAdd = new JButton( "Adicionar" );
		this.btnAdd.setBounds( 650, 83, 100, 30 );
		this.btnAdd.setFont( new Font( "Verdana", 0, 12 ) );
		this.btnAdd.setToolTipText( "Adiciona o Tarefa ao GRID." );
		this.btnAdd.setFocusable( false );

		this.btnParar = new JButton();
		this.btnParar.setBounds( 20, 140, Tela.larguraBotao, 30 );
		this.btnParar.setFont( new Font( "Verdana", 0, 12 ) );
		this.btnParar.setToolTipText( "Para a contagem da tarefa selecionada." );
		this.btnParar.setFocusable( false );

		this.btnContinuar = new JButton();
		this.btnContinuar.setBounds( (20+Tela.larguraBotao), 140, Tela.larguraBotao, 30);
		this.btnContinuar.setFont( new Font( "Verdana", 0, 12 ) );
		this.btnContinuar.setToolTipText( "Continua a contagem da tarefa selecionada" );
		this.btnContinuar.setFocusable( false );

		this.btnExcluir = new JButton();
		this.btnExcluir.setBounds( (20+Tela.larguraBotao*2), 140, Tela.larguraBotao, 30 );
		this.btnExcluir.setFont( new Font( "Verdana", 0, 12 ) );
		this.btnExcluir.setToolTipText( "Exclui a tarefa selecionada" );
		this.btnExcluir.setFocusable( false );

		this.btnCancelar = new JButton();
		this.btnCancelar.setBounds( (20+Tela.larguraBotao*3), 140, Tela.larguraBotao, 30 );
		this.btnCancelar.setFont( new Font( "Verdana", 0, 12 ) );
		this.btnCancelar.setToolTipText( "Cancelar" );
		this.btnCancelar.setFocusable( false );

		this.btnAlterar = new JButton();
		this.btnAlterar.setBounds( (20+Tela.larguraBotao*4), 140, Tela.larguraBotao, 30 );
		this.btnAlterar.setFont( new Font( "Verdana", 0, 12 ) );
		this.btnAlterar.setToolTipText( "Alterar" );
		this.btnAlterar.setFocusable( false );

		this.btnERP = new JButton();
		this.btnERP.setBounds( (20+Tela.larguraBotao*5), 140, Tela.larguraBotao, 30 );
		this.btnERP.setFont( new Font( "Verdana", 0, 12 ) );
		this.btnERP.setToolTipText( "<HTML>Click NORMAL para CORRECAO<BR>Click DO MEIO para PRODUCAO</HTML>" );
		this.btnERP.setFocusable( false );

		this.popupERP = new JPopupMenu();
		String[] itensMenuERP = { "Correção ERP", "Correção FFC", "Correção M", "Produção" };
	for (final String name : itensMenuERP) {
			this.popupERP.add( new JMenuItem( new AbstractAction( name ) {
				@Override
				public void actionPerformed( ActionEvent e ) {
					iniciarERP( name );
				}
			}));
		}

		this.btnMSSQL = new JButton();
		this.btnMSSQL.setBounds( (20+Tela.larguraBotao*6), 140, Tela.larguraBotao, 30 );
		this.btnMSSQL.setFont( new Font( "Verdana", 0, 12 ) );
		this.btnMSSQL.setToolTipText( "Abrir SQL Server conectando no portal selecionado" );
		this.btnMSSQL.setFocusable( false );

		this.popupSQL = new JPopupMenu();
		String[] itensMenuSQL = { "mic", "suporte" };
		for (final String name : itensMenuSQL) {
			this.popupSQL.add( new JMenuItem( new AbstractAction( name ) {
				@Override
				public void actionPerformed( ActionEvent e ) {
					iniciarSQLServer( name );
				}
			}));
		}
		
		this.btnAnexos = new JButton();
		this.btnAnexos.setBounds( (20+Tela.larguraBotao*7), 140, Tela.larguraBotao, 30 );
		this.btnAnexos.setFont( new Font( "Verdana", 0, 12 ) );
		this.btnAnexos.setToolTipText( "Baixar anexos" );
		this.btnAnexos.setFocusable( false );

		this.btnExportar = new JButton( "Exportar" );
		this.btnExportar.setBounds( 576, 5, 144, 30 );
		this.btnExportar.setFont( new Font( "Verdana", 0, 12 ) );
		this.btnExportar.setToolTipText( "Exporta para um arquivo CSV." );
		this.btnExportar.setFocusable( false );

		this.pnlContador = new JPanel();
		this.pnlContador.setBounds( 20, 170, 740, 100 );
		this.pnlContador.setEnabled( true );
		this.pnlContador.setLayout( null );
		this.pnlContador.setBorder( BorderFactory.createEtchedBorder() );

		this.txaDetalhesFollowUp = new JTextArea();
		this.txaDetalhesFollowUp.setLayout( null );
		this.txaDetalhesFollowUp.setBounds( 20, this.pnlContador.getY() + this.pnlContador.getHeight()+10, 740, 110 );
		this.txaDetalhesFollowUp.setFont( new Font( "Verdana", 0, 12 ) );
		this.txaDetalhesFollowUp.setEditable( false );
		this.txaDetalhesFollowUp.setBackground( pnlSuperior.getBackground() );
		this.txaDetalhesFollowUp.setBorder( BorderFactory.createEtchedBorder() );
		this.txaDetalhesFollowUp.setLineWrap( true );
		this.txaDetalhesFollowUp.setWrapStyleWord( true );

		this.scrollPaneHome = new JScrollPane( txaDetalhesFollowUp );
		this.scrollPaneHome.setBounds( 20, this.pnlContador.getY() + this.pnlContador.getHeight()+10, 740, 100 );

		final JScrollBar bar3 = scrollPaneHome.getVerticalScrollBar();
		bar3.setUnitIncrement( 10 );

		this.pnlTempoDecorrido = new JPanel();
		this.pnlTempoDecorrido.setBounds( 20, txaDetalhesFollowUp.getY() + txaDetalhesFollowUp.getHeight() + 10, 740, 40 );
		this.pnlTempoDecorrido.setLayout( null );
		this.pnlTempoDecorrido.setBorder( BorderFactory.createEtchedBorder() );

		this.lblTempoDecorrido = new JLabel( "Tempo Tarefa:" );
		this.lblTempoDecorrido.setBounds( 10, 10, 115, 21 );
		this.lblTempoDecorrido.setFont( new Font( "Verdana", 0, 12 ) );
		this.lblTempoDecorrido.setHorizontalAlignment( JLabel.RIGHT );

		this.lblTotalTarefa = new JLabel( "00:00:00" );
		this.lblTotalTarefa.setBounds( 130, 10, 115, 21 );
		this.lblTotalTarefa.setFont( new Font( "Verdana", 0, 12 ) );

		this.lblTempoTodos = new JLabel( "Tempo Total:" );
		this.lblTempoTodos.setBounds( 510, 10, 115, 21 );
		this.lblTempoTodos.setFont( new Font( "Verdana", 0, 12 ) );
		this.lblTempoTodos.setHorizontalAlignment( JLabel.RIGHT );

		this.lblTotalTempo = new JLabel( "00:00:00" );
		this.lblTotalTempo.setBounds( 630, 10, 115, 21 );
		this.lblTotalTempo.setFont( new Font( "Verdana", 0, 12 ) );

		this.pnlExportar = new JPanel();
		this.pnlExportar.setLayout( null );
		this.pnlExportar.setBounds( 20, pnlTempoDecorrido.getY() + pnlTempoDecorrido.getHeight() + 10, 740, 40 );
		this.pnlExportar.setBorder( BorderFactory.createEtchedBorder() );

		this.lblExportarPara = new JLabel( "Exportar para:" );
		this.lblExportarPara.setBounds( 25, 10, 100, 21 );
		this.lblExportarPara.setHorizontalAlignment( JLabel.RIGHT );
		this.lblExportarPara.setFont( new Font( "Verdana", 0, 12 ) );

		this.txfDiretorio = new JTextField( System.getProperty( "user.home" ) );
		this.txfDiretorio.setText( this.txfDiretorio.getText() + ((OS.isWindows())? "\\Desktop\\Tarefas.csv" : "/Tarefas.csv") );
		this.txfDiretorio.setBounds( 130, 10, 400, 21 );
		this.txfDiretorio.setFont( new Font( "Monospaced", 0, 12 ) );

		this.btnProcurarDiretorio = new JButton();
		this.btnProcurarDiretorio.setBounds( 535, 10, 21, 21 );

		this.pnlEvolucao = new JPanel();
		this.pnlEvolucao.setBounds( 0, 0, 800, 520 );
		this.pnlEvolucao.setLayout( null );
		this.pnlEvolucao.setName( "Evolução" );
		this.pnlEvolucao.setBorder( BorderFactory.createEtchedBorder() );

		this.btnMiniSQL = new JButton();
		this.btnMiniSQL.setBounds( 10, 15, 21, 21 );
		this.btnMiniSQL.setFocusable( false );

		this.btnMiniERP = new JButton();
		this.btnMiniERP.setBounds( 35, 15, 21, 21 );
		this.btnMiniERP.setFocusable( false );

		this.lblTarefa = new JLabel( "Tarefa: " );
		this.lblTarefa.setHorizontalAlignment( JLabel.RIGHT );
		this.lblTarefa.setBounds( 40, 15, 80, 21 );
		this.lblTarefa.setFont( new Font( "Verdana", 0, 12 ) );

		this.txfCodTarefa = new JTextField( "" );
		this.txfCodTarefa.setFont( new Font( "Verdana", 0, 12 ) );
		this.txfCodTarefa.setBounds( 120, 15, 100, 21 );

		this.lblDescrTarefa = new JLabel( "" );
		this.lblDescrTarefa.setFont( new Font( "Verdana", 0, 12 ) );
		this.lblDescrTarefa.setBounds( 235, 15, 700, 21 );

		this.pnlParse = new JTextArea();
		this.pnlParse.setLayout( null );
		this.pnlParse.setBounds( 10, 40, 740, 460 );
		this.pnlParse.setFont( new Font( "Verdana", 0, 12 ) );
		this.pnlParse.setEditable( false );
		this.pnlParse.setBackground( pnlSuperior.getBackground() );
		this.pnlParse.setBorder( BorderFactory.createEtchedBorder() );
		this.pnlParse.setLineWrap( true );
		this.pnlParse.setWrapStyleWord( true );

		this.scrollPane = new JScrollPane( pnlParse );
		this.scrollPane.setBounds( 10, 40, 740, 460 );

		final JScrollBar bar = scrollPane.getVerticalScrollBar();
		bar.setUnitIncrement( 40 );

		this.getContentPane().add( this.pnlSuperior, null );
		this.pnlSuperior.add( this.lblData );
		this.pnlSuperior.add( this.lblHora );
		this.pnlSuperior.add( this.lblLogin );

		this.getContentPane().add( this.tbpPainelAbas );
		this.tbpPainelAbas.add( this.pnlInferior, null );
		this.pnlInferior.add( this.containerCampos );

		this.pnlBuscar = new JPanel();
		this.pnlBuscar.setBounds( 0, 0, 800, 520 );
		this.pnlBuscar.setLayout( null );
		this.pnlBuscar.setName( "Buscar" );
		this.pnlBuscar.setBorder( BorderFactory.createEtchedBorder() );

		this.lblTarefaBuscar = new JLabel( "Código:" );
		this.lblTarefaBuscar.setHorizontalAlignment( JLabel.RIGHT );
		this.lblTarefaBuscar.setBounds( 10, 10, 90, 21 );
		this.lblTarefaBuscar.setFont( new Font( "Verdana", 0, 12 ) );

		this.lblTemposTotalEMedio = new JLabel();
		this.lblTemposTotalEMedio.setBounds( 400, 10, 400, 21 );
		this.lblTemposTotalEMedio.setFont( new Font( "Verdana", 0, 12 ) );

		this.txfTarefaBuscar = new JTextField();
		this.txfTarefaBuscar.setBounds( 105, 10, 90, 21);
		this.txfTarefaBuscar.setFont( new Font( "Monospaced", 0, 12 ) );

		this.lblPeriodoBuscar = new JLabel( "Período" );
		this.lblPeriodoBuscar.setHorizontalAlignment( JLabel.RIGHT );
		this.lblPeriodoBuscar.setBounds( 10, 35, 90, 21 );
		this.lblPeriodoBuscar.setFont( new Font( "Verdana", 0, 12 ) );

		this.dtcPeriodoInicial = new JDateChooser();
		this.dtcPeriodoInicial.setBounds( 105, 35, 90, 21);
		this.dtcPeriodoInicial.setFont( new Font( "Monospaced", 0, 12 ) );

		this.lblA = new JLabel( "à" );
		this.lblA.setHorizontalAlignment( JLabel.CENTER );
		this.lblA.setBounds( 205, 35, 20, 21 );
		this.lblA.setFont( new Font( "Verdana", 0, 12 ) );

		this.dtcPeriodoFinal = new JDateChooser();
		this.dtcPeriodoFinal.setBounds( 235, 35, 90, 21);
		this.dtcPeriodoFinal.setFont( new Font( "Monospaced", 0, 12 ) );

		this.lblStatusBuscar = new JLabel( "Status:" );
		this.lblStatusBuscar.setHorizontalAlignment( JLabel.RIGHT );
		this.lblStatusBuscar.setBounds( 10, 60, 90, 21 );
		this.lblStatusBuscar.setFont( new Font( "Verdana", 0, 12 ) );

		this.rbtTodos = new JRadioButton( "Todos" );
		this.rbtTodos.setSelected( true );
		this.rbtTodos.setBounds( 110, 60, 70, 21 );
		this.rbtTodos.setFont( new Font( "Verdana", 0, 12 ) );

		this.rbtFinalizados = new JRadioButton( "Finalizadas" );
		this.rbtFinalizados.setBounds( 190, 60, 100, 21 );
		this.rbtFinalizados.setFont( new Font( "Verdana", 0, 12 ) );

		this.rbtNaoFinalizados = new JRadioButton( "Não Finalizadas" );
		this.rbtNaoFinalizados.setBounds( 300, 60, 130, 21 );
		this.rbtNaoFinalizados.setFont( new Font( "Verdana", 0, 12 ) );

		this.rbtEmAndamento = new JRadioButton( "Em Desenvolvimento" );
		this.rbtEmAndamento.setBounds( 440, 60, 160, 21 );
		this.rbtEmAndamento.setFont( new Font( "Verdana", 0, 12 ) );

		final ButtonGroup bg = new ButtonGroup();
		bg.add( this.rbtTodos );
		bg.add( this.rbtFinalizados );
		bg.add( this.rbtNaoFinalizados );
		bg.add( this.rbtEmAndamento );

		this.lblTextoLivre = new JLabel( "Texto livre:" );
		this.lblTextoLivre.setHorizontalAlignment( JLabel.RIGHT );
		this.lblTextoLivre.setBounds( 10, 85, 90, 21 );
		this.lblTextoLivre.setFont( new Font( "Verdana", 0, 12 ) );

		this.txfTextoLivre = new JTextField();
		this.txfTextoLivre.setBounds( 105, 85, 450, 21 );
		this.txfTextoLivre.setFont( new Font( "Verdana", 0, 12 ) );

		this.btnBuscar = new JButton( "Buscar" );
		this.btnBuscar.setBounds( 610, 55, 100, 30 );
		this.btnBuscar.setToolTipText( "Buscar Tarefas" );
		this.btnBuscar.setFocusable( false );

		this.pnlResultado = new JPanel();
		this.pnlResultado.setBounds( 10, 125, pnlBuscar.getWidth()-60, pnlBuscar.getHeight()-195 );
		this.pnlResultado.setLayout( null );
		this.pnlResultado.setBorder( BorderFactory.createEtchedBorder() );

		this.btnReativarBusca = new JButton( "Reativar Selecionadas" );
		this.btnReativarBusca.setBounds( 20, pnlResultado.getHeight()+135, 150, 30 );
		this.btnReativarBusca.setToolTipText( "Reativa as Tarefas selecionadas" );
		this.btnReativarBusca.setFocusable( false );

		this.btnExcluirBusca = new JButton( "Excluir Selecionadas" );
		this.btnExcluirBusca.setBounds( 190, pnlResultado.getHeight()+135, 150, 30 );
		this.btnExcluirBusca.setToolTipText( "Excluir do sistema as Tarefas selecionadas" );
		this.btnExcluirBusca.setFocusable( false );

		this.pnlAnotacoes = new JPanel();
		this.pnlAnotacoes.setBounds( 0, 0, 800, 520 );
		this.pnlAnotacoes.setLayout( null );
		this.pnlAnotacoes.setName( "Anotações" );
		this.pnlAnotacoes.setBorder( BorderFactory.createEtchedBorder() );

		this.lblCaracteres = new JLabel( "0 Caracteres Restantes" );
		this.lblCaracteres.setBounds( 10, 10, 350, 21 );
		this.lblCaracteres.setFont( new Font( "Verdana", 0, 12 ) );

		this.txaAnotacoes = new JTextArea();
		this.txaAnotacoes.setLayout( null );
		this.txaAnotacoes.setBounds( 10, 40, 740, 460 );
		this.txaAnotacoes.setFont( new Font( "Verdana", 0, 12 ) );
		this.txaAnotacoes.setEditable( false );
		this.txaAnotacoes.setBackground( Color.white );
		this.txaAnotacoes.setBorder( BorderFactory.createEtchedBorder() );
		this.txaAnotacoes.setLineWrap( true );

		this.scrollPaneAnotacoes = new JScrollPane( txaAnotacoes );
		this.scrollPaneAnotacoes.setBounds( 10, 40, 740, 460 );

		final JScrollBar bar2 = scrollPaneAnotacoes.getVerticalScrollBar();
		bar2.setUnitIncrement( 20 );

	    this.containerCampos.add( this.lblCodigo );
        this.containerCampos.add( this.txfCodigo );
        this.containerCampos.add( this.lblNome );
        this.containerCampos.add( this.txfNome );
	    this.containerCampos.add( this.lblSolicitante );
        this.containerCampos.add( this.txfSolicitante );
	    this.containerCampos.add( this.lblObs );
        this.containerCampos.add( this.txfObs );
		this.containerCampos.add( this.btnLinxERP );
		this.containerCampos.add( this.btnTray );
		this.containerCampos.add( this.btnMSSQL );
		this.containerCampos.add( this.btnERP );
		this.containerCampos.add( this.btnAdd );
		this.containerCampos.add( this.btnAnexos );
		this.containerCampos.add( this.btnParar );
		this.containerCampos.add( this.btnContinuar );
		this.containerCampos.add( this.btnExcluir );
		this.containerCampos.add( this.btnCancelar );
		this.containerCampos.add( this.btnAlterar );

		this.pnlInferior.add( this.pnlContador );
		this.pnlInferior.add( this.pnlTempoDecorrido );
        this.pnlTempoDecorrido.add( this.lblTempoDecorrido );
        this.pnlTempoDecorrido.add( this.lblTotalTarefa );
        this.pnlTempoDecorrido.add( this.lblTempoTodos );
        this.pnlTempoDecorrido.add( this.lblTotalTempo );
		this.pnlInferior.add( this.scrollPaneHome );
		this.pnlInferior.add( this.pnlExportar );
		this.pnlExportar.add( this.lblExportarPara );
		this.pnlExportar.add( this.txfDiretorio );
		this.pnlExportar.add( this.btnProcurarDiretorio );
		this.pnlExportar.add( this.btnExportar );
		this.tbpPainelAbas.add( this.pnlEvolucao, null );
		this.pnlEvolucao.add( this.btnMiniSQL );
		this.pnlEvolucao.add( this.btnMiniERP );
		this.pnlEvolucao.add( this.lblTarefa );
		this.pnlEvolucao.add( this.txfCodTarefa );
		this.pnlEvolucao.add( this.lblDescrTarefa );
		this.pnlEvolucao.add( this.scrollPane );
		this.tbpPainelAbas.add( this.pnlBuscar, null );
		this.pnlBuscar.add( this.lblTarefaBuscar );
		this.pnlBuscar.add( this.lblTemposTotalEMedio );
		this.pnlBuscar.add( this.txfTarefaBuscar );
		this.pnlBuscar.add( this.lblPeriodoBuscar );
		this.pnlBuscar.add( this.dtcPeriodoInicial );
		this.pnlBuscar.add( this.lblA );
		this.pnlBuscar.add( this.dtcPeriodoFinal );
		this.pnlBuscar.add( this.lblStatusBuscar );
		this.pnlBuscar.add( this.rbtTodos );
		this.pnlBuscar.add( this.rbtFinalizados );
		this.pnlBuscar.add( this.rbtNaoFinalizados );
		this.pnlBuscar.add( this.rbtEmAndamento );
		this.pnlBuscar.add( this.lblTextoLivre );
		this.pnlBuscar.add( this.txfTextoLivre );
		this.pnlBuscar.add( this.btnBuscar );
		this.pnlBuscar.add( this.pnlResultado );
		this.pnlBuscar.add( this.btnReativarBusca );
		this.pnlBuscar.add( this.btnExcluirBusca );
		this.tbpPainelAbas.add( this.pnlAnotacoes, null );
		this.pnlAnotacoes.add( this.lblCaracteres );
		this.pnlAnotacoes.add( this.scrollPaneAnotacoes );

		JLabel lblTab1 = new JLabel( "  Tarefas  " );
		lblTab1.setUI(new VerticalLabelUI(false));
		this.tbpPainelAbas.setTabComponentAt(0, lblTab1);

		JLabel lblTab2 = new JLabel( "  Evolução  " );
		lblTab2.setUI(new VerticalLabelUI(false));
		this.tbpPainelAbas.setTabComponentAt(1, lblTab2);

		JLabel lblTab3 = new JLabel( "  Buscar  " );
		lblTab3.setUI(new VerticalLabelUI(false));
		this.tbpPainelAbas.setTabComponentAt(2, lblTab3);

		JLabel lblTab4 = new JLabel( "  Anotações  " );
		lblTab4.setUI(new VerticalLabelUI(false));
		this.tbpPainelAbas.setTabComponentAt(3, lblTab4);
	}

	private void iniciarPrograma( final String pNomeCidade ) {
		setDataPainel( obterData( pNomeCidade ) );
		this.relogio.start();
		habilitarBotoes( false );
	}

	private void iniciarSQLServer( final String pUsuarioSql ) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				Thread thread = new Thread(){
		    @Override
					public void run(){
						Tarefa tarefa = getLinhaSelecionada();

						if( tarefa == null ) {
							tarefa = getTarefa();

							if( tarefa == null ) {
								return;
							}
						}

						if( tarefa.getPortal().isEmpty() ) {
							Mensagem.informacao( "Portal não encontrado.", null );
							return;
						}

						try {
							String tempFolder = System.getenv( "TEMP" );
							String fileName = tempFolder + "\\temp.sql";
							String usuario = (pUsuarioSql.equals( "suporte" ))? "usr_suporte" : "mic" + tarefa.getPortal();
							String senha = (pUsuarioSql.equals( "suporte" ))? "@@#vx3Wt$6v" : "Y87LQ!/m";
							FileWriter sqlFile = new FileWriter( fileName );
							sqlFile.write( "USE portal_" + tarefa.getPortal() );
							sqlFile.close();
							String comando = "Ssms.exe -S " + tarefa.getEnderecoBD() + " -U " + usuario + " -P " + senha + " -nosplash " + fileName;
							System.out.println( "LOG: Iniciando SQL Server: " + comando);
							Runtime.getRuntime().exec( comando );
			} catch (IOException e) {
							System.out.println( "IOException: " + e.getMessage() );
						}
					}
				};

				thread.start();
			}
		});
	}

	private void iniciarERP( final String pProducaoCorrecao ) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				Thread thread = new Thread(){
					public void run(){
						Tarefa tarefa = getLinhaSelecionada();

						if( tarefa == null ) {
							tarefa = getTarefa();

							if( tarefa == null ) {
								return;
							}
						}

						String producaoCorrecao = "CORRECAO";

						if (pProducaoCorrecao.equals("Produção")) {
							producaoCorrecao = "PRODUCAO";
						}

						try {
							String comando = "";

							if( tarefa.getPortal().isEmpty() ) {
								Mensagem.informacao( "Portal não encontrado.", null );
								return;
							}

							if( producaoCorrecao.equals( "CORRECAO" ) ) {
								String destinoUsuario;
								switch (pProducaoCorrecao) {
									case "Correção ERP": {
										destinoUsuario = "correcaoERP";
										break;
									}
									case "Correção FFC": {
										destinoUsuario = "correcaoFFC";
										break;
									}
									case "Correção M": {
										destinoUsuario = "correcaoM";
										break;
									}
									case "Produção": {
										destinoUsuario = "correcaoERP";
										break;
									}
									default: {
										destinoUsuario = "correcaoERP";
									}
								}
								comando = "C:\\Program Files\\Internet Explorer\\iexplore.exe http://192.168.18.12/config/bighost/admin/controle_acesso.asp?operacao=1&d=d&equipe=2&portal=" + tarefa.getPortal().trim() + "&homologacao=S&" + destinoUsuario + "=S";
							} else if (producaoCorrecao.equals("PRODUCAO")) {
								comando = "C:\\Program Files\\Internet Explorer\\iexplore.exe http://192.168.18.12/config/bighost/admin/controle_acesso.asp?operacao=1&d=d&equipe=2&portal=" + tarefa.getPortal().trim() + "&homologacao=N";
							}

							System.out.println( "LOG: Iniciando ERP: " + comando);
							Runtime.getRuntime().exec( comando );
			} catch (IOException e) {
							System.out.println( "IOException: " + e.getMessage() );
						}
					}
				};

				thread.start();
			}
		});
	}

	public void iniciarERPLinx() {
		final JTextField username = new JTextField();
		final JPasswordField password = new JPasswordField();
		final JPasswordField frase = new JPasswordField();

		JPanel panel = new JPanel(new BorderLayout( 5, 5));
		JPanel label = new JPanel( new GridLayout(0, 1, 2, 2 ) );
		JPanel controls = new JPanel( new GridLayout( 0, 1, 2, 2 ) );
		label.add(new JLabel( "Usuário", SwingConstants.RIGHT ) );
		label.add(new JLabel( "Senha", SwingConstants.RIGHT ) );
		label.add(new JLabel( "Frase", SwingConstants.RIGHT ) );

		controls.add( username );
		controls.add( password );
		controls.add( frase );

		panel.add( label, BorderLayout.WEST );
		panel.add( controls, BorderLayout.CENTER );

		int result = JOptionPane.showConfirmDialog(this, panel,
			"Dados de Login", JOptionPane.OK_CANCEL_OPTION
		);

		if( result != JOptionPane.OK_OPTION ){
			return;
		}

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				Thread thread = new Thread(){
		    @Override
					public void run(){
						if( username.getText().isEmpty() ) {
							return;
						}

						try {
			    String comando
				    = "http://wwwf.microvix.com.br/Log.asp?login=" + username.getText()
				    + "&senha=" + new String(password.getPassword())
				    + "&frase=" + new String(frase.getPassword());

							System.out.println( "LOG: Iniciando ERP Linx: " + comando );
							final URI uri = new URI( comando );
							if( Desktop.isDesktopSupported() ) {
								Desktop.getDesktop().browse( uri );
							}
			} catch (IOException e) {
							System.out.println( "IOException: " + e.getMessage() );
			} catch (URISyntaxException ex) {
						}
					}
				};

				thread.start();
			}
		});
	}

	private void criarModel() {
		this.contadorTable = new JTable();
		this.contadorModel = new TarefaModel();
		this.contadorTable.setModel( contadorModel );

		this.contadorTable.setShowGrid(false);
		this.contadorTable.getTableHeader().setReorderingAllowed( false );
		this.contadorTable.setBorder( BorderFactory.createEmptyBorder() );
		this.contadorTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		this.contadorTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		this.contadorTable.setFont( new Font( "Monospaced", 0, 12 ) );

		this.contadorTable.getColumnModel().getColumn(0).setPreferredWidth( 80 );
		this.contadorTable.getColumnModel().getColumn(1).setPreferredWidth( 255 );
		this.contadorTable.getColumnModel().getColumn(2).setPreferredWidth( 110 );
		this.contadorTable.getColumnModel().getColumn(3).setPreferredWidth( 70 );
		this.contadorTable.getColumnModel().getColumn(4).setPreferredWidth( 70 );
		this.contadorTable.getColumnModel().getColumn(5).setPreferredWidth( 70 );
		this.contadorTable.getColumnModel().getColumn(6).setPreferredWidth( 60 );

		this.pnlContador.setLayout( new BorderLayout() );
		this.pnlContador.add( new JScrollPane( contadorTable ) );

		class RRenderer extends DefaultTableCellRenderer {

			@Override
			public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
				Component comp = super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );

				if( row % 2 == 0 ) {
					comp.setBackground( new Color( 202, 225, 255 ) );
		} else {
					comp.setBackground( new Color( 254, 254, 254 ) );
				}

				if( column == 0 ) {
					String url = "http://a-srv63/suporte/followup_find.asp?OBJETO_ID=" + value;
					((JLabel) comp).setText( "<html><a href=\"" + url + "\">" + value + "</a>" );
					((JLabel) comp).setHorizontalAlignment( JLabel.LEFT );
		} else if (column == 6) {
					((JLabel) comp).setHorizontalAlignment( JLabel.CENTER );
		} else {
					((JLabel) comp).setHorizontalAlignment( JLabel.LEFT );
				}

				if( isSelected ){
					comp.setBackground( new Color( comp.getBackground().getRed() - 40, comp.getBackground().getGreen() - 40, comp.getBackground().getBlue() - 40 ) );

					try {
						Tarefa t = getLinhaSelecionada();
						String toolTip = "Incluído em " + t.getDataHoraInclusao();
						((JLabel) comp).setToolTipText( toolTip );
		    } catch (Exception ex) {
						System.out.println( "erro: " + ex.getMessage() );
					}
				}
				return( comp );
			}
		}

		this.contadorTable.setDefaultRenderer( Object.class, new RRenderer() );
		this.contadorTable.setDefaultRenderer( String.class, new RRenderer() );
		this.contadorTable.setDefaultRenderer( Character.class, new RRenderer() );

		this.popm = new JPopupMenu();

		JMenuItem menuItem = new JMenuItem( "Finalizar Atendimento" );
		menuItem.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				comandoTela = "FINALIZAR";
			}
		});

		this.popm.add( menuItem );

		this.contadorTable.setComponentPopupMenu(this.popm);

		((DefaultTableCellRenderer)contadorTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment( JLabel.CENTER );
	}

	public void habilitarBotoes( final boolean simOuNao ) {
		this.btnContinuar.setEnabled( simOuNao );
		this.btnParar.setEnabled( simOuNao );
		habilitarExcluir( simOuNao );
		this.btnAlterar.setEnabled( simOuNao );
		this.btnERP.setEnabled( simOuNao );
		this.btnMSSQL.setEnabled( simOuNao );
		this.btnAnexos.setEnabled( simOuNao );
	}

	public void habilitarContinuar( final boolean simOuNao ) {
		this.btnContinuar.setEnabled( !simOuNao );
		this.btnParar.setEnabled( simOuNao );
	}

	public void habilitarBotaoInserir( final boolean simOuNao ) {
		this.btnAdd.setEnabled( simOuNao );
	}

	public void habilitarExcluir( final boolean simOuNao ) {
		this.btnExcluir.setEnabled( simOuNao );
	}

	private String obterData( final String pNomeCidade ) {
		String cidade = "";

		if( !pNomeCidade.isEmpty() ) {
			cidade = pNomeCidade + ", ";
		}

		final DateFormat dia = new SimpleDateFormat( "dd" );
		final DateFormat mes = new SimpleDateFormat( "MMMM" );
		final DateFormat ano = new SimpleDateFormat( "yyyy" );
		final DateFormat hora = new SimpleDateFormat( "HH" );

		final Date dataAtual = new Date();

		String saudacao = (Integer.parseInt(String.valueOf( hora.format( dataAtual ))) <= 12)? "Bom dia!" : "Boa tarde!";

	return (cidade
		+ dia.format(dataAtual) + " de " + mes.format(dataAtual) + " de " + ano.format(dataAtual) + ". "
		+ saudacao);
	}

	public void acessar() {
		try {
			comandoTela = "";

			while( comandoTela.isEmpty() ) {
				Thread.sleep(50);
			}
	} catch (InterruptedException e) {
	    System.out.println(e.getMessage());
		}
	}

	public void limpar() {
		setTxfCodigo( "" );
		setTxfNome( "" );
		setTxfSolicitante( "" );
		setTxfObs( "" );
		setTxfCodTarefa( "" );
		setPnlParse( "" );
		setTxaDetalhesFollowUp( "" );
		setLblCaracteres( "0 Caracteres Restantes" );
		setTxaAnotacoes( "" );

		this.txaAnotacoes.setEditable( false );
	}

	public String getPnlParse() {
		return( this.pnlParse.getText() );
	}

	public String getTxfCodTarefa() {
		return( this.txfCodTarefa.getText() );
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

	public void setLblCaracteres( final String pLabel ) {
		this.lblCaracteres.setText( pLabel );
	}

	public void setPnlParse( final String pTexto ) {
		this.pnlParse.setText( pTexto );
	}

	public void setTxaDetalhesFollowUp( final String pTexto ) {
		this.txaDetalhesFollowUp.setText( pTexto );
	}

	public void setTxfCodTarefa( final String pCodTarefa ) {
		this.txfCodTarefa.setText( pCodTarefa );
	}

	public void setTxfCodigo( final String codParam ) {
		this.txfCodigo.setText( codParam );
		this.txfCodTarefa.setText( codParam );
	}

	public void setTxfNome( final String nomeParam ) {
		this.txfNome.setText( nomeParam );
		this.lblDescrTarefa.setText( nomeParam );
	}

	public void setTxfSolicitante( final String pSolicitante ) {
		this.txfSolicitante.setText( pSolicitante );
	}

	public void setTxfObs( final String obsParam ) {
		this.txfObs.setText( obsParam );
	}

	public void setTxfDiretorio( final String dirParam ) {
		txfDiretorio.setText( dirParam );
	}

	public String getTxfDiretorio() {
		return( txfDiretorio.getText() );
	}

	public String getComandoTela() {
		return( this.comandoTela );
	}

	public String getLblSuperior() {
		return( this.lblData.getText() );
	}

	public void setDataPainel( final String lblParam ) {
		this.lblData.setText( lblParam );
	}

	public void addTarefa( final Tarefa pTarefa ) {
		this.contadorModel.addLinha( pTarefa );
	}

	public void limparTarefas() {
		this.contadorModel.limpar();
	}

	public void setLblTotalTarefa( final String param ) {
		this.lblTotalTarefa.setText( param );
	}

	public void setLblTotalTempo( final String param ) {
		this.lblTotalTempo.setText( param );
	}

	public void limparTempoDecorrido() {
		setLblTotalTarefa( "00:00:00" );
		setLblTotalTempo( "00:00:00" );
	}

	public void setTarefa( final Tarefa pTarefa ) {
		this.tarefaAtual = pTarefa;

		if( pTarefa != null ) {
			this.txaAnotacoes.setEditable( true );
			setLblCaracteres( String.valueOf( 5000 - pTarefa.getAnotacoes().length() ) + " Caracteres Restantes" );
		}
	}

	public Tarefa getTarefa() {
		return( this.tarefaAtual );
	}

	public void carregarTarefaPosicao( final Tarefa pTarefa, final char pPosicao ) {
		Collection<Tarefa> tarefaList = this.contadorModel.getLinhas();
		int contador = 0;

		if( tarefaList != null && !tarefaList.isEmpty() ) {
			for( Tarefa d : tarefaList ) {
				if( pTarefa.equals( d ) ) {
					break;
				}
				contador++;
			}
		}

		if( pPosicao == '-' ) {
			contador--;
	} else {
			contador++;
		}

		if( (contador+1) > this.contadorModel.getRowCount() || contador < 0 ) {
			return;
		}

		Tarefa antes = this.contadorModel.getLinha( contador );

		if( antes != null ) {
			setTarefa( antes );
			setTxfCodigo( antes.getCodigo() );
			setTxfNome( antes.getNome() );
			setTxfSolicitante( antes.getSolicitante() );
			setTxfObs( antes.getObs() );
		}
	}

	public Tarefa getLinhaSelecionada() {
		int linha = this.contadorTable.getSelectedRow();
		return( this.contadorModel.getLinha( linha ) );
	}

	public TarefaResultado getLinhaResultadoSelecionada() {
		int linha = this.resultadoTable.getSelectedRow();
		return( this.resultadoModel.getLinha( linha ) );
	}

	public Collection<Tarefa> getLinhas() {
		return( this.contadorModel.getLinhas() );
	}

	public void habilitarBtnAlterar() {
		this.btnAlterar.setEnabled( true );
	}

	private String replaceAcentosHTML( final String pLinha ) {
		if( !pLinha.contains( "&" ) ) {
			return( pLinha );
		}

		int ini = pLinha.indexOf( "&" );
		int fim = pLinha.indexOf( ";" );

		if( fim < 0 ) {
			return( "" );
		}

		String parte = pLinha.substring( ini, fim+1 );
		String retorno = pLinha.substring( 0, ini );

		switch( parte ) {
	    case "&nbsp;": {
		retorno += " ";
		break;
	    }
	    case "&ccedil;": {
		retorno += "ç";
		break;
	    }
	    case "&Ccedil;": {
		retorno += "Ç";
		break;
	    }
	    case "&atilde;": {
		retorno += "ã";
		break;
	    }
	    case "&Atilde;": {
		retorno += "Ã";
		break;
	    }
	    case "&aacute;": {
		retorno += "á";
		break;
	    }
	    case "&Aacute;": {
		retorno += "Á";
		break;
	    }
	    case "&eacute;": {
		retorno += "é";
		break;
	    }
	    case "&Eacute;": {
		retorno += "É";
		break;
	    }
	    case "&iacute;": {
		retorno += "í";
		break;
	    }
	    case "&Iacute;": {
		retorno += "Í";
		break;
	    }
	    case "&oacute;": {
		retorno += "ó";
		break;
	    }
	    case "&Oacute;": {
		retorno += "Ó";
		break;
	    }
	    case "&uacute;": {
		retorno += "ú";
		break;
	    }
	    case "&Uacute;": {
		retorno += "Ú";
	    }
		}

		retorno += pLinha.substring( fim+1 );
		return( replaceAcentosHTML( retorno ) );
	}

	private String parseHTML( final ArrayList<String> pHTMLList, final boolean pTarefa ) {
		boolean tableEncontrado = false;
		StringBuilder result = new StringBuilder();
		for( String linha : pHTMLList ) {
			linha = linha.trim();
			if( linha.contains( "<table"  ) ) {
				tableEncontrado = true;
			}

			if( linha.contains( "</table"  ) ) {
				tableEncontrado = false;
			}

			if( !tableEncontrado && pTarefa ) {
				continue;
			}

			String tmp = "";
			int numDeTags = linha.length() - linha.replace("<","").length();
			for( int i=numDeTags; i>= 0; i-- ) {
				if( !linha.startsWith( "<" ) ) {
		    if (linha.contains("<")) {
						tmp += linha.substring( 0, linha.indexOf( "<" ) );
						linha = linha.substring( linha.indexOf( "<" ) );
					}
				}

		if (linha.contains(">")) {
				int posicaoFechamento = linha.indexOf( ">" );
					int quantMaior = linha.length() - linha.replace( "<", "" ).length();
					if( quantMaior > 0 ){
						linha = linha.substring( posicaoFechamento+1 ).trim();
					}
				}
			}

			tmp += linha.trim();

			if( !tmp.isEmpty() ) {
				String t;
				if( pTarefa ) {
					t = replaceAcentosHTML( tmp ).trim();
		} else {
					t = tmp.trim();
				}
				if( !t.endsWith(":") || t.contains("Descrição Detalhada:") || t.equals("Fim Real :") ) {
					t += "\n";
				}
				if( t.equals("Resumo\n") ) {
					t = "Resumo ";
				}
				if( t.equals("Ação\n") ) {
					t = "Ação ";
				}
				result.append( t );
	    } else {
				result.append( "\n" );
			}
		}
		return( result.toString() );
	}

	public String removerEspacos( final String pLinha ) {
		String retorno = "";

		for( int i=2; i<=pLinha.length()-1; i++ ) {
			if( pLinha.charAt(i) == '\n' && pLinha.charAt(i-1) == '\n' && pLinha.charAt(i-2) == '\n') {
				continue;
			}

			retorno += pLinha.charAt(i) + "";
		}
		return( retorno );
	}

	private void setCursorOcupado() {
		getComponent( 0 ).setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
	}

	private void setCursorLivre() {
		getComponent( 0 ).setCursor( Cursor.getDefaultCursor() );
	}

	public void carregarPaginaEvolucao( final char pCarregarDadosInsercao ) {
	URL url;
	BufferedReader in;

		if( getTxfCodTarefa().isEmpty() ) {
			setPnlParse( "" );
			setTxaDetalhesFollowUp( "" );
			return;
		}

		try {
			url = new URL("http://a-srv63/suporte/followup_find.asp?OBJETO_ID=" + this.txfCodTarefa.getText());
	} catch (Exception e) {
			System.out.println( "Exception: " + e.getMessage() );
			return;
		}

		try {
			in = new BufferedReader( new InputStreamReader( url.openStream() ) );
	} catch (IOException ex) {
			System.out.println( "IOException: " + ex.getMessage() );
			return;
		}

		String linha;
	final ArrayList<String> linhas = new ArrayList<>();

		try {
			setCursorOcupado();

			while( (linha = in.readLine()) != null ) {
				linhas.add( linha );
			}

			in.close();
	} catch (IOException e) {
			System.out.println( "IOException: " + e.getMessage() );
			return;
		}

		linha = parseHTML( linhas, true );
		linha = removerEspacos( linha );
		linha = buscarPortal( linha );

		if( pCarregarDadosInsercao == 'S' ) {
			carregarDadosLidos( linha );
		}

		setPnlParse( linha );
		setTxaDetalhesFollowUp( linha );

		setCursorLivre();
	}

	private String buscarPortal( String pConteudo ) {
		Integer posicaoInicio = -1;
		String resultadoDaBusca = "";
		String portal = "";

		for( String linha : pConteudo.split("\n") ) {

			if( linha.toUpperCase().contains( "PORTAL" ) ) {
				int numeroEncontrado = 0;
				for( int i=0; i<linha.length(); i++ ){
					Character charAtual = linha.charAt( i );

		    if (charAtual == '|' || charAtual == ',') {
						continue;
					}

					if( numeroEncontrado == 1 ) {
						portal += charAtual.toString();
					}

		    if (charAtual >= '0' && charAtual <= '9' && numeroEncontrado == 0) {
						numeroEncontrado++;
						portal += charAtual.toString();
		    } else if (numeroEncontrado == 1 && (charAtual == ' ' || i == linha.length() - 1)) {
			posicaoInicio = i;
						break;
					}
				}
			}

			if( linha.toUpperCase().contains( "DATA:" ) ){
				linha = "_________________________________________________________________________________________\n" + linha;
				resultadoDaBusca = resultadoDaBusca.substring( 0, resultadoDaBusca.length()-1 );
			}

			if( posicaoInicio > -1 ) {
				Tarefa tarefa = getLinhaSelecionada();

				if( tarefa == null ) {
					tarefa = new Tarefa();
					setTarefa( tarefa );
				}

				System.out.println("portal: " + portal);

				String enderecoBD = buscarEnderecoBDPortal( portal );

				if( !tarefa.existeBdInformado() ){
					tarefa.setEnderecoBD( enderecoBD );
					tarefa.setPortal( portal );
				}

				if( !enderecoBD.isEmpty() ){
					enderecoBD = " (" + enderecoBD + ")";
				}

				if( linha.substring( 0, posicaoInicio ).length() >= linha.length() ) {
					resultadoDaBusca += linha + enderecoBD + "\n";
		} else {
					resultadoDaBusca += linha.substring( 0, posicaoInicio+1 ) + enderecoBD + linha.substring( posicaoInicio+1 ) + "\n";
				}
				posicaoInicio = -1;
				portal = "";
	    } else {
				resultadoDaBusca += linha + "\n";
			}
		}

		return( resultadoDaBusca );
	}

	private String lerPaginaPortal( final String pPortal ) {
	URL url;
	BufferedReader in;

		try {
			url = new URL("http://192.168.18.12/config/bighost/admin/administrador.asp?equipe=2&opcao_pesquisa=P&equipe=2&operacao=1&conteudo=" + pPortal);
	} catch (Exception e) {
			System.out.println( "Exception: " + e.getMessage() );
			return( "" );
		}

		try {
			in = new BufferedReader( new InputStreamReader( url.openStream() ) );
	} catch (IOException ex) {
			System.out.println( "IOException: " + ex.getMessage() );
			return( "" );
		}

		String linha;
	ArrayList<String> linhas = new ArrayList<>();

		try {
			setCursorOcupado();

			while( (linha = in.readLine()) != null ) {
				linhas.add( linha );
			}

			in.close();
	} catch (IOException e) {
			System.out.println( "IOException: " + e.getMessage() );
		}

		linha = parseHTML( linhas, false );
		linha = removerEspacos( linha );

		setCursorLivre();

		return( linha );
	}

	private String buscarEnderecoBDPortal( final String pPortal ) {
		String pagina = lerPaginaPortal( pPortal );

		for( String linha : pagina.split("\n" ) ) {
			if( linha.contains( "10.10.0" ) ) {
				return( linha.trim() );
			}
		}
		return( "" );
	}

	private void criarModelResultado() {
		this.resultadoTable = new JTable();
		this.resultadoModel = new TarefaResultadoModel();
		this.resultadoTable.setModel( resultadoModel );

		this.resultadoTable.setShowGrid(false);
		this.resultadoTable.getTableHeader().setReorderingAllowed( false );
		this.resultadoTable.setBorder( BorderFactory.createEmptyBorder() );
		this.resultadoTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		this.resultadoTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		this.resultadoTable.setFont( new Font( "Monospaced", 0, 12 ) );

		this.resultadoTable.getColumnModel().getColumn(0).setPreferredWidth( 40 );
		this.resultadoTable.getColumnModel().getColumn(1).setPreferredWidth( 80 );
		this.resultadoTable.getColumnModel().getColumn(2).setPreferredWidth( 300 );
		this.resultadoTable.getColumnModel().getColumn(3).setPreferredWidth( 110 );
		this.resultadoTable.getColumnModel().getColumn(4).setPreferredWidth( 90 );

		this.pnlResultado.setLayout( new BorderLayout() );
		this.scpBusca = new JScrollPane( resultadoTable );
		this.pnlResultado.add( this.scpBusca );

		class RRendererDois extends DefaultTableCellRenderer {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
				Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if( row % 2 == 0 ) {
					comp.setBackground(new Color(202, 225, 255));
		} else {
					comp.setBackground(new Color(254, 254, 254));
				}

				if( column == 4 || column == 5 ) {
					((JLabel) comp).setHorizontalAlignment( JLabel.CENTER );
				}

				if( isSelected ){
					comp.setBackground( new Color( comp.getBackground().getRed() - 40, comp.getBackground().getGreen() - 40, comp.getBackground().getBlue() - 40 ) );

					try {
						TarefaResultado t = getLinhaResultadoSelecionada();

						if( !t.getDataHoraFinalizacao().isEmpty() ) {
							String toolTip = t.getDataHoraFinalizacao();
							((JLabel) comp).setToolTipText( toolTip );
						}

						String toolTip = "Tempo decorrido: " + t.getTempoDecorrido();

						if( ((JLabel) comp).getToolTipText().isEmpty() ) {
							((JLabel) comp).setToolTipText( toolTip );
			} else {
							((JLabel) comp).setToolTipText( "<html>" + ((JLabel) comp).getToolTipText() + "<br>" + toolTip + "</html>" );
						}
		    } catch (Exception ex) {
						System.out.println( "Exception: " + ex.getMessage() );
					}
				}
				return( comp );
			}
		}

		class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

			CheckBoxRenderer() {
				setHorizontalAlignment(JLabel.CENTER);
			}

	    @Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				if( row % 2 == 0 ) {
					setBackground(new Color(202, 225, 255));
		} else {
					setBackground(new Color(254, 254, 254));
				}

				if (isSelected) {
					setForeground(table.getSelectionForeground());
					setBackground( new Color( getBackground().getRed() - 40, getBackground().getGreen() - 40, getBackground().getBlue() - 40 ) );
				}
		setSelected((value != null && ((Boolean) value)));
				return this;
			}
		}

		this.resultadoTable.setDefaultRenderer( Object.class, new RRendererDois() );
		this.resultadoTable.setDefaultRenderer( String.class, new RRendererDois() );
		this.resultadoTable.setDefaultRenderer( Character.class, new RRendererDois() );
		this.resultadoTable.setDefaultRenderer( Boolean.class, new RRendererDois() );

		this.resultadoTable.getColumnModel().getColumn(0).setCellRenderer(new CheckBoxRenderer());
		((DefaultTableCellRenderer)resultadoTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment( JLabel.CENTER );
	}

	public void addTarefaResultado( final TarefaResultado t ) {
		this.resultadoModel.addLinha( t );
	}

	public void limparResultadoBusca() {
		this.resultadoModel.limpar();
	}

	public void setTarefasResultado( final List<TarefaResultado> list ) {
		this.resultadoModel.setLinhas( list );

		calcularTempos( list );
	}

	public String getTxfTarefaBuscar() {
		return( this.txfTarefaBuscar.getText() );
	}

	public Date getDtcPeriodoInicial() {
		return( this.dtcPeriodoInicial.getDate() );
	}

	public Date getDtcPeriodoFinal() {
		return( this.dtcPeriodoFinal.getDate() );
	}

	public void setLblTemposTotalEMedio( final String pTempo ) {
		this.lblTemposTotalEMedio.setText( pTempo );
	}

	public Integer getStatusTarefaBuscar() {
		if( this.rbtEmAndamento.isSelected() ) {
			return( StatusTarefa.EM_ANDAMENTO );
	} else if (this.rbtNaoFinalizados.isSelected()) {
			return( StatusTarefa.NAO_FINALIZADAS );
	} else if (this.rbtFinalizados.isSelected()) {
			return( StatusTarefa.FINALIZADAS );
	} else {
			return( StatusTarefa.TODAS );
		}
	}

	public List<TarefaResultado> getTarefasBusca() {
	ArrayList<TarefaResultado> list = new ArrayList<>();

		for( TarefaResultado t : this.resultadoModel.getLinhas() ) {
			if( t.selecionada() ) {
				list.add( t );
			}
		}

		return( list );
	}

	public void calcularTempos( final List<TarefaResultado> pListaTarefas ) {
		if( pListaTarefas == null || pListaTarefas.isEmpty() || pListaTarefas.get(0).getCodigo().isEmpty() ) {
			setLblTemposTotalEMedio( "" );
			return;
		}

		Integer totalSegundos = 0;

		for( TarefaResultado tp : pListaTarefas ) {
	    Integer segundos;
	    Integer minutos;
	    Integer horas;

			try {
				horas = Integer.parseInt( tp.getTempoDecorrido().substring( 0, 2 ) );
				minutos = Integer.parseInt( tp.getTempoDecorrido().substring( 3, 5 ) );
				segundos = Integer.parseInt( tp.getTempoDecorrido().substring( 6, 8 ) );

				totalSegundos += segundos + (minutos*TimeHelper.secondsInMinuts) + (horas*TimeHelper.secondsInHour);
	    } catch (NumberFormatException excep) {
				System.out.println( "NumberFormatException: " + excep.getMessage() );
	    } catch (IndexOutOfBoundsException excep) {
				System.out.println( "IndexOutOfBoundsException: " + excep.getMessage() );
			}
		}

		if( totalSegundos == 0 ) {
			return;
		}

		Integer segundosMedia = totalSegundos / pListaTarefas.size();

		setLblTemposTotalEMedio(
		"Tempo médio: " + TimeHelper.secondsToTime(segundosMedia)
		+ "    Tempo total: " + TimeHelper.secondsToTime(totalSegundos)
		);
	}

	public String getTxaAnotacoes() {
		return( this.txaAnotacoes.getText().replaceAll( "\n", "##" ) );
	}

	public void setTxaAnotacoes( final String pAnotacoes ) {
		this.txaAnotacoes.setText( pAnotacoes.replaceAll( "##", "\n" ) );
	}

	public String getTxfTextoLivre() {
		return( this.txfTextoLivre.getText() );
	}
}

class TimeHelper {

	public static final Integer secondsInHour = 3600;
	public static final Integer secondsInMinuts = 60;

	public static String secondsToTime( Integer pSeconds ) {
		String hour = String.valueOf( pSeconds / secondsInHour );
		pSeconds -= (Integer.parseInt(hour) * secondsInHour);
		if( Integer.parseInt(hour) < 10 ) {
			hour = "0" + hour;
		}

		String minuts = String.valueOf( pSeconds / secondsInMinuts );
		pSeconds -= (Integer.parseInt(minuts) * secondsInMinuts);
		if( Integer.parseInt(minuts) < 10 ) {
			minuts = "0" + minuts;
		}

		String seconds = pSeconds.toString();
		if( Integer.parseInt(seconds) < 10 ) {
			seconds = "0" + seconds;
		}

		return( hour + ":" + minuts + ":" + seconds );
	}
}

class VerticalLabelUI extends BasicLabelUI {

	static {
		labelUI = new VerticalLabelUI(false);
	}

	protected boolean clockwise;

	public VerticalLabelUI( boolean clockwise ){
		super();
		this.clockwise = clockwise;
	}

    @Override
	public Dimension getPreferredSize(JComponent c){
		Dimension dim = super.getPreferredSize(c);
		return new Dimension( dim.height, dim.width );
	}

    private static final Rectangle paintIconR = new Rectangle();
    private static final Rectangle paintTextR = new Rectangle();
    private static final Rectangle paintViewR = new Rectangle();
	private static Insets paintViewInsets = new Insets(0, 0, 0, 0);

    @Override
	public void paint(Graphics g, JComponent c) {
		JLabel label = (JLabel)c;
		String text = label.getText();
		Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

		if ((icon == null) && (text == null)) {
			return;
		}

		FontMetrics fm = g.getFontMetrics();
		paintViewInsets = c.getInsets(paintViewInsets);

		paintViewR.x = paintViewInsets.left;
		paintViewR.y = paintViewInsets.top;

		// Use inverted height & width
		paintViewR.height = c.getWidth() - (paintViewInsets.left + paintViewInsets.right);
		paintViewR.width = c.getHeight() - (paintViewInsets.top + paintViewInsets.bottom);

		paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
		paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

		String clippedText = layoutCL(label, fm, text, icon, paintViewR, paintIconR, paintTextR);

		Graphics2D g2 = (Graphics2D) g;
		AffineTransform tr = g2.getTransform();

		if( clockwise ) {
			g2.rotate( Math.PI / 2 );
			g2.translate( 0, - c.getWidth() );
	} else {
			g2.rotate( - Math.PI / 2 );
			g2.translate( - c.getHeight(), 0 );
		}

		if (icon != null) {
			icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
		}

		if (text != null) {
			int textX = paintTextR.x;
			int textY = paintTextR.y + fm.getAscent();

			if (label.isEnabled()) {
				paintEnabledText(label, g, clippedText, textX, textY);
	    } else {
				paintDisabledText(label, g, clippedText, textX, textY);
			}
		}

		g2.setTransform( tr );
	}
}
