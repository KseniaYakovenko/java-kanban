package server;

import com.sun.net.httpserver.HttpServer;
import controller.*;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    HttpServer server;
    TaskManager manager;

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
    }

    public HttpTaskServer() {
        this.manager = Managers.getDefaultsTaskManager();
    }

    public static void main(String[] args) throws IOException {
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
}