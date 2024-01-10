import java.util.ArrayList;

public class Subtask extends Task {
    private int epicId;
    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }
    public Subtask(String name, String description, int id, Status status, int epicId) {
        super(name, description);
        this.id = id;
        this.status = status;
        this.epicId = epicId;
    }
    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
