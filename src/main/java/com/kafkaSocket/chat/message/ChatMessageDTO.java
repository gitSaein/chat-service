package com.kafkaSocket.chat.message;


import com.kafkaSocket.chat.enums.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class ChatMessageDTO {
	
	@Getter
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class RequestMessage{
		private Integer roomIdx;
		private Integer userIdx;
		private String message;
		private MessageType messageType;
		
		public ChatMessageEntity toEntity() {
			return ChatMessageEntity.builder()
					.roomIdx(roomIdx)
					.userIdx(userIdx)
					.message(message)
					.messageType(messageType)
					.build();
		}
	}

	
	public static class Result<T> {
	
		
		private T data;
	}
	
}
