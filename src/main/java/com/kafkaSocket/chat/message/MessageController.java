package com.kafkaSocket.chat.message;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kafkaSocket.chat.service.impl.KafkaConsumerServiceImpl;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import org.springframework.http.MediaType;

@RequiredArgsConstructor
@RestController
@RequestMapping("/kafka/v1")
public class MessageController {

	private final KafkaConsumerServiceImpl kafkaConsumerService;
	
//	@GetMapping(value="/room/{roomIdx}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<?> getEventsFlux(@PathVariable int roomIdx){
		return kafkaConsumerService.subscribe(roomIdx);
    }
}
