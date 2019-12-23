package com.sweagle.zajel.controllers;

import com.sweagle.zajel.TestMongoConfig;
import com.sweagle.zajel.ZajelApplication;
import com.sweagle.zajel.configurations.RestResponseExceptionHandler;
import com.sweagle.zajel.entities.User;
import com.sweagle.zajel.exceptions.ResourceAlreadyExistException;
import com.sweagle.zajel.exceptions.ResourceNotFoundException;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {
        ZajelApplication.class,
        TestMongoConfig.class
})
public class UserRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    UserService userService;

    @InjectMocks
    UserRestController userRestController;

    String userJson = "    {\n" +
            "        \"name\": \"John Doe\",\n" +
            "        \"email\": \"john@doe.com\",\n" +
            "        \"username\": \"john_doe\"\n" +
            "    }";

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userRestController)
                .setControllerAdvice(new RestResponseExceptionHandler())
                .build();
    }

    @Test
    void addNewUser() throws Exception {
        User user = new User();
        user.setEmail("john@doe.com");
        user.setUsername("john_doe");
        user.setId("1");
        user.setName("John Doe");

        when(userService.addUser(any(User.class))).thenReturn(user);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(userJson);

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@doe.com")))
                .andExpect(jsonPath("$.username", is("john_doe")));
    }

    @Test
    void addNewUserThrowsException() throws Exception {
        when(userService.addUser(any(User.class))).thenThrow(new ResourceAlreadyExistException("User is already exist"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(userJson);

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("User is already exist")));

    }

    @Test
    void getUserByEmailOrUsername() throws Exception{

        User user = new User();
        user.setEmail("john@doe.com");
        user.setUsername("john_doe");
        user.setId("1");
        user.setName("John Doe");
        List<User> usersList = Arrays.asList(user);
        when(userService.getUserByEmailOrUsername(any(User.class))).thenReturn(usersList);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{\"username\": \"john_doe\"}");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(user.getId())))
                .andExpect(jsonPath("$[0].name", is(user.getName())))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())));
    }

    @Test
    void getUsers() throws Exception{

        User user = new User();
        user.setEmail("john@doe.com");
        user.setUsername("john_doe");
        user.setId("1");
        user.setName("John Doe");
        List<User> usersList = Arrays.asList(user);
        when(userService.getUsers()).thenReturn(usersList);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(user.getId())))
                .andExpect(jsonPath("$[0].name", is(user.getName())))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())));
    }

    @Test
    void getUserById() throws Exception {
        User user = new User();
        user.setEmail("john@doe.com");
        user.setUsername("john_doe");
        user.setId("1");
        user.setName("John Doe");
        when(userService.getUserById(anyString()))
                .thenReturn(user);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@doe.com")))
                .andExpect(jsonPath("$.username", is("john_doe")));
    }

    @Test
    void getUserByIdThrowsException() throws Exception {
        when(userService.getUserById(anyString()))
                .thenThrow(new ResourceNotFoundException("User is not available with the provided id"));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User is not available with the provided id")));
    }

    @Test
    void deleteUser() throws Exception {
        userService.deleteUser(anyString());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

        mockMvc.perform(builder
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
