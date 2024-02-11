package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    Node<Task> first;
    Node<Task> last;
    private final List<Node> historyList = new LinkedList<>();
    private final HashMap<Integer, Node> historyHashMap = new HashMap<>();

    class Node<Task> {
        public Task data;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(Node<Task> prev, Task data, Node<Task> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        public Node<Task> getNext() {
            return next;
        }

        public void setNext(Node<Task> next) {
            this.next = next;
        }

        public Node<Task> getPrev() {
            return prev;
        }

        public void setPrev(Node<Task> prev) {
            this.prev = prev;
        }

        public Task getData() {
            return data;
        }
    }
    public void removeNode(Node<Task> nodeToRemove) {

        if (historyHashMap.size() == 1) {
            historyHashMap.remove(nodeToRemove.data.getId());
            historyList.remove(nodeToRemove);

        } else {
            Node<Task> previousToChange = historyHashMap.get(nodeToRemove.getPrev());
            Node<Task> nextToChange = historyHashMap.get(nodeToRemove.getNext());
            if (previousToChange != null) {
                previousToChange.setNext(nextToChange);
            }
            if (nextToChange != null) {
                nextToChange.setPrev(previousToChange);
            }
            historyHashMap.remove(nodeToRemove.data.getId());
            historyList.remove(nodeToRemove);
        }
    }

    @Override
    public void linkLast(Task task) {
        if (task != null) {

            if (historyHashMap.isEmpty()) {
                Node<Task> newNode = new Node(null, task, null);
                historyList.add(newNode);
                historyHashMap.put(task.getId(), newNode);
                first = newNode;
                last = newNode;

            } else if (historyHashMap.containsKey(task.getId())) {
                removeNode(historyHashMap.get(task.getId()));
                Node<Task> newNode = new Node(last, task, null);
                historyList.add(newNode);
                historyHashMap.put(task.getId(), newNode);
                last = newNode;

            } else {
                Node<Task> newNode = new Node(last, task, null);
                historyList.add(newNode);
                historyHashMap.put(task.getId(), newNode);
                last = newNode;
            }
        }
    }
    @Override
    public List<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();

        for (Node<Task> task : historyList) {
            history.add(task.getData());
        }

        return history;
    }

    @Override
    public void remove(int id) {
        if (historyHashMap.containsKey(id)) {
            removeNode(historyHashMap.get(id));
        }
    }
}
