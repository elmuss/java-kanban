package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static managers.FileBackedTaskManager.loadFromFile;

class FileBackedTaskManagerTest {

    @Test
    void saveManagerToFile() throws IOException {
        FileBackedTaskManager backedManager = new FileBackedTaskManager(File.createTempFile("test", "csv"));
        backedManager.addTask(new Task("задача 1", "описание задачи 1", 1, Status.NEW));
        backedManager.addEpic(new Epic("эпик 1", "описание эпика 1", 2, Status.NEW));
        backedManager.addSubtask(new Subtask("подзадача 1", "описание подзадачи 1", 3, Status.NEW, 2));
        backedManager.getCertainEpic(2);
        backedManager.getCertainSubtask(3);

        backedManager.save();
        StringBuilder sb = new StringBuilder();

        FileReader reader = new FileReader(backedManager.file);
        BufferedReader br = new BufferedReader(reader);

        while (br.ready()) {
            String line = br.readLine();
            sb.append(line + "\n");
        }
        br.close();

        Assertions.assertTrue(sb.toString().contains("id,type,name,status,description,epic"));
        Assertions.assertTrue(sb.toString().contains("2,EPIC,эпик 1,NEW,описание эпика 1"));
        Assertions.assertTrue(sb.toString().contains("2,3"));
    }

    @Test
    void loadingNewFileBackedTaskManagerFromFile() throws IOException {
        FileBackedTaskManager backedManager = new FileBackedTaskManager(File.createTempFile("test", "csv"));
        backedManager.addTask(new Task("задача 1", "описание задачи 1", 1, Status.NEW));
        backedManager.addTask(new Task("задача 2", "описание задачи 2", 2, Status.NEW));
        backedManager.addTask(new Task("задача 3", "описание задачи 3", 3, Status.NEW));
        backedManager.addEpic(new Epic("эпик 1", "описание эпика 1", 4, Status.NEW));
        backedManager.addSubtask(new Subtask("подзадача 1", "описание подзадачи 1", 5, Status.NEW, 4));
        backedManager.addSubtask(new Subtask("подзадача 2", "описание подзадачи 2", 6, Status.NEW, 4));
        backedManager.getCertainTask(1);
        backedManager.getCertainSubtask(6);

        backedManager.save();

        FileBackedTaskManager loadedBackedManager = loadFromFile(backedManager.file);

        Assertions.assertEquals(backedManager.getEpics(), loadedBackedManager.getEpics());
        Assertions.assertEquals(backedManager.getTasks(), loadedBackedManager.getTasks());
        Assertions.assertEquals(backedManager.getSubtasks(), loadedBackedManager.getSubtasks());
        Assertions.assertEquals(backedManager.getHistory(), loadedBackedManager.getHistory());
    }
}