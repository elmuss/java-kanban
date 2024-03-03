package managers;

import tasks.*;

import java.io.*;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    protected static File file;
    CSVTaskFormatter formatter = new CSVTaskFormatter();
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    public FileBackedTaskManager(File file) {
        this.file = file;
    }
    public void save() throws IOException {

        try (Writer fileWriter = new FileWriter(file, true)) {
            fileWriter.write(formatter.toString(inMemoryTaskManager));
            fileWriter.write(formatter.historyToString(inMemoryHistoryManager));

            if (!file.exists()) {
                throw new ManagerSaveException("Файл не существует");
            }

        } catch (IOException e) {
            e.getMessage();
        } catch (ManagerSaveException exception) {
            System.out.println(exception.getMessage());
        }
    }
    public FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);

        while (br.ready()) {
            String line = br.readLine();

            if (formatter.fromString(line).getType().equals(TaskType.TASK)) {
                fileBackedTaskManager.getTasks().put(formatter.fromString(line).getId(), formatter.fromString(line));

            } else if (formatter.fromString(line).getType().equals(TaskType.SUBTASK)) {
                fileBackedTaskManager.getSubtasks().put(formatter.fromString(line).getId(),
                        (Subtask) formatter.fromString(line));

            } else if (formatter.fromString(line).getType().equals(TaskType.EPIC)) {
                fileBackedTaskManager.getEpics().put(formatter.fromString(line).getId(),
                        (Epic) formatter.fromString(line));
            } else {
                List<Integer> history = formatter.historyFromString(line);
                for (Integer element : history) {
                    if (fileBackedTaskManager.getTasks().containsKey(element)) {
                        inMemoryHistoryManager.add(fileBackedTaskManager.getTasks().get(element));
                    } else if (fileBackedTaskManager.getEpics().containsKey(element)) {
                        inMemoryHistoryManager.add(fileBackedTaskManager.getEpics().get(element));
                    } else if (fileBackedTaskManager.getSubtasks().containsKey(element)) {
                        inMemoryHistoryManager.add(fileBackedTaskManager.getSubtasks().get(element));
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
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    @Override
    public void changeEpicStatus(Epic epic) {
        super.changeEpicStatus(epic);
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    @Override
    public void clearTasks() {
        super.clearTasks();
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    @Override
    public void clearEpics() {
        super.clearEpics();
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    @Override
    public void removeCertainTask(int id) {
        super.removeCertainTask(id);
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    @Override
    public void removeCertainEpic(int id) {
        super.removeCertainEpic(id);
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    @Override
    public void removeCertainSubtask(int id) {
        super.removeCertainSubtask(id);
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        try {
            save();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
