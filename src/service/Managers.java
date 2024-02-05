package service;

public class Managers {
    public static TaskManager getDefaultsTaskManager() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


