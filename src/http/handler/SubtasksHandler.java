package http.handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;

public class SubtasksHandler extends TasksHandler {

    public SubtasksHandler(TaskManager manager) {
        super(manager);
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
                                Subtask subtaskToSend = optionalSubtask.get();
                                String response = gson.toJson(subtaskToSend);
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
                        int id = super.parsePathId(pathId);
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
            try {
                exchange.sendResponseHeaders(400, 0);
                sendText(exchange, "В запросе содержится ошибка. Проверьте параметры и повторите запрос.");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            exchange.close();
        }
    }
}