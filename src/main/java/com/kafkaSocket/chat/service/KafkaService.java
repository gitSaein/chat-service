package com.kafkaSocket.chat.service;

import java.util.function.Consumer;

import reactor.core.publisher.Mono;
import reactor.kafka.receiver.ReceiverRecord;

public interface KafkaService {
	Mono<Boolean> send(String topic, String key, Object value);
	Consumer<ReceiverRecord<String, Object>> processReceivedData();
}
