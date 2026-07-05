package Record;

import java.util.*;

class Node {
    int key;
    int value;
    Node prev;
    Node next;

    Node() {}

    Node(int key, int value) {
        this.key = key;
        this.value = value;
        this.prev = null ;
        this.next = null;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}

class LRU {
    private int threshold = 4;
    private int count = 0;
    private final Map<Integer, Node> keyToNodeMapping = new HashMap<>();
    private Node head;

    LRU(int threshold) {
        if(threshold == 0) throw new IllegalArgumentException("Invalid threshold value: 0");

        this.threshold = threshold;
    }

    public void put(int key, int value) {
        if(count == 0) {
            count++;

            Node node = new Node(key, value);
            head = node;
            keyToNodeMapping.put(key, head);
        }
        else {
            boolean isExistingNode = keyToNodeMapping.containsKey(key);

            if(isExistingNode) {
                Node existingNode = keyToNodeMapping.get(key);
                existingNode.value = value;

                Node prevNode = existingNode.prev;
                prevNode.next = null;

                head.prev = existingNode;
                existingNode.next = head;

                head = existingNode;
            } else {
                count++;

                if(count > threshold) throw new IllegalArgumentException("Exceeded Max Limit");

                Node newNode = new Node(key, value);

                head.prev = newNode;
                newNode.next = head;

                head = newNode;
                keyToNodeMapping.put(key, newNode);
            }
        }
    }

    public void display() {
        Node currNode = head;
        int iteration = count;

        while(iteration > 0) {
            System.out.println(currNode.key + "-" + currNode.value);
            currNode = currNode.next;
            iteration--;
        }

        System.out.println("Map: " + keyToNodeMapping);
    }

    public int get(int key) {
        boolean isExistingNode = keyToNodeMapping.containsKey(key);

        if(!isExistingNode) throw new RuntimeException("Key doesn't exist: " + key);

        return keyToNodeMapping.get(key).value;
    }
}

public class LRUDemo {
    public static void main(String[] args) {
        LRU x = new LRU(4);
        x.put(9, 1);
        x.put(6, 1);
        x.put(5, 1);
        x.put(3, 1);

        x.display();

        x.put(9, 2);
        x.put(6, 2);

        x.display();

        int value1 = x.get(3);
        System.out.println(value1);

        int value2 = x.get(9);
        System.out.println(value2);
    }
}
