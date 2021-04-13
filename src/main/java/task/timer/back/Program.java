package task.timer.back;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import task.timer.util.TempoUtil;

public class Program {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final TaskRepository taskRepository;

    public Program() {
        taskRepository = new TaskRepository();
    }

    /**
     * Greetings for top panel
     * @return a String with the message
     */
    public String getGreetings() {
        final DateFormat dia = new SimpleDateFormat("dd");
        final DateFormat mes = new SimpleDateFormat("MMMM");
        final DateFormat ano = new SimpleDateFormat("yyyy");
        final DateFormat hora = new SimpleDateFormat("HH");

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        String saudacao;

        // Day name in week
        saudacao = new SimpleDateFormat("EEEE").format(c.getTime());
        saudacao += ", ";

        // Month name
        saudacao += mes.format(c.getTime());
        saudacao += " ";

        // Day of the month
        saudacao += dia.format(c.getTime());
        saudacao += ", ";

        // Year
        saudacao += ano.format(c.getTime());

        // Greeting
        int horaInt = Integer.parseInt(hora.format(c.getTime()));

        if (horaInt <= 11) {
            saudacao += " - Good morning!";
        } else if (horaInt <= 17) {
            saudacao += " - Good afternoon!";
        } else {
            saudacao += " - Good evening!";
        }

        return saudacao;
    }

    public void addTask(Tarefa t) {
        Tarefa bd = taskRepository.getTarefaById(t.getPrograma());
        if (bd != null) {
            throw new RuntimeException("Tarefa já adicionada!");
        }

        taskRepository.save(t);
    }

    public void updateTask(Tarefa t) {
        Tarefa bd = taskRepository.getTarefaById(t.getPrograma());
        if (bd == null) {
            throw new RuntimeException("Tarefa não encontrada!");
        }

        taskRepository.save(t);
    }

    public void removeTask(Tarefa t) {
        Tarefa bd = taskRepository.getTarefaById(t.getPrograma());
        if (bd == null) {
            throw new RuntimeException("Tarefa não encontrada!");
        }

        taskRepository.remove(t.getPrograma());
    }

    public boolean isAlgumaTarefaEmAndamento() {
        List<Tarefa> tarefas = this.taskRepository.getAll();

        long count = tarefas
                .stream()
                .filter(x -> x.isEmAndamento())
                .count();
        
        logger.info("Running tasks count: {}", count);
        return count > 0;
    }

    /**
     * Create a CSV file on user Desktop (windows) or home folder
     * @return The number of exported taks
     */
    public int exportAllTaks(String filePath) {
        List<Tarefa> tarefas = taskRepository.getAll();

        if (tarefas.isEmpty()) {
            return 0;
        }

        if (filePath.isEmpty()) {
            return 0;
        }

        File file = new File(filePath);

        if (!file.isFile()) {
            throw new RuntimeException(
                    "Arquivo " + filePath + " não é um arquivo texto!"
            );
        }

        if (!file.canWrite()) {
            throw new RuntimeException(
                    "Arquivo " + filePath + " não tem permissão de gravação!"
            );
        }

        FileWriter arquivo = null;

        try {
            arquivo = new FileWriter(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (arquivo == null) {
            throw new RuntimeException(
                    "Não foi possível abrir o arquivo para escrita!"
            );
        }

        PrintWriter gravarArq = new PrintWriter(arquivo);

        final String cabecalho = "Código;Nome;Cliente;Tempo;Tarefa;Acumulado";

        gravarArq.println(cabecalho);

        String tempoTotalTarefas = "00:00:00";
        final char separator = ';';
        final String endline = "\r\n";
        for (Tarefa tarefa : tarefas) {
            StringBuilder sb = new StringBuilder();

            // Código
            sb.append(tarefa.getPrograma()).append(separator);

            // Nome
            sb.append(tarefa.getNome()).append(separator);
            
            // Cliente
            sb.append(tarefa.getCliente()).append(separator);

            // Tempo
            sb.append(tarefa.getDuracao()).append(separator);

            // Tarefa
            sb.append(tarefa.getTarefa()).append(separator);

            tempoTotalTarefas = TempoUtil.somarTempo(tempoTotalTarefas, tarefa.getDuracao());

            // Acumulado
            sb.append(tempoTotalTarefas);

            gravarArq.println(sb.toString() + endline);
        }

        try {
            arquivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tarefas.size();
    }

    public String obterTempoTodasTarefas() {
        List<Tarefa> TarefaList = this.taskRepository.getAll();

        String tempoTotal = "00:00:00";
        for (Tarefa tarefa : TarefaList) {
            tempoTotal = TempoUtil.somarTempo(tempoTotal, tarefa.getDuracao());
        }

        logger.info("tempoTotal: {}", tempoTotal);

        return tempoTotal;
    }
}
