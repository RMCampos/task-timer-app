package task.timer.app;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.Robot;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.swing.JFileChooser;
import java.util.logging.Logger;

public class Programa {
    private String tempoTotal;
    private char transacao;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public Programa() {
        this.tempoTotal = "00:00:00";
        this.transacao = 'I';
    }

    private String somarTempoTotal(String tempo1, String tempo2) {
        try {
            int hora1 = Integer.parseInt(tempo1.substring(0, 2));
            int min1 = Integer.parseInt(tempo1.substring(3, 5));
            int seg1 = Integer.parseInt(tempo1.substring(6, 8));

            int hora2 = Integer.parseInt(tempo2.substring(0, 2));
            int min2 = Integer.parseInt(tempo2.substring(3, 5));
            int seg2 = Integer.parseInt(tempo2.substring(6, 8));

            int hora3 = hora1 + hora2;
            int min3 = min1 + min2;
            int seg3 = seg1 + seg2;

            if (seg3 >= 60) {
                min3++;
                seg3 -= 60;
            }

            if (min3 >= 60) {
                hora3++;
                min3 -= 60;
            }

            String horaS = (hora3 < 10) ? "0" + hora3 + ":" : hora3 + ":";
            String minS = (min3 < 10) ? "0" + min3 + ":" : min3 + ":";
            String segS = (seg3 < 10) ? "0" + seg3 : seg3 + "";

            return horaS + minS + segS;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "00:00:00";
        }
    }

    public void procurarDiretorio() {
        App frame = App.getInstance();
        JFileChooser folder = new JFileChooser();
        folder.setCurrentDirectory(new File("."));
        folder.setDialogTitle("Selecione uma pasta");
        folder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folder.setAcceptAllFileFilterUsed(false);

        if (folder.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            String caminho = folder.getSelectedFile().toString();
            if (OS.isWindows()) {
                caminho += "\\";
            }
            logger.info("Selecionado: " + caminho);
            frame.setTxfDiretorio(folder.getSelectedFile().toString() + "\\");
        }
    }

    public void alterar() {
        App frame = App.getInstance();
        if (this.transacao == 'A') {
            Tarefa tarefa = frame.getTarefa();

            tarefa.setDiagramaPrograma(frame.getTxfCodigo());
            tarefa.setDescricao(frame.getTxfNome());
            tarefa.setCliente(frame.getTxfSolicitante());
            tarefa.setServico(frame.getTxfObs());
            
            frame.contadorModel.fireTableDataChanged();
            frame.limpar();
        }
    }

    private boolean validar() {
        App frame = App.getInstance();
        boolean flValidar = true;
        String strMsg = "";

        if (frame.getTxfSolicitante().isEmpty()) {
            flValidar = false;
            strMsg += "- Client must not be empty.\n";
        }

        if (!flValidar) {
            Mensagem.informacao("Fix this:\n" + strMsg, frame);
        }

        return flValidar;
    }

    private Tarefa criarTarefa() {
        App frame = App.getInstance();
        Tarefa tarefa = new Tarefa();

        tarefa.setDiagramaPrograma(frame.getTxfCodigo());
        tarefa.setDescricao(frame.getTxfNome());
        tarefa.setCliente(frame.getTxfSolicitante());
        tarefa.setServico(frame.getTxfObs());

        return tarefa;
    }

    public void adicionarTarefa() {
        App frame = App.getInstance();

        if (validar() && this.transacao == 'I') {
            Tarefa tarefa = criarTarefa();

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
            
            tarefa.setDataHoraInclusao(df.format(new Date()));
            tarefa.setHoraInicio(df2.format(new Date()));
            tarefa.setHoraIntervalo(new Date());
            tarefa.setEmAndamento(true);
            tarefa.iniciarTempo();
            frame.addTarefa(tarefa);
            frame.limpar();
        } else if (this.transacao == 'A') {
            alterar();
        } else {
            cancelar();
        }
    }

    public void carregarTarefaSelecionado() {
        App frame = App.getInstance();
        Tarefa tarefa = frame.getLinhaSelecionada();

        frame.setTarefa(tarefa);
        transacao = 'A';
        frame.setTxfCodigo(tarefa.getDiagramaPrograma());
        frame.setTxfNome(tarefa.getDescricao());
        frame.setTxfSolicitante(tarefa.getCliente());
        frame.setTxfObs(tarefa.getServico());
        frame.habilitarBotoes(true);
        frame.habilitarContinuar(tarefa.isEmAndamento());
        frame.habilitarBotaoInserir(false);
        frame.setLblTotalTarefa(tarefa.getDuracao());
        frame.setLblTotalTempo(obterTempoTotalDecorrido());
        frame.habilitarBtnAlterar();
    }

