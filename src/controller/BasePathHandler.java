package controller;

import adapter.DurationAdapter;
import adapter.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BasePathHandler {
    ErrorHandler errorHandler = new ErrorHandler();
    TaskManager manager;

    public BasePathHandler(TaskManager manager) {
        this.manager = manager;
    }

    void sendText(HttpExchange h, int code, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Context-Type", "application/json");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
    }

    public Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts.length == 2) {
            switch (requestMethod) {
                case "GET" -> {
                    return Endpoint.GET_ALL;
                }
                case "POST" -> {
                    return Endpoint.POST;
                }
                case "DELETE" -> {
                    return Endpoint.DELETE;
                }
            }
        }
        if ((pathParts.length == 3) && (requestMethod.equals("GET"))) {
            return Endpoint.GET_BY_ID;
        }
        if ((pathParts.length == 4) && (requestMethod.equals("GET"))
                && (pathParts[1].equals("epics") && pathParts[3].equals("subtasks"))) {
            return Endpoint.GET_EPIC_SUBTASKS;
        }

        return Endpoint.UNKNOWN;
    }

    public int getId(String requestPath) throws NumberFormatException {
        String[] pathParts = requestPath.split("/");
        return Integer.parseInt(pathParts[2]);
    }

    public String parseBody(HttpExchange httpExchange) throws IOException {
        InputStream bodyInputStream = httpExchange.getRequestBody();
        return new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}
