package com.kafkaSocket.chat.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.kafkaSocket.chat.param.MessageParam;
import com.kafkaSocket.chat.service.impl.KafkaServiceImpl;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest
public class KafkaServiceTest {
	
	
	
	@MockBean
	KafkaServiceImpl kafkaServiceImpl;

	@Test
	public void testSend() {
		
		//given
		MessageParam messageParam = MessageParam.builder()
				.roomKey("100")
				.content("hi").build();
		
		//when
		Mono<Boolean> senderResponse = kafkaServiceImpl.send("test", messageParam.getRoomKey(), messageParam);
		
		//then
		StepVerifier.create(senderResponse)
		.expectNext(true)
		.verifyComplete();
		
	}

	@Test
	public void testProcessReceivedData() {
//		fail("Not yet implemented");
		
		
//		kafkaServiceImpl.processReceivedData();

	}

}
