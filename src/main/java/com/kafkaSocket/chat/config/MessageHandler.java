package com.kafkaSocket.chat.config;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafkaSocket.chat.model.ChatMessage;
import com.kafkaSocket.chat.param.CreateChatParam;
import com.kafkaSocket.chat.service.KafkaProduceService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MessageHandler {
	
	private final KafkaProduceService<ChatMessage> messageService;

	public Mono<ServerResponse> createFromJson(ServerRequest request){
		
		Mono<CreateChatParam> chatCreateMono = request.bodyToMono(CreateChatParam.class);
		
		return chatCreateMono.flatMap(chatCreate ->
				ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(chatCreateMono, CreateChatParam.class));

	}
	
	public Mono<ServerResponse> sendFromJson(ServerRequest request) {
		
		Mono<ChatMessage> messageSendMono = request.bodyToMono(ChatMessage.class);
//		kafkaTemplate.send(null);
//		messageService.send(messageSendMono.);
		return messageSendMono
				.flatMap(message -> {
					try {
						return messageService.send(message);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				})
				.flatMap(message ->
					ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(messageSendMono, ChatMessage.class));
	}

}
