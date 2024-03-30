package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;


import static http.HttpTaskServer.getGson;
import static http.HttpTaskServer.sendText;

public class PrioritizedHandler implements HttpHandler {
    private final TaskManager manager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.manager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) {

        try {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET": {
                    String response = getGson().toJson(manager.getPrioritizedTasks());
                    sendText(exchange, response);
                    return;

                }
                default: {
                    exchange.sendResponseHeaders(405, 0);

                }
            }
        } catch (Exception exception) {
            exception.getStackTrace();

        } finally {
            exchange.close();
        }
    }
}
