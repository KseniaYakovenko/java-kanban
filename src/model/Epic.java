package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    List<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name) {
        super(name);
        super.setStatus(TaskStatus.NEW);
    }

    public List<SubTask> getSubTask() {
        return subTasks;
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
        if (subTasks.isEmpty() || allSubTaskIsNew()) {
            super.setStatus(TaskStatus.NEW);
        } else if (allSubTaskIsDone()) {
            super.setStatus(TaskStatus.DONE);
        } else super.setStatus(TaskStatus.IN_PROGRESS);
    }

    private boolean allSubTaskIsDone() {
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() != TaskStatus.DONE) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setStatus(TaskStatus status) {
        calculateStatus();
    }

    private boolean allSubTaskIsNew() {
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() != TaskStatus.NEW) {
                return false;
            }
        }
        return true;
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
}
