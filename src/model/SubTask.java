package model;

public class SubTask extends Task {

    private Epic epic;

    public SubTask(String name, Epic epic) {
        super(name);
        this.epic = epic;
    }

    public SubTask(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        this.epic = null;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public void setStatus(TaskStatus status) {
        super.setStatus(status);
        epic.calculateStatus();
    }

    @Override
    public TaskType getType (){
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                ", status='" + this.getStatus() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", epicId='" + epic.getId() + '\'' +
                '}';
    }

    @Override
    public String toDto() {
        return this.getId() + "," + this.getType().name() + "," + this.getName() + "," + this.getStatus().name() + ","
                +  this.getDescription()+ "," + this.epic.getId();
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }
}
