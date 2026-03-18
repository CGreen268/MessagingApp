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

        // populate conversations
        if (conversations.containsKey("Alice Johnson")) {
            conversations.get("Alice Johnson").add(new Message("Alice Johnson", "Hey! How are you?"));
        } else {
            conversations.put("Alice Johnson", new ArrayList<>());
            conversations.get("Alice Johnson").add(new Message("Alice Johnson", "Hey! How are you?"));
        }

        if (conversations.containsKey("Bob Smith")) {
            conversations.get("Bob Smith").add(new Message("Bob Smith", "Hey! How are you?"));
        } else {
            conversations.put("Bob Smith", new ArrayList<>());
            conversations.get("Bob Smith").add(new Message("Bob Smith", "Hey! How are you?"));
        }

        if (conversations.containsKey("Jeffrey Lee")) {
            conversations.get("Jeffrey Lee").add(new Message("Jeffrey Lee", "Hey! How are you?"));
        } else {
            conversations.put("Jeffrey Lee", new ArrayList<>());
            conversations.get("Jeffrey Lee").add(new Message("Jeffrey Lee", "Hey! How are you?"));
        }



        JFrame frame = new JFrame("Messaging App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);

        JList<String> chats = new JList<>();
        chats.setListData(conversations.keySet().toArray(new String[0]));
        frame.add(chats);
    }
}
