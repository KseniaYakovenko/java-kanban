package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    int seq = 0;

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

    public void deleteAllTask() {
        tasks.clear();
    }

    public void deleteEpic(int id) {
        Epic removeEpic = epics.get(id);
        if (removeEpic != null) {
            deleteAllSubTasksFromEpic(removeEpic);
            epics.remove(id);
        } else {
            System.out.println("Incorrect id = " + id + " for deleting");
        }
    }

    private void deleteAllSubTasksFromEpic(Epic removeEpic) {
        List<SubTask> subTaskListForRemoving = removeEpic.getSubTask();
        for (SubTask subTask : subTaskListForRemoving) {
            deleteSubTask(subTask.getId());
        }
    }

    public void deleteAllEPic() {
        for (Epic epic : epics.values()) {
            deleteAllSubTasksFromEpic(epic);
        }
        epics.clear();
    }

    public void deleteSubTask(int id) {
        SubTask removeSubTask = subTasks.get(id);
        deleteSubTaskFromEpic(removeSubTask);
        subTasks.remove(id);
    }

    public void deleteSubTaskFromEpic(SubTask subTask) {
        subTask.getEpic().removeSubTask(subTask);
    }

    public void deleteAllSubTask() {
        for (SubTask subTask : subTasks.values()) {
            deleteSubTaskFromEpic(subTask);
        }
        subTasks.clear();
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
