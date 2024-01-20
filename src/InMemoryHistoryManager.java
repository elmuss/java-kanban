import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        int MAX_SIZE_OF_HISTORY_LIST = 10;
        Task historyTask = new Task(task.getName(), task.getDescription(), task.getId(), task.getStatus());

        if (historyList.size() == MAX_SIZE_OF_HISTORY_LIST) {
            historyList.remove(0);
            historyList.add(historyTask);
        } else {
            historyList.add(historyTask);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
