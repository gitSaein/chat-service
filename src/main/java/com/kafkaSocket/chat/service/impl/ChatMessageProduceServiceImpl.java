package com.kafkaSocket.chat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import com.kafkaSocket.chat.model.KafkaException;
import com.kafkaSocket.chat.model.ChatMessage;
import com.kafkaSocket.chat.service.KafkaProduceService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageProduceServiceImpl implements KafkaProduceService<ChatMessage> {
	
	private final static String TOPIC = "message.room.";	
	private final KafkaTemplate<String, ChatMessage> kafkaTemplate;
    
//https://projectreactor.io/docs/kafka/release/reference/
	@Override
	public Mono<String> send(ChatMessage cm){
	
		ListenableFuture<SendResult<String, ChatMessage>> kafkaResponse = 
				kafkaTemplate.send(TOPIC + cm.getRoomIdx(), cm);
		kafkaResponse.addCallback(new ListenableFutureCallback<SendResult<String, ChatMessage>>() {

			@Override
			public void onSuccess(SendResult<String, ChatMessage> result) {
				// TODO Auto-generated method stub
				log.info("success");
			}

			@Override
			public void onFailure(Throwable ex) {
				// TODO Auto-generated method stub
				log.info("fail");

			}
		});
        	
		return Mono.just(cm.toString());

				

	}

	@Override
	public Flux<ServerSentEvent<Object>> receive() {
		// TODO Auto-generated method stub
		return null;
	}

}
