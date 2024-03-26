package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private Epic epic;

    public SubTask(String name, Epic epic) {
        super(name);
        this.epic = epic;
        super.setStartTime(LocalDateTime.now());
        super.setDuration(Duration.ofMinutes(0));
    }

    public SubTask(String name, Epic epic, LocalDateTime startTime, Duration duration) {
        super(name);
        this.epic = epic;
        super.setStartTime(startTime);
        super.setDuration(duration);
    }

    public SubTask(int id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
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
    public TaskType getType() {
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
                ", startTime='" + this.getStartTime().toString() + '\'' +
                ", duration='" + this.getDuration().toMinutes() + '\'' +
                '}';
    }

    @Override
    public String toDto() {
        return this.getId() + "," + this.getType().name() + "," + this.getName() + "," + this.getStatus().name() + ","
                + this.getDescription() + "," + this.epic.getId() + "," + this.getStartTime() + "," + this.getDuration();
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }
}
