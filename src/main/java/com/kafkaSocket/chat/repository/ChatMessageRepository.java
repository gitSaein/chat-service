package com.kafkaSocket.chat.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kafkaSocket.chat.model.ChatMessage;

@Repository
public interface ChatMessageRepository 
	extends ReactiveCrudRepository<ChatMessage, String> {

}
