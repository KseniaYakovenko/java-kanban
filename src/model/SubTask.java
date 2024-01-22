package model;

public class SubTask extends Task {

    Epic epic;

    public SubTask(String name, Epic epic) {
        super(name);
        this.epic = epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public Epic getEpic() {
        return epic;
    }

    @Override
    public void setStatus(TaskStatus status) {
        super.setStatus(status);
        epic.calculateStatus();
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
}
