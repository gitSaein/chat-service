package com.kafkaSocket.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.kafkaSocket.chat.model.KafkaException;
import com.kafkaSocket.chat.param.MessageParam;
import com.kafkaSocket.chat.service.KafkaConsumerService;
import com.kafkaSocket.chat.service.KafkaProduceService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService<MessageParam> {
	
	private final static String TOPIC = ".room.message";	
    private final KafkaTemplate<String, MessageParam> kafkaTemplate;

    @Autowired
    public KafkaConsumerServiceImpl(KafkaTemplate<String, MessageParam> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

	@Override
	public Mono<String> send(MessageParam mp){
		
		try {
			kafkaTemplate.send(mp + TOPIC, mp);
		} catch (Exception e) {
			return Mono.error(KafkaException.SEND_ERROR);

		}
		return Mono.just("success");

	}

	@Override
	public Flux<ServerSentEvent<Object>> receive() {
		// TODO Auto-generated method stub
		return null;
	}

}
