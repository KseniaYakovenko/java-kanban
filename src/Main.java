import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task = taskManager.createTask(new Task("Новая задача"));
        System.out.println("Create task: " + task);

        Task firstTaskFromManager = taskManager.getTaskById(task.getId());
        System.out.println("Get task: " + firstTaskFromManager);

        firstTaskFromManager.setName("New name");
        firstTaskFromManager.setStatus(TaskStatus.IN_PROGRESS);
        firstTaskFromManager.setDescription("Changed name and status for task");
        taskManager.update(firstTaskFromManager);
        System.out.println("Update task: " + firstTaskFromManager);

        Task secondTask = taskManager.createTask(new Task("Вторая Новая задача"));
        System.out.println("Create task: " + secondTask);

        Task secondTaskFromManager = taskManager.getTaskById(secondTask.getId());

        System.out.println("List of all tasks " + taskManager.getAllTask());


        taskManager.delete(firstTaskFromManager.getId());
        System.out.println("Delete: " + task);

        System.out.println("List of all tasks after first task delete" + taskManager.getAllTask());


        taskManager.delete(secondTaskFromManager.getId());
        System.out.println("Delete: " + secondTask);

        System.out.println("List of all tasks after first and second task delete" + taskManager.getAllTask());



        SubTask subTask = taskManager.createSubTask(new SubTask("Новая подзадача"));
        System.out.println("\nCreate subtask: " + subTask);

        SubTask firstSubTaskFromManager = taskManager.getSubTaskById(subTask.getId());
        System.out.println("Get subTask: " + firstSubTaskFromManager);

        Epic epic = taskManager.createEpic(new Epic("Новый эпик"));
        System.out.println("\nCreate epic: " + epic);

        Epic epicFromManager = taskManager.getEpicById(epic.getId());
        System.out.println("Get epic: " + epicFromManager);
    }
}
