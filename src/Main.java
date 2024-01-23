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
        System.out.println("Get task:    " + firstTaskFromManager);

        firstTaskFromManager.setName("New name");
        firstTaskFromManager.setStatus(TaskStatus.IN_PROGRESS);
        firstTaskFromManager.setDescription("Changed name and status for task");
        taskManager.updateTask(firstTaskFromManager);
        System.out.println("Update task: " + firstTaskFromManager);

        Task secondTask = taskManager.createTask(new Task("Вторая Новая задача"));
        System.out.println("Create task: " + secondTask);

        Task thirdTask = taskManager.createTask(new Task("Третья Новая задача", "create with wrong status", TaskStatus.IN_PROGRESS));
        System.out.println("Create task: " + thirdTask);

        Task secondTaskFromManager = taskManager.getTaskById(secondTask.getId());

        System.out.println("List of all tasks " + taskManager.getAllTask());


        taskManager.deleteTask(firstTaskFromManager.getId());
        System.out.println("Delete: " + task);

        System.out.println("List of all tasks after first task delete" + taskManager.getAllTask());


        taskManager.deleteTask(secondTaskFromManager.getId());
        System.out.println("Delete: " + secondTask);

        System.out.println("List of all tasks after first and second task delete" + taskManager.getAllTask());

        Epic epic = taskManager.createEpic(new Epic("Новый эпик"));
        System.out.println("\nCreate epic: " + epic);

        Epic epicFromManager = taskManager.getEpicById(epic.getId());
        System.out.println("Get epic: " + epicFromManager);

        SubTask firstSubTask = taskManager.createSubTask(new SubTask("Новая подзадача 1", epic));
        System.out.println("\nCreate subtask: " + firstSubTask);

        SubTask firstSubTaskFromManager = taskManager.getSubTaskById(firstSubTask.getId());
        firstSubTaskFromManager.setStatus(TaskStatus.DONE);
        System.out.println("Set status DONE for first subTask");
        System.out.println("Get firstSubTask: " + firstSubTaskFromManager);
        firstSubTaskFromManager.setDescription("updated from updateSubTusk");
        taskManager.updateSubTask(firstSubTaskFromManager);
        System.out.println("Get firstSubTask after updating: " + firstSubTaskFromManager);

        SubTask secondSubTask = taskManager.createSubTask(new SubTask("Новая подзадача 2", epic));
        System.out.println("\nCreate subtask: " + secondSubTask);
        System.out.println("All subTasks: " + taskManager.getAllSubTask());

        System.out.println("\nGet epic after creating two subtask: " + epicFromManager);

        epicFromManager.removeSubTask(firstSubTask);
        System.out.println("Get epic after removing first subtask: " + epicFromManager);

        System.out.println("Set status DONE for secondSubTask");
        secondSubTask.setStatus(TaskStatus.DONE);
        System.out.println(secondSubTask);
        epicFromManager.setDescription("alone subtask with status DONE");
        System.out.println("Get epic after changing status second subtask: " + epicFromManager);

        System.out.println("Get epic after removing: " + epicFromManager);

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getAllEpic());

        taskManager.deleteSubTask(secondSubTask.getId());

        Epic epicById = taskManager.getEpicById(epicFromManager.getId());
        epicById.setDescription("No subtasks");
        taskManager.updateEpic(epicById);

        Epic newEpic = new Epic("epicName", "epicDesc_without_subTasks");
        taskManager.createEpic(newEpic);

        System.out.println(taskManager.getAllEpic());


    }
}
