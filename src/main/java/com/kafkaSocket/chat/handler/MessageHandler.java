package com.kafkaSocket.chat.handler;

import com.kafkaSocket.chat.service.impl.ChatMessageConsumerServiceImpl;
import com.kafkaSocket.chat.service.impl.ChatMessageProduceServiceImpl;
import com.kafkaSocket.chat.service.impl.SinkServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kafkaSocket.chat.model.ChatMessage;
import com.kafkaSocket.chat.param.CreateChatParam;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler {
	
	private final ChatMessageProduceServiceImpl messageService;
	private final ChatMessageConsumerServiceImpl kafkaConsumerService;

	public Mono<ServerResponse> createFromJson(ServerRequest request){
		
		Mono<CreateChatParam> chatCreateMono = request.bodyToMono(CreateChatParam.class);
		
		return chatCreateMono.flatMap(chatCreate ->
				ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(chatCreateMono, CreateChatParam.class));

	}
	
	public Mono<ServerResponse> sendChatMessage(ServerRequest request) {
		
		Mono<ChatMessage> messageSendMono = request.bodyToMono(ChatMessage.class);
		return messageSendMono
				.flatMap(message -> messageService.send(message))
				.flatMap(message ->
					ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(messageSendMono, ChatMessage.class));
	}

	
	public Mono<ServerResponse> getChatMessage(ServerRequest serverRequest){
		return kafkaConsumerService.getChatMessageByTopic(serverRequest);

	}
	
}
