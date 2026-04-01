package io.github.ehteam.messaging;

// Class for a single chat message with sender, content, read status, and timestamp
public class Message {

    // Variables storing the message data
    private String sender;
    private String content;
    private boolean read;
    private String time;

    // Constructor for loading message from a file
    public Message(String sender, String content, boolean read, String time) {
        this.sender = sender;
        this.content = content;
        this.read = read;
        this.time = time;
    }

    // Constructor for new messages — timestamp is set automatically
    public Message(String sender, String content, boolean read) {
        this.sender = sender;
        this.content = content;
        this.read = read;
        this.time = getCurrentTime();
    }

    // Returns the name of the message sender
    public String sender() {
        return sender;
    }

    // Returns the message text
    public String text() {
        return content;
    }

    // Returns whether the message has been read
    public boolean isRead() {
        return read;
    }

    // Returns the timestamp of the message
    public String time() {
        return time;
    }

    // Returns the current time, for timestamping the message
    public String getCurrentTime() {
                java.time.LocalTime now = java.time.LocalTime.now();
                int hour = now.getHour();
                int minute = now.getMinute();
                String amPm = (hour >= 12) ? "PM" : "AM";
                hour = (hour > 12) ? hour - 12 : hour;
                if (hour == 0) {
                    hour = 12;
                }
                return String.format("%d:%02d %s", hour, minute, amPm);
            }
}
