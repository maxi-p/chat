package com.encryptedmessenger.enteties.impl;

import java.util.Date;

import com.encryptedmessenger.enteties.Friendship;
import com.encryptedmessenger.enteties.User;

import java.util.Date;

public class DefaultFriendship implements Friendship{
	private static int friendshipCounter = 0;
	private long friendshipId;
	private User firstFriend, secondFriend;
	private Long lastTimeStamp;
	private String lastMessageBody;
	private Long lastMessageId;
	private User lastMessageFrom, lastMessageTo;
	private Integer notificationsFirst;
	private Integer notificationsSecond;
	
	{
		friendshipId = ++friendshipCounter;
	}
	
	public Long getLastMessageId() {
		return lastMessageId;
	}
	public void setLastMessageId(Long lastMessageId) {
		this.lastMessageId = lastMessageId;
	}
	public User getLastMessageFrom() {
		return lastMessageFrom;
	}
	public void setLastMessageFrom(User lastMessageFrom) {
		this.lastMessageFrom = lastMessageFrom;
	}
	public User getLastMessageTo() {
		return lastMessageTo;
	}
	public void setLastMessageTo(User lastMessageTo) {
		this.lastMessageTo = lastMessageTo;
	}

	public DefaultFriendship() {
		this.lastTimeStamp = System.currentTimeMillis();
	}
	public String getLastTime() {
		System.out.println("getting last time: "+ getLastTimeStamp());
		if(getLastTimeStamp() == null || getLastTimeStamp().equals(-1L)|| getLastTimeStamp().equals(0L))
			return new String("");
		else 
			return (new Date(getLastTimeStamp())).toString();
	}
	
	public DefaultFriendship(User firstFriend, User secondFriend) {
		this.firstFriend = firstFriend;
		this.secondFriend = secondFriend;
		this.lastTimeStamp = System.currentTimeMillis();
	}
	
	@Override
	public User getFirstFriend() {
		return firstFriend;
	}
	
	@Override
	public User getSecondFriend() {
		return secondFriend;
	}
	
	@Override
	public long getFriendshipId() {
		return friendshipId;
	}
	@Override
	public void setFriendshipId(long id) {
		this.friendshipId = id;
	}
	@Override
	public void setSecondFriend(User secondFriend) {
		this.secondFriend = secondFriend;
	}
	@Override
	public void setFirstFriend(User firstFriend) {
		this.firstFriend = firstFriend;
	}

	public static int getFriendshipCounter() {
		return friendshipCounter;
	}

	public static void setFriendshipCounter(int friendshipCounter) {
		DefaultFriendship.friendshipCounter = friendshipCounter;
	}

	public Long getLastTimeStamp() {
		return lastTimeStamp;
	}

	public void setLastTimeStamp(Long lastTimeStamp) {
		this.lastTimeStamp = lastTimeStamp;
	}

	public String getLastMessageBody() {
		return lastMessageBody;
	}

	public void setLastMessageBody(String lastMessageBody) {
		this.lastMessageBody = lastMessageBody;
	}

	public Integer getNotificationsFirst() {
		return notificationsFirst;
	}

	public void setNotificationsFirst(Integer notificationsFirst) {
		this.notificationsFirst = notificationsFirst;
	}

	public Integer getNotificationsSecond() {
		return notificationsSecond;
	}

	public void setNotificationsSecond(Integer notificationsSecond) {
		this.notificationsSecond = notificationsSecond;
	}
	

}
