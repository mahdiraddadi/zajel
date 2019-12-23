package com.sweagle.zajel.services;

import com.sweagle.zajel.entities.Message;
import com.sweagle.zajel.exceptions.ResourceNotFoundException;
import com.sweagle.zajel.payloads.MessageRequest;
import com.sweagle.zajel.payloads.MessageResponse;

import java.util.List;

public interface MessageService {

    Message send(MessageRequest message) throws ResourceNotFoundException;
    List<MessageResponse>  getIncomingMessages(String receiverId) throws ResourceNotFoundException;
    List<MessageResponse>  getSentMessages(String senderId) throws ResourceNotFoundException;
    MessageResponse getMessageById(String messageId) throws ResourceNotFoundException;
    Message getMessageDetailsById(String messageId) throws ResourceNotFoundException;
    void deleteMessage(String messageId);
    Integer countMessagesWillBeSentInDay();
    Integer countMessagesWillBeSentInWeek();
}
