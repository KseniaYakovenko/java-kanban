package service;

import model.Task;
import model.TaskData;
import org.junit.jupiter.api.Test;
import repository.BadCsvFileNameProviderTest;
import repository.CsvFileNameProviderTest;
import repository.CsvTaskRepository;
import repository.TaskRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {

    private final TaskRepository repository = new CsvTaskRepository(new CsvFileNameProviderTest());

    @Test
    public void createTaskAndSaveCSVTest() {
        Task task = new Task("name");
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        TaskData taskData = new TaskData(taskList, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        repository.save(taskData);
        TaskData taskDataLoad = repository.load();
        Task loadTask = taskDataLoad.getTasks().get(0);
        System.out.println(task);
        System.out.println(loadTask);
        assertEquals(task, loadTask, "Записи в файле должны совпадать");
    }

    @Test
    public void createTaskAndSaveHistoryTest() {
        Task task = new Task("name");
        int expectedId = 777;
        task.setId(expectedId);
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        List<Task> historyList = new ArrayList<>();
        historyList.add(task);
        TaskData taskData = new TaskData(taskList, Collections.emptyList(), Collections.emptyList(), historyList);
        repository.save(taskData);
        TaskData taskData1 = repository.load();
        int actualId = taskData1.getHistory().get(0).getId();
        assertEquals(expectedId, actualId, "Записи в истории должны совпадать");
    }

    @Test
    public void testException() {
        TaskRepository badRepository = new CsvTaskRepository(new BadCsvFileNameProviderTest());
        assertThrows(RuntimeException.class, () -> {
            badRepository.load();
        }, "Отсутствие файла должно приводить к исключению");
    }
}