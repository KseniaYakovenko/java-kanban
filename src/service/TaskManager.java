package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.HashMap;

public class TaskManager {

    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, SubTask> subTasks;
    int seq = 0;

    public TaskManager() {
        this.tasks = new HashMap<>();
    }

    private int generateId() {
        return ++seq;
    }

    public Task create(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Task get (int id) {
        return tasks.get(id);
    }

    public Epic get1 (int id) {
        return epics.get(id);
    }

    public void update(Task task){
        tasks.put(task.getId(), task);
    }

    public void delete(int id) {
        tasks.remove(id);
}
    public void deleteSubTask(int id) {
        SubTask removeSubTask = subTasks.remove(id);

        Epic epic = removeSubTask.getEpic();
        Epic epicSaved = epics.get(epic.getId());

        epicSaved.getSubTask().remove(removeSubTask);
        calculateStatus(epicSaved);
    }

    private void calculateStatus(Epic epic) {

    }
}
