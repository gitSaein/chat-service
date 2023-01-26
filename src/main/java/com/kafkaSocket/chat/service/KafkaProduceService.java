package com.kafkaSocket.chat.service;


import com.fasterxml.jackson.core.JsonProcessingException;

import reactor.core.publisher.Mono;

public interface KafkaProduceService<T> {
	Mono<String> send(T value) throws JsonProcessingException;
}
