package com.kafkaSocket.chat.service.impl;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.kafkaSocket.chat.model.ChatMessage;
import com.kafkaSocket.chat.repository.ChatMessageRepository;
import com.kafkaSocket.chat.service.KafkaConsumerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.LinkedList;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements KafkaConsumerService<ChatMessage> {

	private final SinkService sinkService;
	private final ChatMessageRepository chatMessageRepository;
//    private final SocketHandler socketHandler;

	List<ChatMessage> chatMessageList = new LinkedList<>();
   
    @KafkaListener(groupId="chat_message", topics = "1.room.message")
	@Override
	public void consume(ChatMessage cm){

//			Mono<ChatMessage> chatMessage = chatMessageRepository.save(cm);
		Mono.just(cm)
				.doOnNext(e -> {
					chatMessageList.add(0, e);
					log.info(e.toString());
					sinkService.getSink().tryEmitNext(cm);
				})
				.doOnError(e -> log.error("kafka consumer: ", e))
				.subscribe();


	}


}
