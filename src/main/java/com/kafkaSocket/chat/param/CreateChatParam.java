package com.kafkaSocket.chat.param;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class CreateChatParam {

	private Long managerIdx;
	private String roomName;
	private List<Long> participantsIdx;
}
