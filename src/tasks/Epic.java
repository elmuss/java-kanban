package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Epic extends Task {
    private ArrayList<Subtask> subtasksOfEpic;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasksOfEpic = new ArrayList<>();
        this.type = TaskType.EPIC;
    }

    public Epic(String name, String description, int id, Status status) {
        super(name, description);
        this.id = id;
        this.status = status;
        this.subtasksOfEpic = new ArrayList<>();
        this.type = TaskType.EPIC;
    }

    public List<Subtask> getSubtasksOfEpic() {
        return subtasksOfEpic;
    }

    public void addSubtasksOfEpic(Subtask subtask) {
        subtasksOfEpic.add(subtask);

        if (this.startTime == null) {
            this.startTime = subtask.getStartTime();
            this.duration = subtask.getDuration();
            endTime = getEndTime();
        } else {
            this.duration = this.duration.plus(subtask.getDuration());
            endTime = getEndTime();
        }
    }

    public LocalDateTime getEndTime() {

        for (Subtask subtask : subtasksOfEpic) {
            if (endTime == null) {
                endTime = subtask.getEndTime();
            } else if (subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
        }
        return endTime;
    }
    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", type=" + getType() +
                ", listOfSubtasks=" + getSubtasksOfEpic() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                '}';
    }
}