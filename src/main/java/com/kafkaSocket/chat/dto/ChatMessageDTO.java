package com.kafkaSocket.chat.dto;


import java.util.List;

import com.kafkaSocket.chat.entity.ChatMessageEntity;
import com.kafkaSocket.chat.entity.ChatRoomsEntity;
import com.kafkaSocket.chat.enums.MessageType;

import io.swagger.v3.oas.annotations.media.Schema;
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
		@Schema(description = "채팅방 idx ")
		private Long roomIdx;
		@Schema(description = "채팅방 생성자  idx ")
		private Long userIdx;
		@Schema(description = "채팅방 메시지 내용 ")
		private String message;
		
		public ChatMessageEntity toEntity() {
			return ChatMessageEntity.builder()
					.roomIdx(roomIdx)
					.userIdx(userIdx)
					.message(message)
					.messageType(MessageType.CHAT_MESSAGE)
					.build();
		}
	}

	@Getter
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class RequestCreateRoom{
		@Schema(description = "채팅방 idx ")
		private Long roomIdx;
		@Schema(description = "채팅방 생성자  idx ")
		private Long managerIdx;
		@Schema(description = "채팅방 참여 idx 리스트 ")
		private List<Long> participants;
		@Schema(description = "채팅방 이름 ")
		private String name;
		
		public ChatMessageEntity toChatMessageEntity() {
			return ChatMessageEntity.builder()
					.roomIdx(roomIdx)
					.userIdx(managerIdx)
					.messageType(MessageType.CHAT_CREATE)
					.build();
		}
		
		public ChatRoomsEntity toChatRoomEntity() {
			return ChatRoomsEntity.builder()
					.roomIdx(roomIdx)
					.name(name)
					.managerIdx(managerIdx)
					.participants(participants).build();
		}

	}

	
	public static class Result<T> {
	
		
		private T data;
	}
	
}
