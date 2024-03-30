package managers;

import tasks.*;
import tasks.Status;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    Comparator<Task> comparator = (o1, o2) -> {
        if (o1.getStartTime().isBefore(o2.getStartTime())) {
            return -1;
        } else if (o1.getStartTime().isAfter(o2.getStartTime())) {
            return 1;
        }
        return 0;
    };
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(comparator);

    @Override
    public void addTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
        addPrioritizedTasks(task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        addPrioritizedTasks(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtasksOfEpic(subtask);
        changeEpicStatus(epic);
    }

    @Override
    public void changeEpicStatus(Epic epic) {
        List<Subtask> currentList = epic.getSubtasksOfEpic();
        int doneSubtasksCounter = 0;
        int newSubtasksCounter = 0;
        for (Subtask currentSubtask : currentList) {
            if (currentSubtask.getStatus() == Status.DONE) {
                doneSubtasksCounter++;
            } else if (currentSubtask.getStatus() == Status.NEW) {
                newSubtasksCounter++;
            }
        }
        if (newSubtasksCounter == currentList.size()) {
            epic.setStatus(Status.NEW);
        } else if (doneSubtasksCounter == currentList.size()) {
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
        addPrioritizedTasks(epic);
    }

    @Override
    public void clearTasks() {
        tasks.values().forEach(prioritizedTasks::remove);
        tasks.values().forEach(getHistory()::remove);
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.values().forEach(prioritizedTasks::remove);
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        subtasks.values().forEach(prioritizedTasks::remove);
        subtasks.clear();
    }

    @Override
    public void removeCertainTask(int id) {
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeCertainEpic(int id) {
        prioritizedTasks.remove(epics.get(id));
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            List<Subtask> subtaskList = epic.getSubtasksOfEpic();
            for (Subtask subtask : subtaskList) {
                subtasks.remove(subtask.getId());
                prioritizedTasks.remove(subtask);
            }
            epics.remove(id);
        }
        historyManager.remove(id);
    }

    @Override
    public void removeCertainSubtask(int id) {
        prioritizedTasks.remove(subtasks.get(id));
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
        tasks.values().forEach(listTask::add);

        return listTask;
    }

    @Override
    public List<Epic> getListEpic() {
        ArrayList<Epic> listEpic = new ArrayList<>();
        epics.values().forEach(listEpic::add);

        return listEpic;
    }

    @Override
    public List<Subtask> getListSubtask() {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        subtasks.values().forEach(listSubtask::add);

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
        prioritizedTasks.remove(epic);
        Epic updatedEpic = epics.get(epic.getId());
        updatedEpic.setName(epic.getName());
        updatedEpic.setDescription(epic.getDescription());
        updatedEpic.setStatus(epic.getStatus());
        updatedEpic.setId(epic.getId());
        epics.put(updatedEpic.getId(), updatedEpic);
        addPrioritizedTasks(updatedEpic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        prioritizedTasks.remove(subtask);
        Subtask updatedSubtask = subtasks.get(subtask.getId());
        updatedSubtask.setName(subtask.getName());
        updatedSubtask.setDescription(subtask.getDescription());
        updatedSubtask.setStatus(subtask.getStatus());
        updatedSubtask.setId(subtask.getId());
        subtasks.put(updatedSubtask.getId(), updatedSubtask);
        addPrioritizedTasks(updatedSubtask);
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

    protected HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void addPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            if (prioritizedTasks.isEmpty()) {
                prioritizedTasks.add(task);

            } else {
                Set<Task> newSet = prioritizedTasks.stream().filter(e -> taskCross(task, e)).collect(Collectors.toSet());

                if (newSet.isEmpty()) {
                    prioritizedTasks.add(task);
                }
            }
        }
    }

    public boolean taskCross(Task currentTask, Task lastTask) {
        return currentTask.getStartTime().isBefore(lastTask.getEndTime());
    }

    public void addAllToPrioritizedTasks() {
        prioritizedTasks.addAll(getListTask());
        prioritizedTasks.addAll(getListSubtask());
        prioritizedTasks.addAll(getListEpic());
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

}