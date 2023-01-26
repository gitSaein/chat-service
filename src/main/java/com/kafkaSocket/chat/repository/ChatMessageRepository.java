package com.kafkaSocket.chat.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kafkaSocket.chat.message.ChatMessageEntity;

@Repository
public interface ChatMessageRepository 
	extends ReactiveCrudRepository<ChatMessageEntity, String> {

}
