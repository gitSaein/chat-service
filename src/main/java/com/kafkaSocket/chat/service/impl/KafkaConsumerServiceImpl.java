package com.kafkaSocket.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.kafkaSocket.chat.model.KafkaException;
import com.kafkaSocket.chat.param.MessageParam;
import com.kafkaSocket.chat.service.KafkaConsumerService;
import com.kafkaSocket.chat.service.KafkaProduceService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService<MessageParam> {
	
	private final static String TOPIC = ".room.message";	
    private final KafkaTemplate<String, MessageParam> kafkaTemplate;

    @Autowired
    public KafkaConsumerServiceImpl(KafkaTemplate<String, MessageParam> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @KafkaListener(groupId="chat_message", topics = "1.room.message")
	@Override
	public void consume(MessageParam mp){
		
		try {
			log.info(mp.toString());
		} catch (Exception e) {
//			return Mono.error(KafkaException.SEND_ERROR);

		}
//		return Mono.just("success");

	}

	@Override
	public Flux<ServerSentEvent<MessageParam>> receive() {
		// TODO Auto-generated method stub
		return null;
	}

}
