package com.kafkaSocket.chat;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.RequestBody;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@RestController
@Slf4j
public class KafkaController{

//	@GetMapping("/hello")
	Flux<String> hello(){
		log.info("hello ~~");
		return Flux.just("hello", "world");
	}

//	@GetMapping("/stream")
	Flux<Map<String, Integer>> stream() {
		Stream<Integer> stream = Stream.iterate(0, i -> i + 1);
		return Flux.fromStream(stream).zipWith(Flux.interval(Duration.ofSeconds(1)))
				.map(tuple -> Collections.singletonMap("value", tuple.getT1()));
	}

//	@PostMapping("/echo")
	Mono<String> echo(@RequestBody Mono<String> body){
		return body.map(String::toUpperCase);
	}

}
