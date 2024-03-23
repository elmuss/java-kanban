package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static managers.FileBackedTaskManager.loadFromFile;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @BeforeEach
    public void setup() throws IOException {
        taskManager = new FileBackedTaskManager(File.createTempFile("test", "csv"));
    }

    @Test
    void saveManagerToFile() throws IOException {
        taskManager.addTask(new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30)));
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1", 2, Status.NEW));
        taskManager.addSubtask(new Subtask("подзадача 1", "описание подзадачи 1", 3,
                Status.NEW, 2, LocalDateTime.of(2024, 7, 23, 13, 0), Duration.ofDays(2)));
        taskManager.getCertainEpic(2);
        taskManager.getCertainSubtask(3);

        taskManager.save();
        StringBuilder sb = new StringBuilder();

        FileReader reader = new FileReader(taskManager.file);
        BufferedReader br = new BufferedReader(reader);

        while (br.ready()) {
            String line = br.readLine();
            sb.append(line + "\n");
        }
        br.close();

        Assertions.assertTrue(sb.toString().contains("id,type,name,status,description,startTime,duration,epic"));
        Assertions.assertTrue(sb.toString().contains("1,TASK,задача 1,NEW,описание задачи 1,2024.06.13|13:00,1800"));
        Assertions.assertTrue(sb.toString().contains("2,EPIC,эпик 1,NEW,описание эпика 1"));
        Assertions.assertTrue(sb.toString().contains("2,3"));
    }

    @Test
    void loadingNewFileBackedTaskManagerFromFile() throws IOException {
        taskManager.addTask(new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 7, 13, 10, 0), Duration.ofMinutes(120)));
        taskManager.addTask(new Task("задача 2", "описание задачи 2", 2, Status.NEW,
                LocalDateTime.of(2024, 7, 13, 11, 0), Duration.ofMinutes(60)));
        taskManager.addTask(new Task("задача 3", "описание задачи 3", 3, Status.NEW,
                LocalDateTime.of(2024, 7, 14, 10, 0), Duration.ofDays(1)));
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1", 4, Status.NEW));
        taskManager.addSubtask(new Subtask("подзадача 1", "описание подзадачи 1", 5, Status.NEW, 4,
                LocalDateTime.of(2024, 7, 15, 20, 0), Duration.ofMinutes(180)));
        taskManager.addSubtask(new Subtask("подзадача 2", "описание подзадачи 2", 6, Status.NEW, 4,
                LocalDateTime.of(2024, 7, 16, 20, 0), Duration.ofMinutes(120)));
        taskManager.getCertainTask(1);
        taskManager.getCertainSubtask(6);

        taskManager.save();

        FileBackedTaskManager loadedBackedManager = loadFromFile(taskManager.file);

        Assertions.assertEquals(taskManager.getEpics(), loadedBackedManager.getEpics());
        Assertions.assertEquals(taskManager.getTasks(), loadedBackedManager.getTasks());
        Assertions.assertEquals(taskManager.getSubtasks(), loadedBackedManager.getSubtasks());
        Assertions.assertEquals(taskManager.getHistory(), loadedBackedManager.getHistory());
    }

    @Test
    public void testException() {
        File tmpFile = new File("invalid path", "test.csv");
        taskManager = new FileBackedTaskManager(tmpFile);

        Assertions.assertThrows(ManagerSaveException.class, () ->
                taskManager.addTask(new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                        LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30))));
    }
}