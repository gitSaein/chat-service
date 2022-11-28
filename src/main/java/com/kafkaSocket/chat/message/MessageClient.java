package com.kafkaSocket.chat.message;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.kafkaSocket.chat.model.ChatMessage;

import reactor.core.publisher.Mono;

@Component
public class MessageClient {
	private final WebClient client;

	// Spring Boot auto-configures a `WebClient.Builder` instance with nice defaults and customizations.
	// We can use it to create a dedicated `WebClient` for our component.
	public MessageClient(WebClient.Builder builder) {
		this.client = builder.baseUrl("http://localhost:9091").build();
	}

	public Mono<String> getMessage() {
		return this.client.get().uri("/kafka/hello").accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(ChatMessage.class)
				.map(ChatMessage::getMessage);
	}

}
