package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Subtask;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;

import static http.HttpTaskServer.sendText;

public class SubtasksHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public SubtasksHandler(TaskManager taskManager) {
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
                    if (Pattern.matches("^/subtasks$", path)) {
                        String response = gson.toJson(manager.getListSubtask());
                        sendText(exchange, response);
                        return;

                    } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id = parsePathId(pathId);

                        if (id != -1) {
                            Optional<Subtask> optionalSubtask = Optional.ofNullable(manager.getCertainSubtask(id));
                            if (optionalSubtask.isEmpty()) {
                                exchange.sendResponseHeaders(404, 0);
                            } else {
                                String response = gson.toJson(manager.getCertainSubtask(id));
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
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            Optional<Subtask> optionalSubtask = Optional.ofNullable(manager.getCertainSubtask(id));
                            if (optionalSubtask.isEmpty()) {
                                exchange.sendResponseHeaders(404, 0);
                            } else {
                                manager.removeCertainSubtask(id);
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else {
                            exchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/subtasks$", path)) {
                        manager.clearSubtasks();
                        exchange.sendResponseHeaders(200, 0);

                    } else {
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;

                }
                case "POST": {
                    InputStream stream = exchange.getRequestBody();
                    String subtaskJson = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                    Subtask newSubtask = gson.fromJson(subtaskJson, Subtask.class);

                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            Optional<Subtask> optionalSubtask = Optional.ofNullable(manager.getCertainSubtask(id));
                            if (optionalSubtask.isEmpty()) {
                                exchange.sendResponseHeaders(404, 0);
                            } else {
                                manager.updateSubtask(newSubtask);
                                exchange.sendResponseHeaders(201, 0);
                                break;
                            }
                        } else {
                            exchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    } else if (Pattern.matches("^/subtasks$", path)) {
                        manager.addSubtask(newSubtask);
                        if (manager.getPrioritizedTasks().contains(newSubtask)) {
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