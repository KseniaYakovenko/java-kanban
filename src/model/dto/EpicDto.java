package model.dto;

import com.google.gson.annotations.SerializedName;
import model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class EpicDto {

    public int id;
    public String name;
    public String description;
    public TaskStatus status;
    public LocalDateTime startTime;
    public Duration duration;
    public LocalDateTime endTime;
    @SerializedName("subTasks")
    public List<SubTaskDto> subTasksDto;

    public EpicDto(int id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration, LocalDateTime endTime, List<SubTaskDto> subTaskDto) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
        this.subTasksDto = subTaskDto;
    }
}