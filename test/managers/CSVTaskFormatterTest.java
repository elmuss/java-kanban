package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.LocalDateTime;
import java.util.List;

import static managers.CSVTaskFormatter.fromString;

class CSVTaskFormatterTest {

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