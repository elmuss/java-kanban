package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Subtask;
import tasks.Task;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;

import static http.HttpTaskServer.sendText;

public class TasksHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TasksHandler(TaskManager taskManager) {
        this.manager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET": {
                    if (Pattern.matches("^/tasks$", path)) {
                        String response = gson.toJson(manager.getListTask());
                        sendText(exchange, response);
                        return;

                    } else if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/", "");
                        int id = parsePathId(pathId);

                        if (id != -1) {
                            Optional<Task> optionalTask = Optional.ofNullable(manager.getCertainTask(id));
                            if (optionalTask.isEmpty()) {
                                exchange.sendResponseHeaders(404, 0);
                            } else {
                                String response = gson.toJson(manager.getCertainTask(id));
                                sendText(exchange, response);
                            }
                            break;

                        } else {
                            exchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    } else {
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;

                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            Optional<Task> optionalTask = Optional.ofNullable(manager.getCertainTask(id));
                            if (optionalTask.isEmpty()) {
                                exchange.sendResponseHeaders(404, 0);
                            } else {
                                manager.removeCertainTask(id);
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else {
                            exchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/tasks$", path)) {
                        manager.clearTasks();
                        exchange.sendResponseHeaders(200, 0);

                    } else {
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;

                }
                case "POST": {
                    InputStream stream = exchange.getRequestBody();
                    String taskJson = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                    Task newTask = gson.fromJson(taskJson, Task.class);

                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            Optional<Task> optionalTask = Optional.ofNullable(manager.getCertainTask(id));
                            if (optionalTask.isEmpty()) {
                                exchange.sendResponseHeaders(404, 0);
                            } else {
                                manager.updateTask(newTask);
                                exchange.sendResponseHeaders(201, 0);
                            }
                            break;
                        } else {
                            exchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    } else if (Pattern.matches("^/tasks$", path)) {
                        manager.addTask(newTask);
                        if (manager.getPrioritizedTasks().contains(newTask)) {
                            exchange.sendResponseHeaders(201, 0);
                        } else {
                            exchange.sendResponseHeaders(406, 0);
                        }


                    } else {
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;
                }
                default: {
                    exchange.sendResponseHeaders(405, 0);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}