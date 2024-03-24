package service;

public class Managers {
    public static TaskManager getDefaultsTaskManager() {
        return new FileBackedTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


