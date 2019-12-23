package com.sweagle.zajel.services;

import com.sweagle.zajel.entities.Message;
import com.sweagle.zajel.entities.User;
import com.sweagle.zajel.exceptions.ResourceNotFoundException;
import com.sweagle.zajel.payloads.MessageRequest;
import com.sweagle.zajel.payloads.MessageResponse;
import com.sweagle.zajel.repositories.MessageRepository;
import com.sweagle.zajel.repositories.UserRepository;
import com.sweagle.zajel.services.impl.MessageServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MessageServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    UserRepository userRepository;

    @Mock
    MessageRepository messageRepository;

    @InjectMocks
    MessageService messageService = new MessageServiceImpl();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(messageService).build();
    }

    @Test
    void send() throws Exception {
        User sender = new User();
        sender.setEmail("john@doe.com");
        sender.setUsername("john_doe");
        sender.setId("1");
        sender.setName("John Doe");

        User receiver = new User();
        receiver.setEmail("jane@doe.com");
        receiver.setUsername("jane_doe");
        receiver.setId("2");
        receiver.setName("Jane Doe");

        Message message = new Message();
        message.setId("message21654");
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSubject("Hello");
        message.setContent("This is a greeting message");
        message.setDate(LocalDateTime.now());

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setReceiver(receiver.getEmail());
        messageRequest.setSender(sender.getUsername());
        messageRequest.setSubject(message.getSubject());
        messageRequest.setContent(message.getContent());

        when(userRepository.findByEmailOrUsername(anyString())).thenReturn(Optional.of(sender));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message createdMessage = messageService.send(messageRequest);
        assertNotNull(createdMessage);
    }

    @Test
    void sendThrowsException() throws Exception{
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setSender("john_doe");
        messageRequest.setReceiver("jane_doe");
        messageRequest.setSubject("Hello");
        messageRequest.setContent("This is a greeting message");

        when(userRepository.findByEmailOrUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> messageService.send(messageRequest));
    }

    @Test
    void getIncomingMessages() throws ResourceNotFoundException {
        User sender = new User();
        sender.setEmail("john@doe.com");
        sender.setUsername("john_doe");
        sender.setId("1");
        sender.setName("John Doe");

        User receiver = new User();
        receiver.setEmail("jane@doe.com");
        receiver.setUsername("jane_doe");
        receiver.setId("2");
        receiver.setName("Jane Doe");

        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        MessageResponse messageResponse = factory.createProjection(MessageResponse.class);

        messageResponse.setId("message21654");
        messageResponse.setReceiver(receiver);
        messageResponse.setSender(sender);
        messageResponse.setSubject("Hello");
        List<MessageResponse> messageResponseList = Arrays.asList(messageResponse);

        when(userRepository.findByEmailOrUsername(anyString())).thenReturn(Optional.of(receiver));
        when(messageRepository.getByReceiverId(anyString())).thenReturn(messageResponseList);

        List<MessageResponse> incomingMessages = messageService.getIncomingMessages(anyString());

        assertEquals(1, incomingMessages.size());
    }

    @Test
    void getIncomingMessagesThrowsException() throws Exception {
        when(userRepository.findByEmailOrUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> messageService.getIncomingMessages(anyString()));
    }
    @Test
    void getSentMessages() throws ResourceNotFoundException {
        User sender = new User();
        sender.setEmail("john@doe.com");
        sender.setUsername("john_doe");
        sender.setId("1");
        sender.setName("John Doe");

        User receiver = new User();
        receiver.setEmail("jane@doe.com");
        receiver.setUsername("jane_doe");
        receiver.setId("2");
        receiver.setName("Jane Doe");

        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        MessageResponse messageResponse = factory.createProjection(MessageResponse.class);

        messageResponse.setId("message21654");
        messageResponse.setReceiver(receiver);
        messageResponse.setSender(sender);
        messageResponse.setSubject("Hello");
        List<MessageResponse> messageResponseList = Arrays.asList(messageResponse);

        when(userRepository.findByEmailOrUsername(anyString())).thenReturn(Optional.of(sender));
        when(messageRepository.getBySenderId(anyString())).thenReturn(messageResponseList);

        List<MessageResponse> sentMessages = messageService.getSentMessages(anyString());

        assertEquals(1, sentMessages.size());
    }

    @Test
    void getSentMessagesThrowsException() throws Exception {
        when(userRepository.findByEmailOrUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> messageService.getSentMessages(anyString()));
    }

    @Test
    void getMessageById() throws ResourceNotFoundException {
        User sender = new User();
        sender.setEmail("john@doe.com");
        sender.setUsername("john_doe");
        sender.setId("1");
        sender.setName("John Doe");

        User receiver = new User();
        receiver.setEmail("jane@doe.com");
        receiver.setUsername("jane_doe");
        receiver.setId("2");
        receiver.setName("Jane Doe");

        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        MessageResponse messageResponse = factory.createProjection(MessageResponse.class);

        messageResponse.setId("message21654");
        messageResponse.setReceiver(receiver);
        messageResponse.setSender(sender);
        messageResponse.setSubject("Hello");

        when(messageRepository.getById(anyString())).thenReturn(Optional.of(messageResponse));

        MessageResponse foundMessage = messageService.getMessageById(anyString());

        assertNotNull(foundMessage);
    }

    @Test
    void getMessageByIdThrowsException() throws Exception {
        when(messageRepository.getById(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> messageService.getMessageById(anyString()));
    }

    @Test
    void getMessageDetailsById() throws ResourceNotFoundException {
        User sender = new User();
        sender.setEmail("john@doe.com");
        sender.setUsername("john_doe");
        sender.setId("1");
        sender.setName("John Doe");

        User receiver = new User();
        receiver.setEmail("jane@doe.com");
        receiver.setUsername("jane_doe");
        receiver.setId("2");
        receiver.setName("Jane Doe");

        Message message = new Message();
        message.setId("message21654");
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSubject("Hello");
        message.setContent("This is a greeting message");
        message.setDate(LocalDateTime.now());

        when(messageRepository.findById(anyString())).thenReturn(Optional.of(message));

        Message foundMessage = messageService.getMessageDetailsById(anyString());

        assertNotNull(foundMessage);
    }

    @Test
    void getMessageDetailsByIdThrowsException() throws Exception {
        when(messageRepository.getById(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> messageService.getMessageById(anyString()));
    }

    @Test
    void getDeleteUser() throws Exception {
        doNothing().when(messageRepository).deleteById(anyString());
        messageService.deleteMessage(anyString());
        verify(messageRepository, times(1)).deleteById(anyString());
    }

    @Test
    void countMessagesWillBeSentInDay() {
        when(messageRepository.countBySendingDateBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(6);
        Integer count = messageService.countMessagesWillBeSentInDay();
        assertEquals(6, count);
    }


    @Test
    void countMessagesWillBeSentInWeek() {
        when(messageRepository.countBySendingDateBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(73);
        Integer count = messageService.countMessagesWillBeSentInWeek();
        assertEquals(73, count);
    }

}
