package com.kafkaSocket.chat.service.impl;

import com.kafkaSocket.chat.model.ChatMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class SinkServiceImpl {
    private final Sinks.Many<ChatMessage> chatMessageEvent;

    public SinkServiceImpl() {
        this.chatMessageEvent = Sinks.many().multicast().onBackpressureBuffer();
    }

    public Sinks.Many<ChatMessage> getSink() {
        return this.chatMessageEvent;
    }

    public Flux<ChatMessage> asFlux(){
        return this.chatMessageEvent.asFlux();
    }
}
