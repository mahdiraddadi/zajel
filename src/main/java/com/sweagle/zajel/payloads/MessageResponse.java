package com.sweagle.zajel.payloads;

import com.sweagle.zajel.entities.User;

import java.time.LocalDateTime;

public interface MessageResponse {

    String getId();
    User getSender();
    User getReceiver();
    String getSubject();
    LocalDateTime getDate();

    void setId(String id);
    void setSender(User sender);
    void setReceiver(User receiver);
    void setSubject(String subject);
    void setDate(LocalDateTime date);

}
