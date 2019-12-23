package com.sweagle.zajel.services;

import com.sweagle.zajel.entities.User;
import com.sweagle.zajel.exceptions.ResourceAlreadyExistException;
import com.sweagle.zajel.exceptions.ResourceNotFoundException;
import com.sweagle.zajel.repositories.UserRepository;
import com.sweagle.zajel.services.impl.UserServiceImpl;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService = new UserServiceImpl();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userService).build();
    }

    @Test
    void addNewUser() throws Exception {
        User user = new User();
        user.setEmail("john@doe.com");
        user.setUsername("john_doe");
        user.setId("1");
        user.setName("John Doe");

        when(userRepository.existsByEmailOrUsername(anyString(),anyString())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.addUser(user);

        assertNotNull(createdUser);
    }

    @Test
    void addNewUserThrowsExceptionAlreadyExist() throws Exception {
        User user = new User();
        user.setEmail("john@doe.com");
        user.setUsername("john_doe");
        user.setId("1");
        user.setName("John Doe");

        when(userRepository.existsByEmailOrUsername(anyString(),anyString())).thenReturn(true);
        assertThrows(ResourceAlreadyExistException.class,() -> userService.addUser(user));
    }

    @Test
    void getUserByEmailOrUsername() throws Exception {
        User user = new User();
        user.setEmail("john@doe.com");
        user.setUsername("john_doe");
        user.setId("1");
        user.setName("John Doe");

        when(userRepository.findByEmailOrUsername(anyString(),anyString())).thenReturn(Arrays.asList(user));

        List<User> userList = userService.getUserByEmailOrUsername(user);

        assertEquals(1, userList.size());
    }

    @Test
    void getUserById() throws Exception {
        User user = new User();
        user.setEmail("john@doe.com");
        user.setUsername("john_doe");
        user.setId("1");
        user.setName("John Doe");

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));

        User foundedUser = userService.getUserById("13321");

        assertNotNull(foundedUser);
    }

    @Test
    void getUserByIdThrowsNotFoundException() throws Exception {
        User user = new User();
        user.setEmail("john@doe.com");
        user.setUsername("john_doe");
        user.setId("1");
        user.setName("John Doe");

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById("13321"));
    }

    @Test
    void getUsers() throws Exception {
        User user = new User();
        user.setEmail("john@doe.com");
        user.setUsername("john_doe");
        user.setId("1");
        user.setName("John Doe");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        List<User> userList = userService.getUsers();
        assertEquals(1, userList.size());
    }

    @Test
    void getDeleteUser() throws Exception {
        doNothing().when(userRepository).deleteById(anyString());
        userService.deleteUser(anyString());
        verify(userRepository, times(1)).deleteById(anyString());
    }

}
