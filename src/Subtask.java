public class Subtask extends Task {
    private int epicId;
    public Subtask(String description) {
        super(description);
        setName("подзадача");
    }
    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
