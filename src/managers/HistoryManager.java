package managers;

import java.util.List;
import tasks.*;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}
