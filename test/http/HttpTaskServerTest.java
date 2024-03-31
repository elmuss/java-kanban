package http;

import com.google.gson.reflect.TypeToken;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static http.HttpTaskServer.gson;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);

    HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.clearTasks();
        manager.clearSubtasks();
        manager.clearEpics();
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    @Test
    void postTask() throws InterruptedException, IOException {

        Task task = new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30));
        String taskJson = gson.toJson(task);

        HttpResponse<String> response;

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks");
            HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(taskJson)).uri(url)
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getListTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size());
        assertEquals("задача 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    void getTasks() throws InterruptedException, IOException {

        Task task = new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30));
        manager.addTask(task);

        HttpResponse<String> response;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void deleteTaskById() throws InterruptedException, IOException {

        Task task = new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30));
        Task task1 = new Task("задача 2", "описание задачи 2", 2, Status.NEW,
                LocalDateTime.of(2024, 7, 13, 13, 0), Duration.ofMinutes(30));
        manager.addTask(task);
        manager.addTask(task1);

        HttpResponse<String> response;

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/tasks/" + task.getId());
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getListTask().size());
    }

    @Test
    void postSubtask() throws InterruptedException, IOException {

        Epic newEpic = new Epic("эпик 1", "описание эпика 1", 1, Status.NEW);
        Subtask newSubtask = new Subtask("подзадача 1", "описание подзадачи 1", 2,
                Status.NEW, 1, LocalDateTime.of(2024, 9, 23, 13, 0), Duration.ofDays(2));
        manager.addEpic(newEpic);
        String subtaskJson = gson.toJson(newSubtask);

        HttpResponse<String> response;

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks");
            HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).uri(url)
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        assertEquals(201, response.statusCode());

        List<Subtask> subtasksFromManager = manager.getListSubtask();

        assertNotNull(subtasksFromManager, "Подзадачи не возвращаются");
        assertEquals(1, subtasksFromManager.size());
        assertEquals("подзадача 1", subtasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }

    @Test
    void getSubtasks() throws InterruptedException, IOException {

        Epic newEpic = new Epic("эпик 1", "описание эпика 1", 1, Status.NEW);
        Subtask newSubtask = new Subtask("подзадача 1", "описание подзадачи 1", 2,
                Status.NEW, 1, LocalDateTime.of(2024, 9, 23, 13, 0), Duration.ofDays(2));
        manager.addEpic(newEpic);
        manager.addSubtask(newSubtask);

        HttpResponse<String> response;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);

        assertEquals(200, response.statusCode());
    }

    @Test
    void deleteSubtaskById() throws InterruptedException, IOException {

        Epic newEpic = new Epic("эпик 1", "описание эпика 1", 1, Status.NEW);
        Subtask newSubtask = new Subtask("подзадача 1", "описание подзадачи 1", 2,
                Status.NEW, 1, LocalDateTime.of(2024, 9, 23, 13, 0), Duration.ofDays(2));
        manager.addEpic(newEpic);
        manager.addSubtask(newSubtask);

        HttpResponse<String> response;

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/subtasks/" + newSubtask.getId());
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        assertEquals(200, response.statusCode());
        assertTrue(manager.getListSubtask().isEmpty());
    }

    @Test
    void postEpic() throws InterruptedException, IOException {

        Epic newEpic = new Epic("эпик 1", "описание эпика 1", 1, Status.NEW);

        String epicJson = gson.toJson(newEpic);

        HttpResponse<String> response;

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics");
            HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(epicJson)).uri(url)
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = manager.getListEpic();

        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size());
        assertEquals("эпик 1", epicsFromManager.get(0).getName(), "Некорректное имя эпика'");
    }

    @Test
    void getEpics() throws InterruptedException, IOException {

        Epic newEpic = new Epic("эпик 1", "описание эпика 1", 1, Status.NEW);
        manager.addEpic(newEpic);

        HttpResponse<String> response;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response);

        assertEquals(200, response.statusCode());
    }

    @Test
    void deleteEpicById() throws InterruptedException, IOException {

        Epic newEpic = new Epic("эпик 1", "описание эпика 1", 1, Status.NEW);
        manager.addEpic(newEpic);

        HttpResponse<String> response;

        try (HttpClient client = HttpClient.newHttpClient()) {
            URI url = URI.create("http://localhost:8080/epics/" + newEpic.getId());
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }

        assertEquals(200, response.statusCode());
        assertTrue(manager.getListEpic().isEmpty());
    }
    @Test
    void getTaskById() throws InterruptedException, IOException {

        Task task = new Task("задача 1", "описание задачи 1", 1, Status.NEW,
                LocalDateTime.of(2024, 6, 13, 13, 0), Duration.ofMinutes(30));
        Task task1 = new Task("задача 2", "описание задачи 2", 2, Status.NEW,
                LocalDateTime.of(2024, 7, 13, 13, 0), Duration.ofMinutes(30));
        manager.addTask(task);
        manager.addTask(task1);

        HttpResponse<String> response;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task1.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());



        assertEquals(200, response.statusCode());

        final Task taskFromManager = gson.fromJson(response.body(), new TypeToken<Task>() {
        }.getType());
        System.out.println(response.body());

        assertEquals(task1.getId(), taskFromManager.getId(), "Некорректный id задачи");
    }

    @Test
    void getSubtaskById() throws InterruptedException, IOException {

        Epic newEpic = new Epic("эпик 1", "описание эпика 1", 1, Status.NEW);
        Subtask newSubtask = new Subtask("подзадача 1", "описание подзадачи 1", 2,
                Status.NEW, 1, LocalDateTime.of(2024, 9, 23, 13, 0), Duration.ofMinutes(20));
        Subtask newSubtask1 = new Subtask("подзадача 2", "описание подзадачи 1", 3,
                Status.NEW, 1, LocalDateTime.of(2024, 9, 24, 13, 0), Duration.ofMinutes(20));
        manager.addEpic(newEpic);
        manager.addSubtask(newSubtask);
        manager.addSubtask(newSubtask1);

        HttpResponse<String> response;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + newSubtask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        final Subtask subtaskFromManager = gson.fromJson(response.body(), new TypeToken<Subtask>() {}.getType());

        assertEquals(newSubtask.getId(), subtaskFromManager.getId(), "Некорректный id задачи");
    }

    @Test
    void getEpicById() throws InterruptedException, IOException {

        Epic newEpic = new Epic("эпик 1", "описание эпика 1", 1, Status.NEW);
        Subtask newSubtask = new Subtask("подзадача 1", "описание подзадачи 1", 2,
                Status.NEW, 1, LocalDateTime.of(2024, 9, 23, 13, 0), Duration.ofMinutes(20));
        Subtask newSubtask1 = new Subtask("подзадача 2", "описание подзадачи 1", 2,
                Status.NEW, 1, LocalDateTime.of(2024, 9, 24, 13, 0), Duration.ofMinutes(20));
        manager.addEpic(newEpic);
        manager.addTask(newSubtask);
        manager.addTask(newSubtask1);

        HttpResponse<String> response;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + newEpic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        final Epic epicFromManager = gson.fromJson(response.body(), new TypeToken<Epic>() {}.getType());

        assertEquals(newEpic.getId(), epicFromManager.getId(), "Некорректный id эпика");
    }
}