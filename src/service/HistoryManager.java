package service;

import model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void removeFromHistory(int id);

    void removeAll();

    List<Task> getHistory();
}
