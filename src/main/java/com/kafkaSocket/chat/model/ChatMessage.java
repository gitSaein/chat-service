package com.kafkaSocket.chat.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(value = "chatMessages")
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage extends BaseEntity {

	public enum MessageType {
		CHAT_CREATE, CHAT_IN, CHAT_OUT, CHAT_MESSAGE
	}
	
	private Integer roomIdx;
	private Integer userIdx;
	private String message;
	private MessageType messageType;
	
}
