package com.kafkaSocket.chat.service.impl;

import com.kafkaSocket.chat.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@Service
public class SinkServiceImpl {
    private final Sinks.Many<ChatMessage> chatMessageEvent;

    public SinkServiceImpl() {
        this.chatMessageEvent = Sinks.many().multicast().onBackpressureBuffer();
    }

    public Sinks.Many<ChatMessage> getSink() {
        log.info("# currentSubscriberCount: {}", this.chatMessageEvent.currentSubscriberCount());
        return this.chatMessageEvent;
    }

    public Flux<ChatMessage> asFlux(){
        return this.chatMessageEvent.asFlux();
    }
}
