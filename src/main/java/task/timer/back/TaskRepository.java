package task.timer.back;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {

    private final Map<String, Tarefa> taskMap;

    public TaskRepository() {
        taskMap = new HashMap<>();
    }

    public void save(Tarefa tarefa) {
        taskMap.put(tarefa.getPrograma(), tarefa);
    }

    public void remove(String id) {
        taskMap.remove(id);
    }

    public List<Tarefa> getAll() {
        return new ArrayList<>(taskMap.values());
    }

    public Tarefa getTarefaById(String id) {
        return this.taskMap.get(id);
    }
}
