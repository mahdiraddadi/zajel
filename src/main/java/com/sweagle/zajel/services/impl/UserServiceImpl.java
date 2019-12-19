package com.sweagle.zajel.services.impl;

import com.sweagle.zajel.entities.User;
import com.sweagle.zajel.repositories.UserRepository;
import com.sweagle.zajel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmailOrUsername(String emailOrUsername){
        return userRepository.findByEmailOrUsername(emailOrUsername);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser() {

    }


}
