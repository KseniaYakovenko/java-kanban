package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskData;
import repository.CsvFileNameProvider;
import repository.CsvTaskRepository;
import repository.TaskRepository;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final TaskRepository repository = new CsvTaskRepository(new CsvFileNameProvider());

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
        init();
    }

    @Override
    public int createTask(Task task) {
        int newTaskId = super.createTask(task);
        save();
        return newTaskId;
    }

    private void save() {
        TaskData taskData = new TaskData(getAllTask(), getAllSubTask(), getAllEpic(), getHistory());
        repository.save(taskData);
    }

    public void init() {
        TaskData taskData = repository.load();
        taskData.getTasks().forEach(task -> {
            tasks.put(task.getId(), task);
            prioritizedTask.add(task);
        });
        taskData.getSubTasks().forEach(subTask -> {
            subTasks.put(subTask.getId(), subTask);
            prioritizedTask.add(subTask);
        });
        taskData.getEpics().forEach(epic -> {
            epics.put(epic.getId(), epic);
            prioritizedTask.add(epic);
        });
        seq = taskData.getSeq();
        taskData.getHistory().forEach(task -> historyManager.add(task));
    }

    @Override
    public int createSubTask(SubTask subTask) {
        int newSubTaskId = super.createSubTask(subTask);
        save();
        return newSubTaskId;
    }

    @Override
    public int createEpic(Epic epic) {
        int newEpicId = super.createEpic(epic);
        save();
        return newEpicId;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllSubTasksFromEpic(Epic removeEpic) {
        super.deleteAllSubTasksFromEpic(removeEpic);
        save();
    }

    @Override
    public void deleteAllEPic() {
        super.deleteAllEPic();
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteSubTaskFromEpic(SubTask subTask) {
        super.deleteSubTaskFromEpic(subTask);
        save();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }
}
