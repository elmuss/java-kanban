import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    int id = 0;
    HashMap<Integer, Task> taskMap = new HashMap<>();
    HashMap<Integer, Epic> epicMap = new HashMap<>();
    HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    public void addTask(Task task) {
        id++;
        taskMap.put(id, task);
        task.setId(id);
    }
    public void addSubtask(Subtask subtask, int epicId) {
        id++;
        subtask.setId(id);
        subtask.setEpicId(epicId);
        subtaskMap.put(id, subtask);
        Epic epic = epicMap.get(epicId);
        epic.addSubtasksOfEpic(subtaskMap.get(subtask.id));
    }
    public void addEpic(Epic epic) {
        id++;
        epicMap.put(id, epic);
        epic.setId(id);
    }
    public void clearTaskMap() {
        taskMap.clear();
    }
    public void clearEpicMap() {
        epicMap.clear();
    }
    public void clearSubtaskMap() {
        subtaskMap.clear();
    }
    public void removeCertainTask(int id) {
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
        }
    }
    public void removeCertainEpic(int id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            List<Subtask> subtaskList = epic.getSubtasksOfEpic();
            if (subtaskList != null) {
                epicMap.remove(id);
                for (Subtask subtask : subtaskList) {
                    subtaskMap.remove(subtask.id);
                }
            } else {
                epicMap.remove(id);
            }
        }
    }
    public void removeCertainSubtask(int id) {
        if (subtaskMap.containsKey(id)) {
            subtaskMap.remove(id);
        }
    }
    public Task getCertainTask(int id) {
        return taskMap.get(id);
    }
    public Epic getCertainEpic(int id) {
        return epicMap.get(id);
    }
    public Subtask getCertainSubtask(int id) {
        return subtaskMap.get(id);
    }
    public List<Task> getListTask() {
        ArrayList<Task> listTask = new ArrayList<>();
        for (Task task : taskMap.values()) {
            listTask.add(task);
        }
        return listTask;
    }
    public List<Epic> getListEpic() {
        ArrayList<Epic> listEpic = new ArrayList<>();
        for (Epic epic : epicMap.values()) {
            listEpic.add(epic);
        }
        return listEpic;
    }
    public List<Subtask> getListSubtask() {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        for (Subtask subtask : subtaskMap.values()) {
            listSubtask.add(subtask);
        }
        return listSubtask;
    }
    public void updateTask(int id, String description, Status status) {
        Task task = taskMap.get(id);
        task.setId(id);
        task.status = status;
        task.setDescription(description);
        taskMap.put(id, task);
    }
    public void updateEpic(int id, String description) {
        Epic epic = epicMap.get(id);
        epic.setId(id);
        epic.setDescription(description);
        epicMap.put(id, epic);
    }
    public void updateSubtask(int id, String description, Status status) {
        Subtask subtask = subtaskMap.get(id);
        subtask.setId(id);
        subtask.setDescription(description);
        subtask.status = status;
        subtaskMap.put(id, subtask);
        Epic epic = epicMap.get(subtask.getEpicId());
        List<Subtask> subtasks = epic.getSubtasksOfEpic();

        if (!subtasks.isEmpty()) {
            int i = 0;

            for (Subtask subtask1 : subtasks) {
                if (subtask1.status == Status.DONE) {
                    i++;
                }
                if (i == subtasks.size()) {
                    epic.setStatus(Status.DONE);
                } else if (i < subtasks.size()) {
                    epic.setStatus(Status.IN_PROGRESS);
                } else {
                    epic.setStatus(Status.NEW);
                }
            }
        }
        epicMap.get(subtask.getEpicId()).setStatus(epic.status);
    }
}