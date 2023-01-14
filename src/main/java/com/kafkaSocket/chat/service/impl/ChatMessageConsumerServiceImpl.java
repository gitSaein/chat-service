package com.kafkaSocket.chat.service.impl;

import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kafkaSocket.chat.model.ChatMessage;
import com.kafkaSocket.chat.repository.ChatMessageRepository;
import com.kafkaSocket.chat.service.KafkaConsumerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageConsumerServiceImpl implements KafkaConsumerService<ChatMessage> {

	private final SinkServiceImpl sinkService;
	private final ChatMessageRepository chatMessageRepository;

	@Override
    @KafkaListener(groupId="chat_message", topicPattern = "message.room.*")
	public void consume(ChatMessage chatMessage){
		log.info(chatMessage.toString());

		chatMessageRepository.save(chatMessage);
		sinkService.getSink(chatMessage.getRoomIdx().toString()).tryEmitNext(chatMessage);
	}

	
	public Mono<ServerResponse> getChatMessageByTopic(ServerRequest serverRequest){
		Integer roomIdx = Integer.parseInt(serverRequest.pathVariable("roomIdx"));

		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(sinkService.asFlux(roomIdx.toString()), ChatMessage.class).log();
	}

}
