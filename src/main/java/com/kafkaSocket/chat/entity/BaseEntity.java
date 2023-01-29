package com.kafkaSocket.chat.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
public class BaseEntity {

	@Id
	private String id;
	
	@CreatedDate
	private LocalDateTime createdDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
 
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;

}
