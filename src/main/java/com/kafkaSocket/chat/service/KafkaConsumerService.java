package com.kafkaSocket.chat.service;

import org.springframework.http.codec.ServerSentEvent;

import com.fasterxml.jackson.core.JsonProcessingException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface KafkaConsumerService<T> {
	void consume(T value) throws JsonProcessingException;
	Flux<ServerSentEvent<T>> receive();
}
