package model;

public class SubTask extends Task {

    Epic epic;

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }
}
