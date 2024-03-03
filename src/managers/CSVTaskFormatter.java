package managers;

import tasks.*;
import java.util.List;

public class CSVTaskFormatter {
    public static String toString(InMemoryTaskManager inMemoryTaskManager) {
        StringBuilder sb = new StringBuilder("id,type,name,status,description,epic\n");

        for (Task task : inMemoryTaskManager.getTasks().values()) {
            sb.append(Integer.toString(task.getId()) + task.getType() + task.getName() +
                    task.getStatus() + task.getDescription());
            sb.append("\n");

        }
        for (Subtask subtask : inMemoryTaskManager.getSubtasks().values()) {
            sb.append(Integer.toString(subtask.getId()) + subtask.getType() + subtask.getName() +
                    subtask.getStatus() + subtask.getDescription() + Integer.toString(subtask.getEpicId()));
            sb.append("\n");

        }
        for (Epic epic : inMemoryTaskManager.getEpics().values()) {
            sb.append(Integer.toString(epic.getId()) + epic.getType() + epic.getName()
                    + epic.getStatus() + epic.getDescription());
            sb.append("\n");

        }
        return sb.toString();
    }
    static String historyToString(HistoryManager historyManager) {
        StringBuilder sb = new StringBuilder();

        for (Task task : historyManager.getHistory()) {
            sb.append(task.getId());
        }
        return sb.toString();
    }
    static Task fromString(String value) {
        Task taskFromString = new Task(null, null);

        if (!value.startsWith("id")) {
            String[] taskFieldsFromString = value.split(",");

            taskFromString.setId(Integer.parseInt(taskFieldsFromString[0]));
            taskFromString.setName(taskFieldsFromString[2]);

            if (taskFieldsFromString[3].equals("NEW")) {
                taskFromString.setStatus(Status.NEW);

            } else if (taskFieldsFromString[3].equals("IN PROGRESS")) {
                taskFromString.setStatus(Status.IN_PROGRESS);

            } else {
                taskFromString.setStatus(Status.DONE);
            }
            taskFromString.setDescription(taskFieldsFromString[4]);

            if (taskFieldsFromString.length == 6) {
                ((Subtask) taskFromString).setEpicId(Integer.parseInt(taskFieldsFromString[5]));
            }
        }
        return taskFromString;
    }
    static List<Integer> historyFromString(String value) {
        String[] historyFromString = value.split(",");
        List<Integer> historyList = null;
        for (int i = 0; i < historyFromString.length; i++) {
            historyList.add(Integer.parseInt(historyFromString[i]));
        }
        return historyList;
    }
}
