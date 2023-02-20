package com.kafkaSocket.chat.service.impl;

import com.kafkaSocket.chat.entity.ChatMessageEntity;
import com.kafkaSocket.chat.service.SinkService;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@Service
public class SinkServiceImpl<T>{
    private final Map<String, Sinks.Many<T>> messageEvents = new HashMap<>();;

  
    public void onNext(T chatMessage, String topic) {
    	if(!messageEvents.containsKey(topic)) return;
    	
    	messageEvents.get(topic).emitNext(chatMessage, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    public Sinks.Many<T> getSink(String topic) {
        log.info("# currentSubscriberCount: {}", this.messageEvents.get(topic).currentSubscriberCount());
        return this.messageEvents.get(topic);
    }

    public Flux<T> asFlux(String topic){
    	messageEvents.putIfAbsent(topic, Sinks.many().multicast().onBackpressureBuffer());
        return this.messageEvents.get(topic).asFlux();
    }
}