package LinkedList.Concepts.Singly;

import java.util.HashMap;

class Solution {
    public Node addOneToLL(Node head) {
        int carry = (head.next == null) ? 1 : call(head.next);
        int sum = head.data + carry;
        if(sum == 10) {
            head.data = 0;
            Node newNode = new Node(carry);
            newNode.next = head;
            head = newNode;
        }
        else {
            head.data = sum;
        }

        return head;
    }

    public int call(Node currNode) {
        int carry = 1, sum;

        if(currNode.next == null) {
            sum = currNode.data + carry;
            if(sum == 10) {
                currNode.data = 0;
                return carry;
            }
            else {
                currNode.data = sum;
                return 0;
            }
        }

        carry = call(currNode.next);
        sum = currNode.data + carry;
        if(sum == 10) {
            currNode.data = 0;
            return carry;
        }
        else {
            currNode.data = sum;
            return 0;
        }
    }
}

public class Add1ToLinkedList {
    static void main() {
        LinkedList linkedList = new LinkedList();

//        linkedList.insertNewNodeAtBeginning(9);
//        linkedList.insertNewNodeAtBeginning(0);
//        linkedList.insertNewNodeAtBeginning(0);
//        linkedList.insertNewNodeAtBeginning(1);

//        linkedList.insertNewNodeAtBeginning(9);
//        linkedList.insertNewNodeAtBeginning(9);
//        linkedList.insertNewNodeAtBeginning(9);
//        linkedList.insertNewNodeAtBeginning(9);

        linkedList.insertNewNodeAtBeginning(9);

        Solution obj = new Solution();

        Node node = obj.addOneToLL(linkedList.getHeadNode());
        linkedList.displayLinkedList(node);
    }
}
