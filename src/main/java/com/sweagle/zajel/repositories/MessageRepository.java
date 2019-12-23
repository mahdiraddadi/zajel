package com.sweagle.zajel.repositories;

import com.sweagle.zajel.entities.Message;
import com.sweagle.zajel.payloads.MessageResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    List<MessageResponse> getBySenderId(String senderId);
    List<MessageResponse> getByReceiverId(String receiverId);
    Optional<MessageResponse> getById(String messageId);
    Integer countBySendingDateBetween(LocalDateTime from, LocalDateTime to);
}
