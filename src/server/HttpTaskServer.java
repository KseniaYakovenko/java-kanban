package server;

import adapter.DurationAdapter;
import adapter.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import controller.*;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer server;
    private final TaskManager manager;
    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
    }

    public HttpTaskServer() {
        this.manager = Managers.getDefaultsTaskManager();
    }

    public static void main(String[] args) {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.init();
        httpTaskServer.start();

    }

    public void init() {
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new TaskPathHandler(manager));
            server.createContext("/epics", new EpicPathHandler(manager));
            server.createContext("/subtasks", new SubTaskPathHandler(manager));
            server.createContext("/history", new HistoryPathHandler(manager));
            server.createContext("/prioritized", new PrioritizedPathHandler(manager));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        System.out.println("Starting TaskServer " + PORT);
        server.start();
    }

    public void stop() {
        System.out.println("Stopped TaskServer " + PORT);
        server.stop(1);
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}