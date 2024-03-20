package com.encryptedmessenger.services;

import com.encryptedmessenger.enteties.Message;
import com.encryptedmessenger.enteties.User;

public interface MessageManagementService {
	public Long sendMessage(User sender, User receiver, String messageBody);
	public Message getMessageById(Long id);
	public Message[] getMessagesBetween(User firstUser, User secondUser);
	public Message[] getLastNMessagesBetween(User firstUser, User secondUser, int N);
	public String getLastNJsonMessagesBetween(Long friendshipId, String firstUserNickName, String secondUserNickName, int N);
	public Long sendMessageByIds(String userNickName, String friendNickName, Long friendshipId, String message);
	
	
}
