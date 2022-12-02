package com.kafkaSocket.chat.message;

import com.kafkaSocket.chat.service.impl.KafkaConsumerServiceImpl;
import com.kafkaSocket.chat.service.impl.KafkaProduceServiceImpl;
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
	
	private final KafkaProduceServiceImpl messageService;
	private final SinkServiceImpl sinkService;
	private final KafkaConsumerServiceImpl kafkaConsumerService;

	public Mono<ServerResponse> createFromJson(ServerRequest request){
		
		Mono<CreateChatParam> chatCreateMono = request.bodyToMono(CreateChatParam.class);
		
		return chatCreateMono.flatMap(chatCreate ->
				ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(chatCreateMono, CreateChatParam.class));

	}
	
	public Mono<ServerResponse> sendFromJson(ServerRequest request) {
		
		Mono<ChatMessage> messageSendMono = request.bodyToMono(ChatMessage.class);
		return messageSendMono
				.flatMap(message -> messageService.send(message))
				.flatMap(message ->
					ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(messageSendMono, ChatMessage.class));
	}

	public Mono<ServerResponse> subscribe(ServerRequest serverRequest){
		Integer roomIdx = Integer.parseInt(serverRequest.pathVariable("roomIdx"));
		return ServerResponse.ok()
				.contentType(MediaType.TEXT_EVENT_STREAM)
				.body(sinkService.asFlux(), ChatMessage.class).log();
	}

	public Mono<ServerResponse> consumeChatMessage(ServerRequest serverRequest){
		Integer roomIdx = Integer.parseInt(serverRequest.pathVariable("roomIdx"));
		return ServerResponse.ok()
				.contentType(MediaType.TEXT_EVENT_STREAM)
				.body(kafkaConsumerService.subscribe(roomIdx), ChatMessage.class).log();

	}
}
