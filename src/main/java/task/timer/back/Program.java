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
import java.util.stream.Collectors;

public class Program {

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
        Tarefa bd = taskRepository.getTarefaById(t.getDiagramaPrograma());
        if (bd != null) {
            throw new RuntimeException("Task alread exists!");
        }

        taskRepository.save(t);
    }

    public void updateTask(Tarefa t) {
        Tarefa bd = taskRepository.getTarefaById(t.getDiagramaPrograma());
        if (bd == null) {
            throw new RuntimeException("Task not found!");
        }

        taskRepository.save(t);
    }

    public void removeTask(Tarefa t) {
        Tarefa bd = taskRepository.getTarefaById(t.getDiagramaPrograma());
        if (bd == null) {
            throw new RuntimeException("Task not found!");
        }

        taskRepository.remove(t.getDiagramaPrograma());
    }

    /**
     * Get the current running tasks
     * @return A list of running tasks or a empty list
     */
    public List<Tarefa> getRunningTasks() {
        List<Tarefa> tarefas = this.taskRepository.getAll();

        return tarefas
                .stream()
                .filter(x -> x.isEmAndamento())
                .collect(Collectors.toList());
    }

    /**
     * Create a TXT file on user Desktop (windows) or home folder
     * @return The number of exported taks
     */
    public int exportAllTaks(String filePath) {
        List<Tarefa> tarefas = taskRepository.getAll();

        if (tarefas.isEmpty()) {
            return 0;
        }

        if (filePath.isEmpty()) {
            filePath = System.getProperty("user.home");

            if (OS.isWindows()) {
                filePath += File.separator + "Desktop";
            }

            if (!filePath.endsWith(File.separator)) {
                filePath += File.separator;
            }

            filePath += "Tarefas.csv";
        }

        File file = new File(filePath);

        if (!file.isFile()) {
            throw new RuntimeException(
                    "File " + filePath + " is not a file!"
            );
        }

        if (!file.canWrite()) {
            throw new RuntimeException(
                    "File " + filePath + " don't have write permission!"
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
                    "Unable to open file for writing!"
            );
        }

        PrintWriter gravarArq = new PrintWriter(arquivo);

        final String cabecalho = "CODIGO;NOME;SOLICITANTE;TEMPO TOTAL;DATA;HORA INICIO;HORA FIM;OBS";
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dataAtual = formatter.format(new Date());

        gravarArq.println(cabecalho);

        String tempoTotalTarefas = "00:00:00";
        for (Tarefa tarefa : tarefas) {
            String linha = ((tarefa.getDiagramaPrograma().isEmpty()) ? " N " : tarefa.getDiagramaPrograma()) + ";"
                    + tarefa.getDescricao() + ";"
                    + ((tarefa.getCliente().isEmpty()) ? " N " : tarefa.getCliente()) + ";"
                    + tarefa.getDuracao() + ";"
                    + dataAtual + ";"
                    + tarefa.getHoraInicio() + ";"
                    + tarefa.getHoraTermino() + ";"
                    + ((tarefa.getServico().isEmpty()) ? "Nenhuma." : tarefa.getServico());

            tempoTotalTarefas = somarTempo(tempoTotalTarefas, tarefa.getCronometro());

            gravarArq.println(linha);
        }

        // Grava o tempo Total
        gravarArq.println("\r\n \r\nTempo total das tarefas: " + tempoTotalTarefas);

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
            tempoTotal = somarTempo(tempoTotal, tarefa.getCronometro());
        }

        return tempoTotal;
    }

    public String somarTempo(String tempo1, String tempo2) {
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

            return String.format("%02d:%02d:%02d", hora3, min3, seg3);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "00:00:00";
        }
    }

    public String calcularTempoDuracao(String fimParam, String inicParam) {
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
            return String.format("%02d:%02d:%02d", horaFinal, minFinal, segFinal);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "00:00:00";
    }
}
