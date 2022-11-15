package com.kafkaSocket.chat.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebFluxConfig {
	
	private final WebFluxWebSocketHandler socketHandler;
	
	@Bean
	public HandlerMapping handlerMapping() {
		Map<String, WebFluxWebSocketHandler> handlerMap = Map.of(
				"/push", socketHandler);
		
		return new SimpleUrlHandlerMapping(handlerMap, 1);
				
	}

}
