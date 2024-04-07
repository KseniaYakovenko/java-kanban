package model.dto;

import model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTaskDto {
    public int id;
    public String name;
    public String description;
    public TaskStatus status;
    public LocalDateTime startTime;
    public Duration duration;
    public LocalDateTime endTime;
    public int epicId;

    public SubTaskDto(int id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration, LocalDateTime endTime, int epicId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
        this.epicId = epicId;
    }
}