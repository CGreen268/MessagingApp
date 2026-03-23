package io.github.ehteam.messaging;

import java.util.ArrayList;
import java.util.List;

public class ContactLinkedList {

    public static class Node {
        public String name;
        public List<Object> messages;
        public Node next;

        public Node(String name) {
            this.name = name;
            this.messages = new ArrayList<>();
        }
    }

    private Node head;
    private int size;

    public void addToTail(String name) {
        Node newNode = new Node(name);
        if (head == null) {
            head = newNode;
        } else {
            Node cur = head;
            while (cur.next != null) cur = cur.next;
            cur.next = newNode;
        }
        size++;
    }

    
    public Node find(String name) {
        Node cur = head;
        while (cur != null) {
            if (cur.name.equals(name)) return cur;
            cur = cur.next;
        }
        return null;
    }

    public Node getHead() { return head; }

    public int size() { return size; }

}