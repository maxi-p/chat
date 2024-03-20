package com.encryptedmessenger.dto.converter;

import java.util.ArrayList;
import java.util.List;

import com.encryptedmessenger.dto.MessageDto;
import com.encryptedmessenger.enteties.Message;
import com.encryptedmessenger.enteties.impl.DefaultMessage;

public class MessageDtoToMessageConverter {
	private static MessageDtoToMessageConverter instance = null;
	private UserDtoToUserConverter userConverter = UserDtoToUserConverter.getInstance();
	
	public static MessageDtoToMessageConverter getInstance() {
		if(instance == null) {
			instance = new MessageDtoToMessageConverter();
		}
		return instance;
	}
	
	public MessageDto convertMessageIdToMessageDtoWithOnlyId(long messageId) {
		MessageDto messageDto = new MessageDto();
		messageDto.setId(messageId);
		return messageDto;
	}
	
	public Message convertMessageDtoToMessage(MessageDto messageDto) {
		if(messageDto == null) {
			return null;
		}
		
		Message message = new DefaultMessage();
		message.setSender(userConverter.convertUserDtoToUser(messageDto.getSender()));
		message.setReceiver(userConverter.convertUserDtoToUser(messageDto.getReceiver()));
		message.setBody(messageDto.getMessageBody());
		message.setTimeStamp(messageDto.getTimeStamp());
		message.setMessageId(messageDto.getId());
		
		return message;
	}
	
	public MessageDto convertMessageToMessageDto(Message message) {
		MessageDto messageDto = new MessageDto();
		
		messageDto.setSender(userConverter.convertUserToUserDto(message.getSender()));
		messageDto.setReceiver(userConverter.convertUserToUserDto(message.getReceiver()));
		messageDto.setMessageBody(message.getBody());
		messageDto.setTimeStamp(message.getTimeStamp());
		messageDto.setId(message.getMessageId());
		
		return messageDto;
	}
	
	public List<Message> convertMessageDtosToMessages(List<MessageDto> messagesDtos){
		List<Message> messages = new ArrayList<>();
		
		for(MessageDto messageDto : messagesDtos) {
			messages.add(convertMessageDtoToMessage(messageDto));
		}
		return messages;
	}
}
