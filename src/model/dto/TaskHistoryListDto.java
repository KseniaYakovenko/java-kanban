package model.dto;

import model.TaskStatus;

import java.time.LocalDateTime;

public class TaskHistoryListDto {

    public int id;
    public String name;
    public String description;
    public TaskStatus status;
    public LocalDateTime startTime;

    public TaskHistoryListDto(int id, String name, String description, TaskStatus status, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
    }
}