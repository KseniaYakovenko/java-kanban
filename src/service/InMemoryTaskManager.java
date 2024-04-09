package service;

import exception.IntersectionByTime;
import exception.NotFoundException;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import model.dto.SubTaskDto;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected final HistoryManager historyManager;
    protected int seq = 0;

    protected TreeSet<Task> prioritizedTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    private int generateId() {
        return ++seq;
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return new TreeSet<>(prioritizedTask);
    }

    @Override
    public int createTask(Task task) {
        checkIntersections(task);
        task.setId(generateId());
        task.setStatus(TaskStatus.NEW);
        tasks.put(task.getId(), task);
        prioritizedTask.add(task);
        return task.getId();
    }

    private Boolean checkIntersections(Task newTask) {
        for (Task task : prioritizedTask) {
            if (task.checkIntersection(newTask)) {
                throw new IntersectionByTime(
                        "Пересечение задач по времени выполнения: \n"
                                + task.getType() + " " + task.getId() + " " + task.getStartTime() + " - " + task.getEndTime() + "\n"
                                + newTask.getType() + " " + newTask.getId() + " " + newTask.getStartTime() + " - " + newTask.getEndTime()
                );
            }
        }
        return false;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        checkIntersections(subTask);
        subTask.setId(generateId());
        subTask.setStatus(TaskStatus.NEW);
        subTasks.put(subTask.getId(), subTask);
        subTask.getEpic().addSubTask(subTask);
        prioritizedTask.add(subTask);
        return subTask.getId();
    }

    @Override
    public SubTask dtoToModel(SubTaskDto subTaskDto) {
        SubTask subTask = new SubTask(
                generateId(),
                subTaskDto.name,
                subTaskDto.description,
                subTaskDto.status,
                subTaskDto.startTime,
                subTaskDto.duration);
        Epic epic = epics.get(subTaskDto.epicId);
        subTask.setEpic(epic);

        return subTask;
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
    public Task getTaskById(int id) throws NotFoundException {
        Task task = tasks.get(id);
        if (task == null) throw new NotFoundException("Не найдена задача с id=" + id);
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
        if (task == null) throw new NotFoundException("Не найден epic с id=" + id);
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
        if (task == null) throw new NotFoundException("Не найдена подзадача с id=" + id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        checkIntersections(task);
        Task oldTask = tasks.get(task.getId());
        prioritizedTask.remove(oldTask);
        prioritizedTask.add(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTask(int id) {
        Task task = tasks.get(id);
        if (task == null) throw new NotFoundException("Не найдена задачас с id=" + id);
        tasks.remove(id);
        prioritizedTask.remove(task);
        historyManager.removeFromHistory(id);
    }

    @Override
    public void deleteAllTask() {
        tasks.values().forEach(task -> prioritizedTask.remove(task));
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
        prioritizedTask.remove(removeSubTask);
        deleteSubTaskFromEpic(removeSubTask);
        historyManager.removeFromHistory(id);
        subTasks.remove(id);
    }

    @Override
    public void deleteSubTaskFromEpic(SubTask subTask) {
        prioritizedTask.remove(subTask);
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
            checkIntersections(subTask);
            SubTask oldSubTask = subTasks.get(subTask.getId());
            prioritizedTask.remove(oldSubTask);
        subTask.setEpic(oldSubTask.getEpic());
            prioritizedTask.add(subTask);
            subTasks.put(subTask.getId(), subTask);
            subTask.getEpic().calculateStatus();
    }

    @Override
    public List<SubTask> getEpicSubtasks(int id) {
        return getEpicById(id).getSubTask();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
