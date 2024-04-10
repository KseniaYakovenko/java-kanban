package controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import exception.BadRequestException;
import exception.IntersectionByTime;
import exception.ManagerSaveException;
import exception.NotFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ErrorHandler {
    Gson gson = new Gson();

    public void handle(HttpExchange h, ManagerSaveException e) throws IOException {
        e.printStackTrace();
        sendText(h, 400, gson.toJson(e.getMessage()));
    }

    public void handle(HttpExchange h, IntersectionByTime e) throws IOException {
        e.printStackTrace();
        sendText(h, 406, gson.toJson(e.getMessage()));
    }

    public void handle(HttpExchange h, IOException e) throws IOException {
        e.printStackTrace();
        sendText(h, 500, gson.toJson(e.getMessage()));
    }

    public void handle(HttpExchange h, BadRequestException e) throws IOException {
        e.printStackTrace();
        sendText(h, 400, gson.toJson(e.getMessage()));
    }

    public void handle(HttpExchange h, NumberFormatException e) throws IOException {
        e.printStackTrace();
        sendText(h, 404, gson.toJson(e.getMessage()));
    }

    public void handle(HttpExchange h, JsonSyntaxException e) throws IOException {
        e.printStackTrace();
        sendText(h, 400, gson.toJson("Ошибка в сиснтаксисе json: " + e.getMessage()));
    }

    public void handle(HttpExchange h, NotFoundException e) throws IOException {
        e.printStackTrace();
        sendText(h, 404, gson.toJson(e.getMessage()));
    }

    public void handle(HttpExchange h, Exception e) throws IOException {
        e.printStackTrace();
        sendText(h, 500, gson.toJson(e.getMessage()));
    }

    protected void sendText(HttpExchange h, int code, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Context-Type", "application/json");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
    }
}