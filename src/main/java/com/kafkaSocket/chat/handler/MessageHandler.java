package com.kafkaSocket.chat.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kafkaSocket.chat.dto.ChatMessageDTO;
import com.kafkaSocket.chat.entity.ChatMessageEntity;
import com.kafkaSocket.chat.service.impl.AccountServiceImpl;
import com.kafkaSocket.chat.service.impl.ChatServiceImpl;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MessageHandler {
	
	private final ChatServiceImpl chatServiceImpl;
	private final AccountServiceImpl accountServiceImpl;

	public Mono<ServerResponse> createRoom(ServerRequest request){
		
		Mono<ChatMessageDTO.RequestCreateRoom> chatCreateMono = request.bodyToMono(ChatMessageDTO.RequestCreateRoom.class);
		
		return chatCreateMono
				.flatMap(chatCreate -> {
					return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
							.body(chatServiceImpl.createRoom(chatCreate),ChatMessageDTO.RequestCreateRoom.class);
				});

	}
	
	public Mono<ServerResponse> updateRoom(ServerRequest request) {
		String messageType = request.pathVariable("messageType");
		switch(messageType) {
		case "out":
			return request.bodyToMono(ChatMessageDTO.RequestLeaveRoom.class)
					.flatMap(param -> {
						return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
								.body(chatServiceImpl.leaveRoom(param), ChatMessageDTO.RequestLeaveRoom.class);
					});
		case "in":
			return request.bodyToMono(ChatMessageDTO.RequestParticipatedInRoom.class)
					.flatMap(param -> {
						return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
								.body(chatServiceImpl.participatedInRoom(param), ChatMessageDTO.RequestLeaveRoom.class);
					});
			
		case "invite":
			return request.bodyToMono(ChatMessageDTO.RequestInviteRoom.class)
					.flatMap(param -> {
						return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
								.body(accountServiceImpl.inviteRoom(param), ChatMessageDTO.RequestLeaveRoom.class);
					});
		default:
			return ServerResponse.notFound().build();
			
		}

		
	}
	
	public Mono<ServerResponse> creatMessage(ServerRequest request) {
		Mono<ChatMessageDTO.RequestMessage> messageSendMono = request.bodyToMono(ChatMessageDTO.RequestMessage.class);
		return messageSendMono
				.flatMap(message -> {
					return chatServiceImpl.send(message.toEntity());
				})
				.flatMap(message ->
					ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(messageSendMono, ChatMessageEntity.class));
	}
	
	public Mono<ServerResponse> getRoomMessage(ServerRequest serverRequest){
		return chatServiceImpl.getChatMessageByTopic(serverRequest);

	}
	
	public Mono<ServerResponse> getAccountMessage(ServerRequest serverRequest){
		return accountServiceImpl.getMessageByTopic(serverRequest);

	}

}
