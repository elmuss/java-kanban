package managers;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVTaskFormatter {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd|HH:mm");

    public String taskToString(List<Task> tasksToSave, List<Subtask> subtasksToSave, List<Epic> epicsToSave) {
        StringBuilder sb = new StringBuilder("id,type,name,status,description,startTime,duration,epic\n");

        for (Task task : tasksToSave) {
            sb.append(task.getId() + "," + task.getType() + "," + task.getName() + "," +
                    task.getStatus() + "," + task.getDescription() + "," + task.getStartTime().format(formatter) + "," +
                    task.getDuration().toSeconds());
            sb.append("\n");
        }
        for (Subtask subtask : subtasksToSave) {
            sb.append(subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," +
                    subtask.getStatus() + "," + subtask.getDescription() + "," +
                    subtask.getStartTime().format(formatter) + "," + subtask.getDuration().toSeconds() + "," +
                    subtask.getEpicId());
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
                sb.append(task.getId()).append(",");
            }
        }
        return sb.toString();
    }

    static Task fromString(String value) {
        int taskId = 0;
        String taskName = "";
        Status taskStatus = Status.NEW;
        String taskDescription = "";
        TaskType type = null;
        int epicId = 0;
        LocalDateTime startTime = null;
        Duration duration = Duration.ZERO;
        Task taskFromString;

        if (!value.startsWith("id") && !value.isEmpty()) {
            String[] taskFieldsFromString = value.split(",");

            taskId = Integer.parseInt(taskFieldsFromString[0]);
            type = setType(taskFieldsFromString[1]);
            taskName = taskFieldsFromString[2];
            taskStatus = setStatus(taskFieldsFromString[3]);
            taskDescription = taskFieldsFromString[4];

            if (taskFieldsFromString.length == 7) {
                startTime = LocalDateTime.parse(taskFieldsFromString[5], formatter);
                duration = Duration.ofSeconds(Long.parseLong(taskFieldsFromString[6]));
            } else if (taskFieldsFromString.length == 8) {
                epicId = Integer.parseInt(taskFieldsFromString[7]);
            }
        }

        if (type.equals(TaskType.SUBTASK)) {
            taskFromString = new Subtask(taskName, taskDescription, taskId, taskStatus, epicId, startTime, duration);
        } else if (type.equals(TaskType.EPIC)) {
            taskFromString = new Epic(taskName, taskDescription, taskId, taskStatus);
        } else {
            taskFromString = new Task(taskName, taskDescription, taskId, taskStatus, startTime, duration);
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

        Arrays.stream(historyFromString).forEach(e -> historyList.add(Integer.parseInt(e)));

        return historyList;
    }
}
