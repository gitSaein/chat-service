package com.kafkaSocket.chat.router;

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
					beanMethod = "creatMessage",
					operation = @Operation(
							summary = "채팅방에 메시지 송신 ",
							operationId = "creatMessage",
							requestBody = @RequestBody(
									content = @Content(
											schema = @Schema(
													implementation = ChatMessageDTO.RequestMessage.class))))),
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
			
			@RouterOperation(
					path = "/chat/v1/room/out", 
					method = RequestMethod.PUT, 
					beanClass = MessageHandler.class, 
					beanMethod = "updateRoom",
					operation = @Operation(
							summary = "채팅방 나가기 ",
							operationId = "updateRoom",
							requestBody = @RequestBody(
									content = @Content(
											schema = @Schema(
													implementation = ChatMessageDTO.RequestLeaveRoom.class))))),
			@RouterOperation(
					path = "/chat/v1/room/in", 
					method = RequestMethod.PUT, 
					beanClass = MessageHandler.class, 
					beanMethod = "updateRoom",
					operation = @Operation(
							summary = "채팅방 입장 ",
							operationId = "updateRoom",
							requestBody = @RequestBody(
									content = @Content(
											schema = @Schema(
													implementation = ChatMessageDTO.RequestParticipatedInRoom.class))))),
			@RouterOperation(
					path = "/chat/v1/room/invite", 
					method = RequestMethod.PUT, 
					beanClass = MessageHandler.class, 
					beanMethod = "updateRoom",
					operation = @Operation(
							summary = "채팅방 초대 ",
							operationId = "updateRoom",
							requestBody = @RequestBody(
									content = @Content(
											schema = @Schema(
													implementation = ChatMessageDTO.RequestInviteRoom.class))))),
			
			@RouterOperation(
					path = "/chat/v1/message/room/{idx}", 
					method = RequestMethod.GET, 
					produces = MediaType.TEXT_EVENT_STREAM_VALUE,
					beanClass = MessageHandler.class, 
					beanMethod = "getRoomMessage",
					operation = @Operation(
							summary = "채팅방 메시지 수신 ",
							operationId = "getRoomMessage",
							parameters = {
									@Parameter(in = ParameterIn.PATH, name="idx", description="")
							})),
			@RouterOperation(
					path = "/chat/v1/message/account/{idx}", 
					method = RequestMethod.GET, 
					produces = MediaType.TEXT_EVENT_STREAM_VALUE,
					beanClass = MessageHandler.class, 
					beanMethod = "getAccountMessage",
					operation = @Operation(
							summary = "계정 메시지 수신 ",
							operationId = "getAccountMessage",
							parameters = {
									@Parameter(in = ParameterIn.PATH, name="idx", description="")
							})),
	})	
	@Bean
	public RouterFunction<ServerResponse> routes(MessageHandler postHandler){
		return route().path("/chat/v1", builder -> builder
					.POST("/room", postHandler::createRoom)
					.PUT("/room/{messageType}", postHandler::updateRoom)
					.POST("/message", postHandler::creatMessage)
					.GET("/message/room/{idx}", postHandler::getRoomMessage)	
					.GET("/message/account/{idx}", postHandler::getAccountMessage)					

				).build();
	}
}
