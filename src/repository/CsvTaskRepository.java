package repository;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

public class CsvTaskRepository implements TaskRepository {
    private static final String HEADER_CSV = "id,type,name,status,description,epic";
    private final FileNameProvider provider;

    public CsvTaskRepository(FileNameProvider provider) {
        this.provider = provider;
    }

    @Override
    public TaskData load() {
        List<Task> tasks = new ArrayList<>();
        List<SubTask> subTasks = new ArrayList<>();
        List<Epic> epics = new ArrayList<>();
        List<Task> history = new ArrayList<>();
        TaskData taskData = new TaskData(tasks, subTasks, epics, history);
        HashMap<Integer, ArrayList<SubTask>> epicSubTasksMap = new HashMap<>();
        HashMap<Integer, Epic> epicsMap = new HashMap<>();
        HashMap<Integer, Task> fullMap = new HashMap<>();

        try (final BufferedReader reader = new BufferedReader(new FileReader(provider.getTaskFileName()))) {
            reader.readLine();
            int maxId = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                int id = parseLine(line, taskData, epicSubTasksMap, epicsMap, fullMap);
                if (maxId < id) {
                    maxId = id;
                }
            }
            System.out.println("MAX ID" + maxId);
            taskData.setSeq(maxId);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка в файле: " + new File(provider.getTaskFileName()).getAbsolutePath(), e);
        }

        try (final BufferedReader reader = new BufferedReader(new FileReader(provider.getHistoryFileName()))) {
            String historyLine = reader.readLine();
            if (historyLine != null) {
                final String[] columns = historyLine.split(",");
                for (String column : columns) {
                    if (!column.isEmpty()) {
                        int id = parseInt(column);
                        Task task = fullMap.get(id);
                        if (task == null) {
                            throw new ManagerSaveException("Ошибка загрузки истории для идентификатора id = " + id);
                        }
                        taskData.getHistory().add(task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + new File(provider.getHistoryFileName()).getAbsolutePath(), e);
        }

        restoreEpicSubTaskLink(taskData, epicSubTasksMap, epicsMap);
        return taskData;
    }

    private static void restoreEpicSubTaskLink(TaskData taskData, HashMap<Integer, ArrayList<SubTask>> epicSubTasksMap, HashMap<Integer, Epic> epicsMap) {
        for (Epic epic : taskData.getEpics()) {
            if (epicSubTasksMap.get(epic.getId()) != null) {
                epic.setSubtasks(epicSubTasksMap.get(epic.getId()));
            }
        }
        for (SubTask subTask : taskData.getSubTasks()) {
            Epic epic = epicsMap.get(subTask.getEpic().getId());
            subTask.setEpic(epic);
        }
    }

    @Override
    public void save(TaskData taskData) {
        saveTasks(taskData);
        saveHistory(taskData);
    }

    private void saveHistory(TaskData taskData) throws ManagerSaveException {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(provider.getHistoryFileName()))) {
            StringBuilder sb = new StringBuilder();
            for (Task task : taskData.getHistory()) {
                sb.append(task.getId().toString());
                sb.append(',');
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }

            writer.append(sb);
            writer.newLine();

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + new File(provider.getHistoryFileName()).getAbsolutePath(), e);
        }
    }


    private void saveTasks(TaskData taskData) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(provider.getTaskFileName()))) {
            writer.append(HEADER_CSV);
            writer.newLine();
            for (Task task : taskData.getTasks()) {
                writer.append(task.toDto());
                writer.newLine();
            }

            for (Epic epic : taskData.getEpics()) {
                writer.append(epic.toDto());
                writer.newLine();
            }

            for (SubTask subTask : taskData.getSubTasks()) {
                writer.append(subTask.toDto());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка в файле: " + new File(provider.getTaskFileName()).getAbsolutePath(), e);
        }
    }

    private int parseLine(String line, TaskData taskData, HashMap<Integer, ArrayList<SubTask>> epicSubTasksMap,
                          HashMap<Integer, Epic> epicsMap, HashMap<Integer, Task> fullMap) {
        final String[] columns = line.split(",");
        int id = parseInt(columns[0]);
        TaskType type = TaskType.valueOf(columns[1]);
        String name = columns[2];
        TaskStatus status = TaskStatus.valueOf(columns[3]);
        String description = columns[4];

        switch (type) {
            case TASK:
                Task task = new Task(id, name, description, status);
                taskData.getTasks().add(task);
                fullMap.put(id, task);
                break;

            case SUBTASK:
                SubTask subTask = new SubTask(id, name, description, status);
                Integer epicId = parseInt(columns[5]);
                subTask.setEpic(new Epic(epicId));
                taskData.getSubTasks().add(subTask);
                ArrayList<SubTask> subTasks = epicSubTasksMap.get(epicId) == null ?
                        new ArrayList<>() : epicSubTasksMap.get(epicId);
                subTasks.add(subTask);
                epicSubTasksMap.put(epicId, subTasks);
                fullMap.put(id, subTask);
                break;

            case EPIC:
                Epic epic = new Epic(id, name, description, status);
                taskData.getEpics().add(epic);
                epicsMap.put(id, epic);
                fullMap.put(id, epic);
                break;
        }
        return id;
    }
}
