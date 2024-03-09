package managers;

import java.io.File;
import java.io.IOException;

public class Managers {
    static InMemoryTaskManager manager;
    FileBackedTaskManager fileBackedManager;
    static InMemoryHistoryManager historyManager;

    static InMemoryTaskManager getDefault() throws IOException {
        return manager = new FileBackedTaskManager(File.createTempFile("test", "csv"));
    }
    static HistoryManager getDefaultHistory() {
        return historyManager = new InMemoryHistoryManager();
    }
}
