package com.kafkaSocket.chat.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@Document(collection = "chatRooms")
public class ChatRoomsEntity extends BaseEntity{

	private Long roomIdx;
	private String name;
	private String editedName;
	private Long managerIdx;
	private List<Long> participants;
	
}
