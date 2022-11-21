package com.kafkaSocket.chat.service.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class SinkService {
    final Sinks.Many sink;

    public SinkService() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public Sinks.Many getSink() {
        return this.sink;
    }
}
