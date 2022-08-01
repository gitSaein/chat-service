package com.kafkaSocket.chat.config;

import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kafkaSocket.chat.param.CreateChatParam;
import com.kafkaSocket.chat.param.MessageParam;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PostHandler {
	
	private final KafkaTemplate<String, MessageParam> kafkaTemplate;

	public Mono<ServerResponse> createFromJson(ServerRequest request){
		
		Mono<CreateChatParam> chatCreateMono= request.bodyToMono(CreateChatParam.class);
		
		return chatCreateMono.flatMap(chatCreate ->
				ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(chatCreateMono, CreateChatParam.class));

	}
	
	public Mono<ServerResponse> sendFromJson(ServerRequest request) {
		
		Mono<MessageParam> messageSendMono = request.bodyToMono(MessageParam.class);
//		kafkaTemplate.send(null);
		return messageSendMono.flatMap(message ->
				ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(messageSendMono, MessageParam.class));
	}

}
