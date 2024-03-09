package managers;

import tasks.*;

import java.io.IOException;
import java.util.List;

public interface TaskManager {
    void addTask(Task task) throws IOException;

    void addSubtask(Subtask subtask) throws IOException;

    void changeEpicStatus(Epic epic) throws IOException;

    void addEpic(Epic epic) throws IOException;

    void clearTasks() throws IOException;

    void clearEpics() throws IOException;

    void clearSubtasks() throws IOException;

    void removeCertainTask(int id) throws IOException;

    void removeCertainEpic(int id) throws IOException;

    void removeCertainSubtask(int id) throws IOException;

    Task getCertainTask(int id);

    Epic getCertainEpic(int id);

    Subtask getCertainSubtask(int id);

    List<Task> getListTask();

    List<Epic> getListEpic();

    List<Subtask> getListSubtask();

    void updateTask(Task task) throws IOException;

    void updateEpic(Epic epic) throws IOException;

    void updateSubtask(Subtask subtask) throws IOException;
    List<Task> getHistory();
}
