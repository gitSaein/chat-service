package com.kafkaSocket.chat.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
public class BaseEntity {

	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdDate;
 
	@LastModifiedDate
    @Column(updatable = true)
	private LocalDateTime lastModifiedDate;

}
