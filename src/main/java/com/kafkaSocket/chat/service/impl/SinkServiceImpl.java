package com.kafkaSocket.chat.service.impl;

import com.kafkaSocket.chat.message.ChatMessageDTO;
import com.kafkaSocket.chat.message.ChatMessageEntity;
import com.kafkaSocket.chat.service.SinkService;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@Service
public class SinkServiceImpl implements SinkService<ChatMessageDTO.RequestMessage>{
    private final Map<String, Sinks.Many<ChatMessageDTO.RequestMessage>> chatMessageEvents = new HashMap<>();;

  
    @Override
    public void onNext(ChatMessageDTO.RequestMessage chatMessage, String topic) {
    	if(!chatMessageEvents.containsKey(topic)) return;
    	
    	chatMessageEvents.get(topic).emitNext(chatMessage, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Sinks.Many<ChatMessageDTO.RequestMessage> getSink(String topic) {
        log.info("# currentSubscriberCount: {}", this.chatMessageEvents.get(topic).currentSubscriberCount());
        return this.chatMessageEvents.get(topic);
    }

    @Override
    public Flux<ChatMessageDTO.RequestMessage> asFlux(String topic){
    	chatMessageEvents.putIfAbsent(topic, Sinks.many().multicast().onBackpressureBuffer());
        return this.chatMessageEvents.get(topic).asFlux();
    }
}