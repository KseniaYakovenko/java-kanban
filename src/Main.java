import model.*;
import service.Managers;
import service.TaskManager;


public class Main {

    public static void main(String[] args) {
        TaskManager fileBackedTaskManager = Managers.getDefaultsTaskManager();
        printAllTasks(fileBackedTaskManager);


        int firstTaskId = fileBackedTaskManager.createTask(new Task("Новая задача"));

        Task firstTask = fileBackedTaskManager.getTaskById(firstTaskId);

        Task firstTaskFromManager = fileBackedTaskManager.getTaskById(firstTask.getId());
        System.out.println("Get task by ID: " + firstTaskFromManager);

        printHistory(fileBackedTaskManager);

        int secondTaskId = fileBackedTaskManager.createTask(new Task("Вторая Новая задача"));
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
//
//
     //   fileBackedTaskManager.deleteTask(firstTaskFromManager.getId());
        System.out.println("Delete by ID: " + firstTask);

        System.out.println("List of all tasks after first task delete" + fileBackedTaskManager.getAllTask());


     //   fileBackedTaskManager.deleteTask(secondTaskFromManager.getId());
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

        int secondSubTaskId = fileBackedTaskManager.createSubTask(new SubTask("Новая подзадача 2", epic));
        SubTask secondSubTask = fileBackedTaskManager.getSubTaskById(secondSubTaskId);
        System.out.println("\nCreate subtask: " + secondSubTask);
        System.out.println("All subTasks: " + fileBackedTaskManager.getAllSubTask());

        System.out.println(secondSubTask.toDto());

        Epic epicById = fileBackedTaskManager.getEpicById(epicFromManager.getId());
        epicById.setDescription("No subtasks");
        fileBackedTaskManager.updateEpic(epicById);

        Epic newEpic = new Epic("epicName", "epicDesc_without_subTasks");
        fileBackedTaskManager.createEpic(newEpic);

        printAllTasks(fileBackedTaskManager);

        int firstTaskId1 = fileBackedTaskManager.createTask(new Task("LAST_NEW_TASK"));
        Task firstTask1 = fileBackedTaskManager.getTaskById(firstTaskId1);
        System.out.println("Create task: " + firstTask1);
        firstTask.setDescription("UPDATED MANUAL TASK");
        fileBackedTaskManager.updateTask(firstTask1);
        System.out.println("TASK AFTER MANUAL UPDATE " + firstTask);

        Task firstTaskFromManager1 = fileBackedTaskManager.getTaskById(firstTask1.getId());
        System.out.println("Get task by ID: " + firstTaskFromManager1);

        printHistory(fileBackedTaskManager);


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
