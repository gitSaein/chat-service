package com.kafkaSocket.chat.message;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
@RequiredArgsConstructor
public class MessageRouter {

	@Bean
	public RouterFunction<ServerResponse> routes(MessageHandler postHandler){
		return route().path("/kafka/v1", builder -> builder
					.GET("/hello",accept(MediaType.APPLICATION_JSON),
					        request -> ServerResponse.ok().bodyValue("Hello World"))
					.POST("/chat/create", postHandler::createFromJson)
					.POST("/room/messages", postHandler::sendFromJson)
					.GET("/subscribe", postHandler::subscribe)
				).build();
	}
}
