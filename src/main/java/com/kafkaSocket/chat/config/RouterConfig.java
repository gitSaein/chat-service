package com.kafkaSocket.chat.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kafkaSocket.chat.PostHandler;


@Configuration
public class RouterConfig {


	@Bean
	public RouterFunction<ServerResponse> routes(PostHandler postHandler){
		return route()
				.GET("/hello",accept(MediaType.APPLICATION_JSON),
				        request -> ServerResponse.ok().bodyValue("Hello World"))
				.POST("/chat/create", postHandler::createFromJson)
				.build();
	}
}
