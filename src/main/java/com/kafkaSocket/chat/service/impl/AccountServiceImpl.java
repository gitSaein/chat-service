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
import com.kafkaSocket.chat.entity.AccountMessageEntity;
import com.kafkaSocket.chat.entity.ChatMessageEntity;
import com.kafkaSocket.chat.entity.ChatRoomsEntity;
import com.kafkaSocket.chat.enums.MessageType;
import com.kafkaSocket.chat.repository.AccountMessageRepository;
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
public class AccountServiceImpl implements KafkaConsumerService<AccountMessageEntity>, KafkaProduceService<AccountMessageEntity>{

	private final SinkServiceImpl<AccountMessageEntity> sinkService;
	private final ChatMessageRepository chatMessageRepository;
	private final AccountMessageRepository accountMessageRepository;
	private final ChatRoomsRepository chatRoomsRepository;
	private final static String ACCOUNT_TOPIC = "account.";
	private final KafkaTemplate<String, AccountMessageEntity> kafkaTemplate;
    
   
    @Override
    @KafkaListener(groupId="account_message", topicPattern = ACCOUNT_TOPIC + "*")
	public void consume(AccountMessageEntity accountMessage){
		sinkService.getSink(accountMessage.getUserIdx().toString()).tryEmitNext(accountMessage);
	}
    
    public Mono<ServerResponse> getMessageByTopic(ServerRequest serverRequest){
		Integer userIdx = Integer.parseInt(serverRequest.pathVariable("userIdx"));

		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(sinkService.asFlux(userIdx.toString()), ChatMessageDTO.RequestMessage.class).log();
	}
    

  	@Override
  	public Mono<String> send(AccountMessageEntity dto){
  	
  		kafkaTemplate.send(ACCOUNT_TOPIC + dto.getUserIdx(), dto)
  		.addCallback(new ListenableFutureCallback<SendResult<String, AccountMessageEntity>>() {

  			@Override
  			public void onSuccess(SendResult<String, AccountMessageEntity> result) {
  				log.info("success: {}", result);
  				AccountMessageEntity entity = dto;
  				Mono<AccountMessageEntity> entityr = accountMessageRepository.save(entity);
  				entityr.subscribe(System.out::println);
  			}

  			@Override
  			public void onFailure(Throwable ex) {
  				log.info("fail");

  			}
  		});
          	
  		return Mono.just(dto.toString());	
  	}
  	
  	public Mono<ChatRoomsEntity> inviteRoom(ChatMessageDTO.RequestInviteRoom dto) {
  		return chatRoomsRepository.findById(dto.getRoomId())
  				.map(v -> {
  					dto.getParticipants().forEach( p -> {
  						int idx = v.getParticipants().indexOf(p);
  						if( idx == -1) {
  							v.getParticipants().add(p);
  						}else {
  							dto.getParticipants().remove(idx);
  						}
  					});
  					return v;
  				})
  				.doOnNext(this.chatRoomsRepository::save)
  				.doOnSuccess(chatRoom -> {
  					dto.getParticipants().forEach(userIdx ->{
  						this.send(AccountMessageEntity.builder()
  	  							.message("초대합니다.")
  	  							.messageType(MessageType.CHAT_INVITE)
  	  							.userIdx(userIdx).build());
  					});
  					
  				});
  	}
  

}
