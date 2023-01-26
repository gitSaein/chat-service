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

import com.kafkaSocket.chat.message.ChatMessageDTO;
import com.kafkaSocket.chat.message.ChatMessageEntity;
import com.kafkaSocket.chat.repository.ChatMessageRepository;
import com.kafkaSocket.chat.service.KafkaConsumerService;
import com.kafkaSocket.chat.service.KafkaProduceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements KafkaConsumerService<ChatMessageDTO.RequestMessage>, KafkaProduceService<ChatMessageDTO.RequestMessage>{

	private final SinkServiceImpl sinkService;
	private final ChatMessageRepository chatMessageRepository;
	private final static String TOPIC = "message.room.";	
	private final KafkaTemplate<String, ChatMessageDTO.RequestMessage> kafkaTemplate;
    
   
    @Override
    @KafkaListener(groupId="chat_message", topicPattern = "message.room.*")
	public void consume(ChatMessageDTO.RequestMessage chatMessage){
		log.info(chatMessage.toString());

		sinkService.getSink(chatMessage.getRoomIdx().toString()).tryEmitNext(chatMessage);
	}
    
    public Mono<ServerResponse> getChatMessageByTopic(ServerRequest serverRequest){
		Integer roomIdx = Integer.parseInt(serverRequest.pathVariable("roomIdx"));

		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(sinkService.asFlux(roomIdx.toString()), ChatMessageDTO.RequestMessage.class).log();
	}


    
  //https://projectreactor.io/docs/kafka/release/reference/
  	@Override
  	public Mono<String> send(ChatMessageDTO.RequestMessage requestMessage){
  	
  		ListenableFuture<SendResult<String, ChatMessageDTO.RequestMessage>> kafkaResponse = 
  				kafkaTemplate.send(TOPIC + requestMessage.getRoomIdx(), requestMessage);
  		kafkaResponse.addCallback(new ListenableFutureCallback<SendResult<String, ChatMessageDTO.RequestMessage>>() {

  			@Override
  			public void onSuccess(SendResult<String, ChatMessageDTO.RequestMessage > result) {
  				// TODO Auto-generated method stub
  				log.info("success: {}", result);
  				ChatMessageEntity entity = requestMessage.toEntity();
  				Mono<ChatMessageEntity> entityr = chatMessageRepository.save(entity);
  				entityr.subscribe(System.out::println);
  			}

  			@Override
  			public void onFailure(Throwable ex) {
  				// TODO Auto-generated method stub
  				log.info("fail");

  			}
  		});
          	
  		return Mono.just(requestMessage.toString());

  				

  	}

}
