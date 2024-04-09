package model.dto;

import model.TaskStatus;
import model.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskDto {
    public int id;
    public TaskType type;
    public String name;
    public String description;
    public TaskStatus status;
    public LocalDateTime startTime;
    public Duration duration;
    public LocalDateTime endTime;
    public int epicId;

    public SubTaskDto(int id, TaskType type, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration, LocalDateTime endTime, int epicId) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
        this.epicId = epicId;
    }

    public Integer getId() {
        return this.id;
    }
}