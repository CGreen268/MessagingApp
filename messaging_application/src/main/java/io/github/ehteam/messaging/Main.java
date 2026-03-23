package io.github.ehteam.messaging;

import java.awt.BorderLayout;

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

    private static ContactLinkedList contacts = new ContactLinkedList();
    private static DefaultListModel<String> contactModel = new DefaultListModel<>();
    private static JList<String> contactList = new JList<>();
    private static JFrame frame = new JFrame("Messaging App");

    public static void main(String[] args) {
        initializeConversations();

        DefaultListModel<String> messageModel = new DefaultListModel<>();

        JPanel messagePanel = new JPanel(new BorderLayout());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        contactList.setModel(contactModel);
        refreshContactModel();
        frame.add(contactList, BorderLayout.WEST);

        JList<String> chats = new JList<>();
        chats.setModel(messageModel);
        frame.add(chats, BorderLayout.CENTER);

        JTextField input = new JTextField();
        JButton sendButton = new JButton("Send");

        messagePanel.add(new JSeparator(SwingConstants.VERTICAL));

        frame.add(input, BorderLayout.SOUTH);
        frame.add(sendButton, BorderLayout.EAST);
        frame.add(sendButton, BorderLayout.EAST);

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