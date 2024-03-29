package managers;

import tasks.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    protected File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save() {
        List<Task> tasksToSave = new ArrayList<>((super.getTasks()).values());

        List<Subtask> subtasksToSave = new ArrayList<>((super.getSubtasks()).values());

        List<Epic> epicsToSave = new ArrayList<>((super.getEpics()).values());

        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write(CSVTaskFormatter.taskToString(tasksToSave, subtasksToSave, epicsToSave));
            fileWriter.write("\n");
            fileWriter.write(CSVTaskFormatter.historyToString(super.getHistory()));

        } catch (IOException exception) {
            throw new ManagerSaveException("Файл не сохранен");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);

        while (br.ready()) {
            String line = br.readLine();

            if (!line.startsWith("id") && !line.isBlank() && (line.contains("TASK") || line.contains("EPIC"))) {
                Task loadedTask = CSVTaskFormatter.fromString(line);

                if (loadedTask.getType().equals(TaskType.TASK)) {
                    fileBackedTaskManager.getTasks().put(loadedTask.getId(), loadedTask);

                } else if (loadedTask.getType().equals(TaskType.SUBTASK)) {
                    fileBackedTaskManager.getSubtasks().put(loadedTask.getId(), (Subtask) loadedTask);

                } else if (loadedTask.getType().equals(TaskType.EPIC)) {
                    fileBackedTaskManager.getEpics().put(loadedTask.getId(), (Epic) loadedTask);
                }

            } else if (!line.contains("TASK") && !line.contains("EPIC") && !line.startsWith("id") && !line.isBlank()) {
                List<Integer> loadedHistory = CSVTaskFormatter.historyFromString(line);

                for (Integer element : loadedHistory) {
                    if (fileBackedTaskManager.getTasks().containsKey(element)) {
                        fileBackedTaskManager.getHistoryManager().add(fileBackedTaskManager.getTasks().get(element));
                    } else if (fileBackedTaskManager.getSubtasks().containsKey(element)) {
                        fileBackedTaskManager.getHistoryManager().add(fileBackedTaskManager.getSubtasks().get(element));
                    } else if (fileBackedTaskManager.getEpics().containsKey(element)) {
                        fileBackedTaskManager.getHistoryManager().add(fileBackedTaskManager.getEpics().get(element));
                    }
                }
            }
        }
        br.close();
        return fileBackedTaskManager;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void changeEpicStatus(Epic epic) {
        super.changeEpicStatus(epic);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void removeCertainTask(int id) {
        super.removeCertainTask(id);
        save();
    }

    @Override
    public void removeCertainEpic(int id) {
        super.removeCertainEpic(id);
        save();
    }

    @Override
    public void removeCertainSubtask(int id) {
        super.removeCertainSubtask(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void addAllToPrioritizedTasks() {
        super.addAllToPrioritizedTasks();
    }
}