    private String obterTempoTotalDecorrido() {
        App frame = App.getInstance();
        Collection<Tarefa> TarefaList = frame.contadorModel.getLinhas();

        this.tempoTotal = "00:00:00";
        for (Tarefa tarefa : TarefaList) {
            this.tempoTotal = somarTempoTotal(this.tempoTotal, tarefa.getCronometro());
        }
        return tempoTotal;
    }

    public void continuar() {
        App frame = App.getInstance();
        Tarefa tarefa = frame.getLinhaSelecionada();

        if (tarefa == null) {
            Mensagem.informacao("Nenhuma linha selecionada.", frame);
            return;
        }

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        tarefa.setHoraInicio(df.format(new Date()));

        tarefa.setHoraIntervalo(new Date());
        tarefa.setEmAndamento(true);
        tarefa.iniciarTempo();

        frame.habilitarContinuar(true);
        frame.contadorModel.fireTableDataChanged();
        this.transacao = 'I';

        togglePlayPause();
    }

    public void parar(Tarefa pTarefa) {
        App frame = App.getInstance();
        frame.setTarefa(null);

        Tarefa tarefa = pTarefa;

        if (tarefa == null) {
            tarefa = frame.getLinhaSelecionada();
        }

        DateFormat data = new SimpleDateFormat("HH:mm:ss");
        tarefa.setHoraTermino(data.format(new Date()));

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        tarefa.setDataHoraTermino(df.format(new Date()));

        if (tarefa.getDuracao().isEmpty()) {
            String duracao = obterDuracao(tarefa.getHoraTermino(), data.format(tarefa.getHoraIntervalo()));
            tarefa.setDuracao(duracao);
        } else {
            String duracaoAtual = tarefa.getDuracao();
            String novaDuracao = obterDuracao(tarefa.getHoraTermino(), data.format(tarefa.getHoraIntervalo()));
            tarefa.setDuracao(somarDuracao(duracaoAtual, novaDuracao));
        }

        tarefa.setEmAndamento(false);
        tarefa.setHoraIntervalo(new Date());
        tarefa.pararTempo();

        frame.habilitarContinuar(false);
        frame.contadorModel.fireTableDataChanged();
        frame.limpar();
        this.transacao = 'I';

        togglePlayPause();
    }

    public void cancelar() {
        App frame = App.getInstance();
        frame.limpar();
        frame.setTarefa(null);
        frame.limparTempoDecorrido();
        frame.habilitarBotoes(false);
        frame.habilitarBotaoInserir(true);
        frame.contadorTable.getSelectionModel().clearSelection();
        this.transacao = 'I';
    }

