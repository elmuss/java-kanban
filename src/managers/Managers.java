package managers;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        TaskManager fileBackedManager = new FileBackedTaskManager(new File("resources/task.csv"));
        return fileBackedManager;
    }

    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
