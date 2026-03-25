package io.github.ehteam.messaging;

public class Message {

    private String sender;
    private String content;
    private boolean read;
    private String time;

    public Message(String sender, String content, boolean read, String time) {
        this.sender = sender;
        this.content = content;
        this.read = read;
        this.time = time;
    }

    public Message(String sender, String content, boolean read) {
        this.sender = sender;
        this.content = content;
        this.read = read;
        this.time = getCurrentTime();
    }

    public String sender() {
        return sender;
    }

    public String text() {
        return content;
    }

    public boolean isRead() {
        return read;
    }

    public String time() {
        return time;
    }

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
