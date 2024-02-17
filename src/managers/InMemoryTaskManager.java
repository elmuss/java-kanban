package managers;

import tasks.*;
import tasks.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
    }
    @Override
    public void addSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtasksOfEpic(subtask);
        changeEpicStatus(epic);
    }
    @Override
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
        if (newSubtasksCounter == currentList.size()) {
            epic.setStatus(Status.NEW);
        } else if (doneSubtasksCounter == currentList.size()){
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
    @Override
    public void addEpic(Epic epic) {
        id++;
        epics.put(id, epic);
        epic.setId(id);
    }
    @Override
    public void clearTasks() {
        tasks.clear();
    }
    @Override
    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }
    @Override
    public void clearSubtasks() {
        subtasks.clear();
    }
    @Override
    public void removeCertainTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }
    @Override
    public void removeCertainEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            List<Subtask> subtaskList = epic.getSubtasksOfEpic();
            for (Subtask subtask : subtaskList) {
                subtasks.remove(subtask.getId());
            }
            epics.remove(id);
        }
        historyManager.remove(id);
    }
    @Override
    public void removeCertainSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        List<Subtask> subtaskList = epic.getSubtasksOfEpic();
        subtaskList.remove(subtask);
        subtasks.remove(id);
        changeEpicStatus(epic);
        historyManager.remove(id);
    }
    @Override
    public Task getCertainTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }
    @Override
    public Epic getCertainEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }
    @Override
    public Subtask getCertainSubtask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }
    @Override
    public List<Task> getListTask() {
        ArrayList<Task> listTask = new ArrayList<>();
        for (Task task : tasks.values()) {
            listTask.add(task);
        }
        return listTask;
    }
    @Override
    public List<Epic> getListEpic() {
        ArrayList<Epic> listEpic = new ArrayList<>();
        for (Epic epic : epics.values()) {
            listEpic.add(epic);
        }
        return listEpic;
    }
    @Override
    public List<Subtask> getListSubtask() {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            listSubtask.add(subtask);
        }
        return listSubtask;
    }
    @Override
    public void updateTask(Task task) {
        Task updatedTask = tasks.get(task.getId());
        updatedTask.setName(task.getName());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setStatus(task.getStatus());
        updatedTask.setId(task.getId());
        tasks.put(updatedTask.getId(), updatedTask);
    }
    @Override
    public void updateEpic(Epic epic) {
        Epic updatedEpic = epics.get(epic.getId());
        updatedEpic.setName(epic.getName());
        updatedEpic.setDescription(epic.getDescription());
        updatedEpic.setStatus(epic.getStatus());
        updatedEpic.setId(epic.getId());
        epics.put(updatedEpic.getId(), updatedEpic);
    }
    @Override
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
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}