public class Managers {
    InMemoryTaskManager manager;
    static InMemoryHistoryManager historyManager;

    public InMemoryTaskManager getDefault() {
        return manager = new InMemoryTaskManager();
    }
    static HistoryManager getDefaultHistory() {
        return historyManager = new InMemoryHistoryManager();
    }
}
