package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    @Test
    void setDescription() {
        String description = "testDescription";
        Epic epic = new Epic("Test addNewEpic", description);
        String newDescription = "NewTestDescription";
        epic.setDescription(newDescription);
        String actualDescription = epic.getDescription();
        assertEquals(newDescription, actualDescription, "Епики не совпадают");
    }

    @Test
    void equalsEpics() {
        int id = 100;
        Epic epic = new Epic("EpicName");
        epic.setId(id);
        Epic epicExpected = new Epic("EpicAnotherName");
        epicExpected.setId(id);
        assertEquals(epicExpected, epic, "Епики должны совпадать");
    }
    @Test
    void shouldBeNotChangedStatus(){
        Epic epic = new Epic("EpicName");
        TaskStatus expectedImmutableStatus = epic.getStatus();
        TaskStatus newStatus = TaskStatus.IN_PROGRESS;
        epic.setStatus(newStatus);
        TaskStatus actualStatus = epic.getStatus();
        assertEquals(expectedImmutableStatus, actualStatus, "Статус эпика не должен измениться");
    }

    @Test
    void allSubTaskNew() {
        Epic epic = new Epic("EpicName");
        SubTask subTask1 = new SubTask("first", epic);
        SubTask subTask2 = new SubTask("second", epic);
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        TaskStatus expected = TaskStatus.NEW;
        TaskStatus actual = epic.getStatus();
        assertEquals(expected, actual, "Статус эпика должен быть NEW");
    }

    @Test
    void allSubTaskDone() {
        Epic epic = new Epic("EpicName");
        SubTask subTask1 = new SubTask("first", epic);
        SubTask subTask2 = new SubTask("second", epic);
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        subTask1.setStatus(TaskStatus.DONE);
        subTask2.setStatus(TaskStatus.DONE);
        TaskStatus expected = TaskStatus.DONE;
        TaskStatus actual = epic.getStatus();
        assertEquals(expected, actual, "Статус эпика должен быть DONE");
    }

    @Test
    void subTaskHasDoneAndNewStatusesEpicInProgress() {
        Epic epic = new Epic("EpicName");
        SubTask subTask1 = new SubTask("first", epic);
        SubTask subTask2 = new SubTask("second", epic);
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        subTask1.setStatus(TaskStatus.NEW);
        subTask2.setStatus(TaskStatus.DONE);
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        TaskStatus actual = epic.getStatus();
        assertEquals(expected, actual, "Статус эпика должен быть IN_PROGRESS");
    }

    @Test
    void allSubTaskInProgress() {
        Epic epic = new Epic("EpicName");
        SubTask subTask1 = new SubTask("first", epic);
        SubTask subTask2 = new SubTask("second", epic);
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        TaskStatus expected = TaskStatus.IN_PROGRESS;
        TaskStatus actual = epic.getStatus();
        assertEquals(expected, actual, "Статус эпика должен быть IN_PROGRESS");
    }
}