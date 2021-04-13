package task.timer.back;

import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Timer;

import task.timer.util.TempoUtil;

public class TempoTarefa {
    
    private String horaInicio;
    private String horaTermino;
    private String duracao;
    private boolean emAndamento;
    private final Timer timer;
    private String contador;

    public TempoTarefa() {
        this.horaInicio = "00:00:00";
        this.horaTermino = "00:00:00";
        this.duracao = "00:00:00";
        this.emAndamento = false;
        this.contador = "00:00:00";

        ActionListener updateClockAction = e -> incrementarUmSegundo();
        this.timer = new Timer(1000, updateClockAction);

        iniciar();
    }

    private void incrementarUmSegundo() {
        try {

            int hora = Integer.parseInt(contador.substring(0, 2));
            int min = Integer.parseInt(contador.substring(3, 5));
            int seg = Integer.parseInt(contador.substring(6, 8));

            seg++;

            if (seg >= 60) {
                min++;
                seg -= 60;
            }

            if (min >= 60) {
                hora++;
                min -= 60;
            }

            contador = String.format("%02d:%02d:%02d", hora, min, seg);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void iniciar() {
        setHoraInicio(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        setHoraTermino("00:00:00");
        setEmAndamento(true);
        iniciarTimer();
    }

    public void parar() {
        setHoraTermino(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        setEmAndamento(false);
        pararTimer();

        // Somar o tempo decorrido
        String novaDuracao = TempoUtil.calcularTempoDuracao(getHoraTermino(), getHoraInicio());
        this.duracao = TempoUtil.somarTempo(this.duracao, novaDuracao);
    }
    
    private void iniciarTimer() {
        this.timer.start();
    }

    public void pararTimer() {
        this.timer.stop();
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public boolean isEmAndamento() {
        return emAndamento;
    }

    public void setEmAndamento(boolean emAndamento) {
        this.emAndamento = emAndamento;
    }
}
