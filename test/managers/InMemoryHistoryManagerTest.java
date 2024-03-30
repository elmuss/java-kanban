package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void addingNewObjectInHistoryIsCorrect() {
        final HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30)));
        historyManager.add(new Task("задача 2", "описание задачи 2", 2, Status.NEW,
                LocalDateTime.of(2024, 6, 14, 13, 0), Duration.ofMinutes(30)));
        historyManager.add(new Task("задача 3", "описание задачи 3", 3, Status.NEW,
                LocalDateTime.of(2024, 6, 15, 13, 0), Duration.ofMinutes(30)));
        historyManager.add(new Task("задача 4", "описание задачи 4", 4, Status.NEW,
                LocalDateTime.of(2024, 6, 16, 13, 0), Duration.ofMinutes(30)));
        historyManager.add(new Task("задача 2", "описание задачи 2", 2, Status.NEW,
                LocalDateTime.of(2024, 6, 14, 13, 0), Duration.ofMinutes(30)));
        historyManager.add(new Task("задача 3", "описание задачи 3", 3, Status.NEW,
                LocalDateTime.of(2024, 6, 15, 13, 0), Duration.ofMinutes(30)));
        historyManager.add(new Task("задача 4", "описание задачи 4", 4, Status.NEW,
                LocalDateTime.of(2024, 6, 16, 13, 0), Duration.ofMinutes(30)));
        historyManager.add(new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30)));
        Assertions.assertTrue(historyManager.getHistory().size() == 4);

    }

    @Test
    void removingObjectInHistoryIsCorrect() {
        final HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30)));
        historyManager.add(new Task("задача 2", "описание задачи 2", 2, Status.NEW,
                LocalDateTime.of(2024, 6, 14, 13, 0), Duration.ofMinutes(30)));
        historyManager.add(new Task("задача 3", "описание задачи 3", 3, Status.NEW,
                LocalDateTime.of(2024, 6, 15, 13, 0), Duration.ofMinutes(30)));
        historyManager.add(new Task("задача 4", "описание задачи 4", 4, Status.NEW,
                LocalDateTime.of(2024, 6, 16, 13, 0), Duration.ofMinutes(30)));
        historyManager.add(new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30)));
        historyManager.remove(3);
        Assertions.assertTrue(historyManager.getHistory().size() == 3);

    }
}