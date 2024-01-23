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
        subTask.getEpic().addSubTask(subTask);
        return subTask;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteSubTask(int id) {
        SubTask removeSubTask = subTasks.remove(id);
        Epic epic = removeSubTask.getEpic();
        Epic epicSaved = epics.get(epic.getId());
        epicSaved.removeSubTask(removeSubTask);
    }

    public void updateEpic(Epic epic) {
        int epicId = epic.getId();
        Epic savedEpic = epics.get(epicId);
        epic.setSubtasks(savedEpic.getSubTask());
        epic.calculateStatus();
        epics.put(epicId, epic);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        subTask.getEpic().calculateStatus();
    }
}
