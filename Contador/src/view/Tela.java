package view;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
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
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager;
import javax.swing.Timer;
import data.Tarefa;
import model.TarefaModel;
import utils.Mensagem;
import utils.OS;

public class Tela extends JFrame {
    public static int larguraBotao = 148;
    private String comandoTela;
    private Timer relogio;
    private Tarefa tarefaAtual;
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
    private JButton btnSair;
    private JButton btnAdd;
    private JButton btnContinuar;
    private JButton btnParar;
    private JButton btnExcluir;
    private JButton btnExportar;
    private JButton btnAlterar;
    private JPanel pnlContador;
    private JPanel pnlTempoDecorrido;
    private JLabel lblTempoDecorrido;
    private JLabel lblTotalTarefa;
    private JLabel lblTempoTodos;
    private JLabel lblTotalTempo;
    private JPanel pnlExportar;
    private JLabel lblExportarPara;
    private JTextField txfDiretorio;
    private JButton btnProcurarDiretorio;
    private JPanel containerCampos;
    public JTable contadorTable;
    public TarefaModel contadorModel;
    private TrayIcon trayIcon;
    private SystemTray tray;

    public Tela() {
        iniciarComponentes();
        getContentPane().setLayout(null);
        setSize(815, 640);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        iniciarPrograma();
        criarModel();
        iniciarConsole();
        setLocationRelativeTo(null);
        setarIcones();
        adicionarListener();
    }

