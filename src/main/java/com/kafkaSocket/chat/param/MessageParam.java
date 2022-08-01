package com.kafkaSocket.chat.param;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class MessageParam {

	private Long senderIdx;
	private String content;
	
	
}
