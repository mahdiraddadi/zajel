package com.sweagle.zajel.entities;

import com.sweagle.zajel.payloads.MessageRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Message {

    @Id
    private String id;

    private User sender;
    private User receiver;
    private String subject;
    private String content;
    private LocalDateTime date;

    public Message() {
    }

    public Message(MessageRequest messageRequest) {
        this.subject = messageRequest.getSubject();
        this.content = messageRequest.getContent();
        this.date = LocalDateTime.now();
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
