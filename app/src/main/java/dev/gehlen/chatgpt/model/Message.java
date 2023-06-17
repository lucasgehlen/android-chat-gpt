package dev.gehlen.chatgpt.model;

public class Message {
    private String content;
    private String role;

    public Message(String content, String role) {
        this.content = content;
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public String getRole() {
        return role;
    }
}

