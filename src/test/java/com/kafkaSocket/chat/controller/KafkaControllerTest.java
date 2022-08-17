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

import com.kafkaSocket.chat.param.MessageParam;
import com.kafkaSocket.chat.service.impl.KafkaServiceImpl;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(KafkaController.class)
@Slf4j
class KafkaControllerTest {
	
	@Autowired
	private WebTestClient webClient;
	
	@MockBean
	private KafkaServiceImpl kafkaService;

	@Test
	void testSendMessage() {
		
		//given
		MessageParam messageParam = MessageParam.builder()
				.roomKey("100")
				.content("hi").build();
		Mono<Boolean> tfMono = Mono.just(true);
		
		
		//when
		when(kafkaService.send("test", messageParam.getRoomKey(), messageParam))
		.thenReturn(tfMono);
//		Mono<Boolean> senderResponse = kafkaService.send("test", messageParam.getRoomKey(), messageParam);
		
		//then
		this.webClient.post().uri("/send")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(messageParam), MessageParam.class)
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
