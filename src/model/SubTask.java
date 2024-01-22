package model;

public class SubTask extends Task {

    Epic epic;

    public SubTask() {
        super();
    }
    public SubTask(String name) {
        super(name);
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public Epic getEpic() {
        return epic;
    }
}
