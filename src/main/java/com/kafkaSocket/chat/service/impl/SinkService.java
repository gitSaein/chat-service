package com.kafkaSocket.chat.service.impl;

import com.kafkaSocket.chat.model.ChatMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class SinkService {
    final Sinks.Many<ChatMessage> sink;

    public SinkService() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public Sinks.Many<ChatMessage> getSink() {
        return this.sink;
    }
}
