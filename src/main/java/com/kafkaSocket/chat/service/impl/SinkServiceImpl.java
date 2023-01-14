package com.kafkaSocket.chat.service.impl;

import com.kafkaSocket.chat.model.ChatMessage;
import com.kafkaSocket.chat.service.SinkService;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@Service
public class SinkServiceImpl implements SinkService<ChatMessage>{
    private final Map<String, Sinks.Many<ChatMessage>> chatMessageEvents = new HashMap<>();;

  
    @Override
    public void onNext(ChatMessage chatMessage, String topic) {
    	if(!chatMessageEvents.containsKey(topic)) return;
    	
    	chatMessageEvents.get(topic).emitNext(chatMessage, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Sinks.Many<ChatMessage> getSink(String topic) {
        log.info("# currentSubscriberCount: {}", this.chatMessageEvents.get(topic).currentSubscriberCount());
        return this.chatMessageEvents.get(topic);
    }

    @Override
    public Flux<ChatMessage> asFlux(String topic){
    	chatMessageEvents.putIfAbsent(topic, Sinks.many().multicast().onBackpressureBuffer());
        return this.chatMessageEvents.get(topic).asFlux();
    }
}
