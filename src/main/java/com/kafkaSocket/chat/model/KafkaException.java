package com.kafkaSocket.chat.model;

public class KafkaException extends RuntimeException {

	public static final KafkaException SEND_ERROR = new KafkaException("send fail");
			
	public KafkaException(String msg) {
        super(msg);
    }
}
