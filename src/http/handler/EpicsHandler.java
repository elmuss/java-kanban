package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Epic;
import tasks.Task;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;

import static http.HttpTaskServer.sendText;

public class EpicsHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager) {
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
                    if (Pattern.matches("^/epics$", path)) {
                        String response = gson.toJson(manager.getListEpic());
                        sendText(exchange, response);
                        return;

                    } else if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        String[] subtasksOfEpic = pathId.split("/");
                        int id = parsePathId(pathId);

                        if (id != -1) {
                            Optional<Epic> optionalEpic = Optional.ofNullable(manager.getCertainEpic(id));
                            if (subtasksOfEpic.length == 1) {
                                if (optionalEpic.isEmpty()) {
                                    exchange.sendResponseHeaders(404, 0);
                                } else {
                                    String response = gson.toJson(manager.getCertainEpic(id));
                                    sendText(exchange, response);
                                }
                                break;
                            } else {
                                if (optionalEpic.isEmpty()) {
                                    exchange.sendResponseHeaders(404, 0);
                                } else {
                                    String response = gson.toJson(manager.getCertainEpic(id).getSubtasksOfEpic());
                                    sendText(exchange, response);
                                }
                            }

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
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            Optional<Epic> optionalEpic = Optional.ofNullable(manager.getCertainEpic(id));
                            if (optionalEpic.isEmpty()) {
                                exchange.sendResponseHeaders(404, 0);
                            } else {
                                manager.removeCertainEpic(id);
                                exchange.sendResponseHeaders(200, 0);
                            }
                        } else {
                            exchange.sendResponseHeaders(405, 0);
                        }
                    } else if (Pattern.matches("^/epics$", path)) {
                        manager.clearEpics();
                        exchange.sendResponseHeaders(200, 0);

                    } else {
                        exchange.sendResponseHeaders(405, 0);
                    }
                    break;

                }
                case "POST": {
                    InputStream stream = exchange.getRequestBody();
                    String epicJson = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                    Epic newEpic = gson.fromJson(epicJson, Epic.class);

                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            Optional<Epic> optionalEpic = Optional.ofNullable(manager.getCertainEpic(id));
                            if (optionalEpic.isEmpty()) {
                                exchange.sendResponseHeaders(404, 0);
                            } else {
                                manager.updateEpic(newEpic);
                                exchange.sendResponseHeaders(201, 0);
                            }
                            break;
                        } else {
                            exchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    } else if (Pattern.matches("^/epics$", path)) {
                        manager.addEpic(newEpic);
                        exchange.sendResponseHeaders(201, 0);

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