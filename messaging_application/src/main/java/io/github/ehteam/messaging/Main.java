package io.github.ehteam.messaging;


import javax.swing.JFrame;
import javax.swing.JList;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Messaging App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);
        JList<String> chats = new JList<>();
    }
}