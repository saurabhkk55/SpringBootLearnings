package LinkedList.Concepts.Singly;

import java.util.Objects;

class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}

class LinkedList {
    private Node head;
    private Node tail;
    private int linkedListLength = 0;

    public LinkedList() {
        this.head = null;
    }

    public void insertNewNodeAtBeginning(int data) {
        Node newNode = new Node(data);
        if (linkedListLength != 0) newNode.next = head;
        head = newNode;
        linkedListLength++;
        if (linkedListLength == 1) tail = head;
    }

    public void insertNewNodeAtEnd(int data) {
        Node newNode = new Node(data);

        tail.next = newNode;
        tail = newNode;

        linkedListLength++;
    }

    public void insertNewNodeInMiddle(int data, int position) {
        if (position < 0 || position > linkedListLength) {
            throw new RuntimeException("Invalid position, new node can be inserted from range: 0 to " + (linkedListLength));
        }
        else if (position == 0) {
            insertNewNodeAtBeginning(data);
        } else if (position == linkedListLength) {
            insertNewNodeAtEnd(data);
        } else {
            Node newNode = new Node(data);

            Node currNode = head;
            boolean isCurrNodeNull = Objects.isNull(currNode);

            while (!isCurrNodeNull && !Objects.isNull(currNode.next) && (position - 1) > 0) {
                currNode = currNode.next;
                position--;
            }

            Node currNodeNext = currNode.next;
            currNode.next = newNode;
            newNode.next = currNodeNext;

            linkedListLength++;
        }
    }

    public void deleteNodeFromBeginning() {
        if (linkedListLength > 0) {
            Node nextNode = head.next;
            head.next = null;
            head = nextNode;

            linkedListLength--;
        }

        if (linkedListLength == 0) {
            setHeadAndTailNodeToNull();
        }
    }

    public void deleteNodeFromEnd() {
        if (linkedListLength > 0) {
            Node currNode = head;
            Node prevNode = head;
            boolean isCurrNodeNull = Objects.isNull(currNode);

            while (!isCurrNodeNull && !Objects.isNull(currNode.next)) {
                prevNode = currNode;
                currNode = currNode.next;
            }

            prevNode.next = null;
            tail = prevNode;

            linkedListLength--;
        }

        if (linkedListLength == 0) {
            setHeadAndTailNodeToNull();
        }
    }

    public void deleteFromMiddle(int position) {
        if (position < 0 || position >= linkedListLength) {
            throw new RuntimeException("Invalid position, new node can be removed from range: 0 to " + (linkedListLength));
        } else if (position == 0) {
            deleteNodeFromBeginning();
        } else if (position == linkedListLength - 1) {
            deleteNodeFromEnd();
        } else {
            Node currNode = head;
            Node prevNode = head;
            boolean isCurrNodeNull = Objects.isNull(currNode);
            int nodeCounter = 1;

            while (!isCurrNodeNull && !Objects.isNull(currNode.next)) {
                prevNode = currNode;
                currNode = currNode.next;

                if (nodeCounter == position) break;

                nodeCounter++;
            }

            prevNode.next = currNode.next;
            currNode.next = null;

            linkedListLength--;
        }
    }

    public void searchNodeHavingData(int data) {
        if (linkedListLength > 0) {
            Node currNode = head;
            boolean isCurrNodeNull = Objects.isNull(currNode);
            int position = 0;
            boolean isNodeFound = false;

            while (!isCurrNodeNull && !Objects.isNull(currNode.next)) {
                if (currNode.data == data) {
                    isNodeFound = true;
                    System.out.println("Found node: "+currNode+"["+currNode.data+" | "+currNode.next+"] at position/index: " + position);
                }

                currNode = currNode.next;
                position++;
            }

            if (!isNodeFound && currNode.data == data) {
                isNodeFound = true;
                System.out.println("Found node: "+currNode+"["+currNode.data+" | "+currNode.next+"] at position/index: " + position);
            }

            if (!isNodeFound) {
                System.out.println("No such node exist with data: " + data);
            }
        }
    }

    public void reverseLinkedist() {
        if (linkedListLength > 1) {
            Node nextNode = null, prevNode = null, currNode = head;

            while (currNode != null) {
                nextNode = currNode.next;
                currNode.next = prevNode;
                prevNode = currNode;
                currNode = nextNode;
            }

            tail = head;
            head = prevNode;
        }
    }

    public void displayLinkedList() {
        Node currNode = head;
        boolean isCurrNodeNull = Objects.isNull(currNode);

        while(!isCurrNodeNull && currNode.next != null) {
            System.out.println(currNode + "[" + currNode.data + " | " + currNode.next + "] -> ");
            currNode = currNode.next;
        }

        if(!isCurrNodeNull) System.out.println(currNode + "[" + currNode.data + " | " + currNode.next + "]");
    }

    public void displayLinkedList(Node head) {
        Node currNode = head;
        boolean isCurrNodeNull = Objects.isNull(currNode);

        while(!isCurrNodeNull && currNode.next != null) {
            System.out.println(currNode + "[" + currNode.data + " | " + currNode.next + "] -> ");
            currNode = currNode.next;
        }

        if(!isCurrNodeNull) System.out.println(currNode + "[" + currNode.data + " | " + currNode.next + "]");
    }

    public int getLinkedListLength() {
        return linkedListLength;
    }

    public Node getHeadNode() {
        if (!Objects.isNull(head)) System.out.println("Head Node: " + head + "[" + head.data + " | " + head.next + "]");
        else System.out.println("Head Node: " + head);

        return head;
    }

    public void getTailNode() {
        if (!Objects.isNull(tail)) System.out.println("Tail Node: " + tail + "[" + tail.data + " | " + tail.next + "]");
        else System.out.println("Tail Node: " + tail);
    }

    public void setHeadAndTailNodeToNull() {
        head = null;
        tail = null;
    }
}

public class AddNode {
    static void main() {
        LinkedList linkedList = new LinkedList();

        linkedList.insertNewNodeAtBeginning(5);
        linkedList.insertNewNodeAtBeginning(4);
        linkedList.insertNewNodeAtBeginning(3);

        linkedList.insertNewNodeAtEnd(6);
        linkedList.insertNewNodeAtEnd(7);

        linkedList.insertNewNodeAtBeginning(2);

        linkedList.insertNewNodeInMiddle(0, 2);
        linkedList.insertNewNodeInMiddle(8, 7);

        System.out.println("linkedListLength: " + linkedList.getLinkedListLength());
        linkedList.displayLinkedList();

        linkedList.deleteNodeFromBeginning();
        linkedList.deleteNodeFromEnd();
        linkedList.deleteFromMiddle(5);

        linkedList.searchNodeHavingData(2);
        linkedList.searchNodeHavingData(5);
        linkedList.searchNodeHavingData(8);
        linkedList.searchNodeHavingData(70);

        linkedList.reverseLinkedist();

        System.out.println("linkedListLength: " + linkedList.getLinkedListLength());
        linkedList.displayLinkedList();

        linkedList.getHeadNode();
        linkedList.getTailNode();
    }
}
