package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(new InMemoryHistoryManager());

    @BeforeEach
    void clear(){
        inMemoryTaskManager.tasks.clear();
        inMemoryTaskManager.subTasks.clear();
        inMemoryTaskManager.epics.clear();
    }

    @Test
    void generateId() {
        int expected = inMemoryTaskManager.generateId();
        int actual = inMemoryTaskManager.seq;
        assertEquals(expected, actual, "Sequence не совпадает");

    }

    @Test
    void createTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatus.NEW);
        final int taskId = inMemoryTaskManager.createTask(task);
        final Task savedTask = inMemoryTaskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = inMemoryTaskManager.getAllTask();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createSubTask() {
        Epic epic = new Epic("Epic", "epicDescription");
        SubTask subTask = new SubTask("Test addNewSubTask", epic);
        final int subTaskId = inMemoryTaskManager.createSubTask(subTask);
        final Task savedSubTask = inMemoryTaskManager.getSubTaskById(subTaskId);
        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");
        final List<SubTask> subTasks = inMemoryTaskManager.getAllSubTask();
        assertNotNull(subTask, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.get(0), "Подзадачи не совпадают.");
    }

    @Test
    void shouldNocCreateEpicAsSubTask() {
        Epic epic = new Epic("Epic", "epicDescription");
        SubTask subTask = new SubTask("SubTask", epic);
        final int epicId = inMemoryTaskManager.createEpic(epic);
        final int subTaskId = inMemoryTaskManager.createSubTask(subTask);

        epic.setId(subTaskId);
        inMemoryTaskManager.updateSubTask(subTask);
        Epic actual = inMemoryTaskManager.getEpicById(epicId);
        assertEquals(epic, actual, "Эпик не должен измениться");

    }

    @Test
    void createEpic() {
        Epic epic = new Epic("Epic", "epicDescription");
        final int epicId = inMemoryTaskManager.createEpic(epic);
        final Epic savedEpic = inMemoryTaskManager.getEpicById(epicId);
        assertNotNull(savedEpic, "Эпик не найдена.");
        assertEquals(epic, savedEpic, "Эпик не совпадают.");
        final List<Epic> epics = inMemoryTaskManager.getAllEpic();
        assertNotNull(epic, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void getAllTask() {
        Task task1 = new Task("Task1", "description1", TaskStatus.NEW);
        Task task2 = new Task("Task2", "description2", TaskStatus.DONE);
        Task task3 = new Task("Task3", "description3", TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        List<Task> expected = inMemoryTaskManager.getAllTask();
        List<Task> actual =  new ArrayList<>();
        actual.add(task1);
        actual.add(task2);
        actual.add(task3);
        assertEquals(expected, actual);
    }

    @Test
    void updateEpic() {
        Epic actual = new Epic("Epic", "epicDescription");
        Epic expected = new Epic("Epic", "Updated description");
        final int expectedId = inMemoryTaskManager.createEpic(actual);
        expected.setId(expectedId);
        actual.setDescription("Updated description");
        inMemoryTaskManager.updateEpic(expected);
        assertEqualsEpic(expected, actual, "Эпики должны совпадать");
    }

    @Test
    void shouldBeChangedStatusForEpicWhenSubTaskDone(){

        Epic epic = new Epic("EpicName");
        inMemoryTaskManager.createEpic(epic);
        SubTask subTask = new SubTask("subTask", epic);
        inMemoryTaskManager.createSubTask(subTask);
        TaskStatus expectedStatus = TaskStatus.DONE;
        subTask.setStatus(expectedStatus);
        TaskStatus epicActualStatus = epic.getStatus();
        assertEquals(expectedStatus, epicActualStatus, "Статус эпика должен измениться");
    }

    private static void assertEqualsEpic(Epic expected, Epic actual, String message){
        assertEquals(expected.getId(), actual.getId(), message + ", id");
        assertEquals(expected.getName(), actual.getName(), message + ", name");
        assertEquals(expected.getDescription(), actual.getDescription(), message + ", description ");
        assertEquals(expected.getStatus(), actual.getStatus(), message + ", status");
        assertEquals(expected.getSubTask(), actual.getSubTask(), message + ", subTask");

    }
}