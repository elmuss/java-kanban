package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormatter {
    public static String taskToString(List<Task> tasksToSave, List<Subtask> subtasksToSave, List<Epic> epicsToSave) {
        StringBuilder sb = new StringBuilder("id,type,name,status,description,epic\n");

        for (Task task : tasksToSave) {
            sb.append(task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription());
            sb.append("\n");
        }
        for (Subtask subtask : subtasksToSave) {
            sb.append(subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," +
                    subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpicId());
            sb.append("\n");
        }
        for (Epic epic : epicsToSave) {
            sb.append(epic.getId() + "," + epic.getType() + "," + epic.getName() + "," +
                    epic.getStatus() + "," + epic.getDescription());
            sb.append("\n");
        }
        return sb.toString();
    }
    static String historyToString(List<Task> historyToSave) {
        StringBuilder sb = new StringBuilder();

        for (Task task : historyToSave) {
            if (task.equals(historyToSave.getLast())) {
                sb.append(task.getId());
            } else {
                sb.append(task.getId() + ",");
            }

        }
        return sb.toString();
    }
    static Task fromString(String value) {
        int taskId = 0;
        String taskName = "";
        Status taskStatus = Status.NEW;
        String taskDescription = "";
        TaskType type = TaskType.TASK;
        int epicId = 0;
        Task taskFromString;

        if (!value.startsWith("id") && !value.isEmpty()) {
            String[] taskFieldsFromString = value.split(",");

            taskId = Integer.parseInt(taskFieldsFromString[0]);
            type = setType(taskFieldsFromString[1]);
            taskName = taskFieldsFromString[2];
            taskStatus = setStatus(taskFieldsFromString[3]);
            taskDescription = taskFieldsFromString[4];

            if (taskFieldsFromString.length == 6) {
                epicId = Integer.parseInt(taskFieldsFromString[5]);
            }
        }

        if (type.equals(TaskType.SUBTASK)) {
            taskFromString = new Subtask(taskName, taskDescription, taskId, taskStatus, epicId);
        } else if (type.equals(TaskType.EPIC)) {
            taskFromString = new Epic(taskName, taskDescription, taskId, taskStatus);
        } else {
            taskFromString = new Task(taskName, taskDescription, taskId, taskStatus);
        }

        return taskFromString;
    }
    static Status setStatus(String status) {
        return Status.valueOf(status);
    }
    static TaskType setType(String type) {
        return TaskType.valueOf(type);

    }
    static List<Integer> historyFromString(String value) {
        List<Integer> historyList = new ArrayList<>();
        String[] historyFromString = value.split(",");

        for (String element : historyFromString) {
            historyList.add(Integer.parseInt(element));
        }

        return historyList;
    }
}
