package Record;

import java.util.*;

class Node<K, V> {
    K key;
    V value;
    Node<K, V> prev;
    Node<K, V> next;

    Node(K key, V value) {
        this.key = key;
        this.value = value;
        this.prev = null ;
        this.next = null;
    }
}

class LRU<K, V> {
    private int threshold = 4;
    private int count = 0;
    private final Map<K, Node<K, V>> keyToNodeMapping = new HashMap<>();
    private Node<K, V> head;

    LRU(int threshold) {
        if(threshold == 0) throw new IllegalArgumentException("Invalid threshold value: 0");

        this.threshold = threshold;
    }

    public void put(K key, V value) {
        if(count == 0) {
            count++;

            Node<K, V> node = new Node<>(key, value);
            head = node;
            keyToNodeMapping.put(key, head);
        }
        else {
            boolean isExistingNode = keyToNodeMapping.containsKey(key);

            if(isExistingNode) {
                Node<K, V> existingNode = keyToNodeMapping.get(key);
                existingNode.value = value;

                Node<K, V> prevNode = existingNode.prev;
                prevNode.next = null;

                head.prev = existingNode;
                existingNode.next = head;

                head = existingNode;
            } else {
                count++;

                if(count > threshold) throw new IllegalArgumentException("Exceeded Max Limit");

                Node<K, V> newNode = new Node<>(key, value);

                head.prev = newNode;
                newNode.next = head;

                head = newNode;
                keyToNodeMapping.put(key, newNode);
            }
        }
    }

    public void display() {
        Node<K, V> currNode = head;
        int iteration = count;

        while(iteration > 0) {
            System.out.println(currNode.key + "-" + currNode.value);
            currNode = currNode.next;
            iteration--;
        }

        System.out.println("Map: " + keyToNodeMapping);
    }

    public V get(K key) {
        boolean isExistingNode = keyToNodeMapping.containsKey(key);

        if(!isExistingNode) throw new RuntimeException("Key doesn't exist: " + key);

        return keyToNodeMapping.get(key).value;
    }
}

public class LRUDemo {
    public static void main(String[] args) {
        LRU<Integer, Integer> x = new LRU(4);
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

        x.display();

        System.out.println("--------------------");

        LRU<String, Integer> y = new LRU(4);
        y.put("9", 1);
        y.put("6", 1);
        y.put("5", 1);
        y.put("3", 1);

        y.display();

        y.put("9", 2);
        y.put("6", 2);

        y.display();

        int value3 = y.get("3");
        System.out.println(value3);

        int value4 = y.get("9");
        System.out.println(value4);
    }
}
