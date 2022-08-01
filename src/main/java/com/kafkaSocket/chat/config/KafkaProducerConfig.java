package com.kafkaSocket.chat.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.protocol.Message;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.kafkaSocket.chat.param.MessageParam;

@EnableKafka
@Configuration
public class KafkaProducerConfig {
	
	@Bean
	public Map<String, Object> producerConfigurations() {
		Map<String, Object> configurations = new HashMap<>();
		configurations.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
		configurations.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configurations.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return configurations;
	}
	
	@Bean
	public ProducerFactory<String, MessageParam> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigurations());
	}
	
	@Bean
	public KafkaTemplate<String, MessageParam> kafkaTemplate(){
		return new KafkaTemplate<>(producerFactory());
	}
}
