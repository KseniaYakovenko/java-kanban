package model;

import java.util.List;

public class TaskData {
    private final List<Task> tasks;
    private final List<SubTask> subTasks;
    private final List<Epic> epics;
    final List<Task> history;
    int seq = 0;

    public TaskData(List<Task> tasks, List<SubTask> subTasks, List<Epic> epics, List<Task> history) {
        this.tasks = tasks;
        this.subTasks = subTasks;
        this.epics = epics;
        this.history = history;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }


    public int getSeq() {
        return seq;
    }


    public List<Task> getTasks() {
        return tasks;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public List<Epic> getEpics() {
        return epics;
    }

    public List<Task> getHistory() {
        return history;
    }
}