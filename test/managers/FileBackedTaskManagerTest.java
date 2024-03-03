package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static managers.FileBackedTaskManager.file;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void save() throws IOException {
        FileBackedTaskManager backedManager = new FileBackedTaskManager(File.createTempFile("test", "csv"));
        backedManager.addTask(new Task("задача 1", "описание задачи 1", 1, Status.NEW));

        FileReader reader = new FileReader(backedManager.file);
        BufferedReader br = new BufferedReader(reader);

        while (br.ready()) {
            String line = br.readLine();
        }
        System.out.println(br.toString());
        Assertions.assertNotNull(backedManager.file);
    }

    @Test
    void loadFromFile() throws IOException {
        FileBackedTaskManager backedManager = new FileBackedTaskManager(File.createTempFile("test", "csv"));
        backedManager.addTask(new Task("задача 1", "описание задачи 1", 1, Status.NEW));
        backedManager.loadFromFile(backedManager.file);
        Assertions.assertTrue(backedManager.getTasks().get(1).getId() == 1);

    }
}