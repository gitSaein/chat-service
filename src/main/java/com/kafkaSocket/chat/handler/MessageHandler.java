package com.kafkaSocket.chat.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kafkaSocket.chat.dto.ChatMessageDTO;
import com.kafkaSocket.chat.entity.ChatMessageEntity;
import com.kafkaSocket.chat.service.impl.ChatServiceImpl;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MessageHandler {
	
	private final ChatServiceImpl chatServiceImpl;

	public Mono<ServerResponse> createRoom(ServerRequest request){
		
		Mono<ChatMessageDTO.RequestCreateRoom> chatCreateMono = request.bodyToMono(ChatMessageDTO.RequestCreateRoom.class);
		
		return chatCreateMono.flatMap(chatCreate ->
				ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(chatCreateMono, ChatMessageDTO.RequestCreateRoom.class));

	}
	
	public Mono<ServerResponse> create(ServerRequest request) {
		Mono<ChatMessageDTO.RequestMessage> messageSendMono = request.bodyToMono(ChatMessageDTO.RequestMessage.class);
		return messageSendMono
				.flatMap(message -> {
					return chatServiceImpl.send(message.toEntity());
				})
				.flatMap(message ->
					ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(messageSendMono, ChatMessageEntity.class));
	}
	
	public Mono<ServerResponse> get(ServerRequest serverRequest){
		return chatServiceImpl.getChatMessageByTopic(serverRequest);

	}

}
