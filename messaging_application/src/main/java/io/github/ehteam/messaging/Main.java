package io.github.ehteam.messaging;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Main {

    private static Map<String, List<Message>> conversations = new LinkedHashMap<>();
    private static JList<String> contactList = new JList<>();
    private static JFrame frame = new JFrame("Messaging App");

    public static void main(String[] args) {
        initializeConversations();

        DefaultListModel<String> messageModel = new DefaultListModel<>();
        

        JPanel messagePanel = new JPanel(new BorderLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Contact list on the left
        contactList.setListData(conversations.keySet().toArray(new String[0]));
        frame.add(contactList, BorderLayout.WEST);

        // Message list in the center
        JList<String> chats = new JList<>();
        chats.setModel(messageModel);
        frame.add(chats, BorderLayout.CENTER);

        // Input field and send button at the bottom
        JTextField input = new JTextField();
        JButton sendButton = new JButton("Send");

        messagePanel.add(new JSeparator(SwingConstants.VERTICAL));

        frame.add(input, BorderLayout.SOUTH);
        frame.add(sendButton, BorderLayout.EAST);
        frame.add(sendButton, BorderLayout.EAST);

        

        // Selection listener to load messages for selected contact
        contactList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String currentContact = contactList.getSelectedValue();
                    messageModel.clear();
                    if (currentContact != null) {
                        List<Message> messages = conversations.get(currentContact);
                        for (Message msg : messages) {
                            messageModel.addElement(msg.sender() + ": " + msg.text());
                        }
                    }
                }
            }
        });

        // Send button listener
        sendButton.addActionListener(e -> {
            String currentContact = contactList.getSelectedValue();
            String text = input.getText().trim();
            if (currentContact != null && !text.isEmpty()) {
                Message msg = new Message("You", text);
                conversations.get(currentContact).add(msg);
                messageModel.addElement(msg.sender() + ": " + msg.text());
                input.setText("");
            }
        });

        frame.setVisible(true);
    }

    private static void initializeConversations() {
        conversations.put("Alice Johnson", new ArrayList<>());
        conversations.put("Bob Smith", new ArrayList<>());
        conversations.put("Jeffrey Lee", new ArrayList<>());

        conversations.get("Alice Johnson").add(new Message("Alice Johnson", "Hey! How are you?"));
        conversations.get("Bob Smith").add(new Message("Bob Smith", "Hey! How are you?"));
        conversations.get("Jeffrey Lee").add(new Message("Jeffrey Lee", "Hey! How are you?"));
    }

    private static class Message {
        private String sender;
        private String content;

        public Message(String sender, String content) {
            this.sender = sender;
            this.content = content;
        }

        public String sender() {
            return sender;
        }

        public String text() {
            return content;
        }
    }
}