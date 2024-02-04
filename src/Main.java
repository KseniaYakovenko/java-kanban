import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = Managers.getDefaultsTaskManager();
        printAllTasks(inMemoryTaskManager);

        int firstTaskId = inMemoryTaskManager.createTask(new Task("Новая задача"));
        Task firstTask = inMemoryTaskManager.getTaskById(firstTaskId);
        System.out.println("Create task: " + firstTask);
        firstTask.setDescription("UPDATED MANUAL TASK");
        inMemoryTaskManager.updateTask(firstTask);
        System.out.println("TASK AFTER MANUAL UPDATE " + firstTask);

        Task firstTaskFromManager = inMemoryTaskManager.getTaskById(firstTask.getId());
        System.out.println("Get task by ID: " + firstTaskFromManager);

        printHistory(inMemoryTaskManager);

        int secondTaskId = inMemoryTaskManager.createTask(new Task("Вторая Новая задача"));
        Task secondTask = inMemoryTaskManager.getTaskById(secondTaskId);
        secondTask.setName("Вторая Новая задача after set name");
        System.out.println("Create task: " + secondTask);

        int thirdTaskId = inMemoryTaskManager.createTask(new Task("Третья Новая задача", "create with wrong status", TaskStatus.IN_PROGRESS));
        Task thirdTask = inMemoryTaskManager.getTaskById(thirdTaskId);
        System.out.println("Create task: " + thirdTask);

        Task secondTaskFromManager = inMemoryTaskManager.getTaskById(secondTask.getId());
        System.out.println("Task by ID: " + secondTaskFromManager);

        System.out.println("List of all tasks " + inMemoryTaskManager.getAllTask());

        printAllTasks(inMemoryTaskManager);


        inMemoryTaskManager.deleteTask(firstTaskFromManager.getId());
        System.out.println("Delete by ID: " + firstTask);

        System.out.println("List of all tasks after first task delete" + inMemoryTaskManager.getAllTask());


        inMemoryTaskManager.deleteTask(secondTaskFromManager.getId());
        System.out.println("Delete: " + secondTask);

        System.out.println("List of all tasks after first and second task delete" + inMemoryTaskManager.getAllTask());

        int epicId = inMemoryTaskManager.createEpic(new Epic("Новый эпик"));
        Epic epic = inMemoryTaskManager.getEpicById(epicId);
        System.out.println("\nCreate epic: " + epic);

        Epic epicFromManager = inMemoryTaskManager.getEpicById(epic.getId());

        System.out.println("Getting epic BY ID 10 times ");
        getEpicDyIdSomeTimes(inMemoryTaskManager, epic, 10);

        printHistory(inMemoryTaskManager);

        int firstSubTaskId = inMemoryTaskManager.createSubTask(new SubTask("Новая подзадача 1", epic));
        SubTask firstSubTask = inMemoryTaskManager.getSubTaskById(firstSubTaskId);
        System.out.println("\nCreate subtask: " + firstSubTask);

        SubTask firstSubTaskFromManager = inMemoryTaskManager.getSubTaskById(firstSubTask.getId());
        firstSubTaskFromManager.setStatus(TaskStatus.DONE);
        System.out.println("Set status DONE for first subTask");
        System.out.println("Get firstSubTask: " + firstSubTaskFromManager);
        firstSubTaskFromManager.setDescription("updated from updateSubTusk");
        inMemoryTaskManager.updateSubTask(firstSubTaskFromManager);
        System.out.println("Get firstSubTask after updating: " + firstSubTaskFromManager);

        int secondSubTaskId = inMemoryTaskManager.createSubTask(new SubTask("Новая подзадача 2", epic));
        SubTask secondSubTask = inMemoryTaskManager.getSubTaskById(secondSubTaskId);
        System.out.println("\nCreate subtask: " + secondSubTask);
        System.out.println("All subTasks: " + inMemoryTaskManager.getAllSubTask());


        Epic epicById = inMemoryTaskManager.getEpicById(epicFromManager.getId());
        epicById.setDescription("No subtasks");
        inMemoryTaskManager.updateEpic(epicById);

        Epic newEpic = new Epic("epicName", "epicDesc_without_subTasks");
        inMemoryTaskManager.createEpic(newEpic);

        System.out.println("All tasks: " + inMemoryTaskManager.getAllTask());
        System.out.println("All subTasks: " + inMemoryTaskManager.getAllSubTask());
        System.out.println("All epics: " + inMemoryTaskManager.getAllEpic());

        System.out.println("===================");
        printAllTasks(inMemoryTaskManager);
        System.out.println("===================");

        inMemoryTaskManager.deleteAllEPic();
        inMemoryTaskManager.deleteEpic(4);
        inMemoryTaskManager.deleteEpic(7);
        inMemoryTaskManager.deleteAllSubTask();
        inMemoryTaskManager.deleteAllTask();
        System.out.println("\nAll tasks after removing all: " + inMemoryTaskManager.getAllTask());
        System.out.println("All subTasks after removing all: " + inMemoryTaskManager.getAllSubTask());
        System.out.println("All epics after removing all: " + inMemoryTaskManager.getAllEpic());


        printAllTasks(inMemoryTaskManager);

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
