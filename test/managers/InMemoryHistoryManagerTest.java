package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void addingNewObjectInHistoryIsCorrect() {
        final HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(new Task("задача 1", "описание задачи 1", 1, Status.NEW));
        historyManager.add(new Task("задача 2", "описание задачи 2", 2, Status.NEW));
        historyManager.add(new Task("задача 3", "описание задачи 3", 3, Status.NEW));
        historyManager.add(new Task("задача 4", "описание задачи 4", 4, Status.NEW));
        historyManager.add(new Task("задача 2", "описание задачи 2", 2, Status.NEW));
        historyManager.add(new Task("задача 3", "описание задачи 3", 3, Status.NEW));
        historyManager.add(new Task("задача 4", "описание задачи 4", 4, Status.NEW));
        historyManager.add(new Task("задача 1", "описание задачи 1", 1, Status.NEW));
        Assertions.assertTrue(historyManager.getHistory().size() == 4);

    }
    @Test
    void removingObjectInHistoryIsCorrect() {
        final HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(new Task("задача 1", "описание задачи 1", 1, Status.NEW));
        historyManager.add(new Task("задача 2", "описание задачи 2", 2, Status.NEW));
        historyManager.add(new Task("задача 3", "описание задачи 3", 3, Status.NEW));
        historyManager.add(new Task("задача 4", "описание задачи 4", 4, Status.NEW));
        historyManager.add(new Task("задача 1", "описание задачи 1", 1, Status.NEW));
        historyManager.remove(3);
        Assertions.assertTrue(historyManager.getHistory().size() == 3);

    }
}