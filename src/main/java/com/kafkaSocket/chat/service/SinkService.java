package com.kafkaSocket.chat.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public interface SinkService<T> {

	public void onNext(T t, String topic);
	public Sinks.Many<T> getSink(String topic);
	public Flux<T> asFlux(String topic);
}