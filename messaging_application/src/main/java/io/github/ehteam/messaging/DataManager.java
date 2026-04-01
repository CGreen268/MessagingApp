package io.github.ehteam.messaging;

import java.io.*;

public class DataManager {

    private static final String FILE_PATH = "data.txt";

    // Saving

    public static void saveAll(ContactLinkedList contacts,
                               String userName, String userBio, String userPhone) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            // Save user profile
            writer.write(userName);  writer.newLine();
            writer.write(userBio);   writer.newLine();
            writer.write(userPhone); writer.newLine();

            // Save number of contacts
            writer.write(String.valueOf(contacts.size())); writer.newLine();

            // Save each contact
            ContactLinkedList.Node cur = contacts.getHead();
            while (cur != null) {
                writer.write(cur.name);  writer.newLine();
                writer.write(cur.bio);   writer.newLine();
                writer.write(cur.phone); writer.newLine();

                // Save number of messages for this contact
                writer.write(String.valueOf(cur.messages.size())); writer.newLine();

                // Save each message
                for (Object obj : cur.messages) {
                    Message msg = (Message) obj;
                    writer.write(msg.sender());           writer.newLine();
                    writer.write(msg.text());             writer.newLine();
                    writer.write(String.valueOf(msg.isRead())); writer.newLine();
                    writer.write(msg.time());             writer.newLine();
                }

                cur = cur.next;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loading

    public static AppData loadAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {

            // Load user profile
            String userName  = reader.readLine();
            String userBio   = reader.readLine();
            String userPhone = reader.readLine();

            // Load contacts
            ContactLinkedList contacts = new ContactLinkedList();
            int contactCount = Integer.parseInt(reader.readLine());

            for (int i = 0; i < contactCount; i++) {
                String name  = reader.readLine();
                String bio   = reader.readLine();
                String phone = reader.readLine();

                contacts.addToTail(name);
                ContactLinkedList.Node node = contacts.find(name);
                node.bio   = bio;
                node.phone = phone;

                int messageCount = Integer.parseInt(reader.readLine());
                for (int j = 0; j < messageCount; j++) {
                    String sender  = reader.readLine();
                    String content = reader.readLine();
                    boolean read   = Boolean.parseBoolean(reader.readLine());
                    String time    = reader.readLine();
                    node.messages.add(new Message(sender, content, read, time));
                }
            }

            return new AppData(contacts, userName, userBio, userPhone);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // App Data Class

    public static class AppData {
        public final ContactLinkedList contacts;
        public final String userName, userBio, userPhone;

        public AppData(ContactLinkedList contacts,
                       String userName, String userBio, String userPhone) {
            this.contacts  = contacts;
            this.userName  = userName;
            this.userBio   = userBio;
            this.userPhone = userPhone;
        }
    }
}