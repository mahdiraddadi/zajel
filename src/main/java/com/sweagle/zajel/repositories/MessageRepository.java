package com.sweagle.zajel.repositories;

import com.sweagle.zajel.entities.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {


}
