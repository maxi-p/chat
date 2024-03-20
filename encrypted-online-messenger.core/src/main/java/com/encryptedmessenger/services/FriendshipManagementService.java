package com.encryptedmessenger.services;

import java.util.List;

import com.encryptedmessenger.enteties.Friendship;
import com.encryptedmessenger.enteties.Message;
import com.encryptedmessenger.enteties.User;

public interface FriendshipManagementService {

	public String createFriendship(User firstUser, User secondUser);
	public boolean doesFriendshipExist(User firstUser, User secondUser);
	public boolean setLastMessage(Long friendship, Message message);
	public Message getLastMessage(Long friendshipId);
	public User[] getFriendsByUser(User user);
	public List<User> getFriendsListByUser(User user);
	public List<Friendship> getFriendshipsListByUser(User user);
	public Long getFriendshipId(String firstUser, String secondUser);
	public Friendship getFriendship(User firstUser, User secondUser);
	public Friendship getFriendshipById(Long friendshipId);
	public boolean readMessage(Friendship friendship, User user);
	public boolean readMessageByFriendshipId(Long friendshipId, String userNickName);
	public Integer getNotification(Friendship friendship, User user);
	public User getFriendByUser(Friendship friendship, User user);
	public String getFriendshipsJsonByUser(User user);
	public String getLastTimeById(Long friendshipId);
	public String getFriendNickByFriendshipIdAndUserNick(String FriendshipId, String userNickName);
	public boolean doesFriendshipExistIds(String firstUser, String secondUser, Long friendshipId);
	public boolean doesFriendshipExistIds(String firstUser, String secondUser);
	public String setLastMessageByIdsJson(Long messageTimeStamp, String message, Long friendshipLong, String senderNickName, String receiverNickName);
	public String createFriendshipIds(String firstUser, String secondUser);
}
