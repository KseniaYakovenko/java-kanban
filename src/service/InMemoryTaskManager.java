package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected final HistoryManager historyManager;
    protected int seq = 0;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private int generateId() {
        return ++seq;
    }

    @Override
    public int createTask(Task task) {
        task.setId(generateId());
        task.setStatus(TaskStatus.NEW);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTask.setStatus(TaskStatus.NEW);
        subTasks.put(subTask.getId(), subTask);
        subTask.getEpic().addSubTask(subTask);
        return subTask.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpicById(int id) {
        Task task = epics.get(id);
        historyManager.add(task);
        return epics.get(id);
    }

    @Override
    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask task = subTasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.removeFromHistory(id);
    }

    @Override
    public void deleteAllTask() {
        tasks.clear();
        historyManager.removeAll();
    }

    @Override
    public void deleteEpic(int id) {
        Epic removeEpic = epics.get(id);
        if (removeEpic != null) {
            deleteAllSubTasksFromEpic(removeEpic);
            epics.remove(id);
            historyManager.removeFromHistory(id);
        } else {
            System.out.println("Incorrect id = " + id + " for deleting");
        }
    }

    @Override
    public void deleteAllSubTasksFromEpic(Epic removeEpic) {
        List<SubTask> subTaskListForRemoving = removeEpic.getSubTask();
        for (Task subTask : subTaskListForRemoving) {
            int subTaskId = subTask.getId();
            deleteSubTask(subTaskId);
            historyManager.removeFromHistory(subTaskId);
        }
    }

    @Override
    public void deleteAllEPic() {
        for (Epic epic : epics.values()) {
            deleteAllSubTasksFromEpic(epic);
            historyManager.removeFromHistory(epic.getId());
        }
        epics.clear();
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask removeSubTask = subTasks.get(id);
        deleteSubTaskFromEpic(removeSubTask);
        historyManager.removeFromHistory(id);
        subTasks.remove(id);
    }

    @Override
    public void deleteSubTaskFromEpic(SubTask subTask) {
        subTask.getEpic().removeSubTask(subTask);
        historyManager.removeFromHistory(subTask.getId());
    }

    @Override
    public void deleteAllSubTask() {
        for (SubTask subTask : subTasks.values()) {
            deleteSubTaskFromEpic(subTask);
            historyManager.removeFromHistory(subTask.getId());
        }
        subTasks.clear();
    }

    @Override
    public void updateEpic(Epic epic) {
        int epicId = epic.getId();
        Epic savedEpic = epics.get(epicId);
        epic.setSubtasks(savedEpic.getSubTask());
        epic.calculateStatus();
        epics.put(epicId, epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpic().getId())) {
            subTasks.put(subTask.getId(), subTask);
            subTask.getEpic().calculateStatus();
        }
    }

    @Override
    public List<SubTask> getEpicSubtasks(int id) {
        return getEpicById(id).getSubTask();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void init() {
    }
}
