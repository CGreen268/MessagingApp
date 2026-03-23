package io.github.ehteam.messaging;

public class Message {

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
