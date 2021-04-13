package task.timer.back;

import java.text.SimpleDateFormat;
import java.util.Date;

import task.timer.util.TempoUtil;

public class Tarefa {

    private String programa;
    private String nome;
    private String cliente;
    private String tarefa;
    private TempoTarefa tempoTarefa;

    public Tarefa() {
        this.programa = "";
        this.nome = "";
        this.cliente = "";
        this.tarefa = "";
        this.tempoTarefa = new TempoTarefa();
    }

    public String getPrograma() {
        return programa;
    }

    public String getNome() {
        return nome;
    }

    public String getCliente() {
        return cliente;
    }

    public String getTarefa() {
        return tarefa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setTarefa(String tarefa) {
        this.tarefa = tarefa;
    }

    public String getHoraInicio() {
        return tempoTarefa.getHoraInicio();
    }

    public String getHoraTermino() {
        return tempoTarefa.getHoraTermino();
    }

    public boolean isEmAndamento() {
        return tempoTarefa.isEmAndamento();
    }

    public String getDuracao() {
        String duracao = tempoTarefa.getDuracao();
        String novaDuracao = "00:00:00";
        if (tempoTarefa.isEmAndamento()) {
            novaDuracao = TempoUtil.calcularTempoDuracao(
                new SimpleDateFormat("HH:mm:ss").format(new Date()),
                tempoTarefa.getHoraInicio()
            );
        }
        return TempoUtil.somarTempo(duracao, novaDuracao);
    }

    public void parar() {
        tempoTarefa.parar();
    }

    public void continuar() {
        tempoTarefa.iniciar();
    }
}
