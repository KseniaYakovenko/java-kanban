package model;

import model.dto.EpicDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Epic extends Task {

    private List<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        super.setStartTime(LocalDateTime.now());
        super.setDuration(Duration.ofMinutes(0));
    }

    public Epic(String name) {
        super(name);
        super.setStatus(TaskStatus.NEW);
    }

    public Epic(int id) {
        super(id);
    }

    public Epic(int id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        this.subTasks = Collections.emptyList();
    }

    public List<SubTask> getSubTask() {
        return new ArrayList<>(subTasks);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        calculateStatus();
    }

    public void removeSubTask(SubTask subTask) {
        subTasks.remove(subTask);
        calculateStatus();
    }

    @Override
    public Duration getDuration() {
        return subTasks.stream().map(Task::getDuration).reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public LocalDateTime getStartTime() {
        if (subTasks.isEmpty()) {
            return super.getStartTime();
        }
        return subTasks.stream()
                .min(Comparator.comparing(SubTask::getStartTime))
                .map(Task::getStartTime)
                .orElseGet(super::getStartTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subTasks.isEmpty()) {
            return super.getStartTime();
        }
        return subTasks.stream()
                .max(Comparator.comparing(SubTask::getEndTime))
                .map(Task::getEndTime)
                .orElseGet(super::getEndTime);
    }

    public void calculateStatus() {
        if (subTasks.isEmpty()) {
            super.setStatus(TaskStatus.NEW);
            return;
        }
        boolean allSubTaskStatusesIsNEW = true;
        boolean allSubTaskStatusesIsDONE = true;
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() != TaskStatus.NEW) {
                allSubTaskStatusesIsNEW = false;
            }
            if (subTask.getStatus() != TaskStatus.DONE) {
                allSubTaskStatusesIsDONE = false;
            }
            if (!(allSubTaskStatusesIsNEW || allSubTaskStatusesIsDONE)) {
                super.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
        }
        if (allSubTaskStatusesIsNEW) {
            super.setStatus(TaskStatus.NEW);
        } else if (allSubTaskStatusesIsDONE) {
            super.setStatus(TaskStatus.DONE);
        }
    }

    @Override
    public void setStatus(TaskStatus status) {
        calculateStatus();
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                ", status='" + this.getStatus() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", subTasks='" + this.getSubTask() + '\'' +
                ", startTime='" + this.getStartTime().toString() + '\'' +
                ", duration='" + this.getDuration().toMinutes() + '\'' +
                '}';
    }

    public void setSubtasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public String toDto() {
        return this.getId() + "," + this.getType().name() + "," + this.getName() + "," + this.getStatus().name() + ","
                + this.getDescription() + "," + null + "," + this.getStartTime() + "," + this.getDuration();
    }

    public EpicDto mapperToDto() {
        return new EpicDto(
                this.getId(),
                this.getType(),
                this.getName(),
                this.getDescription(),
                this.getStatus(),
                this.getStartTime(),
                this.getDuration(),
                this.getEndTime(),
                this.getSubTask().stream().map(SubTask::mapperToDto).toList()
        );
    }
}
