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
}
