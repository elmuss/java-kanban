package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(String name, String description, int id, Status status, int epicId,
                   LocalDateTime startTime, Duration duration) {
        super(name, description);
        this.id = id;
        this.status = status;
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
