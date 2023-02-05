package com.kafkaSocket.chat.service.impl;

import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

	private final SinkServiceImpl sinkService;
	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomsRepository chatRoomsRepository;
	private final static String TOPIC = "message.room.";	
	private final KafkaTemplate<String, ChatMessageEntity> kafkaTemplate;
    
   
    @Override
    @KafkaListener(groupId="chat_message", topicPattern = "message.room.*")
	public void consume(ChatMessageEntity chatMessage){
		sinkService.getSink(chatMessage.getRoomIdx().toString()).tryEmitNext(chatMessage);
	}
    
    public Mono<ServerResponse> getChatMessageByTopic(ServerRequest serverRequest){
		Integer roomIdx = Integer.parseInt(serverRequest.pathVariable("roomIdx"));

		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(sinkService.asFlux(roomIdx.toString()), ChatMessageDTO.RequestMessage.class).log();
	}


    
  //https://projectreactor.io/docs/kafka/release/reference/
  	@Override
  	public Mono<String> send(ChatMessageEntity chatMessageEntity){
  	
  		ListenableFuture<SendResult<String, ChatMessageEntity>> kafkaResponse = 
  				kafkaTemplate.send(TOPIC + chatMessageEntity.getRoomIdx(), chatMessageEntity);
  		kafkaResponse.addCallback(new ListenableFutureCallback<SendResult<String, ChatMessageEntity>>() {

  			@Override
  			public void onSuccess(SendResult<String, ChatMessageEntity> result) {
  				// TODO Auto-generated method stub
  				log.info("success: {}", result);
  				ChatMessageEntity entity = chatMessageEntity;
  				Mono<ChatMessageEntity> entityr = chatMessageRepository.save(entity);
  				entityr.subscribe(System.out::println);
  			}

  			@Override
  			public void onFailure(Throwable ex) {
  				// TODO Auto-generated method stub
  				log.info("fail");

  			}
  		});
          	
  		return Mono.just(chatMessageEntity.toString());	
  	}
  	
  	@Transactional
  	public Mono<String> createRoom(ChatMessageDTO.RequestCreateRoom dto) {
  		Mono<ChatRoomsEntity> entityr = chatRoomsRepository.save(dto.toChatRoomEntity()).log();
  		entityr.subscribe(System.out::println);
  		return this.send(dto.toChatMessageEntity());
  	}

}
