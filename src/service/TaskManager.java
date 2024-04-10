package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.dto.SubTaskDto;

import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    int createTask(Task task);

    int createSubTask(SubTask subTask);

    int createEpic(Epic epic);

    List<Task> getAllTask();

    Task getTaskById(int id);

    List<Epic> getAllEpic();

    Epic getEpicById(int id);

    List<SubTask> getAllSubTask();

    SubTask getSubTaskById(int id);

    TreeSet<Task> getPrioritizedTasks();

    void updateTask(Task task);

    void deleteTask(int id);

    void deleteAllTask();

    void deleteEpic(int id);

    void deleteAllSubTasksFromEpic(Epic removeEpic);

    void deleteAllEPic();

    void deleteSubTask(int id);

    void deleteSubTaskFromEpic(SubTask subTask);

    void deleteAllSubTask();

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    List<SubTask> getEpicSubtasks(int id);

    List<Task> getHistory();

    SubTask dtoToModel(SubTaskDto subTaskDto);
}
