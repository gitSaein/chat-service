package com.kafkaSocket.chat.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kafkaSocket.chat.model.ChatMessage;
import com.kafkaSocket.chat.model.ChatMessage.MessageType;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@DataMongoTest
@ExtendWith(SpringExtension.class)
class ChatMessageRepositoryTest {

	private final ChatMessageRepository chatMessageRepository;
	
	@ParameterizedTest
    @MethodSource("sendMessage")
	void test(ChatMessage chatMessage) {
		
		//when
		Mono<ChatMessage> result = chatMessageRepository.save(chatMessage);
		
		//test
		
		
		assertEquals(Mono.just(chatMessage), result);
	}
	
    private static Stream<ChatMessage> sendMessage() {
        return Stream.of(
        		ChatMessage.builder()
				.roomIdx(1)
				.message("jiji")
				.messageType(MessageType.CHAT_MESSAGE)
				.build(),
				ChatMessage.builder()
				.roomIdx(1)
				.message("jijidddddddd")
				.messageType(MessageType.CHAT_MESSAGE)
				.build()
        );
    }

}
