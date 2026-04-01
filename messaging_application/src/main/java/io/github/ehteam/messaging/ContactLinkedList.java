package io.github.ehteam.messaging;

import java.util.ArrayList;
import java.util.List;

public class ContactLinkedList {

    public static class Node {
    public String name;
    public String bio;
    public String phone;
    public List<Object> messages;
    public Node next;

    // Constructor for a new contact with just a name
    public Node(String name) {
        this.name = name;
        this.bio = "";
        this.phone = "";
        this.messages = new ArrayList<>();
    }
}

    private Node head;
    private int size;

    // Adds a new contact to the end of the list
    public void addToTail(String name) {
        Node newNode = new Node(name);
        if (head == null) {
            head = newNode;
        } else {
            Node cur = head;
            while (cur.next != null) {
                cur = cur.next;
            }
            cur.next = newNode;
        }
        size++;
    }

    // Moves the contact given to the head of the list
    public void moveToHead(String name) {
        if (head == null || head.name.equals(name)) {
            return;
        }

        Node prev = null;
        Node cur = head;

        while (cur != null && !cur.name.equals(name)) {
            prev = cur;
            cur = cur.next;
        }

        if (cur == null) {
            return;
        }

        prev.next = cur.next;
        cur.next = head;
        head = cur;
    }

    // Finds and returns the node with the given name
    public Node find(String name) {
        Node cur = head;
        while (cur != null) {
            if (cur.name.equals(name)) {
                return cur;
            }
            cur = cur.next;
        }
        return null;
    }

    // Removes the contact with the given name
    public void remove(String name) {
        if (head == null) {
            return;
        }

        if (head.name.equals(name)) {
            head = head.next;
            size--;
            return;
        }

        Node prev = null;
        Node cur = head;
        while (cur != null && !cur.name.equals(name)) {
            prev = cur;
            cur = cur.next;
        }

        if (cur != null) {
            prev.next = cur.next;
            size--;
        }
    }

    // Gets the head of the linked list
    public Node getHead() {
        return head;
    }

    // Returns the number of contacts in the list
    public int size() {
        return size;
    }

    // Converts the linked list of contacts to an array of contact names
    public String[] toArray() {
        String[] result = new String[size];
        Node cur = head;
        for (int i = 0; i < size; i++) {
            result[i] = cur.name;
            cur = cur.next;
        }
        return result;
    }
}
