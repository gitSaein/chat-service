package com.kafkaSocket.chat.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kafkaSocket.chat.entity.ChatMessageEntity;
import com.kafkaSocket.chat.enums.MessageType;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RequiredArgsConstructor
@DataMongoTest
@ExtendWith(SpringExtension.class)
class ChatMessageRepositoryTest {

	@Autowired
	private ChatMessageRepository chatMessageRepository;
	
    private static Stream<Arguments> sendMessage() {
        return Stream.of(
        		Arguments.of(1,1, "---- test ----", MessageType.CHAT_MESSAGE),
        		Arguments.of(1,1, "---- test2 ----", MessageType.CHAT_MESSAGE)
        );
    }
	
	@DisplayName("save message on mongoDB")
	@ParameterizedTest
    @MethodSource("sendMessage")
	void test(Long roomIdx, Long userIdx, String message, MessageType messageType) {
		//given
		ChatMessageEntity chatMessage = ChatMessageEntity.builder()
		.roomIdx(roomIdx)
		.userIdx(userIdx)
		.message(message)
		.messageType(MessageType.CHAT_MESSAGE)
		.build();
		
		//when
		Mono<ChatMessageEntity> result = chatMessageRepository.save(chatMessage);
		
		//test
		StepVerifier
		.create(result)
		.assertNext(value -> {	
			assertEquals(chatMessage.getRoomIdx(), value.getRoomIdx());
		})
		.expectComplete()
		.verify();
	}
	

}
