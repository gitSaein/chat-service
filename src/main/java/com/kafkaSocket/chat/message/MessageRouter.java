package com.kafkaSocket.chat.message;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;


@OpenAPIDefinition(info = @Info(title = "Chat-Service API", version = "0.0.1", 
description = "채팅방, 메시지 REST APIs v0.0.1"))
@Configuration
public class MessageRouter {


	@Bean
	@RouterOperations({
			@RouterOperation(
					path = "/chat/v1/message", 
					method = RequestMethod.POST, 
					produces = {MediaType.APPLICATION_JSON_VALUE}, 
					beanClass = MessageHandler.class, 
					beanMethod = "createMessage",
					operation = @Operation(operationId = "createMessage",
						requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = ChatMessageEntity.class)))
					)),
			
	})	
	public RouterFunction<ServerResponse> routes(MessageHandler postHandler){
		return route().path("/chat/v1", builder -> builder
					.GET("/hello",accept(MediaType.APPLICATION_JSON),
					        request -> ServerResponse.ok().bodyValue("Hello World"))
					.POST("/create", postHandler::createFromJson)
					.POST("/message", postHandler::createMessage)
					.GET("/message/room/{roomIdx}", postHandler::getChatMessage)					
				).build();
	}
}
