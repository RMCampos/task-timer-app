package task.timer.back;

import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tarefa {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private String diagramaPrograma;
    private String descricao;
    private String cliente;
    private String servico;
    private String horaInicio;
    private String duracao;
    private String horaTermino;
    private Date horaIntervalo;
    private boolean emAndamento;
    private boolean finalizada;
    private final Timer timer;
    private String cronometro;
    private String dataHoraInclusao;

    public Tarefa() {
        this.diagramaPrograma = "";
        this.descricao = "";
        this.cliente = "";
        this.servico = "";
        this.horaInicio = "";
        this.duracao = "00:00:00";
        this.horaTermino = "00:00:00";
        this.dataHoraInclusao = "00:00:00";
        this.horaIntervalo = new Date();
        this.emAndamento = false;
        this.finalizada = false;
        this.cronometro = "00:00:00";

        ActionListener updateClockAction = e -> cronometro = incrementarUmSegundo(cronometro);
        this.timer = new Timer(1000, updateClockAction);
        iniciar();
    }

    public void iniciar() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        setDataHoraInclusao(df.format(date));
        setHoraInicio(df2.format(date));
        setHoraIntervalo(date);
        setEmAndamento(true);
        iniciarTempo();
    }

    public void continuar() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        setHoraInicio(df.format(date));
        setHoraIntervalo(date);
        setEmAndamento(true);
        iniciarTempo();
    }

    public void parar() {
        Date date = new Date();

        setHoraTermino(new SimpleDateFormat("HH:mm:ss").format(date));
        setEmAndamento(false);
        setHoraIntervalo(date);
        pararTempo();
    }

    private String incrementarUmSegundo(String tempo) {
        try {
            int hora = Integer.parseInt(tempo.substring(0, 2));
            int min = Integer.parseInt(tempo.substring(3, 5));
            int seg = Integer.parseInt(tempo.substring(6, 8));

            seg++;

            if (seg >= 60) {
                min++;
                seg -= 60;
            }

            if (min >= 60) {
                hora++;
                min -= 60;
            }

            String novo = String.format("%02d:%02d:%02d", hora, min, seg);
            logger.info("incrementarUmSegundo novo: {}", novo);
            return novo;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "00:00:00";
        }
    }

    public String getDiagramaPrograma() {
        return diagramaPrograma;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCliente() {
        return cliente;
    }

    public String getServico() {
        return servico;
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

    public boolean isEmAndamento() {
        return this.emAndamento;
    }

    public Date getHoraIntervalo() {
        return horaIntervalo;
    }

    public String getCronometro() {
        return (this.cronometro);
    }

    public void setCronometro(String pCron) {
        this.cronometro = pCron;
    }

    public void setDiagramaPrograma(String diagramaPrograma) {
        this.diagramaPrograma = diagramaPrograma;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
        setCronometro(duracao);
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public void setHoraIntervalo(Date hora) {
        this.horaIntervalo = hora;
    }

    public void setEmAndamento(boolean sim) {
        this.emAndamento = sim;
    }

    public void iniciarTempo() {
        this.timer.start();
    }

    public void pararTempo() {
        this.timer.stop();
        this.cronometro = getDuracao();
    }

    public String getDataHoraInclusao() {
        return (this.dataHoraInclusao);
    }

    public void setDataHoraInclusao(String pData) {
        this.dataHoraInclusao = pData;
    }

    public boolean finalizada() {
        return (this.finalizada);
    }

    public void setFinalizada(boolean pFin) {
        this.finalizada = pFin;
    }
}
