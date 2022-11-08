package com.kafkaSocket.chat.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Id;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseEntity {

	@Id
	private ObjectId id;
	
	@CreatedDate
	private LocalDateTime createdDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
 
	

}
