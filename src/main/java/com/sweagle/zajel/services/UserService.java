package com.sweagle.zajel.services;

import com.sweagle.zajel.entities.User;
import com.sweagle.zajel.exceptions.ResourceAlreadyExistException;
import com.sweagle.zajel.exceptions.ResourceNotFoundException;
import com.sweagle.zajel.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User addUser(User user) throws ResourceAlreadyExistException;
    List<User> getUserByEmailOrUsername(User user);
    User getUserById(String id) throws ResourceNotFoundException;
    List<User> getUsers();
    void deleteUser(String id);



}
