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





}