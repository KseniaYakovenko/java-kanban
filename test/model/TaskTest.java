package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void setName() {
        String name = "name";
        Task task = new Task(name, "description", TaskStatus.IN_PROGRESS);
        String newName = "newName";
        task.setName(newName);
        String actualName = task.getName();
        assertEquals(newName, actualName, "Имя не совпадают");

    }

    @Test
    void setStatus() {
        TaskStatus status = TaskStatus.IN_PROGRESS;
        Task task = new Task("Test addNewTask", "description", status);
        TaskStatus newStatus = TaskStatus.DONE;
        task.setStatus(newStatus);
        TaskStatus statusActual = task.getStatus();
        assertEquals(newStatus, statusActual, "Статусы не совпадают");

    }

    @Test
    void setDescription() {
        String description = "testDescription";
        Task task = new Task("Test addNewTask", description, TaskStatus.NEW);
        String newDescription = "NewTestDescription";
        task.setDescription(newDescription);
        String actualDescription = task.getDescription();
        assertEquals(newDescription, actualDescription, "Описания не совпадают");
    }

    @Test
    void getStatus() {
        TaskStatus status = TaskStatus.IN_PROGRESS;
        Task task = new Task("Test addNewTask", "description", status);
        TaskStatus statusActual = task.getStatus();
        assertEquals(status, statusActual, "Статусы не совпадают");
    }

    @Test
    void getDescription() {
        String description = "testDescription";
        Task task = new Task("Test addNewTask", description, TaskStatus.NEW);
        String actualDescription = task.getDescription();
        assertEquals(description, actualDescription, "Описания не совпадают");
    }

    @Test
    void checkNoIntersection() {
        Task task1 = new Task("first", LocalDateTime.now(), Duration.ofMinutes(1));
        Task task2 = new Task("first", LocalDateTime.now().plusMinutes(2), Duration.ofMinutes(1));
        Boolean intersection = task1.checkIntersection(task2);
        assertFalse(intersection);
    }

    @Test
    void checkIntersection() {
        Task task1 = new Task("first", LocalDateTime.now(), Duration.ofMinutes(2));
        Task task2 = new Task("first", LocalDateTime.now().plusMinutes(1), Duration.ofMinutes(1));
        task1.setId(1);
        task2.setId(2);
        Boolean intersection = task1.checkIntersection(task2);
        assertTrue(intersection);
    }
}