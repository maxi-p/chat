package com.encryptedmessenger.dto;

public class MessageDto {
	private Long id;
	private String messageBody;
	private Long timeStamp;
	private UserDto sender;
	private UserDto receiver;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public UserDto getSender() {
		return sender;
	}
	public void setSender(UserDto sender) {
		this.sender = sender;
	}
	public UserDto getReceiver() {
		return receiver;
	}
	public void setReceiver(UserDto receiver) {
		this.receiver = receiver;
	}
	
	
}
