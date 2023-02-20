package com.kafkaSocket.chat.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.kafkaSocket.chat.entity.ChatRoomsEntity;

public interface ChatRoomsRepository extends ReactiveCrudRepository<ChatRoomsEntity, String> {

}
