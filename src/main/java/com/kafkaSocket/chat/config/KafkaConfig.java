package com.kafkaSocket.chat.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;

import reactor.core.scheduler.Schedulers;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaConfig {
	
	@Value("${kafka.host}")
	private String host;
	
	@Value("${kafka.port}")
	private String port;
	
	@Bean("kafkaSender")
	public KafkaSender<String, Object> kafkaSender() {
		SenderOptions<String, Object> senderOptions = SenderOptions.create(getProducerProps());
        senderOptions.scheduler(Schedulers.parallel());
        senderOptions.closeTimeout(Duration.ofSeconds(5));
        return KafkaSender.create(senderOptions);	
    }
	
    private Map<String, Object> getProducerProps() {
		return new HashMap<>() {{
			put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host + ":" + port);
			put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 2000);
		}};
	
	}

}
