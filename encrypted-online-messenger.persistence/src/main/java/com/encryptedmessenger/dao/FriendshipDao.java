package com.encryptedmessenger.dao;

import java.util.List;

import com.encryptedmessenger.dto.FriendshipDto;
import com.encryptedmessenger.dto.MessageDto;
import com.encryptedmessenger.dto.UserDto;

public interface FriendshipDao {
	public FriendshipDto getFriendshipById(long id);
	
	public boolean areFriends(UserDto first, UserDto second);
	public String saveFriendship(FriendshipDto friendship);
	public boolean setLastMessage(Long FriendshipId, MessageDto messageDto);
	public List<FriendshipDto> getFriendshipsByUser(UserDto user);
	public List<UserDto> getFriendsByUser(UserDto user);
	public FriendshipDto getFriendshipBetween(UserDto firstUser,UserDto secondUser);
	public boolean setNotification(FriendshipDto friendshipDto,int val, int flag);
	public boolean isFirst(FriendshipDto friendshipDto, UserDto userDto);
	public Integer getNotification(FriendshipDto friendshipDto, int flag);
	public UserDto getFriendByUser(FriendshipDto friendshipDto, UserDto userDto);
	public String getFriendshipsJsonByUser(UserDto user);
	public String getLastTimeById(Long friendshipId);
	public Long getFriendshipIdBetween(String firstUserId,String secondUserId);
	public boolean isFirstById(Long friendshipId, String userNickName);
	public boolean setNotificationById(Long friendshipId,int val, int flag);
	public String getFriendNickByFriendshipIdAndUserNick(String FriendshipId, String userNickName);
	public Long areFriendsIds(String firstUser, String secondUser);
	public Integer getNotificationIds(Long friendshipId, int flag);
	public boolean setLastMessageByIds(Long messageTimeStamp, String message, Long friendshipLong, String senderNickName, String receiverNickName, Integer notification, int flag);
	public String saveFriendshipIds(String firstUser, String secondUser, Long lastTimeStamp);
}

