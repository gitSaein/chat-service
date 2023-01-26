package com.kafkaSocket.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface KafkaConsumerService<T> {
	void consume(T value) throws JsonProcessingException;
}
