package com.kafkaSocket.chat.service.impl;

import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import com.kafkaSocket.chat.service.KafkaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
	
	private final KafkaSender<String, Object> kafkaSender;
	private final Sinks.Many<Object> sinksMany;
	private final ReceiverOptions<String, Object> receiverOptions;
	
	private String TOPIC = "test";
	private Disposable disposable;
	
	@PostConstruct
	public void init() {
		disposable = KafkaReceiver.create(receiverOptions).receive()
				.doOnNext(processReceivedData())
				.doOnError(e -> {
					log.error("receiver init error: %d", e);
					init();
				})
				.subscribe();
	}
	
	@PreDestroy
	public void desctroy() {
		if (disposable != null) {
			disposable.dispose();
		}
		kafkaSender.close();
	}

	@Override
	public Mono<Boolean> send(String topic, String key, Object value) {
		// TODO Auto-generated method stub
		return kafkaSender.createOutbound()
				.send(Mono.just(new ProducerRecord<>(TOPIC, key, value)))
				.then()
				.map(ret -> true)
				.onErrorResume(e -> {
					log.error("Kafka send error: %d", e);
					return Mono.just(false);
				});
	}

	@Override
	public Consumer<ReceiverRecord<String, Object>> processReceivedData() {
		// TODO Auto-generated method stub
		return r -> {
			log.info("received: %d", r);
			Object receivedData = r.value();
			if (receivedData != null) {
				sinksMany.emitNext(r.value(), Sinks.EmitFailureHandler.FAIL_FAST);
			}
			r.receiverOffset().acknowledge();
		};
	}

}
