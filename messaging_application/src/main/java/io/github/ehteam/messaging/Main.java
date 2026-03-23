package io.github.ehteam.messaging;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Main {

    private static Map<String, List<Message>> conversations = new LinkedHashMap<>();
    private static JList<String> contactList = new JList<>();
    private static JFrame frame = new JFrame("Messaging App");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            initializeConversations();

            DefaultListModel<String> messageModel = new DefaultListModel<>();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());

            CardLayout cardLayout = new CardLayout();
            JPanel mainArea = new JPanel(cardLayout);

            // Chat panel
            JPanel chatPanel = new JPanel(new BorderLayout());

            contactList.setListData(conversations.keySet().toArray(new String[0]));
            frame.add(new JScrollPane(contactList), BorderLayout.WEST);

            JList<String> chats = new JList<>(messageModel);
            chatPanel.add(new JScrollPane(chats), BorderLayout.CENTER);

            JTextField input = new JTextField();
            JButton sendButton = new JButton("Send");

            JPanel inputPanel = new JPanel(new BorderLayout());
            inputPanel.add(input, BorderLayout.CENTER);
            inputPanel.add(sendButton, BorderLayout.EAST);

            chatPanel.add(inputPanel, BorderLayout.SOUTH);

            // Profile page
            ProfilePage profilePage = new ProfilePage();

            mainArea.add(chatPanel, "chat");
            mainArea.add(profilePage, "profile");

            frame.add(mainArea, BorderLayout.CENTER);

            JButton profileBtn = new JButton("Profile");
            profileBtn.addActionListener(e -> {
                if (profileBtn.getText().equals("Profile")) {
                    cardLayout.show(mainArea, "profile");
                    profileBtn.setText("Back");
                } else {
                    cardLayout.show(mainArea, "chat");
                    profileBtn.setText("Profile");
                }
            });

            frame.add(profileBtn, BorderLayout.NORTH);

            // List selection
            contactList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    String currentContact = contactList.getSelectedValue();
                    messageModel.clear();
                    if (currentContact != null) {
                        for (Message msg : conversations.get(currentContact)) {
                            messageModel.addElement(msg.sender() + ": " + msg.text());
                        }
                    }
                }
            });

            // Send button
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
        });
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