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

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements KafkaConsumerService<ChatMessage> {

	private final ChatMessageRepository chatMessageRepository;
    
    @KafkaListener(groupId="chat_message", topics = "1.room.message")
	@Override
	public void consume(ChatMessage cm){
		
		try {
			log.info(cm.toString());
			Mono<ChatMessage> chatMessage = chatMessageRepository.save(cm);
			
			chatMessage.subscribe(e -> {
				log.info(e.toString());

			});
		} catch (Exception e) {
			log.error("[ERROR]", e);
//			return Mono.error(KafkaException.SEND_ERROR);

		}
//		return Mono.just("success");

	}

	@Override
	public Flux<ServerSentEvent<ChatMessage>> receive() {
		// TODO Auto-generated method stub
		return null;
	}

}
