package com.kafkaSocket.chat.service.impl;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import com.kafkaSocket.chat.model.ChatMessage;
import com.kafkaSocket.chat.repository.ChatMessageRepository;
import com.kafkaSocket.chat.service.KafkaConsumerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements KafkaConsumerService<ChatMessage> {

	private final SinkServiceImpl sinkService;
	private final ChatMessageRepository chatMessageRepository;

	@Value("spring.kafka.consumer.group_id")
	private String groupId;
	@Value("spring.kafka.consumer.group_id")
	private String bootstrapServer;
	@Value("spring.kafka.consumer.key-deserializer")
	private String keyDeserializer;
	@Value("spring.kafka.consumer.key-deserializer")
	private String keySerializer;

	private Map<String, Object> initKafkaSettings(){
		Map<String, Object> consumerProps = new HashMap<>();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "chat-message");
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.kafkaSocket.chat.model");


		return consumerProps;
	}
	


//    @KafkaListener(groupId="chat_message", topics = "1.room.message")
	@Override
	public void consume(ChatMessage chatMessage){
		log.info(chatMessage.toString());

		chatMessageRepository.save(chatMessage);
		sinkService.getSink().tryEmitNext(chatMessage);
	}

	public Flux<ChatMessage> subscribe(int roomIdx){

		ReceiverOptions<String, ChatMessage> receiverOptions = 
				ReceiverOptions.<String, ChatMessage>create(initKafkaSettings())
				.subscription(Collections.singleton(roomIdx + ".room.message"));
//		Flux<ReceiverRecord<String, ChatMessage>> inboundFlux = KafkaReceiver.create(receiverOptions);
		Flux<ChatMessage> response =  KafkaReceiver.create(receiverOptions)
				.receive()
				.doOnError(e -> log.error("[ERROR]", e))
				.doOnNext(record -> {
					log.info("Receiver message: %s\n", record);
					record.receiverOffset().acknowledge();
				})
				.map(ReceiverRecord::value);
		
		return response;
	}


}
