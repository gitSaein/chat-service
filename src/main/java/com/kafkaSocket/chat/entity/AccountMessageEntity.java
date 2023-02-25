package com.kafkaSocket.chat.entity;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.kafkaSocket.chat.enums.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Document(collection = "accountMessages")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountMessageEntity extends BaseEntity {

	@Id
	private String id;
	private Long userIdx;
	private String message;
	private MessageType messageType;
	
}
