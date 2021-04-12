package task.timer.front;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;

import task.timer.back.OS;
import task.timer.back.Program;
import task.timer.back.Tarefa;
import task.timer.util.ExportUtil;


public class View extends JFrame {
    
    private static final long serialVersionUID = -8160996694338418927L;
    public static int larguraBotao = 148;
    private Timer relogio;
    private Tarefa tarefaAtual;
    private FrameConsole console;
    private JPanel pnlSuperior;
    private JLabel lblData;
    private JLabel lblHora;
    private JPanel pnlInferior;
    private JButton btnCancelar;
    private JTextField txfCodigo;
    private JTextField txfNome;
    private JTextField txfSolicitante;
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
    private JLabel lblTotalTarefa;
    private JLabel lblTotalTempo;
    private JPanel pnlExportar;
    private JTextField txfDiretorio;
    private JButton btnProcurarDiretorio;
    private JPanel containerCampos;
    public JTable contadorTable;
    public TarefaModel contadorModel;

    private final Program program;

    public View() {
        this.program = new Program();

        try {
            SwingUtilities.invokeAndWait(() -> {
                iniciarComponentes();
                setLayout(null);
                setSize(815, 640);
                setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                setTitle("TT - A simple task timer - v2.0.0 - 28/10/2020");
                startApp();
                criarModel();
                iniciarConsole();
                setLocationRelativeTo(null);
                setarIcones();
                adicionarListener();
                setVisible(true);
            });
        }
        catch (InterruptedException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    private void adicionarListener() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarPosicao();
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            try {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_I && e.getID() == KeyEvent.KEY_PRESSED) {
                    console.setVisible(!console.isVisible());
                }
            } catch (Exception exc) {
                System.out.println("Erro ao disparar evento de tecla: " + exc.getMessage());
            }
            return false;
        });

        this.contadorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                Tarefa tarefa = getLinhaSelecionada();

                setTxfCodigo(tarefa.getDiagramaPrograma());
                setTxfNome(tarefa.getDescricao());
                setTxfSolicitante(tarefa.getCliente());
                setTxfObs(tarefa.getServico());
                habilitarBotoes(true);
                habilitarContinuar(tarefa.isEmAndamento());
                habilitarBotaoInserir(false);
                setLblTotalTarefa(tarefa.getDuracao());
                habilitarBtnAlterar();

                setLblTotalTempo(program.obterTempoTodasTarefas());
            }
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

        this.txfObs.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent fe) {
                txfObs.selectAll();
            }
        });

        this.txfObs.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnAdd.doClick();
                }
            }
        });

        this.btnSair.addActionListener(evt -> {
            if (program.getRunningTasks().isEmpty()) {
                relogio.stop();
                console.dispose();
                dispose();
            } else {
                Mensagem.informacao("Não é possível sair com tarefas em andamento.", this);
            }
        });

        this.btnAdd.addActionListener(e -> {
            Tarefa tarefa = new Tarefa();
            tarefa.setDiagramaPrograma(getTxfCodigo());
            tarefa.setDescricao(getTxfNome());
            tarefa.setCliente(getTxfSolicitante());
            tarefa.setServico(getTxfObs());

            program.addTask(tarefa);

            addTarefa(tarefa);
            limpar();
        });

        this.btnContinuar.addActionListener(e -> {
            Tarefa tarefa = getLinhaSelecionada();
            tarefa.continuar();

            program.updateTask(tarefa);

            habilitarContinuar(true);
            contadorModel.fireTableDataChanged();
            togglePlayPause();
        });

        this.btnParar.addActionListener(e -> {
            Tarefa tarefa = getLinhaSelecionada();
            tarefa.parar();

            program.updateTask(tarefa);

            DateFormat data = new SimpleDateFormat("HH:mm:ss");

            if (tarefa.getDuracao().isEmpty()) {
                String duracao = program.calcularTempoDuracao(tarefa.getHoraTermino(), data.format(tarefa.getHoraIntervalo()));
                tarefa.setDuracao(duracao);
            } else {
                String duracaoAtual = tarefa.getDuracao();
                String novaDuracao = program.calcularTempoDuracao(tarefa.getHoraTermino(), data.format(tarefa.getHoraIntervalo()));
                tarefa.setDuracao(program.somarTempo(duracaoAtual, novaDuracao));
            }

            habilitarContinuar(false);
            contadorModel.fireTableDataChanged();
            limpar();
            togglePlayPause();
        });

        this.btnExcluir.addActionListener(e -> {
            Tarefa tarefa = getLinhaSelecionada();

            if (Mensagem.confirmar("Confirma a exclusão da tarefa?", this)) {
                tarefa.parar();
                program.removeTask(tarefa);
                contadorModel.removeLinha(tarefa);
                contadorTable.getSelectionModel().clearSelection();
                habilitarBotaoInserir(true);
                habilitarBotoes(false);
                limpar();
                limparTempoDecorrido();
            }
        });

        this.btnAlterar.addActionListener(e -> {
            Tarefa tarefa = getLinhaSelecionada();
            tarefa.setDiagramaPrograma(getTxfCodigo());
            tarefa.setDescricao(getTxfNome());
            tarefa.setCliente(getTxfSolicitante());
            tarefa.setServico(getTxfObs());

            program.updateTask(tarefa);

            contadorModel.fireTableDataChanged();
            limpar();
        });

        this.btnExportar.addActionListener(e -> {
            int count = program.exportAllTaks(getTxfDiretorio());
            if (count == 0) {
                Mensagem.informacao("No tasks to export!", this);
            } else {
                Mensagem.informacao("Taks exported sucessfuly!", this);
            }
        });

        this.btnCancelar.addActionListener(e -> {
            limpar();
            limparTempoDecorrido();
            habilitarBotoes(false);
            habilitarBotaoInserir(true);
            contadorTable.getSelectionModel().clearSelection();
        });

        this.btnProcurarDiretorio.addActionListener(e -> {
            String dir = FileChooser.chooseDir();
            if (dir != null) {
                setTxfDiretorio(dir);
            }
        });
    }

    private void togglePlayPause() {
        try {
            Robot robot = new Robot();

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_P);

            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_ALT);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    private void ajustarPosicao() {
        final int largura = this.getWidth();
        final int altura = this.getHeight();

        // ajusta a largura do painel superior
        this.pnlSuperior.setBounds(this.pnlSuperior.getX(), this.pnlSuperior.getY(), largura, this.pnlSuperior.getHeight());

        // ajusta a largura e tamanho do painel inferior (com abas)
        this.pnlInferior.setBounds(this.pnlInferior.getX(), this.pnlInferior.getY(), largura, (altura - this.pnlSuperior.getHeight()));

        // ajusta a posicao do painel containerCampos
        this.containerCampos.setBounds(((largura - this.containerCampos.getWidth()) / 2) - 20, this.containerCampos.getY(), this.containerCampos.getWidth(), this.containerCampos.getHeight());

        // centraliza o painel grid
        this.pnlContador.setBounds(((largura - this.pnlContador.getWidth()) / 2) - 20, this.pnlContador.getY(), this.pnlContador.getWidth(), this.pnlContador.getHeight());

        // centraliza o painel de tempo
        this.pnlTempoDecorrido.setBounds(((largura - this.pnlTempoDecorrido.getWidth()) / 2) - 20, this.pnlContador.getY() + this.pnlContador.getHeight() + 10, this.pnlTempoDecorrido.getWidth(), this.pnlTempoDecorrido.getHeight());

        // centraliza o painel de exportar
        this.pnlExportar.setBounds(((largura - this.pnlExportar.getWidth()) / 2) - 20, this.pnlTempoDecorrido.getY() + this.pnlTempoDecorrido.getHeight() + 10, this.pnlExportar.getWidth(), this.pnlExportar.getHeight());
    }

    private void setarIcones() {
        this.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/timer.png"))).getImage());
        this.btnProcurarDiretorio.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/folder.png"))));
    }

    private void iniciarConsole() {
        this.console = new FrameConsole();
        this.console.setVisible(false);
        this.console.setTitle("Console");
    }

    private void iniciarComponentes() {
        this.tarefaAtual = null;
        // Fonte: http://stackoverflow.com/questions/2959718/dynamic-clock-in-java
        ActionListener updateClockAction = e -> {
            // Assumes clock is a JLabel
            DateFormat hora = new SimpleDateFormat("HH:mm:ss");
            lblHora.setText(hora.format(new Date()));
            setDataPainel(program.getGreetings());
            if (tarefaAtual != null) {
                setLblTotalTarefa(tarefaAtual.getCronometro());
            }
        };
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

        final Font notoSansLabel = new Font("Noto Sans", Font.PLAIN, 13);
        final Font notoSansInput = new Font("Noto Sans", Font.PLAIN, 12);
        final Font notoSans18 = new Font("Noto Sans", Font.PLAIN, 18);

        // Painel com data e hora do dia
        this.pnlSuperior = new JPanel();
        this.pnlSuperior.setBounds(0, 0, 800, 80);
        this.pnlSuperior.setLayout(null);
        this.pnlSuperior.setBorder(BorderFactory.createEtchedBorder());

        this.lblData = new JLabel();
        this.lblData.setBounds(25, 25, 500, 21);
        this.lblData.setFont(notoSans18);

        this.lblHora = new JLabel();
        this.lblHora.setBounds(700, 25, 100, 21);
        this.lblHora.setFont(notoSans18);

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

        final JLabel lblCodigo = new JLabel("Programa:");
        lblCodigo.setHorizontalAlignment(JLabel.RIGHT);
        lblCodigo.setBounds(10, 10, 140, 21); // y = 31 + 5 // x = 100
        lblCodigo.setFont(notoSansLabel);

        this.txfCodigo = new JTextField();
        this.txfCodigo.setBounds(155, 10, 90, 21);
        this.txfCodigo.setFont(notoSansInput);

        final JLabel lblNome = new JLabel("Nome:");
        lblNome.setHorizontalAlignment(JLabel.RIGHT);
        lblNome.setBounds(30, 36, 120, 21); // y = 57 + 5 // x = 100
        lblNome.setFont(notoSansLabel);

        this.txfNome = new JTextField();
        this.txfNome.setBounds(155, 36, 300, 21);
        this.txfNome.setFont(notoSansInput);

        final JLabel lblSolicitante = new JLabel("Cliente:*");
        lblSolicitante.setHorizontalAlignment(JLabel.RIGHT);
        lblSolicitante.setBounds(0, 62, 150, 21); // y = 83 + 5 // x = 100
        lblSolicitante.setFont(notoSansLabel);

        this.txfSolicitante = new JTextField();
        this.txfSolicitante.setBounds(155, 62, 120, 21);
        this.txfSolicitante.setFont(notoSansInput);

        final JLabel lblObs = new JLabel("Tarefa:");
        lblObs.setHorizontalAlignment(JLabel.RIGHT);
        lblObs.setBounds(60, 88, 90, 21); // y = 109 + 5 // x = 100
        lblObs.setFont(notoSansLabel);

        this.txfObs = new JTextField();
        this.txfObs.setBounds(155, 88, 400, 21);
        this.txfObs.setFont(notoSansInput);

        this.btnSair = new JButton("Sair");
        this.btnSair.setBounds(650, 45, 100, 30);
        this.btnSair.setFont(notoSansLabel);
        this.btnSair.setToolTipText("Fechar o programa e sair");
        this.btnSair.setFocusable(false);

        this.btnAdd = new JButton("Adicionar");
        this.btnAdd.setBounds(650, 83, 100, 30);
        this.btnAdd.setFont(notoSansLabel);
        this.btnAdd.setToolTipText("Add task to system board");
        this.btnAdd.setFocusable(false);

        this.btnParar = new JButton("Parar");
        this.btnParar.setBounds((20 + larguraBotao), 140, larguraBotao, 30);
        this.btnParar.setFont(notoSansLabel);
        this.btnParar.setToolTipText("Para a contagem da tarefa selecionada.");
        this.btnParar.setFocusable(false);
        this.btnParar.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/pause.png"))));

        this.btnContinuar = new JButton("Iniciar");
        this.btnContinuar.setBounds(20, 140, larguraBotao, 30);
        this.btnContinuar.setFont(notoSansLabel);
        this.btnContinuar.setToolTipText("Continua a contagem da tarefa selecionada");
        this.btnContinuar.setFocusable(false);
        this.btnContinuar.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/play.png"))));

        this.btnExcluir = new JButton("Excluir");
        this.btnExcluir.setBounds((20 + larguraBotao * 2), 140, larguraBotao, 30);
        this.btnExcluir.setFont(notoSansLabel);
        this.btnExcluir.setToolTipText("Exclui a tarefa selecionada");
        this.btnExcluir.setFocusable(false);
        this.btnExcluir.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/trash.png"))));

        this.btnCancelar = new JButton("Cancelar");
        this.btnCancelar.setBounds((20 + larguraBotao * 3), 140, larguraBotao, 30);
        this.btnCancelar.setFont(notoSansLabel);
        this.btnCancelar.setToolTipText("Cancelar");
        this.btnCancelar.setFocusable(false);
        this.btnCancelar.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/cancel.png"))));

        this.btnAlterar = new JButton("Alterar");
        this.btnAlterar.setBounds((20 + larguraBotao * 4), 140, larguraBotao, 30);
        this.btnAlterar.setFont(notoSansLabel);
        this.btnAlterar.setToolTipText("Alterar");
        this.btnAlterar.setFocusable(false);
        this.btnAlterar.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("images/edit.png"))));

        this.btnExportar = new JButton("Exportar!");
        this.btnExportar.setBounds(576, 5, 144, 30);
        this.btnExportar.setFont(notoSansLabel);
        this.btnExportar.setToolTipText("Exporta para um arquivo CSV no diretório home.");
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

        final JLabel lblTempoDecorrido = new JLabel("Tarefa:");
        lblTempoDecorrido.setBounds(10, 10, 135, 21);
        lblTempoDecorrido.setFont(notoSansLabel);
        lblTempoDecorrido.setHorizontalAlignment(JLabel.RIGHT);

        this.lblTotalTarefa = new JLabel("00:00:00");
        this.lblTotalTarefa.setBounds(150, 10, 115, 21);
        this.lblTotalTarefa.setFont(notoSansLabel);

        final JLabel lblTempoTodos = new JLabel("Total:");
        lblTempoTodos.setBounds(510, 10, 115, 21);
        lblTempoTodos.setFont(notoSansLabel);
        lblTempoTodos.setHorizontalAlignment(JLabel.RIGHT);

        this.lblTotalTempo = new JLabel("00:00:00");
        this.lblTotalTempo.setBounds(630, 10, 115, 21);
        this.lblTotalTempo.setFont(notoSansLabel);

        // Painel que contém os campos para exportar
        this.pnlExportar = new JPanel();
        this.pnlExportar.setLayout(null);
        this.pnlExportar.setBounds(20, pnlTempoDecorrido.getY() + pnlTempoDecorrido.getHeight() + 10, 740, 40);
        this.pnlExportar.setBorder(BorderFactory.createEtchedBorder());

        final JLabel lblExportarPara = new JLabel("Exportar:");
        lblExportarPara.setBounds(25, 10, 100, 21);
        lblExportarPara.setHorizontalAlignment(JLabel.RIGHT);
        lblExportarPara.setFont(notoSansLabel);

        this.txfDiretorio = new JTextField();
        this.txfDiretorio.setText(ExportUtil.getDestPath());
        this.txfDiretorio.setBounds(130, 10, 400, 21);
        this.txfDiretorio.setFont(notoSansInput);

        this.btnProcurarDiretorio = new JButton();
        this.btnProcurarDiretorio.setBounds(535, 10, 21, 21);

        this.getContentPane().add(this.pnlSuperior, null);
        this.pnlSuperior.add(this.lblData);
        this.pnlSuperior.add(this.lblHora);

        this.getContentPane().add(this.pnlInferior, null);
        this.pnlInferior.add(this.containerCampos);

        this.containerCampos.add(lblCodigo);
        this.containerCampos.add(this.txfCodigo);
        this.containerCampos.add(lblNome);
        this.containerCampos.add(this.txfNome);
        this.containerCampos.add(lblSolicitante);
        this.containerCampos.add(this.txfSolicitante);
        this.containerCampos.add(lblObs);
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
        this.pnlTempoDecorrido.add(lblTempoDecorrido);
        this.pnlTempoDecorrido.add(this.lblTotalTarefa);
        this.pnlTempoDecorrido.add(lblTempoTodos);
        this.pnlTempoDecorrido.add(this.lblTotalTempo);
        this.pnlInferior.add(this.pnlExportar);
        this.pnlExportar.add(lblExportarPara);
        this.pnlExportar.add(this.txfDiretorio);
        this.pnlExportar.add(this.btnProcurarDiretorio);
        this.pnlExportar.add(this.btnExportar);
    }

    private void startApp() {
        setDataPainel(program.getGreetings());
        habilitarBotoes(false);
        this.relogio.start();
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
        this.contadorTable.setFont(new Font("Monospaced", Font.PLAIN, 12));

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
        this.btnExcluir.setEnabled(simOuNao);
        this.btnAlterar.setEnabled(simOuNao);
    }

    public void habilitarContinuar(final boolean simOuNao) {
        this.btnContinuar.setEnabled(!simOuNao);
        this.btnParar.setEnabled(simOuNao);
    }

    public void habilitarBotaoInserir(final boolean simOuNao) {
        this.btnAdd.setEnabled(simOuNao);
    }

    public void limpar() {
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

    public void setDataPainel(final String lblParam) {
        this.lblData.setText(lblParam);
    }

    public void addTarefa(final Tarefa pTarefa) {
        if (pTarefa.getDiagramaPrograma().isEmpty()) {
            Mensagem.informacao("Informe o programa!", this);
            return;
        }
        if (pTarefa.getCliente().isEmpty()) {
            Mensagem.informacao("Informe o cliente!", this);
            return;
        }
        if (pTarefa.getServico().isEmpty()) {
            Mensagem.informacao("Informe a tarefa!", this);
            return;
        }

        if (pTarefa.getDescricao().isEmpty()) {
            pTarefa.setDescricao("Não informado");
        }

        this.contadorModel.addLinha(pTarefa);
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

    public Tarefa getLinhaSelecionada() {
        int linha = this.contadorTable.getSelectedRow();
        return (this.contadorModel.getLinha(linha));
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
