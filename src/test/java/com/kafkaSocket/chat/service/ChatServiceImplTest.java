package com.kafkaSocket.chat.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import com.kafkaSocket.chat.dto.ChatMessageDTO;
import com.kafkaSocket.chat.enums.MessageType;
import com.kafkaSocket.chat.repository.ChatMessageRepository;
import com.kafkaSocket.chat.service.impl.ChatServiceImpl;

import reactor.core.publisher.Mono;

@WebFluxTest
public class ChatServiceImplTest {
	
	
	@MockBean
	ChatServiceImpl chatServiceImpl;
	
	@MockBean
	ChatMessageRepository chatMessageRepository;
	
	@MockBean
	KafkaTemplate<String, ChatMessageDTO.RequestMessage> kafkaTemplate;

	@Test
	public void testSend() {
		//given
		ChatMessageDTO.RequestMessage messageParam = ChatMessageDTO.RequestMessage
				.builder()
				.message("hihi")
				.roomIdx(3L)
				.userIdx(1L).build();
		
		//when
		Mono<String> senderResponse = chatServiceImpl.send(messageParam.toEntity());
		
		//then
		senderResponse.subscribe(System.out::println);
		
	}

	public void testProcessReceivedData() {
//		fail("Not yet implemented");
		
		
//		kafkaServiceImpl.processReceivedData();

	}

}
