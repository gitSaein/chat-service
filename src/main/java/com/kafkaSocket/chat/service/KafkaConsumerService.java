package com.kafkaSocket.chat.service;

public interface KafkaConsumerService<T> {
	void consume(T value);
}
