package com.sweagle.zajel.controllers;

import com.sweagle.zajel.entities.User;
import com.sweagle.zajel.exceptions.ResourceNotFoundException;
import com.sweagle.zajel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserRestController {

    @Autowired
    UserService userService;

    @PostMapping
    public User saveUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @GetMapping("/byEmailOrUsername/{emailOrUsername}")
    public User getUser(@PathVariable("emailOrUsername") String emailOrUsername){
        return userService.getUserByEmailOrUsername(emailOrUsername);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") String id) throws ResourceNotFoundException {
        return userService.getUserById(id).orElseThrow(() -> new ResourceNotFoundException("User is not available with the provided id"));
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }


}
