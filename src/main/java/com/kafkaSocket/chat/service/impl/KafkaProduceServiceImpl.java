package com.kafkaSocket.chat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

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
public class KafkaProduceServiceImpl implements KafkaProduceService<ChatMessage> {
	
	private final static String TOPIC = ".room.message";	
    
    private Map<String, Object> initKafkaSettings(){
		Map<String, Object> produeceProps = new HashMap<>();
		produeceProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		produeceProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		produeceProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return produeceProps;
	}

//https://projectreactor.io/docs/kafka/release/reference/
	@Override
	public Mono<String> send(ChatMessage mp){
		
//		try {
//			kafkaTemplate.send(mp.getRoomIdx() + TOPIC, mp);
//		} catch (Exception e) {
//			return Mono.error(KafkaException.SEND_ERROR);
//
//		}
//		return Mono.just(mp.toString());
		KafkaSender.create(SenderOptions
				.<String, ChatMessage>create(initKafkaSettings()))
		.send(Mono.just(SenderRecord.create(new ProducerRecord<>(mp.getRoomIdx() + TOPIC,mp),1)))
				.then()
				.doOnError(e -> log.error("Error: {}", e))
				.doOnSuccess(e -> log.info("Success: {}", e))
				.subscribe();
		return Mono.empty();
		
		
				

	}

	@Override
	public Flux<ServerSentEvent<Object>> receive() {
		// TODO Auto-generated method stub
		return null;
	}

}
