package com.kafkaSocket.chat.message;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafkaSocket.chat.param.CreateChatParam;
import com.kafkaSocket.chat.service.KafkaProduceService;
import com.kafkaSocket.chat.service.impl.ChatServiceImpl;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MessageHandler {
	
	private final ChatServiceImpl chatServiceImpl;

	public Mono<ServerResponse> createFromJson(ServerRequest request){
		
		Mono<CreateChatParam> chatCreateMono = request.bodyToMono(CreateChatParam.class);
		
		return chatCreateMono.flatMap(chatCreate ->
				ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(chatCreateMono, CreateChatParam.class));

	}
	
	public Mono<ServerResponse> createMessage(ServerRequest request) {
		Mono<ChatMessageDTO.RequestMessage> messageSendMono = request.bodyToMono(ChatMessageDTO.RequestMessage.class);
		return messageSendMono
				.flatMap(message -> {
					return chatServiceImpl.send(message);
				})
				.flatMap(message ->
					ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(messageSendMono, ChatMessageEntity.class));
	}
	
	public Mono<ServerResponse> getChatMessage(ServerRequest serverRequest){
		return chatServiceImpl.getChatMessageByTopic(serverRequest);

	}

}
