import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private int id = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

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
        if (doneSubtasksCounter == currentList.size() && !currentList.isEmpty()) {
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
            for (Subtask subtask : subtaskList) {
                subtasks.remove(subtask.getId());
            }
            epics.remove(id);
        }
    }
    public void removeCertainSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        List<Subtask> subtaskList = epic.getSubtasksOfEpic();
        subtaskList.remove(subtask);
        subtasks.remove(id);
        changeEpicStatus(epic);
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
        updatedTask.setName(task.getName());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setStatus(task.getStatus());
        updatedTask.setId(task.getId());
        tasks.put(updatedTask.getId(), updatedTask);
    }
    public void updateEpic(Epic epic) {
        Epic updatedEpic = epics.get(epic.getId());
        updatedEpic.setName(epic.getName());
        updatedEpic.setDescription(epic.getDescription());
        updatedEpic.setStatus(epic.getStatus());
        updatedEpic.setId(epic.getId());
        epics.put(updatedEpic.getId(), updatedEpic);
    }
    public void updateSubtask(Subtask subtask) {
        Subtask updatedSubtask = subtasks.get(subtask.getId());
        updatedSubtask.setName(subtask.getName());
        updatedSubtask.setDescription(subtask.getDescription());
        updatedSubtask.setStatus(subtask.getStatus());
        updatedSubtask.setId(subtask.getId());
        subtasks.put(updatedSubtask.getId(), updatedSubtask);
        Epic epic = epics.get(subtask.getEpicId());
        changeEpicStatus(epic);
    }
}