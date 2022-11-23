package com.kafkaSocket.chat.config;

import com.kafkaSocket.chat.service.impl.SinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafkaSocket.chat.model.ChatMessage;
import com.kafkaSocket.chat.param.CreateChatParam;
import com.kafkaSocket.chat.service.KafkaProduceService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageHandler {
	
	private final KafkaProduceService<ChatMessage> messageService;
	private final SinkService sinkService;

	public Mono<ServerResponse> createFromJson(ServerRequest request){
		
		Mono<CreateChatParam> chatCreateMono = request.bodyToMono(CreateChatParam.class);
		
		return chatCreateMono.flatMap(chatCreate ->
				ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(chatCreateMono, CreateChatParam.class));

	}
	
	public Mono<ServerResponse> sendFromJson(ServerRequest request) {
		
		Mono<ChatMessage> messageSendMono = request.bodyToMono(ChatMessage.class);
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

	public Mono<ServerResponse> subscribe(ServerRequest serverRequest){
		return ServerResponse.ok()
				.body(sinkService.getSink().asFlux().log(), ChatMessage.class);
	}

	//https://stackoverflow.com/questions/70684474/persisting-kafka-message-for-sse-client-once-they-disconnect
//	public Flux<>

}
