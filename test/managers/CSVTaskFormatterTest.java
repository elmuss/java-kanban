package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static managers.CSVTaskFormatter.fromString;

class CSVTaskFormatterTest {

    @Test
    void taskToStringAndHistoryToString() {
        Task task = new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30));
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);

        Subtask subtask = new Subtask("подзадача 1", "описание подзадачи 1", 3, Status.NEW, 2,
                LocalDateTime.of(2024, 6, 20, 13, 0), Duration.ofMinutes(30));
        List<Subtask> subtaskList = new ArrayList<>();
        subtaskList.add(subtask);

        Epic epic = new Epic("эпик 1", "описание эпика 1", 2, Status.NEW);
        List<Epic> epicList = new ArrayList<>();
        epicList.add(epic);

        List<Task> history = new ArrayList<>();
        history.add(task);
        history.add(epic);
        history.add(subtask);

        Assertions.assertEquals("1,2,3", CSVTaskFormatter.historyToString(history));
        Assertions.assertTrue(CSVTaskFormatter.taskToString(taskList, subtaskList, epicList)
                .contains("1,TASK,задача 1,NEW,описание задачи 1,2024.06.13|13:00,1800"));
        Assertions.assertTrue(CSVTaskFormatter.taskToString(taskList, subtaskList, epicList)
                .contains("2,EPIC,эпик 1,NEW,описание эпика 1"));
    }

    @Test
    void taskFromString() {
        String stringSubtask = "3,SUBTASK,подзадача 1,NEW,описание подзадачи 1,2024.06.13|13:00,1800,2";
        String stringTask = "1,TASK,задача 1,NEW,описание задачи 1,2024.07.13|10:00,7200";
        Subtask loadedSubtask = (Subtask) fromString(stringSubtask);
        Task loadedTask = fromString(stringTask);

        Assertions.assertEquals(loadedSubtask.getId(), 3);
        Assertions.assertEquals(loadedSubtask.getType(), TaskType.SUBTASK);
        Assertions.assertEquals(loadedSubtask.getStatus(), Status.NEW);
        Assertions.assertEquals(loadedTask.getStartTime(), LocalDateTime.of(2024, 7, 13, 10, 0));
    }

    @Test
    void taskHistoryFromString() {
        String string = "1,2,3,4,5,6";

        List<Integer> loadedHistory = CSVTaskFormatter.historyFromString(string);

        Assertions.assertEquals(6, loadedHistory.size());
    }

}