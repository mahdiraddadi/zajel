package com.sweagle.zajel.repositories;

import com.sweagle.zajel.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findByEmailOrUsername(String username, String email);
    Boolean existsByEmailOrUsername(String username, String email);
}
