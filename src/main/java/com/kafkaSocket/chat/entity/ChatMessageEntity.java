package com.kafkaSocket.chat.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.kafkaSocket.chat.enums.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Document(collection = "chatMessages")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageEntity extends BaseEntity {

	private Long roomIdx;
	private Long userIdx;
	private String message;
	private MessageType messageType;
	
}