    public void exportar() {
        App frame = App.getInstance();
        Collection<Tarefa> tarefaList = frame.contadorModel.getLinhas();
        String tempoTotalTarefas = obterTempoTotalDecorrido();

        if (tarefaList == null || tarefaList.isEmpty()) {
            Mensagem.informacao("Não existe nada para ser exportado.", frame);
            return;
        }

        final String cabecalho = "CODIGO;NOME;SOLICITANTE;TEMPO TOTAL;DATA;HORA INICIO;HORA FIM;OBS";
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dataAtual = formatter.format(new Date());
        String camArquivo = frame.getTxfDiretorio();

        if (camArquivo.isEmpty()) {
            if (OS.isWindows()) {
                camArquivo = System.getProperty("user.home") + "\\Desktop\\";
            } else {
                camArquivo = System.getProperty("user.home") + "/";
            }
        }
        String nomeArquivo = "Tarefas.csv";

        File desktop = new File(camArquivo);

        if (!desktop.exists()) {
            if (!desktop.mkdir()) {
                logger.info("Unable to create directory: " + desktop.getAbsolutePath());
            }
        }

        File arquivoExportado = new File(camArquivo + nomeArquivo);
        FileWriter arquivo = null;

        try {
            arquivo = new FileWriter(arquivoExportado);
            logger.info("Arquivo criado em: " + camArquivo + nomeArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (arquivo == null) {
            logger.severe("Unable to open file for writing!");
            return;
        }

        PrintWriter gravarArq = new PrintWriter(arquivo);

        gravarArq.printf(cabecalho + "\r\n");

        for (Tarefa tarefa : tarefaList) {
            String linha = ((tarefa.getDiagramaPrograma().isEmpty()) ? " N " : tarefa.getDiagramaPrograma()) + ";"
                    + tarefa.getDescricao() + ";"
                    + ((tarefa.getCliente().isEmpty()) ? " N " : tarefa.getCliente()) + ";"
                    + tarefa.getDuracao() + ";"
                    + dataAtual + ";"
                    + tarefa.getHoraInicio() + ";"
                    + tarefa.getHoraTermino() + ";"
                    + ((tarefa.getServico().isEmpty()) ? "Nenhuma." : tarefa.getServico()) + "\r\n";

            logger.info("gravando a linha: " + linha);

            gravarArq.printf(linha);
        }

        // Grava o tempo Total
        gravarArq.printf("\r\n \r\nTempo total das tarefas: " + tempoTotalTarefas + "\r\n");

        try {
                arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Mensagem.informacao("Contador exportado com sucesso!\nLocal: " + camArquivo + "\nNome: " + nomeArquivo, frame);
    }

    public void excluir() {
        App frame = App.getInstance();
        Tarefa tarefa = frame.getLinhaSelecionada();

        if (tarefa == null) {
            Mensagem.informacao("Nenhuma tarefa selecionada.", frame);
        }

        if (Mensagem.confirmar("Confirma a exclusão da tarefa?", frame)) {
            parar(tarefa);
            frame.contadorModel.removeLinha(tarefa);
            frame.contadorTable.getSelectionModel().clearSelection();
            frame.habilitarBotaoInserir(true);
            frame.habilitarBotoes(false);
            frame.limpar();
            frame.limparTempoDecorrido();
        }
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
    private String somarDuracao(String duracao1Param, String duracao2Param) {
        try {
            Integer hor1 = Integer.parseInt(duracao1Param.substring(0, 2).replaceAll(":", ""));
            Integer min1 = Integer.parseInt(duracao1Param.substring(3, 5).replaceAll(":", ""));
            Integer seg1 = Integer.parseInt(duracao1Param.substring(6, 8).replaceAll(":", ""));

            Integer hor2 = Integer.parseInt(duracao2Param.substring(0, 2).replaceAll(":", ""));
            Integer min2 = Integer.parseInt(duracao2Param.substring(3, 5).replaceAll(":", ""));
            Integer seg2 = Integer.parseInt(duracao2Param.substring(6, 8).replaceAll(":", ""));

            String duracaoTotal;
            int horTotal = hor1 + hor2;
            int minTotal = min1 + min2;
            int segTotal = seg1 + seg2;

            if (segTotal > 60) {
                segTotal -= 60;
                minTotal++;
            }

            if (minTotal > 60) {
                minTotal -= 60;
                horTotal++;
            }

            duracaoTotal = String.format("%02d",  horTotal) + ":";
            duracaoTotal += String.format("%02d",  minTotal) + ":";
            duracaoTotal += String.format("%02d",  segTotal);

            return duracaoTotal;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String obterDuracao(String fimParam, String inicParam) {
        try {
            int horFim = Integer.parseInt(fimParam.substring(0, 2));
            int minFim = Integer.parseInt(fimParam.substring(3, 5));
            int segFim = Integer.parseInt(fimParam.substring(6, 8));

            int horIni = Integer.parseInt(inicParam.substring(0, 2));
            int minIni = Integer.parseInt(inicParam.substring(3, 5));
            int segIni = Integer.parseInt(inicParam.substring(6, 8));

            int horaFinal = 0;
            int minFinal;
            int segFinal;

            if (horFim > horIni) {
                horaFinal = horFim - horIni;
            }

            minFinal = minFim - minIni;
            segFinal = segFim - segIni;

            if (segFinal < 0) {
                segFinal += 60;
                minFinal -= 1;
            }

            if (minFinal < 0) {
                minFinal += 60;
                horaFinal -= 1;
            }

            // constroi a hora final
            String duracao;

            duracao = String.format("%02d", horaFinal) + ":";
            duracao += String.format("%02d", minFinal) + ":";
            duracao += String.format("%02d", segFinal);
            return duracao;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "00:00:00";
    }
}
