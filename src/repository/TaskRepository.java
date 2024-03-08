package repository;
import exception.ManagerSaveException;
import model.TaskData;

public interface TaskRepository {
    TaskData load() throws ManagerSaveException;

    void save(TaskData taskData);
}