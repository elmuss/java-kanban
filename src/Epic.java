import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private ArrayList<Subtask> subtasksOfEpic;

    public Epic(String description) {
        super(description);
        this.subtasksOfEpic = new ArrayList<>();
        setName("эпик");
    }
    public List<Subtask> getSubtasksOfEpic() {
        return subtasksOfEpic;
    }
    public void addSubtasksOfEpic(Subtask subtask) {
            subtasksOfEpic.add(subtask);
    }
}
