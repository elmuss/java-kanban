import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private int id = 0;
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public void addTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
    }
    public void addSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtasksOfEpic(subtask);
        changeEpicStatus(epic);
    }
    public void changeEpicStatus(Epic epic) {
        List<Subtask> currentList = epic.getSubtasksOfEpic();
        int doneSubtasksCounter = 0;
        int newSubtasksCounter = 0;
        for (Subtask currentSubtask: currentList) {
            if (currentSubtask.status == Status.DONE) {
                doneSubtasksCounter++;
            } else if (currentSubtask.status == Status.NEW) {
                newSubtasksCounter++;
            }
        }
        if (doneSubtasksCounter == currentList.size()) {
            epic.setStatus(Status.DONE);
        } else if (newSubtasksCounter == currentList.size()){
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
    public void addEpic(Epic epic) {
        id++;
        epics.put(id, epic);
        epic.setId(id);
    }
    public void clearTasks() {
        tasks.clear();
    }
    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }
    public void clearSubtasks() {
        subtasks.clear();
    }
    public void removeCertainTask(int id) {
            tasks.remove(id);
    }
    public void removeCertainEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            List<Subtask> subtaskList = epic.getSubtasksOfEpic();
            epics.remove(id);
            for (Subtask subtask : subtaskList) {
                subtasks.remove(subtask.id);
            }
        }
    }
    public void removeCertainSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        List<Subtask> subtaskList = epic.getSubtasksOfEpic();
        subtaskList.remove(id);
        subtasks.remove(id);
    }
    public Task getCertainTask(int id) {
        return tasks.get(id);
    }
    public Epic getCertainEpic(int id) {
        return epics.get(id);
    }
    public Subtask getCertainSubtask(int id) {
        return subtasks.get(id);
    }
    public List<Task> getListTask() {
        ArrayList<Task> listTask = new ArrayList<>();
        for (Task task : tasks.values()) {
            listTask.add(task);
        }
        return listTask;
    }
    public List<Epic> getListEpic() {
        ArrayList<Epic> listEpic = new ArrayList<>();
        for (Epic epic : epics.values()) {
            listEpic.add(epic);
        }
        return listEpic;
    }
    public List<Subtask> getListSubtask() {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            listSubtask.add(subtask);
        }
        return listSubtask;
    }
    public void updateTask(Task task) {
        Task updatedTask = tasks.get(task.getId());
        task.setName(updatedTask.getName());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());
    }
    public void updateEpic(int id, String description) {
        Epic epic = epics.get(id);
        epic.setId(id);
        epic.setDescription(description);
        epics.put(id, epic);
    }
    public void updateSubtask(int id, String description, Status status) {
        Subtask subtask = subtasks.get(id);
        subtask.setId(id);
        subtask.setDescription(description);
        subtask.status = status;
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());

        if (!subtasks.isEmpty()) {
            changeEpicStatus(epic);
        }
        epics.get(subtask.getEpicId()).setStatus(epic.status);
    }
}