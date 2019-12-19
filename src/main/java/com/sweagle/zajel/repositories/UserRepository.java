package com.sweagle.zajel.repositories;

import com.sweagle.zajel.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findByEmailOrUsername(String usernameOrEmail);
}
