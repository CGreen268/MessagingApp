package io.github.ehteam.messaging;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class Main {

    private static ContactLinkedList contacts = new ContactLinkedList();
    private static DefaultListModel<String> contactModel = new DefaultListModel<>();
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

            JPanel chatPanel = new JPanel(new BorderLayout());

            contactList.setModel(contactModel);
            refreshContactModel();
            frame.add(new JScrollPane(contactList), BorderLayout.WEST);

            JList<String> chats = new JList<>();
            chats.setModel(messageModel);
            chatPanel.add(new JScrollPane(chats), BorderLayout.CENTER);

            JTextField input = new JTextField();
            JButton sendButton = new JButton("Send");

            JPanel inputPanel = new JPanel(new BorderLayout());
            inputPanel.add(input, BorderLayout.CENTER);
            inputPanel.add(sendButton, BorderLayout.EAST);

            chatPanel.add(inputPanel, BorderLayout.SOUTH);

            ProfilePage[] profilePage = new ProfilePage[1];
            profilePage[0] = new ProfilePage(() -> {
                String name = profilePage[0].getDisplayName();
                if (!name.isEmpty() && contacts.find(name) == null) {
                    contacts.addToTail(name);
                    refreshContactModel();
                }
                cardLayout.show(mainArea, "chat");
            });

            mainArea.add(chatPanel, "chat");
            mainArea.add(profilePage[0], "profile");

            frame.add(mainArea, BorderLayout.CENTER);

            JButton newContactBtn = new JButton("New Contact");
            newContactBtn.addActionListener(e -> {
                profilePage[0].clearFields();
                cardLayout.show(mainArea, "profile");
            });

            JButton profileBtn = new JButton("Profile");
            profileBtn.addActionListener(e -> {
                if (profileBtn.getText().equals("Profile")) {
                    cardLayout.show(mainArea, "profile");
                    profileBtn.setText("Profile");
                } else {
                    cardLayout.show(mainArea, "chat");
                    profileBtn.setText("Back");
                }
            });

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(profileBtn, BorderLayout.EAST);
            topPanel.add(newContactBtn, BorderLayout.WEST);
            frame.add(topPanel, BorderLayout.NORTH);

            contactList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        String currentContact = contactList.getSelectedValue();
                        messageModel.clear();
                        if (currentContact != null) {
                            ContactLinkedList.Node node = contacts.find(currentContact);
                            for (Object obj : node.messages) {
                                Message msg = (Message) obj;
                                messageModel.addElement(msg.sender() + ": " + msg.text());
                            }
                        }
                    }
                }
            });

            sendButton.addActionListener(e -> {
                String currentContact = contactList.getSelectedValue();
                String text = input.getText().trim();
                if (currentContact != null && !text.isEmpty()) {
                    Message msg = new Message("You", text);
                    contacts.find(currentContact).messages.add(msg);
                    messageModel.addElement(msg.sender() + ": " + msg.text());
                    input.setText("");
                    contacts.moveToHead(currentContact);
                    refreshContactModel();
                    contactList.setSelectedValue(currentContact, true);
                }
            });

            frame.setVisible(true);
        });
    }

    private static void refreshContactModel() {
        contactModel.clear();
        for (String name : contacts.toArray()) {
            contactModel.addElement(name);
        }
    }

    private static void initializeConversations() {
        contacts.addToTail("Alice Johnson");
        contacts.addToTail("Bob Smith");
        contacts.addToTail("Jeffrey Lee");

        contacts.find("Alice Johnson").messages.add(new Message("Alice Johnson", "Hey! How are you?"));
        contacts.find("Bob Smith").messages.add(new Message("Bob Smith", "Hey! How are you?"));
        contacts.find("Jeffrey Lee").messages.add(new Message("Jeffrey Lee", "Hey! How are you?"));
    }
}