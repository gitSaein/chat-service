package com.kafkaSocket.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}

//
//	@Bean
//	public NewTopic topic() {
//		return TopicBuilder.name("topic1")
//				.partitions(0)
//				.replicas(1)
//				.build();
//	}
//
//	@KafkaListener(id = "myId", topics = "topic1")
//	public void listen(String in) {
//		System.out.println(in);
//	}
//
//	@Bean
//	public ApplicationRunner runner(KafkaTemplate<String, String> template) {
//		return args -> {;
//		template.send("topic1", "test");
//		};
//	}
//

}
