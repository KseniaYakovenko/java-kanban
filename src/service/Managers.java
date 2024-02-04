package service;

public class Managers {
    public static TaskManager getDefaultsTaskManager(){
        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }
}


