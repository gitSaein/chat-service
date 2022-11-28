package com.kafkaSocket.chat.service.impl;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.kafkaSocket.chat.model.ChatMessage;
import com.kafkaSocket.chat.repository.ChatMessageRepository;
import com.kafkaSocket.chat.service.KafkaConsumerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerServiceImpl implements KafkaConsumerService<ChatMessage> {

	private final SinkServiceImpl sinkService;
	private final ChatMessageRepository chatMessageRepository;

    @KafkaListener(groupId="chat_message", topics = "1.room.message")
	@Override
	public void consume(ChatMessage chatMessage){
		log.info(chatMessage.toString());

		chatMessageRepository.save(chatMessage);
		sinkService.getSink().tryEmitNext(chatMessage);
	}



}
