package com.sweagle.zajel.controllers;

import com.sweagle.zajel.configurations.RestResponseExceptionHandler;
import com.sweagle.zajel.entities.Message;
import com.sweagle.zajel.entities.User;
import com.sweagle.zajel.exceptions.ResourceNotFoundException;
import com.sweagle.zajel.payloads.MessageRequest;
import com.sweagle.zajel.payloads.MessageResponse;
import com.sweagle.zajel.services.MessageService;
import com.sweagle.zajel.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MessageRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    UserService userService;

    @Mock
    MessageService messageService;

    @InjectMocks
    MessageRestController messageRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(messageRestController)
                .setControllerAdvice(new RestResponseExceptionHandler())
                .build();
    }

    String messageJson ="    {\n" +
            "        \"sender\": \"john@doe.com\",\n" +
            "        \"receiver\": \"jane@doe.com\",\n" +
            "        \"subject\": \"Hello\",\n" +
            "        \"content\": \"This is a greeting message\"\n" +
            "    }";

    @Test
    void sendMessage() throws Exception {
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

        when(messageService.send(any(MessageRequest.class))).thenReturn(message);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/messages/send")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(messageJson);

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("message21654")))
                .andExpect(jsonPath("$.subject", is("Hello")))
                .andExpect(jsonPath("$.content", is("This is a greeting message")))
                .andExpect(jsonPath("$.sender.username", is("john_doe")))
                .andExpect(jsonPath("$.receiver.username", is("jane_doe")));
    }

    @Test
    void sendMessageThrowsResourceNotFound() throws Exception {

        when(messageService.send(any(MessageRequest.class)))
                .thenThrow(new ResourceNotFoundException("Sender with email or username john@doe.com is not not found"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/messages/send")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(messageJson);

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Sender with email or username john@doe.com is not not found")));
    }

    @Test
    void getMessage() throws Exception {
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

        when(messageService.getMessageById(anyString())).thenReturn(messageResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/messages/message21654")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("message21654")))
                .andExpect(jsonPath("$.subject", is("Hello")))
                .andExpect(jsonPath("$.sender.username", is("john_doe")))
                .andExpect(jsonPath("$.receiver.username", is("jane_doe")));
    }

    @Test
    void getMessageDetails() throws Exception {
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

        when(messageService.getMessageDetailsById(anyString())).thenReturn(message);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/messages/message21654/details")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("message21654")))
                .andExpect(jsonPath("$.subject", is("Hello")))
                .andExpect(jsonPath("$.sender.username", is("john_doe")))
                .andExpect(jsonPath("$.receiver.username", is("jane_doe")))
                .andExpect(jsonPath("$.content", is("This is a greeting message")));
    }

    @Test
    void deleteMessage() throws Exception {
        messageService.deleteMessage(anyString());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/api/messages/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void getMessageByIdThrowsException() throws Exception {
        when(messageService.getMessageById(anyString()))
                .thenThrow(new ResourceNotFoundException("Message is not found"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/messages/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Message is not found")));
    }

    @Test
    void getMessageDetailsByIdThrowsException() throws Exception {
        when(messageService.getMessageDetailsById(anyString()))
                .thenThrow(new ResourceNotFoundException("Message is not found"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/messages/1/details")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Message is not found")));
    }

    @Test
    void getIncomingMessage() throws Exception {
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
        List<MessageResponse> messageResponseList = new ArrayList<>();
        messageResponseList.add(messageResponse);

        when(messageService.getIncomingMessages(anyString())).thenReturn(messageResponseList);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/messages/incoming/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("message21654")))
                .andExpect(jsonPath("$[0].subject", is("Hello")))
                .andExpect(jsonPath("$[0].sender.username", is("john_doe")))
                .andExpect(jsonPath("$[0].receiver.username", is("jane_doe")));
    }


    @Test
    void getSentMessage() throws Exception {
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
        List<MessageResponse> messageResponseList = new ArrayList<>();
        messageResponseList.add(messageResponse);

        when(messageService.getSentMessages(anyString())).thenReturn(messageResponseList);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/messages/sent/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("message21654")))
                .andExpect(jsonPath("$[0].subject", is("Hello")))
                .andExpect(jsonPath("$[0].sender.username", is("john_doe")))
                .andExpect(jsonPath("$[0].receiver.username", is("jane_doe")));
    }

    @Test
    void getIncomingMessagesThrowsException() throws Exception {
        when(messageService.getIncomingMessages(anyString()))
                .thenThrow(new ResourceNotFoundException("Message is not found"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/messages/incoming/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Message is not found")));
    }

    @Test
    void getSentMessagesThrowsException() throws Exception {
        when(messageService.getSentMessages(anyString()))
                .thenThrow(new ResourceNotFoundException("Message is not found"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/messages/sent/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Message is not found")));
    }
}
