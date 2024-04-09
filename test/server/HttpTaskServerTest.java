package server;

import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskServerTest {
    static HttpTaskServer httpTaskServer;
    static TaskManager manager = new InMemoryTaskManager(Managers.getDefaultHistory());


    @BeforeAll
    static void initServer() {
        httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.init();
        httpTaskServer.start();
    }

    @BeforeEach
    void clearManager() {
        manager.deleteAllTask();
        manager.deleteAllSubTask();
        manager.deleteAllEPic();
    }

    @Test
    void shouldReturnTaskById() throws IOException, InterruptedException {
        Task task = new Task("name", "description", TaskStatus.NEW);
        int id = manager.createTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task actualTask = HttpTaskServer.getGson().fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode());
        assertEquals(task, actualTask);
    }

    @Test
    void shouldReturnSubTaskById() throws IOException, InterruptedException {
        Epic epic = new Epic(1);
        SubTask subTask = new SubTask("name", epic);
        int id = manager.createSubTask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask actualSubTask = HttpTaskServer.getGson().fromJson(response.body(), SubTask.class);

        assertEquals(200, response.statusCode());
        assertEquals(subTask, actualSubTask);
    }

    @Test
    void shouldReturnEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic(1);
        int id = manager.createEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic actualEpic = HttpTaskServer.getGson().fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode());
        assertEquals(epic, actualEpic);
    }

    @Test
    void shouldReturnAllTasksList() throws IOException, InterruptedException {

        Task task1 = new Task("name1", "description1", TaskStatus.NEW);
        Task task2 = new Task("name2", "description2", TaskStatus.NEW);
        manager.createTask(task1);
        manager.createTask(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksList = HttpTaskServer.getGson().fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertEquals(200, response.statusCode());
        assertTrue(tasksList.containsAll(List.of(task1, task2)));
    }

    @Test
    void shouldReturnAllSubTasksList() throws IOException, InterruptedException {
        Epic epic = new Epic(1);
        SubTask subTask1 = new SubTask("name1", epic);
        SubTask subTask2 = new SubTask("name2", epic);
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        manager.createEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SubTask> subTasksList = HttpTaskServer.getGson().fromJson(response.body(), new TypeToken<List<SubTask>>() {
        }.getType());
        assertEquals(200, response.statusCode());
        assertTrue(subTasksList.containsAll(List.of(subTask1, subTask2)));
    }

    @Test
    void shouldReturnAllEpicList() throws IOException, InterruptedException {

        Epic epic1 = new Epic("epic1Name");
        Epic epic2 = new Epic("epic2Name");
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> epicsList = HttpTaskServer.getGson().fromJson(response.body(), new TypeToken<List<Epic>>() {
        }.getType());

        assertEquals(200, response.statusCode());
        assertTrue(epicsList.containsAll(List.of(epic1, epic2)));
    }

    @Test
    void shouldCreateTask() throws IOException, InterruptedException {
        Task task = new Task("name", "description", TaskStatus.NEW);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        String body = HttpTaskServer.getGson().toJson(task);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(body);

        HttpRequest request = HttpRequest
                .newBuilder()
                .method("POST", bodyPublisher)
                .uri(url)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task expected = HttpTaskServer.getGson().fromJson(response.body(), Task.class);
        Task actualTask = manager.getAllTask().getFirst();

        assertEquals(201, response.statusCode());
        assertEquals(expected, actualTask);
    }

    @Test
    void shouldUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("name", "description", TaskStatus.NEW);
        int id = manager.createTask(task);
        task.setId(id);
        String nameExpected = "expectedNewName";
        task.setName(nameExpected);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        String body = HttpTaskServer.getGson().toJson(task);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(body);

        HttpRequest request = HttpRequest
                .newBuilder()
                .method("POST", bodyPublisher)
                .uri(url)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task actualTask = manager.getAllTask().getFirst();

        assertEquals(200, response.statusCode());
        assertEquals(nameExpected, actualTask.getName());
    }

    @Test
    void shouldDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("name", "description", TaskStatus.NEW);
        manager.createTask(task);
        assertEquals(1, manager.getAllTask().size());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        String body = HttpTaskServer.getGson().toJson(task);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(body);
        HttpRequest request = HttpRequest
                .newBuilder()
                .method("DELETE", bodyPublisher)
                .uri(url)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());
        assertEquals(0, manager.getAllTask().size());
    }


    @Test
    void shouldReturnCode404OnIncorrectId() throws IOException, InterruptedException {
        Task task = new Task("name", "description", TaskStatus.NEW);
        manager.createTask(task);
        String id = "bagRequestId";
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    void shouldReturnCode404() throws IOException, InterruptedException {
        Task task = new Task("name", "description", TaskStatus.NEW);
        manager.createTask(task);
        int id = 100;
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    void shouldReturn406OnIntersectionTasks() throws IOException, InterruptedException {
        Task task1 = new Task("name1", LocalDateTime.now(), Duration.ofMinutes(1));
        Task task2 = new Task("name2", LocalDateTime.now(), Duration.ofMinutes(1));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        String body = HttpTaskServer.getGson().toJson(task1);
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(body);

        HttpRequest request1 = HttpRequest
                .newBuilder()
                .method("POST", bodyPublisher)
                .uri(url)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response1.statusCode());

        body = HttpTaskServer.getGson().toJson(task2);
        bodyPublisher = HttpRequest.BodyPublishers.ofString(body);

        HttpRequest request2 = HttpRequest
                .newBuilder()
                .method("POST", bodyPublisher)
                .uri(url)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response2.statusCode());
    }

    @Test
    void shouldReturn400EmptyBody() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        String body = "";
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(body);
        HttpRequest request = HttpRequest
                .newBuilder()
                .method("POST", bodyPublisher)
                .uri(url)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }

    @Test
    void shouldReturnHistoryList() throws IOException, InterruptedException {
        Managers.getDefaultHistory().removeAll();
        Task task1 = new Task("name1", "description1", TaskStatus.NEW);
        Task task2 = new Task("name2", "description2", TaskStatus.NEW);
        int id1 = manager.createTask(task1);
        int id2 = manager.createTask(task2);
        manager.getTaskById(id1);
        manager.getTaskById(id2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> history = HttpTaskServer.getGson().fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        assertEquals(200, response.statusCode());
        assertTrue(history.containsAll(List.of(task1, task2)));
    }

    @Test
    void shouldReturnPrioritizedList() throws IOException, InterruptedException {
        Managers.getDefaultHistory().removeAll();
        Task task1 = new Task("name1", LocalDateTime.now(), Duration.ofMinutes(1));
        Task task2 = new Task("name2", LocalDateTime.now().minusDays(1), Duration.ofMinutes(1));
        Task task3 = new Task("name3", LocalDateTime.now().plusMinutes(100), Duration.ofMinutes(1));
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        List<Task> expected = manager.getPrioritizedTasks().stream().toList();

        System.out.println(expected);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> actual = HttpTaskServer.getGson().fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());

        System.out.println(actual);
        assertEquals(200, response.statusCode());
        assertTrue(expected.containsAll(actual));
    }

    @AfterAll
    static void stop() {
        httpTaskServer.stop();
    }
}