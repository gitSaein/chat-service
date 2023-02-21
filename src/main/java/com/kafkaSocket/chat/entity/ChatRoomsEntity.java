package com.kafkaSocket.chat.entity;

import java.util.List;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatRooms")
public class ChatRoomsEntity extends BaseEntity {

	@Id
	private String id;
	private Long roomIdx;
	private String name;
	private String editedName;
	private Long managerIdx;
	private List<Long> participants;
	
	public void changedManager(Long userIdx) {
		this.managerIdx = userIdx;
	}
	
}