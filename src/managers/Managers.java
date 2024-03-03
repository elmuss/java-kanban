package managers;

import java.io.File;

public class Managers {
    InMemoryTaskManager manager;
    FileBackedTaskManager fileBackedManager;
    static InMemoryHistoryManager historyManager;

    public FileBackedTaskManager getDefault() {
        return fileBackedManager = new FileBackedTaskManager(new File("resources", "tasks.csv"));
    }
    static HistoryManager getDefaultHistory() {
        return historyManager = new InMemoryHistoryManager();
    }
}
