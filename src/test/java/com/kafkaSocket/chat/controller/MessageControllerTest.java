package com.kafkaSocket.chat.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafkaSocket.chat.model.ChatMessage;
import com.kafkaSocket.chat.service.KafkaProduceService;
import com.kafkaSocket.chat.service.impl.KafkaProduceServiceImpl;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
//@WebFluxTest(MessageController.class)
class MessageControllerTest {
	
	@Autowired
	private WebTestClient webClient;
	
	@MockBean
	private KafkaProduceService<ChatMessage> messageService;

	@Test
	void testSendMessage() {
		
		//given
		ChatMessage messageParam = ChatMessage.builder()
				.roomIdx(100)
				.message("hi").build();
		Mono<String> tfMono = Mono.just("success");
		
		
		//when
		try {
			when(messageService.send(messageParam))
			.thenReturn(tfMono);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Mono<Boolean> senderResponse = kafkaService.send("test", messageParam.getRoomKey(), messageParam);
		
		//then
		this.webClient.post().uri("/send")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(messageParam), ChatMessage.class)
		.exchange()
		.expectStatus().isOk()
		.expectBody(Boolean.class)
		.consumeWith(result -> {
	        // custom assertions (e.g. AssertJ)...
			Boolean map = result.getResponseBody();

			log.info("hi: {}", result);
	        });
	}

}
