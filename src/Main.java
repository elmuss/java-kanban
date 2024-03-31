import http.HttpTaskServer;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {
        TaskManager manager = new InMemoryTaskManager();
        HttpTaskServer server = new HttpTaskServer(manager);
        server.start();
        manager.addTask(new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30)));
        manager.addTask(new Task("задача 2", "описание задачи 2", 2, Status.NEW,
                LocalDateTime.of(2024, 7, 13, 13, 0), Duration.ofMinutes(30)));
        manager.addTask(new Task("задача 3", "описание задачи 3", 3, Status.NEW,
                LocalDateTime.of(2024, 8, 13, 13, 0), Duration.ofMinutes(30)));
        manager.addEpic(new Epic("эпик 1", "описание эпика 1", 4, Status.NEW));
        manager.addSubtask(new Subtask("подзадача 1", "описание подзадачи 1", 5,
                Status.NEW, 4, LocalDateTime.of(2024, 9, 23, 13, 0), Duration.ofDays(2)));
        manager.addSubtask(new Subtask("подзадача 2", "описание подзадачи 2", 6,
                Status.NEW, 4, LocalDateTime.of(2024, 10, 23, 13, 0), Duration.ofDays(2)));



        /*TaskManager taskManager = new InMemoryTaskManager();
        taskManager.addTask(new Task("задача 1", "описание задачи 1")); //1
        taskManager.addTask(new Task("задача 2", "описание задачи 2")); //2
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1")); //3
        taskManager.addSubtask(new Subtask("подзадача 1", "описание подзадачи 1", 3));//4
        taskManager.addSubtask(new Subtask("подзадача 2", "описание подзадачи 2", 3)); //5
        taskManager.addEpic(new Epic("эпик 2", "описание эпика 2")); //6
        taskManager.addSubtask(new Subtask("подзадача 3", "описание подзадачи 3", 6)); //7
        System.out.println("список задач: " + taskManager.getListTask());
        System.out.println("список эпиков: " + taskManager.getListEpic());
        System.out.println("список подзадач: " + taskManager.getListSubtask());
        System.out.println("список задач: " + taskManager.getListTask());
        System.out.println("список эпиков: " + taskManager.getListEpic());
        System.out.println("список подзадач: " + taskManager.getListSubtask());
        taskManager.removeCertainTask(1);
        taskManager.removeCertainEpic(6);
        System.out.println("список задач: " + taskManager.getListTask());
        System.out.println("список эпиков: " + taskManager.getListEpic());
        System.out.println("список подзадач: " + taskManager.getListSubtask());
        taskManager.getCertainTask(2);
        taskManager.getCertainSubtask(4);
        taskManager.getCertainSubtask(5);
        taskManager.getCertainTask(2);
        taskManager.getCertainEpic(3);
        System.out.println("история: " + taskManager.getHistory());
        taskManager.removeCertainTask(2);
        taskManager.getCertainSubtask(5);
        System.out.println("история: " + taskManager.getHistory());*/
    }
}
