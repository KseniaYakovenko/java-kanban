package service;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private static final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void add() {
        Task task1 = new Task("Task1", "description1", TaskStatus.NEW);
        final List<Task> historyBeforeAdding = historyManager.getHistory();
        historyManager.add(task1);
        final List<Task> historyAfterAdding = historyManager.getHistory();
        assertEquals(1, historyAfterAdding.size() - historyBeforeAdding.size(), "История не пустая.");
    }

    @Test
    void getHistory() {
        Task task1 = new Task("Task1", "description1", TaskStatus.NEW);
        final List<Task> historyExpected = new ArrayList<>(List.of(task1));
        historyManager.add(task1);
        final List<Task> historyActual = historyManager.getHistory();
        assertEquals(historyExpected, historyActual, "История не совпадает.");
    }
}