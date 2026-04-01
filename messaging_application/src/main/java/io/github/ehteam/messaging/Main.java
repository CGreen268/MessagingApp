package io.github.ehteam.messaging;

// import statements
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Renderer so long chat text wraps correctly in the message list.
class WrappingListCellRenderer extends DefaultListCellRenderer {
    private JTextArea textArea;

    public WrappingListCellRenderer() {
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        String text = (value == null) ? "" : value.toString();
        textArea.setText(text);
        textArea.setSize(list.getWidth(), Integer.MAX_VALUE);
        textArea.setPreferredSize(null);
        int height = textArea.getPreferredSize().height;
        if (height < 20) {
            height = 20;
        }

        textArea.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        textArea.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

        return textArea;
    }
}

public class Main {

    // Initialise UI frame and contact/message data structures
    private static ContactLinkedList contacts = new ContactLinkedList();
    private static DefaultListModel<String> contactModel = new DefaultListModel<>();
    private static JList<String> contactList = new JList<>();
    private static JFrame frame = new JFrame("Messaging App");

    // User's own profile
    private static String userProfileName = "You";
    private static String userProfileBio = "Hey there! I'm using MessagingApp.";
    private static String userProfilePhone = "+1 555 123 4567";

    public static void main(String[] args) {
        // Build and show the Swing UI
        SwingUtilities.invokeLater(() -> {
            // Load saved app data, and load hardcoded data if no saved data found
            DataManager.AppData saved = DataManager.loadAll();
            if (saved != null) {
                contacts = saved.contacts;
                userProfileName = saved.userName;
                userProfileBio = saved.userBio;
                userProfilePhone = saved.userPhone;
            } else {
                initializeConversations();
            }

            DefaultListModel<String> messageModel = new DefaultListModel<>();

            // Save data on close before exiting
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    DataManager.saveAll(contacts, userProfileName, userProfileBio, userProfilePhone);
                    System.exit(0);
                }
            });
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());

            CardLayout cardLayout = new CardLayout();
            JPanel mainArea = new JPanel(cardLayout);

            JPanel chatPanel = new JPanel(new BorderLayout());

            // Left side contact list
            contactList.setModel(contactModel);
            refreshContactModel(null);
            frame.add(new JScrollPane(contactList), BorderLayout.WEST);

            // Show messages for selected contact
            JList<String> chats = new JList<>();
            chats.setModel(messageModel);
            chats.setCellRenderer(new WrappingListCellRenderer());
            chats.setFixedCellHeight(-1);
            chatPanel.add(new JScrollPane(chats), BorderLayout.CENTER);

            // Bottom input area for sending new messages
            JTextField input = new JTextField();
            JButton sendButton = new JButton("Send");
            JTextField sendLabel = new JTextField("Send Message");

            JPanel inputPanel = new JPanel(new BorderLayout());
            inputPanel.add(sendLabel, BorderLayout.WEST);
            inputPanel.add(input, BorderLayout.CENTER);
            inputPanel.add(sendButton, BorderLayout.EAST);

            chatPanel.add(inputPanel, BorderLayout.SOUTH);

            // New contact page
            ProfilePage[] profilePage = new ProfilePage[1];
            profilePage[0] = new ProfilePage(() -> {
                String name = profilePage[0].getDisplayName();
                if (!name.isEmpty() && contacts.find(name) == null) {
                    contacts.addToTail(name);
                    ContactLinkedList.Node node = contacts.find(name);
                    node.bio = profilePage[0].getContactBio();
                    node.phone = profilePage[0].getContactPhone();
                    refreshContactModel(null);
                }
                cardLayout.show(mainArea, "chat");
            }, "New Contact");

            JPanel profilePanel = new JPanel(new BorderLayout());
            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> cardLayout.show(mainArea, "chat"));
            profilePanel.add(backButton, BorderLayout.NORTH);
            profilePanel.add(profilePage[0], BorderLayout.CENTER);

            // User profile page
            ProfilePage[] userProfilePage = new ProfilePage[1];
            userProfilePage[0] = new ProfilePage(() -> {
                // Save button to update user's own profile details
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

            // Existing contact profile page for viewing/editing contact details
            ContactProfile[] contactProfilePage = new ContactProfile[1];
            contactProfilePage[0] = new ContactProfile(
                    () -> {
                        // Save button to update selected contact details
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

            // Buttons at the top for adding/removing contacts, searching, and viewing profile
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

            // Load selected contact's details into profile page for viewing/editing when "View Profile" button is clicked
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

            // Contact search bar
            JTextField searchField = new JTextField();
            topPanel.add(searchField, BorderLayout.CENTER);

            searchField.addActionListener(e -> {
                String text = searchField.getText();
                refreshContactModel(text);
            });

            JLabel searchLabel = new JLabel("Search Contacts:");

            JPanel searchPanel = new JPanel(new BorderLayout());
            searchPanel.add(searchLabel, BorderLayout.WEST);
            searchPanel.add(searchField, BorderLayout.CENTER);

            topPanel.add(searchPanel, BorderLayout.CENTER);

            // Message search bar
            JTextField messageSearchField = new JTextField();
            JLabel messageSearchLabel = new JLabel("Search messages:");

            JPanel messageSearchPanel = new JPanel(new BorderLayout(5, 0));
            messageSearchPanel.add(messageSearchLabel, BorderLayout.WEST);
            messageSearchPanel.add(messageSearchField, BorderLayout.CENTER);

            chatPanel.add(messageSearchPanel, BorderLayout.NORTH);

            messageSearchField.addActionListener(e -> {
                String currentContact = contactList.getSelectedValue();
                refreshMessages(currentContact, messageSearchField.getText(), messageModel);
            });

            // Reload messages when selected contact changes
            contactList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        String currentContact = contactList.getSelectedValue();
                        refreshMessages(currentContact, messageSearchField.getText(), messageModel);
                        messageModel.clear();
                        if (currentContact != null) {
                            ContactLinkedList.Node node = contacts.find(currentContact);
                            for (Object obj : node.messages) {
                                Message msg = (Message) obj;
                                String readStatus = msg.isRead() ? "✓ Read" : "● Unread";
                                messageModel.addElement("[" + msg.time() + "] " + msg.sender() + ": " + msg.text()
                                        + "  (" + readStatus + ")");
                            }
                        }
                    }
                }
            });

            // Send a new message to the selected contact
            sendButton.addActionListener(e -> {
                String currentContact = contactList.getSelectedValue();
                String text = input.getText().trim();
                if (currentContact != null && !text.isEmpty()) {
                    Message msg = new Message("You", text, false);
                    contacts.find(currentContact).messages.add(msg);
                    String readStatus = msg.isRead() ? "Read" : "Unread";
                    messageModel.addElement(
                            "[" + msg.time() + "] " + msg.sender() + ": " + msg.text() + "  (" + readStatus + ")");
                    input.setText("");
                    contacts.moveToHead(currentContact);
                    refreshContactModel(null);
                    contactList.setSelectedValue(currentContact, true);
                }
            });

            // Pressing Enter sends a message
            input.addActionListener(e -> sendButton.doClick());

            frame.setVisible(true);
        });
    }

    // Refresh contact list with optional name filter
    private static void refreshContactModel(String filter) {
        contactModel.clear();
        for (String name : contacts.toArray()) {
            if (filter == null || name.toLowerCase().contains(filter.toLowerCase())) {
                contactModel.addElement(name);
            }
        }
    }

    // Sample contacts and messages for initial launch
    private static void initializeConversations() {
        contacts.addToTail("Alice Johnson");
        contacts.addToTail("Bob Smith");
        contacts.addToTail("Jeffrey Lee");

        contacts.find("Alice Johnson").messages
                .add(new Message("Alice Johnson", "Hi, Where is my money!!", true, "10:00 AM"));
        contacts.find("Bob Smith").messages.add(new Message("Bob Smith",
                "Hey! I saw you in your bedroom the other day while I was walking my fish", true, "10:05 AM"));
        contacts.find("Jeffrey Lee").messages.add(new Message("Jeffrey Lee",
                "Hello, Just wanted to let you know that your rent is overdue, please get it to me by the end of the week or I'll have to send the eviction notice.",
                true, "10:10 AM"));
        contacts.find("Alice Johnson").messages.add(new Message("You",
                "I'm really sorry, I've been working extra shifts at the cheese & cake factory but my boss has been refusing to pay me for the extra hours. I'm trying to get it sorted but it's been a nightmare.",
                true, "10:15 AM"));
        contacts.find("Bob Smith").messages.add(new Message("You",
                "Hell yeah dude, I was so embarrassed when I saw you, I thought I was the only one who did that, but when i saw you I was like 'oh thank god, I'm not the only one!'",
                true, "10:20 AM"));
        contacts.find("Jeffrey Lee").messages.add(new Message("You",
                "I hate this stupid flat, the other day some guy was spying on me through my bedroom window!! My boss has been refusing to pay me for extra hours.",
                true, "10:25 AM"));
        contacts.find("Alice Johnson").messages.add(new Message("Alice Johnson",
                "That's not good enough! You need to find the money and get it to me immediately or I'm going to have to take action!",
                true, "10:30 AM"));
        contacts.find("Bob Smith").messages.add(new Message("Bob Smith",
                "Yeah man, I was just wondering what you were up to the other day, I saw lots of flashing lights and couldnt figure out what you were doing. Was there a rave in your bedroom that I wasn't invited to??",
                true, "10:35 AM"));
        contacts.find("Jeffrey Lee").messages.add(new Message("Jeffrey Lee",
                "Listen, I don't care what your excuses are, you need to get the rent to me ASAP, I reccomend you start a side hustle or something to make the money faster, I heard that people are making good money running a small online foot-focused venture, if you catch my drift.",
                true, "10:40 AM"));
    }

    // Refresh message list for selected contact with optional text filter
    private static void refreshMessages(String contactName, String filter, DefaultListModel<String> messageModel) {
        messageModel.clear();

        if (contactName == null) {
            return;
        }

        ContactLinkedList.Node node = contacts.find(contactName);

        for (Object obj : node.messages) {
            Message msg = (Message) obj;

            String readStatus = "Unread";
            if (msg.isRead()) {
                readStatus = "Read";
            }

            String fullText = "[" + msg.time() + "] " + msg.sender() + ": " + msg.text() + "  (" + readStatus + ")";

            if (filter == null || filter.isEmpty()
                    || fullText.toLowerCase().contains(filter.toLowerCase())) {
                messageModel.addElement(fullText);
            }
        }
    }
}