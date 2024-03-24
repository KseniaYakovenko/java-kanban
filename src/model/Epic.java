package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Epic extends Task {

    private List<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        super.setStatus(TaskStatus.NEW);
    }

    public Epic(String name) {
        super(name);
        super.setStatus(TaskStatus.NEW);
    }

    public Epic(int id) {
        super(id);
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
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
            } else if (subTask.getStatus() != TaskStatus.DONE) {
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
                '}';
    }

    public void setSubtasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public String toDto() {
        return this.getId() + "," + this.getType().name() + "," + this.getName() + "," + this.getStatus().name() + ","
                + this.getDescription() + "," + null;
    }
}
