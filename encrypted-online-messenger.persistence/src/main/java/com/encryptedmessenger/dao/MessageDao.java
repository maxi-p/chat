package com.encryptedmessenger.dao;

import java.util.List;

import com.encryptedmessenger.dto.FriendshipDto;
import com.encryptedmessenger.dto.MessageDto;
import com.encryptedmessenger.dto.UserDto;

public interface MessageDao {
	public Long saveMessage(MessageDto message);
	public List<MessageDto> getMessagesByUsers(UserDto first, UserDto second);
	public MessageDto getLastMessageByFriendshipId(Long friendshipId);
	public MessageDto getMessageById(Long id);
	public List<MessageDto> getLastNMessagesByUsers(UserDto first, UserDto second, int N);
	public String getLastNJsonMessagesByUsers(Long friendshipId, String firstNickName, String secondNickName, int N);
	public Long saveMessageIds(String senderNickName, String receiverNickName, Long timeStamp, String message);
}

