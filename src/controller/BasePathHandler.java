package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static server.HttpTaskServer.getGson;

public class BasePathHandler {
    protected final ErrorHandler errorHandler = new ErrorHandler();
    protected final TaskManager manager;
    static Gson gson = getGson();

    public BasePathHandler(TaskManager manager) {
        this.manager = manager;
    }

    void sendText(HttpExchange h, int code, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Context-Type", "application/json");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
    }

    void sendText(HttpExchange h, int code) throws IOException {
        h.sendResponseHeaders(code, -1);
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

    public String parseBody(HttpExchange httpExchange) throws BadRequestException, IOException {
        InputStream bodyInputStream = httpExchange.getRequestBody();
        byte[] inputStreamBytes = bodyInputStream.readAllBytes();
        if (inputStreamBytes.length == 0) throw new BadRequestException("Пустое тело запроса");
        return new String(inputStreamBytes, StandardCharsets.UTF_8);
    }
}
