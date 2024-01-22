package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{

    List<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
    }

    public Epic(String name) {
        super(name);
    }


    public List<SubTask> getSubTask() {

        return subTasks;
   }

    public void addSubTask(SubTask subTask) {

    }

    public void removeSubTask(SubTask subTask) {

    }

    public void calculateStatus() {

    }

}
