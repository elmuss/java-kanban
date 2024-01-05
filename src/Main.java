public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Task("описание задачи 1")); //1
        taskManager.addTask(new Task("описание задачи 2")); //2
        taskManager.addEpic(new Epic("описание эпика 1")); //3
        taskManager.addSubtask(new Subtask("описание подзадачи 1"), 3); //4
        taskManager.addSubtask(new Subtask("описание подзадачи 2"), 3); //5
        taskManager.addEpic(new Epic("описание эпика 2")); //6
        taskManager.addSubtask(new Subtask("описание подзадачи 1"), 6); //7
        System.out.println("список задач: " + taskManager.getListTask());
        System.out.println("список эпиков: " + taskManager.getListEpic());
        System.out.println("список подзадач: " + taskManager.getListSubtask());
        taskManager.updateTask(2, "новое описание задачи 2", Status.DONE);
        taskManager.updateEpic(3, "новое описание эпика 1");
        taskManager.updateSubtask(4,"новое описание подзадачи 1", Status.DONE);
        taskManager.updateSubtask(5,"новое описание подзадачи 2", Status.DONE);
        System.out.println("список задач: " + taskManager.getListTask());
        System.out.println("список эпиков: " + taskManager.getListEpic());
        System.out.println("список подзадач: " + taskManager.getListSubtask());
        taskManager.removeCertainTask(1);
        taskManager.removeCertainEpic(6);
        System.out.println("список задач: " + taskManager.getListTask());
        System.out.println("список эпиков: " + taskManager.getListEpic());
        System.out.println("список подзадач: " + taskManager.getListSubtask());

    }
}
