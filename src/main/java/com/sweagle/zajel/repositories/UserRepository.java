package com.sweagle.zajel.repositories;

import com.sweagle.zajel.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findByEmailOrUsername(String username, String email);
    Boolean existsByEmailOrUsername(String username, String email);

    @Query(value = "{$or : [ {username: ?0}, {email: ?0}]}")
    Optional<User> findByEmailOrUsername(String usernameOrEmail);

}
