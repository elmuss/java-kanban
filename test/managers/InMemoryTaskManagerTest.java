package managers;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.List;

class InMemoryTaskManagerTest {

    @Test
    void examplesOfClassTaskWithEqualIdAreEqual() {
        Task task1 = new Task("задача 1", "описание задачи 1", 1, Status.NEW);
        Task task2 = new Task("задача 1", "описание задачи 1", 1, Status.NEW);
        if (task1.getId() == task2.getId()) {
            Assertions.assertEquals(task1, task2);
        }
    }

    @Test
    void examplesOfClassTaskInheritorsWithEqualIdAreEqual() {
        Task epic1 = new Epic("эпик 1", "описание эпика 1", 1, Status.NEW);
        Task epic2 = new Epic("эпик 1", "описание эпика 1", 1, Status.NEW);
        if (epic1.getId() == epic2.getId()) {
            Assertions.assertEquals(epic1, epic2);
        }
    }

    @Test
    void epicCanNotBeItsSubtask() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1"));
        taskManager.addSubtask(new Subtask("подзадача 3","описание подзадачи 3", 1));
        List<Epic> listEpics = taskManager.getListEpic();
        Assertions.assertNotNull(taskManager.getCertainEpic(1));
    }

    @Test
    void subtaskCanNotBeItsEpic() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1"));
        taskManager.addSubtask(new Subtask("подзадача 3","описание подзадачи 3", 1));
        List<Epic> listEpics = taskManager.getListEpic();
        List<Subtask> listSubtasks = taskManager.getListSubtask();
        Assertions.assertNotNull(taskManager.getCertainEpic(1));
        Assertions.assertNotNull(taskManager.getCertainSubtask(2));
    }

    @Test
    void classManagersReturnsInitialisedClasManager() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Assertions.assertNotNull(taskManager.getHistoryManager());
    }

    @Test
    void inMemoryTaskManagerAbleToAddObjectsWithDifferentTypes() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1"));
        taskManager.addTask(new Task("задача 1","описание задачи 1"));
        Epic newEpic = new Epic("новый эпик", "описание нового эпика");
        Task newTask = new Task("новая задача", "описание новой задачи");
        Assertions.assertEquals(taskManager.getCertainEpic(1).getClass(), newEpic.getClass());
        Assertions.assertEquals(taskManager.getCertainTask(2).getClass(), newTask.getClass());
        Assertions.assertNotNull(taskManager.getCertainEpic(1));
        Assertions.assertNotNull(taskManager.getCertainTask(2));
    }

    @Test
    void canNotChangeObjectInHistory() throws IOException {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        taskManager.addTask(new Task("задача 1", "описание задачи 1"));
        taskManager.getCertainTask(1);
        taskManager.updateTask(new Task("задача 1", "новое описание задачи 1", 1, Status.DONE));
        List<Task> currentHistory = taskManager.getHistory();
        Assertions.assertNotEquals(currentHistory.get(0), taskManager.getCertainTask(1));
    }
}