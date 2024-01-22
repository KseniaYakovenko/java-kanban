package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {

    HashMap<Integer, Task> tasks;
    HashMap<Integer, Epic> epics;
    HashMap<Integer, SubTask> subTasks;
    int seq = 0;

    public TaskManager() {

        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    private int generateId() {
        return ++seq;
    }

    public Task createTask(Task task) {
        task.setId(generateId());
        task.setStatus(TaskStatus.NEW);
        tasks.put(task.getId(), task);
        return task;
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTask.setStatus(TaskStatus.NEW);
        subTasks.put(subTask.getId(), subTask);
        return subTask;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epic.setStatus(TaskStatus.NEW);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public List<Task> getAllTask () {
        return new ArrayList<>(tasks.values());
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public List<Epic> getAllEpic () {
        return new ArrayList<>(epics.values());
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
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
        epicSaved.calculateStatus();
    }



}
