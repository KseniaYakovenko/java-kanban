package service;

import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        historyManager.removeAll();
        Task task1 = new Task("Task1", "description1", TaskStatus.NEW);
        final List<Task> historyExpected = new ArrayList<>(List.of(task1));
        historyManager.add(task1);
        final List<Task> historyActual = historyManager.getHistory();
        assertEquals(historyExpected, historyActual, "История не совпадает.");
    }

    @Test
    void delFromMiddleHistory() {
        Task task1 = new Task(1);
        Task task2 = new Task(2);
        Task task3 = new Task(3);
        final List<Task> historyExpected = new ArrayList<>(List.of(task1, task3));
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.removeFromHistory(task2.getId());
        final List<Task> historyActual = historyManager.getHistory();
        assertEquals(historyExpected, historyActual, "История не совпадает.");
    }

    @Test
    void delFromStartOfHistory() {
        Task task1 = new Task(1);
        Task task2 = new Task(2);
        Task task3 = new Task(3);
        final List<Task> historyExpected = new ArrayList<>(List.of(task2, task3));
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.removeFromHistory(task1.getId());
        final List<Task> historyActual = historyManager.getHistory();
        assertEquals(historyExpected, historyActual, "История не совпадает.");
    }

    @Test
    void delFromEndOfHistory() {
        historyManager.removeAll();
        Task task1 = new Task(1);
        Task task2 = new Task(2);
        Task task3 = new Task(3);
        final List<Task> historyExpected = new ArrayList<>(List.of(task1, task2));
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.removeFromHistory(task3.getId());
        final List<Task> historyActual = historyManager.getHistory();
        assertEquals(historyExpected, historyActual, "История не совпадает.");
    }

    @Test
    void checkNoRepeatInHistory() {
        Task task1 = new Task(1);
        Task task2 = new Task(2);
        final List<Task> historyExpected = new ArrayList<>(List.of(task1, task2));
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task2);
        final List<Task> historyActual = historyManager.getHistory();
        assertEquals(historyExpected, historyActual, "История не совпадает.");
    }
}