package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    void examplesOfClassTaskWithEqualIdAreEqual() {
        Task task1 = new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30));
        Task task2 = new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30));
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
    void classManagersReturnsInitialisedClasManager() {
        Assertions.assertNotNull(taskManager.getHistory());
    }

    @Test
    void inMemoryTaskManagerAbleToAddObjectsWithDifferentTypes() {
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1", 1, Status.NEW));
        taskManager.addTask(new Task("задача 1", "описание задачи 1", 2, Status.NEW,
                LocalDateTime.of(2024, 7, 13, 10, 0), Duration.ofMinutes(120)));
        Epic newEpic = new Epic("новый эпик", "описание нового эпика");
        Task newTask = new Task("новая задача", "описание новой задачи");
        Assertions.assertEquals(taskManager.getCertainEpic(1).getClass(), newEpic.getClass());
        Assertions.assertEquals(taskManager.getCertainTask(2).getClass(), newTask.getClass());
        Assertions.assertNotNull(taskManager.getCertainEpic(1));
        Assertions.assertNotNull(taskManager.getCertainTask(2));
    }

    @Test
    void epicStatusAllSubtasksNew() {
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1", 1, Status.NEW));
        taskManager.addSubtask(new Subtask("подзадача 1", "описание подзадачи 1", 2,
                Status.NEW, 1, LocalDateTime.of(2024, 7, 3, 13, 0), Duration.ofHours(2)));
        taskManager.addSubtask(new Subtask("подзадача 2", "описание подзадачи 2", 3,
                Status.NEW, 1, LocalDateTime.of(2024, 7, 4, 13, 0), Duration.ofHours(2)));

        Assertions.assertEquals(Status.NEW, taskManager.getCertainEpic(1).getStatus());
    }

    @Test
    void epicStatusAllSubtasksDone() {
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1", 1, Status.NEW));
        taskManager.addSubtask(new Subtask("подзадача 1", "описание подзадачи 1", 2,
                Status.DONE, 1, LocalDateTime.of(2024, 7, 3, 13, 0), Duration.ofHours(2)));
        taskManager.addSubtask(new Subtask("подзадача 2", "описание подзадачи 2", 3,
                Status.DONE, 1, LocalDateTime.of(2024, 7, 4, 13, 0), Duration.ofHours(2)));

        Assertions.assertEquals(Status.DONE, taskManager.getCertainEpic(1).getStatus());
    }

    @Test
    void epicStatusSubtaskNewSubtaskDone() {
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1", 1, Status.NEW));
        taskManager.addSubtask(new Subtask("подзадача 1", "описание подзадачи 1", 2,
                Status.DONE, 1, LocalDateTime.of(2024, 7, 3, 13, 0), Duration.ofHours(2)));
        taskManager.addSubtask(new Subtask("подзадача 2", "описание подзадачи 2", 3,
                Status.NEW, 1, LocalDateTime.of(2024, 7, 4, 13, 0), Duration.ofHours(2)));

        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getCertainEpic(1).getStatus());
    }

    @Test
    void epicStatusAllSubtasksInProgress() {
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1", 1, Status.NEW));
        taskManager.addSubtask(new Subtask("подзадача 1", "описание подзадачи 1", 2,
                Status.IN_PROGRESS, 1, LocalDateTime.of(2024, 7, 3, 13, 0), Duration.ofHours(2)));
        taskManager.addSubtask(new Subtask("подзадача 2", "описание подзадачи 2", 3,
                Status.IN_PROGRESS, 1, LocalDateTime.of(2024, 7, 4, 13, 0), Duration.ofHours(2)));

        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getCertainEpic(1).getStatus());
    }

    @Test
    void subtaskHasItsEpic() {
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1", 1, Status.NEW));
        taskManager.addSubtask(new Subtask("подзадача 1", "описание подзадачи 1", 2,
                Status.DONE, 1, LocalDateTime.of(2024, 7, 3, 13, 0), Duration.ofHours(2)));
        taskManager.addSubtask(new Subtask("подзадача 2", "описание подзадачи 2", 3,
                Status.DONE, 1, LocalDateTime.of(2024, 7, 4, 13, 0), Duration.ofHours(2)));

        Assertions.assertEquals(taskManager.getCertainEpic(1),
                taskManager.getCertainEpic(taskManager.getCertainSubtask(2).getEpicId()));
    }

    @Test
    void checkIfTasksCrosses() {
        taskManager.addTask(new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 7, 13, 10, 0), Duration.ofMinutes(60)));
        taskManager.addTask(new Task("задача 2", "описание задачи 2", 2, Status.NEW,
                LocalDateTime.of(2024, 7, 13, 10, 30), Duration.ofMinutes(60)));
        taskManager.addTask(new Task("задача 3", "описание задачи 3", 3, Status.NEW,
                LocalDateTime.of(2024, 7, 13, 12, 0), Duration.ofMinutes(50)));

        Assertions.assertEquals(2, taskManager.getPrioritizedTasks().size());
    }
}