    private void adicionarListener() {
        if (!SystemTray.isSupported()) {
            return;
        }

        this.tray = SystemTray.getSystemTray();
        Image img = new ImageIcon(getClass().getResource("/images/clock.png")).getImage();
        PopupMenu pop = new PopupMenu();
        final MenuItem defaultItem = new MenuItem("Restaurar");
        defaultItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
                setExtendedState(JFrame.NORMAL);
            }
        });

        pop.add(defaultItem);

        this.trayIcon = new TrayIcon(img, "KTaxímetro", pop);
        this.trayIcon.setImageAutoSize(true);

        this.trayIcon.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    tray.remove(trayIcon);
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    tray.remove(trayIcon);
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    tray.remove(trayIcon);
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    tray.remove(trayIcon);
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    tray.remove(trayIcon);
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            }
        });

        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == MAXIMIZED_BOTH) {
                    tray.remove(trayIcon);
                    setVisible(true);
                }
                if (e.getNewState() == NORMAL) {
                    tray.remove(trayIcon);
                    setVisible(true);
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarPosicao();
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                try {
                    if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_I && e.getID() == KeyEvent.KEY_PRESSED) {
                        if (console.isVisible()) {
                            console.setVisible(false);
                        } else {
                            console.setVisible(true);
                        }
                    }
                } catch (Exception exc) {
                    System.out.println("Erro ao disparar evento de tecla: " + exc.getMessage());
                }
                return false;
            }
        });
        this.contadorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    comandoTela = "CARREGAR_TAREFA";
                }
            }
        });
        this.contadorTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                    comandoTela = "CARREGAR_PROXIMA_LINHA";
                } else if (ke.getKeyCode() == KeyEvent.VK_UP) {
                    comandoTela = "CARREGAR_LINHA_ANTERIOR";
                }
            }
        });
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent we) {
                sendToTray();
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
        this.txfCodigo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent kev) {
                if (kev.getKeyCode() == KeyEvent.VK_ENTER) {
                    txfNome.requestFocus();
                }
            }
        });
        this.txfCodigo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent fe) {
                txfCodigo.selectAll();
            }
        });
        this.txfNome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent kev) {
                if (kev.getKeyCode() == KeyEvent.VK_ENTER) {
                    txfSolicitante.requestFocus();
                }
            }
        });
        this.txfNome.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent fe) {
                txfNome.selectAll();
            }
        });
        this.txfSolicitante.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent kev) {
                if (kev.getKeyCode() == KeyEvent.VK_ENTER) {
                    txfObs.requestFocus();
                }
            }
        });
        this.txfSolicitante.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent fe) {
                txfSolicitante.selectAll();
            }
        });
        this.txfObs.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent kev) {
                if (kev.getKeyCode() == KeyEvent.VK_ENTER) {
                    comandoTela = "ADICIONAR_TAREFA";
                }
            }
        });
        this.txfObs.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent fe) {
                txfObs.selectAll();
            }
        });
        this.btnSair.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (todasTarefasParadas()) {
                    System.exit(0);
                } else {
                    Mensagem.informacao("Não é possível sair com tarefas em andamento.", null);
                }
            }
        });
        this.btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comandoTela = "ADICIONAR_TAREFA";
            }
        });
        this.btnContinuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comandoTela = "CONTINUAR";
            }
        });
        this.btnParar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comandoTela = "PARAR";
            }
        });
        this.btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comandoTela = "EXCLUIR";
            }
        });
        this.btnAlterar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comandoTela = "ALTERAR";
            }
        });
        this.btnExportar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comandoTela = "EXPORTAR";
            }
        });
        this.btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comandoTela = "CANCELAR";
            }
        });
        this.btnProcurarDiretorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comandoTela = "PROCURAR_DIRETORIO";
            }
        });
    }

    private void carregarDadosLidos(String pTexto) {
        String tituloTp = "";
        String solicitanteTp = "";
        String obsTp = "";

        for (String linha : pTexto.split("\n")) {
            if (tituloTp.isEmpty() && linha.contains("Chamado:")) {
                tituloTp = linha;
            }
            if (solicitanteTp.isEmpty() && linha.contains("Cadastrado por:")) {
                solicitanteTp = linha.replaceAll("Cadastrado por:", "");
            }
            if (obsTp.isEmpty() && linha.contains("Fim Previsto")) {
                obsTp = linha;
            }

            if (!solicitanteTp.isEmpty()) {
                break;
            }
        }

        if (getTxfNome().isEmpty()) {
            
        }
        setTxfSolicitante(solicitanteTp);
        setTxfObs(obsTp);
    }
    
    public void sendToTray() {
        try {
            tray.add(trayIcon);
            setVisible(false);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } catch (AWTException ex) {
            System.out.println("ERRO: AWTException: " + ex.getLocalizedMessage());
        }
    }

    private void ajustarPosicao() {
        final int largura = this.getWidth();
        final int altura = this.getHeight();
        final int alturaTela = Toolkit.getDefaultToolkit().getScreenSize().height;

        // ajusta a largura do painel superior
        this.pnlSuperior.setBounds(this.pnlSuperior.getX(), this.pnlSuperior.getY(), largura, this.pnlSuperior.getHeight());

        // ajusta a largura e tamanho do painel inferior (com abas)
        this.pnlInferior.setBounds(this.pnlInferior.getX(), this.pnlInferior.getY(), largura, (altura - this.pnlSuperior.getHeight()));

        // ajusta a posicao do painel containerCampos
        this.containerCampos.setBounds(((largura - this.containerCampos.getWidth()) / 2) - 20, this.containerCampos.getY(), this.containerCampos.getWidth(), this.containerCampos.getHeight());

        // centraliza o painel grid
        this.pnlContador.setBounds(((largura - this.pnlContador.getWidth()) / 2) - 20, this.pnlContador.getY(), this.pnlContador.getWidth(), this.pnlContador.getHeight());

        // centraliza o painel followup_home
        int alturaPainel = altura - (this.pnlSuperior.getHeight()
                + this.containerCampos.getHeight()
                + this.pnlContador.getHeight()
                + this.pnlTempoDecorrido.getHeight()
                + this.pnlExportar.getHeight() + 80);

        // centraliza o painel de tempo
        this.pnlTempoDecorrido.setBounds(((largura - this.pnlTempoDecorrido.getWidth()) / 2) - 20, this.pnlContador.getY() + this.pnlContador.getHeight() + 10, this.pnlTempoDecorrido.getWidth(), this.pnlTempoDecorrido.getHeight());

        // centraliza o painel de exportar
        this.pnlExportar.setBounds(((largura - this.pnlExportar.getWidth()) / 2) - 20, this.pnlTempoDecorrido.getY() + this.pnlTempoDecorrido.getHeight() + 10, this.pnlExportar.getWidth(), this.pnlExportar.getHeight());
    }

    private void setarIcones() {
        try {
            this.setIconImage(new ImageIcon(getClass().getResource("/images/clock.png")).getImage());
            this.btnProcurarDiretorio.setIcon(new ImageIcon(getClass().getResource("/images/folder.png")));
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public boolean todasTarefasParadas() {
        Collection<Tarefa> dList = this.contadorModel.getLinhas();

        for (Tarefa t : dList) {
            if (t.isEmAndamento()) {
                return (false);
            }
        }
        return (true);
    }

    private void iniciarConsole() {
        this.console = new FrameConsole();
        this.console.setVisible(false);
        this.console.setTitle("Console");
    }

    private void iniciarComponentes() {
        this.tarefaAtual = null;
        // Fonte: http://stackoverflow.com/questions/2959718/dynamic-clock-in-java
        ActionListener updateClockAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Assumes clock is a JLabel
                DateFormat hora = new SimpleDateFormat("HH:mm:ss");
                lblHora.setText(hora.format(new Date()));
                setDataPainel(obterData());
                if (tarefaAtual != null) {
                    setLblTotalTarefa(tarefaAtual.getCronometro());
                }
            }
        };
        this.comandoTela = "";
        this.relogio = new Timer(1000, updateClockAction);

        if (OS.isWindows()) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (ClassNotFoundException ex) {
                System.out.println("ClassNotFoundException: " + ex.getLocalizedMessage());
            } catch (InstantiationException ex) {
                System.out.println("InstantiationException: " + ex.getLocalizedMessage());
            } catch (IllegalAccessException ex) {
                System.out.println("IllegalAccessException: " + ex.getLocalizedMessage());
            } catch (UnsupportedLookAndFeelException ex) {
                System.out.println("UnsupportedLookAndFeelException: " + ex.getLocalizedMessage());
            }
        }

        // Painel com data e hora do dia
        this.pnlSuperior = new JPanel();
        this.pnlSuperior.setBounds(0, 0, 800, 80);
        this.pnlSuperior.setLayout(null);
        this.pnlSuperior.setBorder(BorderFactory.createEtchedBorder());

        this.lblData = new JLabel();
        this.lblData.setBounds(10, 20, 500, 21);
        this.lblData.setFont(new Font("Verdana", 0, 18));

        this.lblHora = new JLabel();
        this.lblHora.setBounds(700, 20, 100, 21);
        this.lblHora.setFont(new Font("Verdana", 0, 18));

        // Painel inferior geral
        this.pnlInferior = new JPanel();
        this.pnlInferior.setBounds(0, 80, 800, 520);
        this.pnlInferior.setLayout(null);
        this.pnlInferior.setName("Tarefas");
        this.pnlInferior.setBorder(BorderFactory.createEtchedBorder());

        // Container para centralização dos campos quando alterado o tamanho
        this.containerCampos = new JPanel();
        this.containerCampos.setLayout(null);
        this.containerCampos.setBounds(0, 2, 780, 170);

        this.lblCodigo = new JLabel("Diagrama/Programa:");
        this.lblCodigo.setHorizontalAlignment(JLabel.RIGHT);
        this.lblCodigo.setBounds(10, 10, 140, 21); // y = 31 + 5 // x = 100
        this.lblCodigo.setFont(new Font("Verdana", 0, 12));

        this.txfCodigo = new JTextField();
        this.txfCodigo.setBounds(155, 10, 90, 21);
        this.txfCodigo.setFont(new Font("Monospaced", 0, 12));

        this.lblNome = new JLabel("Descrição:");
        this.lblNome.setHorizontalAlignment(JLabel.RIGHT);
        this.lblNome.setBounds(30, 36, 120, 21); // y = 57 + 5 // x = 100
        this.lblNome.setFont(new Font("Verdana", 0, 12));

        this.txfNome = new JTextField();
        this.txfNome.setBounds(155, 36, 300, 21);
        this.txfNome.setFont(new Font("Monospaced", 0, 12));

        this.lblSolicitante = new JLabel("Cliente:");
        this.lblSolicitante.setHorizontalAlignment(JLabel.RIGHT);
        this.lblSolicitante.setBounds(0, 62, 150, 21); // y = 83 + 5 // x = 100
        this.lblSolicitante.setFont(new Font("Verdana", 0, 12));

        this.txfSolicitante = new JTextField();
        this.txfSolicitante.setBounds(155, 62, 120, 21);
        this.txfSolicitante.setFont(new Font("Monospaced", 0, 12));

        this.lblObs = new JLabel("Serviço:");
        this.lblObs.setHorizontalAlignment(JLabel.RIGHT);
        this.lblObs.setBounds(60, 88, 90, 21); // y = 109 + 5 // x = 100
        this.lblObs.setFont(new Font("Verdana", 0, 12));

        this.txfObs = new JTextField();
        this.txfObs.setBounds(155, 88, 400, 21);
        this.txfObs.setFont(new Font("Monospaced", 0, 12));
        
        this.btnSair = new JButton("Sair");
        this.btnSair.setBounds(650, 45, 100, 30);
        this.btnSair.setFont(new Font("Verdana", 0, 12));
        this.btnSair.setToolTipText("Sair do programa");
        this.btnSair.setFocusable(false);

        this.btnAdd = new JButton("Adicionar");
        this.btnAdd.setBounds(650, 83, 100, 30);
        this.btnAdd.setFont(new Font("Verdana", 0, 12));
        this.btnAdd.setToolTipText("Add task to system board");
        this.btnAdd.setFocusable(false);

        this.btnParar = new JButton("Parar");
        this.btnParar.setBounds((20 + Tela.larguraBotao), 140, Tela.larguraBotao, 30);
        this.btnParar.setFont(new Font("Verdana", 0, 12));
        this.btnParar.setToolTipText("Para a contagem da tarefa selecionada.");
        this.btnParar.setFocusable(false);

        this.btnContinuar = new JButton("Iniciar/Continuar");
        this.btnContinuar.setBounds(20, 140, Tela.larguraBotao, 30);
        this.btnContinuar.setFont(new Font("Verdana", 0, 12));
        this.btnContinuar.setToolTipText("Continua a contagem da tarefa selecionada");
        this.btnContinuar.setFocusable(false);

        this.btnExcluir = new JButton("Excluir");
        this.btnExcluir.setBounds((20 + Tela.larguraBotao * 2), 140, Tela.larguraBotao, 30);
        this.btnExcluir.setFont(new Font("Verdana", 0, 12));
        this.btnExcluir.setToolTipText("Exclui a tarefa selecionada");
        this.btnExcluir.setFocusable(false);

        this.btnCancelar = new JButton("Cancelar");
        this.btnCancelar.setBounds((20 + Tela.larguraBotao * 3), 140, Tela.larguraBotao, 30);
        this.btnCancelar.setFont(new Font("Verdana", 0, 12));
        this.btnCancelar.setToolTipText("Cancelar");
        this.btnCancelar.setFocusable(false);

        this.btnAlterar = new JButton("Alterar");
        this.btnAlterar.setBounds((20 + Tela.larguraBotao * 4), 140, Tela.larguraBotao, 30);
        this.btnAlterar.setFont(new Font("Verdana", 0, 12));
        this.btnAlterar.setToolTipText("Alterar");
        this.btnAlterar.setFocusable(false);

        this.btnExportar = new JButton("Salvar!");
        this.btnExportar.setBounds(576, 5, 144, 30);
        this.btnExportar.setFont(new Font("Verdana", 0, 12));
        this.btnExportar.setToolTipText("Exporta para um arquivo CSV.");
        this.btnExportar.setFocusable(false);

        // Painel que contém as tarefas adicionadas
        this.pnlContador = new JPanel();
        this.pnlContador.setBounds(20, 170, 740, 240);
        this.pnlContador.setEnabled(true);
        this.pnlContador.setLayout(null);
        this.pnlContador.setBorder(BorderFactory.createEtchedBorder());

        // Painel que contem os dados do tempo decorrido
        this.pnlTempoDecorrido = new JPanel();
        this.pnlTempoDecorrido.setBounds(20, pnlContador.getY() + pnlContador.getHeight() + 10, 740, 40);
        this.pnlTempoDecorrido.setLayout(null);
        this.pnlTempoDecorrido.setBorder(BorderFactory.createEtchedBorder());

        this.lblTempoDecorrido = new JLabel("Tempo do Serviço:");
        this.lblTempoDecorrido.setBounds(10, 10, 135, 21);
        this.lblTempoDecorrido.setFont(new Font("Verdana", 0, 12));
        this.lblTempoDecorrido.setHorizontalAlignment(JLabel.RIGHT);

        this.lblTotalTarefa = new JLabel("00:00:00");
        this.lblTotalTarefa.setBounds(150, 10, 115, 21);
        this.lblTotalTarefa.setFont(new Font("Verdana", 0, 12));

        this.lblTempoTodos = new JLabel("Tempo Total:");
        this.lblTempoTodos.setBounds(510, 10, 115, 21);
        this.lblTempoTodos.setFont(new Font("Verdana", 0, 12));
        this.lblTempoTodos.setHorizontalAlignment(JLabel.RIGHT);

        this.lblTotalTempo = new JLabel("00:00:00");
        this.lblTotalTempo.setBounds(630, 10, 115, 21);
        this.lblTotalTempo.setFont(new Font("Verdana", 0, 12));

        // Painel que contém os campos para exportar
        this.pnlExportar = new JPanel();
        this.pnlExportar.setLayout(null);
        this.pnlExportar.setBounds(20, pnlTempoDecorrido.getY() + pnlTempoDecorrido.getHeight() + 10, 740, 40);
        this.pnlExportar.setBorder(BorderFactory.createEtchedBorder());

        this.lblExportarPara = new JLabel("Exportar:");
        this.lblExportarPara.setBounds(25, 10, 100, 21);
        this.lblExportarPara.setHorizontalAlignment(JLabel.RIGHT);
        this.lblExportarPara.setFont(new Font("Verdana", 0, 12));

        this.txfDiretorio = new JTextField(System.getProperty("user.home"));
        this.txfDiretorio.setText(this.txfDiretorio.getText() + ((OS.isWindows()) ? "\\Desktop\\Tarefas.csv" : "/Tarefas.csv"));
        this.txfDiretorio.setBounds(130, 10, 400, 21);
        this.txfDiretorio.setFont(new Font("Monospaced", 0, 12));

        this.btnProcurarDiretorio = new JButton();
        this.btnProcurarDiretorio.setBounds(535, 10, 21, 21);

        this.getContentPane().add(this.pnlSuperior, null);
        this.pnlSuperior.add(this.lblData);
        this.pnlSuperior.add(this.lblHora);

        this.getContentPane().add(this.pnlInferior, null);
        this.pnlInferior.add(this.containerCampos);

        this.containerCampos.add(this.lblCodigo);
        this.containerCampos.add(this.txfCodigo);
        this.containerCampos.add(this.lblNome);
        this.containerCampos.add(this.txfNome);
        this.containerCampos.add(this.lblSolicitante);
        this.containerCampos.add(this.txfSolicitante);
        this.containerCampos.add(this.lblObs);
        this.containerCampos.add(this.txfObs);
        this.containerCampos.add(this.btnSair);
        this.containerCampos.add(this.btnAdd);
        this.containerCampos.add(this.btnParar);
        this.containerCampos.add(this.btnContinuar);
        this.containerCampos.add(this.btnExcluir);
        this.containerCampos.add(this.btnCancelar);
        this.containerCampos.add(this.btnAlterar);

        this.pnlInferior.add(this.pnlContador);
        this.pnlInferior.add(this.pnlTempoDecorrido);
        this.pnlTempoDecorrido.add(this.lblTempoDecorrido);
        this.pnlTempoDecorrido.add(this.lblTotalTarefa);
        this.pnlTempoDecorrido.add(this.lblTempoTodos);
        this.pnlTempoDecorrido.add(this.lblTotalTempo);
        this.pnlInferior.add(this.pnlExportar);
        this.pnlExportar.add(this.lblExportarPara);
        this.pnlExportar.add(this.txfDiretorio);
        this.pnlExportar.add(this.btnProcurarDiretorio);
        this.pnlExportar.add(this.btnExportar);
    }

    private void iniciarPrograma() {
        setDataPainel(obterData());
        this.relogio.start();
        habilitarBotoes(false);
    }

    private void criarModel() {
        this.contadorTable = new JTable();
        this.contadorModel = new TarefaModel();
        this.contadorTable.setModel(contadorModel);

        this.contadorTable.setShowGrid(false);
        this.contadorTable.getTableHeader().setReorderingAllowed(false);
        this.contadorTable.setBorder(BorderFactory.createEmptyBorder());
        this.contadorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.contadorTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.contadorTable.setFont(new Font("Monospaced", 0, 12));

        this.contadorTable.getColumnModel().getColumn(0).setPreferredWidth(110);
        this.contadorTable.getColumnModel().getColumn(1).setPreferredWidth(240);
        this.contadorTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        this.contadorTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        this.contadorTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        this.contadorTable.getColumnModel().getColumn(5).setPreferredWidth(70);
        this.contadorTable.getColumnModel().getColumn(6).setPreferredWidth(60);

        this.pnlContador.setLayout(new BorderLayout());
        this.pnlContador.add(new JScrollPane(contadorTable));

        class RRenderer extends DefaultTableCellRenderer {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (row % 2 == 0) {
                    comp.setBackground(new Color(202, 225, 255));
                } else {
                    comp.setBackground(new Color(254, 254, 254));
                }

                if (column == 0) {
                    ((JLabel) comp).setHorizontalAlignment(JLabel.LEFT);
                } else if (column == 6) {
                    ((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
                } else {
                    ((JLabel) comp).setHorizontalAlignment(JLabel.LEFT);
                }

                if (isSelected) {
                    comp.setBackground(new Color(comp.getBackground().getRed() - 40, comp.getBackground().getGreen() - 40, comp.getBackground().getBlue() - 40));

                    try {
                        Tarefa t = getLinhaSelecionada();
                        String toolTip = "Incluído em " + t.getDataHoraInclusao();
                        ((JLabel) comp).setToolTipText(toolTip);
                    } catch (Exception ex) {
                        System.out.println("erro: " + ex.getMessage());
                    }
                }
                return (comp);
            }
        }

        this.contadorTable.setDefaultRenderer(Object.class, new RRenderer());
        this.contadorTable.setDefaultRenderer(String.class, new RRenderer());
        this.contadorTable.setDefaultRenderer(Character.class, new RRenderer());

        ((DefaultTableCellRenderer) contadorTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
    }

    public void habilitarBotoes(final boolean simOuNao) {
        this.btnContinuar.setEnabled(simOuNao);
        this.btnParar.setEnabled(simOuNao);
        habilitarExcluir(simOuNao);
        this.btnAlterar.setEnabled(simOuNao);
    }

    public void habilitarContinuar(final boolean simOuNao) {
        this.btnContinuar.setEnabled(!simOuNao);
        this.btnParar.setEnabled(simOuNao);
    }

    public void habilitarBotaoInserir(final boolean simOuNao) {
        this.btnAdd.setEnabled(simOuNao);
    }

    public void habilitarExcluir(final boolean simOuNao) {
        this.btnExcluir.setEnabled(simOuNao);
    }

    private String obterData() {
        final DateFormat dia = new SimpleDateFormat("dd");
        final DateFormat mes = new SimpleDateFormat("MMMM");
        final DateFormat ano = new SimpleDateFormat("yyyy");
        final DateFormat hora = new SimpleDateFormat("HH");

        final Date dataAtual = new Date();

        String saudacao = "";
        if (Integer.parseInt(String.valueOf(hora.format(dataAtual))) <= 12) {
            saudacao = "Bom dia!";
        } else if (Integer.parseInt(String.valueOf(hora.format(dataAtual))) <= 18) {
            saudacao = "Boa tarde!";
        } else {
            saudacao = "Não é hora de parar?";
        }

        return (dia.format(dataAtual) + " de " + mes.format(dataAtual) + " de " + ano.format(dataAtual) + ". "
                + saudacao);
    }

    public void acessar() {
        try {
            comandoTela = "";

            while (comandoTela.isEmpty()) {
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void limpar() {;
        setTxfCodigo("");
        setTxfNome("");
        setTxfSolicitante("");
        setTxfObs("");
    }

    public String getTxfCodigo() {
        return (this.txfCodigo.getText().trim());
    }

    public String getTxfNome() {
        return (this.txfNome.getText().trim());
    }

    public String getTxfSolicitante() {
        return (this.txfSolicitante.getText());
    }

    public String getTxfObs() {
        return (this.txfObs.getText());
    }

    public void setTxfSolicitante(final String pSolicitante) {
        this.txfSolicitante.setText(pSolicitante);
    }

    public void setTxfObs(final String obsParam) {
        this.txfObs.setText(obsParam);
    }

    public void setTxfDiretorio(final String dirParam) {
        txfDiretorio.setText(dirParam);
    }

    public String getTxfDiretorio() {
        return (txfDiretorio.getText());
    }

    public String getComandoTela() {
        return (this.comandoTela);
    }

    public String getLblSuperior() {
        return (this.lblData.getText());
    }

    public void setDataPainel(final String lblParam) {
        this.lblData.setText(lblParam);
    }

    public void addTarefa(final Tarefa pTarefa) {
        this.contadorModel.addLinha(pTarefa);
    }

    public void limparTarefas() {
        this.contadorModel.limpar();
    }

    public void setLblTotalTarefa(final String param) {
        this.lblTotalTarefa.setText(param);
    }

    public void setLblTotalTempo(final String param) {
        this.lblTotalTempo.setText(param);
    }

    public void limparTempoDecorrido() {
        setLblTotalTarefa("00:00:00");
        setLblTotalTempo("00:00:00");
    }

    public void setTarefa(final Tarefa pTarefa) {
        this.tarefaAtual = pTarefa;
    }

    public Tarefa getTarefa() {
        return (this.tarefaAtual);
    }

    public void carregarTarefaPosicao(final Tarefa pTarefa, final char pPosicao) {
        Collection<Tarefa> tarefaList = this.contadorModel.getLinhas();
        int contador = 0;

        if (tarefaList != null && !tarefaList.isEmpty()) {
            for (Tarefa d : tarefaList) {
                if (pTarefa.equals(d)) {
                    break;
                }
                contador++;
            }
        }

        if (pPosicao == '-') {
            contador--;
        } else {
            contador++;
        }

        if ((contador + 1) > this.contadorModel.getRowCount() || contador < 0) {
            return;
        }

        Tarefa antes = this.contadorModel.getLinha(contador);

        if (antes != null) {
            setTarefa(antes);
            setTxfSolicitante(antes.getCliente());
            setTxfObs(antes.getServico());
        }
    }

    public Tarefa getLinhaSelecionada() {
        int linha = this.contadorTable.getSelectedRow();
        return (this.contadorModel.getLinha(linha));
    }

    public Collection<Tarefa> getLinhas() {
        return (this.contadorModel.getLinhas());
    }

    public void habilitarBtnAlterar() {
        this.btnAlterar.setEnabled(true);
    }

    public void setTxfCodigo(String codigo) {
        this.txfCodigo.setText(codigo);
    }

    public void setTxfNome(String nome) {
        this.txfNome.setText(nome);
    }
}

class TimeHelper {

    public static final Integer secondsInHour = 3600;
    public static final Integer secondsInMinuts = 60;

    public static String secondsToTime(Integer pSeconds) {
        String hour = String.valueOf(pSeconds / secondsInHour);
        pSeconds -= (Integer.parseInt(hour) * secondsInHour);
        if (Integer.parseInt(hour) < 10) {
            hour = "0" + hour;
        }

        String minuts = String.valueOf(pSeconds / secondsInMinuts);
        pSeconds -= (Integer.parseInt(minuts) * secondsInMinuts);
        if (Integer.parseInt(minuts) < 10) {
            minuts = "0" + minuts;
        }

        String seconds = pSeconds.toString();
        if (Integer.parseInt(seconds) < 10) {
            seconds = "0" + seconds;
        }

        return (hour + ":" + minuts + ":" + seconds);
    }
}

