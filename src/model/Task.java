package model;

import model.dto.TaskDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus status;
    private LocalDateTime startTime;
    private Duration duration;

    public Task(String name) {
        this.name = name;
        this.status = TaskStatus.NEW;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(0);
    }

    public Task(String name, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.status = TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id) {
        this.id = id;
        this.status = TaskStatus.NEW;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(0);
    }


    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(0);
    }

    public Task(int id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ZERO;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }

    public Boolean checkIntersection(Task task) {
        if (this.equals(task)) {
            return false;
        }
        if (this.getEndTime().isBefore(task.startTime)) {
            return false;
        }
        if (this.startTime.isAfter(task.getEndTime())) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", startTime='" + startTime.toString() + '\'' +
                ", duration='" + duration.toMinutes() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String toDto() {
        return this.getId() + "," + this.getType().name() + "," + this.getName() + "," + this.getStatus().name() + ","
                + this.getDescription() + "," + null + "," + this.getStartTime() + "," + this.getDuration();
    }

    public TaskDto mapperToTaskDto() {
        return new TaskDto(
                this.getId(),
                this.getType(),
                this.getName(),
                this.getDescription(),
                this.getStatus(),
                this.getStartTime(),
                this.getDuration(),
                this.getEndTime()
        );
    }
}
