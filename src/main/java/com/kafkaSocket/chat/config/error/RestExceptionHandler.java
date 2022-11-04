package com.kafkaSocket.chat.config.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kafkaSocket.chat.model.KafkaException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
	
	@ExceptionHandler(KafkaException.class)
	ResponseEntity<Object> kafkaException(KafkaException e) {
		log.debug("handling exception::" + e);
		return ResponseEntity.accepted().build();
	}
}
