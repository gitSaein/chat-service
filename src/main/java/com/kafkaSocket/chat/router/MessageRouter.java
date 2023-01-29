package com.kafkaSocket.chat.router;

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

import com.kafkaSocket.chat.dto.ChatMessageDTO;
import com.kafkaSocket.chat.handler.MessageHandler;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;


@OpenAPIDefinition(info = @Info(title = "Chat-Service API", version = "0.0.1", 
description = "채팅방, 메시지 REST APIs v0.0.1"))
@Tag(name = "Chat API")
@Configuration
public class MessageRouter {

	@RouterOperations({
			@RouterOperation(
					path = "/chat/v1/message", 
					method = RequestMethod.POST,
					beanClass = MessageHandler.class, 
					beanMethod = "create",
					operation = @Operation(
							summary = "채팅방에 메시지 송신 ",
							operationId = "create",
							requestBody = @RequestBody(
									content = @Content(
											schema = @Schema(
													implementation = ChatMessageDTO.RequestMessage.class))))),
			@RouterOperation(
					path = "/chat/v1/message/room/{roomIdx}", 
					method = RequestMethod.GET, 
					produces = MediaType.TEXT_EVENT_STREAM_VALUE,
					beanClass = MessageHandler.class, 
					beanMethod = "get",
					operation = @Operation(
							summary = "채팅방 메시지 수신 ",
							operationId = "get",
							parameters = {
									@Parameter(in = ParameterIn.PATH, name="roomIdx", description="")
							})),
			@RouterOperation(
					path = "/chat/v1/room", 
					method = RequestMethod.POST, 
					beanClass = MessageHandler.class, 
					beanMethod = "createRoom",
					operation = @Operation(
							summary = "채팅방 생성",
							operationId = "createRoom",
							requestBody = @RequestBody(
									content = @Content(
											schema = @Schema(
													implementation = ChatMessageDTO.RequestCreateRoom.class))))),
	})	
	@Bean
	public RouterFunction<ServerResponse> routes(MessageHandler postHandler){
		return route().path("/chat/v1", builder -> builder
					.POST("/room", postHandler::createRoom)
					.POST("/message", postHandler::create)
					.GET("/message/room/{roomIdx}", postHandler::get)					
				).build();
	}
}
