import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private ArrayList<Subtask> subtasksOfEpic;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasksOfEpic = new ArrayList<>();
    }
    public Epic(String name, String description, int id, Status status) {
        super(name, description);
        this.id = id;
        this.status = status;
        this.subtasksOfEpic = new ArrayList<>();
    }
    public List<Subtask> getSubtasksOfEpic() {
        return subtasksOfEpic;
    }
    public void addSubtasksOfEpic(Subtask subtask) {
            subtasksOfEpic.add(subtask);
    }
}
