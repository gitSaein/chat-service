//package com.kafkaSocket.chat.controller;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.Map;
//import java.util.stream.Stream;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.kafkaSocket.chat.param.MessageParam;
//import com.kafkaSocket.chat.service.impl.KafkaServiceImpl;
//import com.kafkaSocket.chat.service.impl.MessageServiceImpl;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
////
////@Slf4j
////@RestController("/kafka/v1/room/messages")
////@RequiredArgsConstructor
//public class MessageController{
//	
//	private final MessageServiceImpl messageService;
//
////	@GetMapping("/hello")
//	Flux<String> hello(){
//		log.info("hello ~~");
//		return Flux.just("hello", "world");
//	}
//
////	@GetMapping("/stream")
//	Flux<Map<String, Integer>> stream() {
//		Stream<Integer> stream = Stream.iterate(0, i -> i + 1);
//		return Flux.fromStream(stream).zipWith(Flux.interval(Duration.ofSeconds(1)))
//				.map(tuple -> Collections.singletonMap("value", tuple.getT1()));
//	}
//
////	@PostMapping("/echo")
//	Mono<String> echo(@RequestBody Mono<String> body){
//		return body.map(String::toUpperCase);
//	}
//	
//	@PostMapping
//	public Mono<String> sendMessage(@RequestBody MessageParam messageParam){
//		return messageService.send(messageParam);
//
//	}
//	
////	public Flux<ReceiverRecord<Object>> consumeMessage() {
////		return kafkaService.processReceivedData();
////	}
//
//}
