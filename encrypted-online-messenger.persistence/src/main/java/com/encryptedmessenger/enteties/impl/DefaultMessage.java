package com.encryptedmessenger.enteties.impl;

import java.util.Date;

import com.encryptedmessenger.enteties.Message;
import com.encryptedmessenger.enteties.User;
public class DefaultMessage implements Message{
	private static int messageCount = 0;
	private long messageId;
	private Long timeStamp;
	private String messageBody;
	private User sender;
	private User receiver;
	
	{
		messageId = ++messageCount;
	}
	
	
	public DefaultMessage(User sender, User receiver, String messageBody) {
		this.sender = sender;
		this.receiver = receiver;
		this.messageBody = messageBody;
		this.timeStamp = System.currentTimeMillis();
	}
	
	public DefaultMessage() {
		this.timeStamp = System.currentTimeMillis();
	}
	
	public String getTime() {
		if(getTimeStamp() == null || getTimeStamp().equals(0L) || getTimeStamp().equals(-1L))
			return new String("");
		return (new Date(getTimeStamp())).toString();
	}
	
	@Override
	public long getMessageId() {
		return this.messageId;
	}
	
	@Override
	public User getSender() {
		return this.sender;
	}
	
	@Override
	public User getReceiver() {
		return this.receiver;
	}
	
	@Override
	public String getBody() {
		return this.messageBody;
	}
	
	public Long getTimeStamp() {
		return this.timeStamp;
	}
	@Override
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	@Override
	public void setBody(String body) {
		this.messageBody = body;
	}
	@Override
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Override
	public void setSender(User sender) {
		this.sender = sender;
	}
	@Override
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
}
