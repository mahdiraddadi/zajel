package com.sweagle.zajel.services.impl;

import com.sweagle.zajel.entities.Message;
import com.sweagle.zajel.entities.User;
import com.sweagle.zajel.exceptions.ResourceNotFoundException;
import com.sweagle.zajel.payloads.MessageRequest;
import com.sweagle.zajel.payloads.MessageResponse;
import com.sweagle.zajel.repositories.MessageRepository;
import com.sweagle.zajel.repositories.UserRepository;
import com.sweagle.zajel.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;
    @Override
    public Message send(MessageRequest messageRequest) throws ResourceNotFoundException {
        User sender = userRepository.findByEmailOrUsername(messageRequest.getSender())
                .orElseThrow(() -> new ResourceNotFoundException("Sender with email or username "+messageRequest.getSender()+" is not not found"));

        User receiver = userRepository.findByEmailOrUsername(messageRequest.getReceiver())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver with email or username "+messageRequest.getReceiver()+" is not not found"));

        Message message= new Message(messageRequest);

        message.setSender(sender);
        message.setReceiver(receiver);
        return messageRepository.save(message);
    }

    @Override
    public List<MessageResponse> getIncomingMessages(String receiver) throws ResourceNotFoundException {
        User user = userRepository.findByEmailOrUsername(receiver)
                .orElseThrow(() -> new ResourceNotFoundException("Sender with email or username "+receiver+" is not not found"));
        return messageRepository.getByReceiverId(user.getId());
    }

    @Override
    public List<MessageResponse> getSentMessages(String sender) throws ResourceNotFoundException {
        User user = userRepository.findByEmailOrUsername(sender)
                .orElseThrow(() -> new ResourceNotFoundException("Sender with email or username "+sender+" is not not found"));
        return messageRepository.getBySenderId(user.getId());
    }

    @Override
    public MessageResponse getMessageById(String messageId) throws ResourceNotFoundException {
        return messageRepository.getById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message is not found"));
    }

    @Override
    public Message getMessageDetailsById(String messageId) throws ResourceNotFoundException {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message is not found"));
    }

    @Override
    public void deleteMessage(String messageId) {
        messageRepository.deleteById(messageId);
    }
}
