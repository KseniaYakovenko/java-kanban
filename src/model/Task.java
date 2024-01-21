package model;

public class Task {

    private int id;
    private String name;
    private String description;
    private TaskStatus status;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }



    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Task(){

    }

    public Task (String name) {
        this.name = name;
    }


    public Task(Task task) {
        this.id = task.id;
        this.name = task.name;
        this.status = task.status;
        this.description = task.description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
