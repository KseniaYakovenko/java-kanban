package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{

    List<SubTask> subTasks = new ArrayList<>();

    public List<SubTask> getSubTask() {
       return subTasks;
   }



}
