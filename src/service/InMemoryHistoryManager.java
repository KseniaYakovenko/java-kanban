package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task item;
        Node next;
        Node prev;

        Node(Node prev, Task element, Node next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    HashMap<Integer, Node> historyMap = new HashMap<>();
    Node first;
    Node last;

    @Override
    public void add(Task task) {

        int taskId = task.getId();
        if (historyMap.get(taskId) != null) {
            removeFromHistory(taskId);
        }
        Node oldLast = last;
        Node newNode = new Node(oldLast, task, null);
        last = newNode;
        if (oldLast == null)
            first = newNode;
        else
            oldLast.next = newNode;
        historyMap.put(taskId, newNode);
    }

    @Override
    public void removeFromHistory(int id) {
        Node nodeForDelete = historyMap.get(id);
        if (nodeForDelete == null) return;
        Node next = nodeForDelete.next;
        Node prev = nodeForDelete.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            nodeForDelete.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            nodeForDelete.next = null;
        }
        nodeForDelete.item = null;
        historyMap.remove(id);
    }

    @Override
    public void removeAll() {
        historyMap.clear();
        first = null;
        last = null;
    }


    @Override
    public List<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        Node currentNode = first;
        while (currentNode != null) {
            Task task = currentNode.item;
            history.add(task);
            currentNode = currentNode.next;
        }
        return history;
    }
}
