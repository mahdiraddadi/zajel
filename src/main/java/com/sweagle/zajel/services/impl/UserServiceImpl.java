package com.sweagle.zajel.services.impl;

import com.sweagle.zajel.entities.User;
import com.sweagle.zajel.exceptions.ResourceAlreadyExistException;
import com.sweagle.zajel.exceptions.ResourceNotFoundException;
import com.sweagle.zajel.repositories.UserRepository;
import com.sweagle.zajel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User addUser(User user) throws ResourceAlreadyExistException {
        if (userRepository.existsByEmailOrUsername(user.getEmail(), user.getUsername())) {
            throw new ResourceAlreadyExistException("User is already exist");
        } else {
            return userRepository.save(user);
        }
    }

    @Override
    public List<User> getUserByEmailOrUsername(User user){
        return userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());
    }

    @Override
    public User getUserById(String id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User is not available with the provided id"));
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

}
