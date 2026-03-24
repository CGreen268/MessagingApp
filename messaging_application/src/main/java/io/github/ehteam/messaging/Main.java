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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.java.accessibility.util.TopLevelWindowListener;

public class Main {

    private static ContactLinkedList contacts = new ContactLinkedList();
    private static DefaultListModel<String> contactModel = new DefaultListModel<>();
    private static JList<String> contactList = new JList<>();
    private static JFrame frame = new JFrame("Messaging App");

    // User's own profile
    private static String userProfileName = "You";
    private static String userProfileBio = "Hey there! I'm using MessagingApp.";
    private static String userProfilePhone = "+1 555 123 4567";

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
            refreshContactModel(null);
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

            // New Contact profile page
            ProfilePage[] profilePage = new ProfilePage[1];
            profilePage[0] = new ProfilePage(() -> {
                String name = profilePage[0].getDisplayName();
                if (!name.isEmpty() && contacts.find(name) == null) {
                    contacts.addToTail(name);
                    refreshContactModel(null);
                }
                cardLayout.show(mainArea, "chat");
            }, "New Contact");

            JPanel profilePanel = new JPanel(new BorderLayout());
            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> cardLayout.show(mainArea, "chat"));
            profilePanel.add(backButton, BorderLayout.NORTH);
            profilePanel.add(profilePage[0], BorderLayout.CENTER);

            // User's own profile page
            ProfilePage[] userProfilePage = new ProfilePage[1];
            userProfilePage[0] = new ProfilePage(() -> {
                // Save user's own profile
                userProfileName = userProfilePage[0].getDisplayName();
                userProfileBio = userProfilePage[0].getContactBio();
                userProfilePhone = userProfilePage[0].getContactPhone();
                cardLayout.show(mainArea, "chat");
            }, "Profile");

            JPanel userProfilePanel = new JPanel(new BorderLayout());
            JButton userBackButton = new JButton("Back");
            userBackButton.addActionListener(e -> cardLayout.show(mainArea, "chat"));
            userProfilePanel.add(userBackButton, BorderLayout.NORTH);
            userProfilePanel.add(userProfilePage[0], BorderLayout.CENTER);

// Contact profile page
            ContactProfile[] contactProfilePage = new ContactProfile[1];
            contactProfilePage[0] = new ContactProfile(
                    () -> {
                        // Save button — update the node's details
                        String selected = contactList.getSelectedValue();
                        if (selected != null) {
                            ContactLinkedList.Node node = contacts.find(selected);
                            if (node != null) {
                                String newName = contactProfilePage[0].getContactName();
                                node.name = newName;
                                node.bio = contactProfilePage[0].getContactBio();
                                node.phone = contactProfilePage[0].getContactPhone();
                                refreshContactModel(null);
                                contactList.setSelectedValue(newName, true);
                            }
                        }
                        cardLayout.show(mainArea, "chat");
                    },
                    () -> cardLayout.show(mainArea, "chat") // Cancel button
            );

            JPanel contactProfilePanel = new JPanel(new BorderLayout());
            contactProfilePanel.add(contactProfilePage[0], BorderLayout.CENTER);

            mainArea.add(chatPanel, "chat");
            mainArea.add(profilePanel, "profile");
            mainArea.add(userProfilePanel, "userProfile");
            mainArea.add(contactProfilePanel, "contactProfile");

            frame.add(mainArea, BorderLayout.CENTER);

            JButton newContactBtn = new JButton("New Contact");
            JButton removeContactBtn = new JButton("Remove Contact");
            JButton viewContactBtn = new JButton("View Profile");

            newContactBtn.addActionListener(e -> {
                profilePage[0].clearFields();
                cardLayout.show(mainArea, "profile");
            });

            removeContactBtn.addActionListener(e -> {
                String selected = contactList.getSelectedValue();
                if (selected != null) {
                    contacts.remove(selected);
                    contactModel.removeElement(selected);
                    messageModel.clear();
                }
            });

            viewContactBtn.addActionListener(e -> {
                String selected = contactList.getSelectedValue();
                if (selected != null) {
                    ContactLinkedList.Node node = contacts.find(selected);
                    contactProfilePage[0].loadContact(node);
                    cardLayout.show(mainArea, "contactProfile");
                }
            });

            JButton profileBtn = new JButton("Profile");
            profileBtn.addActionListener(e -> {
                userProfilePage[0].setDisplayName(userProfileName);
                userProfilePage[0].setContactBio(userProfileBio);
                userProfilePage[0].setContactPhone(userProfilePhone);
                cardLayout.show(mainArea, "userProfile");
            });

            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(profileBtn, BorderLayout.EAST);
            JPanel leftButtons = new JPanel();
            leftButtons.add(newContactBtn);
            leftButtons.add(removeContactBtn);
            leftButtons.add(viewContactBtn);
            topPanel.add(leftButtons, BorderLayout.WEST);
            frame.add(topPanel, BorderLayout.NORTH);

            // search bar for contacts
            JTextField searchField = new JTextField();
            topPanel.add(searchField, BorderLayout.CENTER);

            searchField.addActionListener(e -> {
                String text = searchField.getText();
                refreshContactModel(text);
            });

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
                    refreshContactModel(null);
                    contactList.setSelectedValue(currentContact, true);
                }
            });

            input.addActionListener(e -> sendButton.doClick());

            frame.setVisible(true);
        });
    }

    private static void refreshContactModel(String filter) {
        contactModel.clear();
        for (String name : contacts.toArray()) {
            if (filter == null || name.toLowerCase().contains(filter.toLowerCase())) {
                contactModel.addElement(name);
            }
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
