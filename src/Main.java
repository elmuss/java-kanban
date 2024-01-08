public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Task("задача 1", "описание задачи 1")); //1
        taskManager.addTask(new Task("задача 2","описание задачи 2")); //2
        taskManager.addEpic(new Epic("эпик 1", "описание эпика 1")); //3
        taskManager.addSubtask(new Subtask("подзадача 1", "описание подзадачи 1", 3));//4
        taskManager.addSubtask(new Subtask("подзадача 2","описание подзадачи 2", 3)); //5
        taskManager.addEpic(new Epic("эпик 2", "описание эпика 2")); //6
        taskManager.addSubtask(new Subtask("подзадача 3","описание подзадачи 1", 6)); //7
        System.out.println("список задач: " + taskManager.getListTask());
        System.out.println("список эпиков: " + taskManager.getListEpic());
        System.out.println("список подзадач: " + taskManager.getListSubtask());
        taskManager.updateTask(new Task("задача 2","описание задачи 2", 2, Status.DONE));
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
