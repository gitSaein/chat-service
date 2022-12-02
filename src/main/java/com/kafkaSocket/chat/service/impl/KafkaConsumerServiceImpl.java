package com.kafkaSocket.chat.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
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

	private Properties initKafkaSettings(){
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
		props.put("partition.assignment.strategy", "range");
		return props;
	}

//    @KafkaListener(groupId="chat_message", topics = "1.room.message")
	@Override
	public void consume(ChatMessage chatMessage){
		log.info(chatMessage.toString());

		chatMessageRepository.save(chatMessage);
		sinkService.getSink().tryEmitNext(chatMessage);
	}

	public Flux<ChatMessage> subscribe(int roomIdx){

		ReceiverOptions<String, ChatMessage> receiverOptions = ReceiverOptions.<String, ChatMessage>create()
				.subscription(Collections.singleton(roomIdx + ".room.chat"));
//		Flux<ReceiverRecord<String, ChatMessage>> inboundFlux = KafkaReceiver.create(receiverOptions);
		return KafkaReceiver.create(receiverOptions)
				.receive()
				.log()
				.doOnNext(record -> {
					log.info("Receiver message: %s\n", record);
					record.receiverOffset().acknowledge();
				})
				.map(ReceiverRecord::value);
	}


}
