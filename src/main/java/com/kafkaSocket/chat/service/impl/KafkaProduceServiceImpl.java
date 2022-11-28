package com.kafkaSocket.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.kafkaSocket.chat.model.KafkaException;
import com.kafkaSocket.chat.model.ChatMessage;
import com.kafkaSocket.chat.service.KafkaProduceService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KafkaProduceServiceImpl implements KafkaProduceService<ChatMessage> {
	
	private final static String TOPIC = ".room.message";	
    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;


	@Override
	public Mono<String> send(ChatMessage mp){
		
		try {
			kafkaTemplate.send(mp.getRoomIdx() + TOPIC, mp);
		} catch (Exception e) {
			return Mono.error(KafkaException.SEND_ERROR);

		}
		return Mono.just(mp.toString());

	}

	@Override
	public Flux<ServerSentEvent<Object>> receive() {
		// TODO Auto-generated method stub
		return null;
	}

}
