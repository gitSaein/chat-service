package com.kafkaSocket.chat.service;

import reactor.core.publisher.Flux;

public interface KafkaConsumerService<T> {
	void consume(T value);
}
