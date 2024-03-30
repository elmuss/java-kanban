package managers;

import tasks.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public interface TaskManager {
    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void changeEpicStatus(Epic epic);

    void addEpic(Epic epic);

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    void removeCertainTask(int id);

    void removeCertainEpic(int id);

    void removeCertainSubtask(int id);

    Task getCertainTask(int id);

    Epic getCertainEpic(int id);

    Subtask getCertainSubtask(int id);

    List<Task> getListTask();

    List<Epic> getListEpic();

    List<Subtask> getListSubtask();

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    List<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();
}
