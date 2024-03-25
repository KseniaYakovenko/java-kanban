import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager fileBackedTaskManager = Managers.getDefaultsTaskManager();
        printAllTasks(fileBackedTaskManager);

        printHistory(fileBackedTaskManager);

        int secondTaskId = fileBackedTaskManager.createTask(
                new Task("Вторая Новая задача", LocalDateTime.now().plusMinutes(15), Duration.ofMinutes(1))
        );
        Task secondTask = fileBackedTaskManager.getTaskById(secondTaskId);
        secondTask.setName("Вторая Новая задача after set name");
        System.out.println("Create task: " + secondTask);

        int thirdTaskId = fileBackedTaskManager.createTask(new Task("Третья Новая задача", "create with wrong status", TaskStatus.IN_PROGRESS));
        Task thirdTask = fileBackedTaskManager.getTaskById(thirdTaskId);
        System.out.println("Create task: " + thirdTask);

        System.out.println(thirdTask.toDto());

        Task secondTaskFromManager = fileBackedTaskManager.getTaskById(secondTask.getId());
        System.out.println("Task by ID: " + secondTaskFromManager);

        System.out.println("List of all tasks " + fileBackedTaskManager.getAllTask());

        printAllTasks(fileBackedTaskManager);

        System.out.println("List of all tasks after first task delete" + fileBackedTaskManager.getAllTask());


        System.out.println("Delete: " + secondTask);

        System.out.println("List of all tasks after first and second task delete" + fileBackedTaskManager.getAllTask());

        int epicId = fileBackedTaskManager.createEpic(new Epic("Новый эпик"));
        Epic epic = fileBackedTaskManager.getEpicById(epicId);
        System.out.println("\nCreate epic: " + epic);
        System.out.println(epic.toDto());

        Epic epicFromManager = fileBackedTaskManager.getEpicById(epic.getId());

        System.out.println("Getting epic BY ID 10 times ");
        getEpicDyIdSomeTimes(fileBackedTaskManager, epic, 10);

        printHistory(fileBackedTaskManager);

        int firstSubTaskId = fileBackedTaskManager.createSubTask(new SubTask("Новая подзадача 1", epic));
        SubTask firstSubTask = fileBackedTaskManager.getSubTaskById(firstSubTaskId);
        System.out.println("\nCreate subtask: " + firstSubTask);

        SubTask firstSubTaskFromManager = fileBackedTaskManager.getSubTaskById(firstSubTask.getId());
        firstSubTaskFromManager.setStatus(TaskStatus.DONE);
        System.out.println("Set status DONE for first subTask");
        System.out.println("Get firstSubTask: " + firstSubTaskFromManager);
        firstSubTaskFromManager.setDescription("updated from updateSubTusk");
        fileBackedTaskManager.updateSubTask(firstSubTaskFromManager);
        System.out.println("Get firstSubTask after updating: " + firstSubTaskFromManager);

        int secondSubTaskId = fileBackedTaskManager.createSubTask(
                new SubTask("Новая подзадача 2", epic, LocalDateTime.now().plusMinutes(4), Duration.ofMinutes(1))
        );
        SubTask secondSubTask = fileBackedTaskManager.getSubTaskById(secondSubTaskId);
        System.out.println("\nCreate subtask: " + secondSubTask);
        System.out.println("All subTasks: " + fileBackedTaskManager.getAllSubTask());

        System.out.println(secondSubTask.toDto());

        Epic epicById = fileBackedTaskManager.getEpicById(epicFromManager.getId());
        epicById.setDescription("No subtasks");
        fileBackedTaskManager.updateEpic(epicById);

        printAllTasks(fileBackedTaskManager);

        int firstTaskId1 = fileBackedTaskManager.createTask(
                new Task("LAST_NEW_TASK", LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(1))
        );
        Task firstTask1 = fileBackedTaskManager.getTaskById(firstTaskId1);
        System.out.println("Create task: " + firstTask1);

        fileBackedTaskManager.updateTask(firstTask1);
        Task firstTaskFromManager1 = fileBackedTaskManager.getTaskById(firstTask1.getId());
        System.out.println("Get task by ID: " + firstTaskFromManager1);

        printHistory(fileBackedTaskManager);

        System.out.println("Список задач в порядке приоритета");
        fileBackedTaskManager.getPrioritizedTasks().forEach(
                task ->
                        System.out.println(
                                task.getType() + " " + task.getStartTime() + " - [" + task.getDuration().toMinutes() +
                                        "] - " + task.getEndTime())
        );

    }

    private static void getEpicDyIdSomeTimes(TaskManager inMemoryTaskManager, Epic epic, int times) {
        for (int i = 0; i < times; i++) {
            System.out.println("Get epic: " + inMemoryTaskManager.getEpicById(epic.getId()));
        }
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("\nЗадачи:");
        for (Task task : manager.getAllTask()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpic()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubTask()) {
            System.out.println(subtask);
        }

        printHistory(manager);
    }

    private static void printHistory(TaskManager manager) {
        System.out.println("История:");

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("\n");
    }
}
