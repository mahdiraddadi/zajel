package com.sweagle.zajel.controllers;

import com.sweagle.zajel.entities.User;
import com.sweagle.zajel.exceptions.ResourceAlreadyExistException;
import com.sweagle.zajel.exceptions.ResourceNotFoundException;
import com.sweagle.zajel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("messages")
public class MessageRestController {

    @Autowired
    UserService userService;

    @PutMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) throws ResourceAlreadyExistException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.addUser(user));
    }

    @PostMapping
    public ResponseEntity<List<User>> getUser(@RequestBody User user){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByEmailOrUsername(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.getUserById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User is not available with the provided id")));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
