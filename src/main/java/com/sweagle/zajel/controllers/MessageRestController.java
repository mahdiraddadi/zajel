package com.sweagle.zajel.controllers;

import com.sweagle.zajel.entities.Message;
import com.sweagle.zajel.exceptions.ResourceNotFoundException;
import com.sweagle.zajel.payloads.MessageRequest;
import com.sweagle.zajel.payloads.MessageResponse;
import com.sweagle.zajel.services.MessageService;
import com.sweagle.zajel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/messages")
public class MessageRestController {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest message) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.send(message));
    }


    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<MessageResponse>> getSentMessages(@PathVariable("senderId") String id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getSentMessages(id));
    }

    @GetMapping("/incoming/{receiverId}")
    public ResponseEntity<List<MessageResponse>> getIncomingMessages(@PathVariable("receiverId") String id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getIncomingMessages(id));
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponse> getMessage(@PathVariable("messageId") String id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessageById(id));
    }

    @GetMapping("/{messageId}/details")
    public ResponseEntity<Message> getMessageDetails(@PathVariable("messageId") String id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessageDetailsById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMessage(@PathVariable("id") String id) {
        messageService.deleteMessage(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/countMessagesInRestOfDay")
    public ResponseEntity<Integer> countMessagesInRestOfDay() {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.countMessagesWillBeSentInDay());
    }

    @GetMapping("/countMessagesInRestOfWeek")
    public ResponseEntity<Integer> countMessagesInRestOfWeek() {
        return ResponseEntity.status(HttpStatus.OK).body(messageService.countMessagesWillBeSentInWeek());
    }
}
