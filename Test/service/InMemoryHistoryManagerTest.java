package service;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @BeforeEach
    void clearHistory(){
        historyManager.history.clear();
    }

    @Test
    void add() {
        Task task1 = new Task("Task1", "description1", TaskStatus.NEW);
        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void getHistory() {
        Task task1 = new Task("Task1", "description1", TaskStatus.NEW);
        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        final List<Task> historyExpected = new ArrayList<>();
        historyExpected.add(task1);
        assertEquals(historyExpected, history, "История не совпадает.");
    }
}