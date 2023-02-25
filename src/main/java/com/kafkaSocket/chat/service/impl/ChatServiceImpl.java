package com.kafkaSocket.chat.service.impl;

import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kafkaSocket.chat.dto.ChatMessageDTO;
import com.kafkaSocket.chat.entity.ChatMessageEntity;
import com.kafkaSocket.chat.entity.ChatRoomsEntity;
import com.kafkaSocket.chat.repository.ChatMessageRepository;
import com.kafkaSocket.chat.repository.ChatRoomsRepository;
import com.kafkaSocket.chat.service.KafkaConsumerService;
import com.kafkaSocket.chat.service.KafkaProduceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements KafkaConsumerService<ChatMessageEntity>, KafkaProduceService<ChatMessageEntity>{

	private final SinkServiceImpl<ChatMessageEntity> sinkService;
	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomsRepository chatRoomsRepository;
	private final static String ROOM_TOPIC = "room.";	
	private final KafkaTemplate<String, ChatMessageEntity> kafkaTemplate;
    
   
    @Override
    @KafkaListener(groupId="room_message", topicPattern = ROOM_TOPIC + "*")
	public void consume(ChatMessageEntity chatMessage){
		sinkService.getSink(chatMessage.getRoomIdx().toString()).tryEmitNext(chatMessage);
	}
    
    public Mono<ServerResponse> getChatMessageByTopic(ServerRequest serverRequest){
		Integer roomIdx = Integer.parseInt(serverRequest.pathVariable("idx"));

		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(sinkService.asFlux(roomIdx.toString()), ChatMessageDTO.RequestMessage.class).log();
	}

  	@Override
  	public Mono<String> send(ChatMessageEntity chatMessageEntity){
  	
  		ListenableFuture<SendResult<String, ChatMessageEntity>> kafkaResponse = 
  				kafkaTemplate.send(ROOM_TOPIC + chatMessageEntity.getRoomIdx(), chatMessageEntity);
  		kafkaResponse.addCallback(new ListenableFutureCallback<SendResult<String, ChatMessageEntity>>() {

  			@Override
  			public void onSuccess(SendResult<String, ChatMessageEntity> result) {
  				log.info("success: {}", result);
  				ChatMessageEntity entity = chatMessageEntity;
  				Mono<ChatMessageEntity> entityr = chatMessageRepository.save(entity);
  				entityr.subscribe(System.out::println);
  			}

  			@Override
  			public void onFailure(Throwable ex) {
  				log.info("fail");

  			}
  		});
          	
  		return Mono.just(chatMessageEntity.toString());	
  	}
  	
  	public Mono<ChatRoomsEntity> createRoom(ChatMessageDTO.RequestCreateRoom dto) {
  		return chatRoomsRepository.save(dto.toChatRoomEntity())
  				.doOnSuccess(chatRoom -> {
  					this.send(dto.toChatMessageEntity());
  				});
  	}
  	
  	public Mono<ChatRoomsEntity> leaveRoom(ChatMessageDTO.RequestLeaveRoom dto) {
  		return chatRoomsRepository.findById(dto.getRoomId())
  				.flatMap(chatRoom -> {
  					int idx = chatRoom.getParticipants().indexOf(dto.getUserIdx());
  					if(idx > -1 ) {
  						chatRoom.getParticipants().remove(idx);
  					}
  					if(chatRoom.getParticipants().size() == 0) {
  						return Mono.just(chatRoom);
  					}
  					if(chatRoom.getManagerIdx() == dto.getUserIdx()) {
  						chatRoom.changedManager(chatRoom.getParticipants().get(0));
  					}
  					return Mono.just(chatRoom);
  				})
  				.flatMap(this.chatRoomsRepository::save)
  				.doOnSuccess(chatRoom -> {
  					this.send(dto.toChatMessageEntity(chatRoom.getRoomIdx()));
  				});
  		
  	}
  	
  	public Mono<ChatRoomsEntity> participatedInRoom(ChatMessageDTO.RequestParticipatedInRoom dto) {
  		return chatRoomsRepository.findById(dto.getRoomId())
  				.map(v -> {
					int idx = v.getParticipants().indexOf(dto.getUserIdx());
					if( idx == -1) {
						v.getParticipants().add(dto.getUserIdx());
					}
  					return v;
  				})
  				.doOnNext(this.chatRoomsRepository::save)
  				.doOnSuccess(chatRoom -> {
  					this.send(dto.toChatMessageEntity(chatRoom.getRoomIdx()));  					
  				});
  	}
  	
}
