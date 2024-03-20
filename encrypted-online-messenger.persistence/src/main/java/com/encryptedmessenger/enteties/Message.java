package com.encryptedmessenger.enteties;

import java.util.Date;

public interface Message {
	
	public long getMessageId();
	public String getBody();
	public Long getTimeStamp();
	public User getSender();
	public User getReceiver();
	public String getTime();
	
	public void setMessageId(long messageId);
	public void setBody(String body);
	public void setTimeStamp(Long timeStamp);
	public void setSender(User sender);
	public void setReceiver(User receiver);
	
}
