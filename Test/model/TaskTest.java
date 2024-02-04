package model;

import org.junit.jupiter.api.Test;

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
}