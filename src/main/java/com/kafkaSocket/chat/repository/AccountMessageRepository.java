package com.kafkaSocket.chat.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kafkaSocket.chat.entity.AccountMessageEntity;
import com.kafkaSocket.chat.entity.ChatMessageEntity;

public interface AccountMessageRepository extends ReactiveCrudRepository<AccountMessageEntity, String> {

}
