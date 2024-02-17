package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Node first;
    private Node last;
    private final Map<Integer, Node> historyHashMap = new HashMap<>();

    public class Node {
        private final Task data;
        private Node next;
        private Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Task getData() {
            return data;
        }
    }

    public void removeNode(Node nodeToRemove) {
        if (nodeToRemove != null) {

            if (nodeToRemove.getPrev() == null && nodeToRemove.getNext() != null) {
                first = nodeToRemove.getNext();
                nodeToRemove.getNext().setPrev(null);
                historyHashMap.remove(nodeToRemove.data.getId());

            } else if (nodeToRemove.getPrev() != null && nodeToRemove.getNext() == null) {
                last = nodeToRemove.getPrev();
                nodeToRemove.getPrev().setNext(null);
                historyHashMap.remove(nodeToRemove.data.getId());

            } else if (nodeToRemove.getPrev() == null && nodeToRemove.getNext() == null) {
                historyHashMap.remove(nodeToRemove.data.getId());

            } else {
                nodeToRemove.getPrev().setNext(nodeToRemove.getNext());
                nodeToRemove.getNext().setPrev(nodeToRemove.getPrev());
                historyHashMap.remove(nodeToRemove.data.getId());
            }
        }
    }

    public void linkLast(Task task) {

        if (historyHashMap.isEmpty()) {
            Node newNode = new Node(null, task, null);
            historyHashMap.put(task.getId(), newNode);
            first = newNode;
            last = newNode;

        } else if (historyHashMap.containsKey(task.getId())) {
            removeNode(historyHashMap.get(task.getId()));
            Node newNode = new Node(last, task, null);
            historyHashMap.put(task.getId(), newNode);
            last.setNext(newNode);
            last = newNode;

        } else {
            Node newNode = new Node(last, task, null);
            historyHashMap.put(task.getId(), newNode);
            last.setNext(newNode);
            last = newNode;
        }
    }

    ArrayList<Task> getTasks() {
        ArrayList<Task> history = new ArrayList<>();
        history.add(first.data);

        for (int i = 0; i < historyHashMap.size() - 1; i++) {
            history.add(historyHashMap.get(history.get(i).getId()).getNext().getData());
        }
        return history;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(historyHashMap.get(id));
    }
}
