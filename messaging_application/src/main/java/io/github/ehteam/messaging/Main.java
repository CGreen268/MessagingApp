package io.github.ehteam.messaging;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JList;

public class Main {

    public static void main(String[] args) {
        record Message(String sender, String text) {}
        Map<String, List<Message>> conversations = new LinkedHashMap<>();

        conversations
                .computeIfAbsent("Alice Johnson", k -> new ArrayList<>())
                .add(new Message("me", "Hey Alice!"));

        JFrame frame = new JFrame("Messaging App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);

        JList<String> chats = new JList<>();
        chats.setListData(conversations.keySet().toArray(new String[0]));
        frame.add(chats);
    }
}
