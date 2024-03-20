package com.encryptedmessenger.enteties;

public interface Friendship {
	
	public long getFriendshipId();
	public User getFirstFriend();
	public User getSecondFriend();
	public Long getLastTimeStamp();
	public String getLastMessageBody();
	public Integer getNotificationsFirst();
	public Integer getNotificationsSecond();
	public String getLastTime();
	public void setLastMessageId(Long lastMessageId);
	public void setLastMessageFrom(User lastMessageFrom);
	public void setLastMessageTo(User lastMessageTo);
	
	public Long getLastMessageId();
	public User getLastMessageFrom();
	public User getLastMessageTo();
	public void setFriendshipId(long id);
	public void setSecondFriend(User secondFriend);
	public void setFirstFriend(User firstFriend);
	public void setLastTimeStamp(Long lastTimeStamp);
	public void setLastMessageBody(String lastMessageBody);
	public void setNotificationsFirst(Integer notificationsFirst);
	public void setNotificationsSecond(Integer notificationsSecond);
}
