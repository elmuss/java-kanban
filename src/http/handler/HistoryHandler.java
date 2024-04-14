package http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;

import static http.HttpTaskServer.*;

public class HistoryHandler implements HttpHandler {
    private final TaskManager manager;

    public HistoryHandler(TaskManager taskManager) {
        this.manager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) {

        try {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET": {
                    String response = getGson().toJson(manager.getHistory());
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
