package com.encryptedmessenger.dto;

public class FriendshipDto {
	private Long id;
	private UserDto firstFriend;
	private UserDto secondFriend;
	private String lastMessageBody;
	private Long lastTimeStamp;
	private Long lastMessageId;
	private UserDto lastMessageFrom;
	private UserDto lastMessageTo;
	private Integer notificationsFirst;
	private Integer notificationsSecond;

	public Long getLastMessageId() {
		return lastMessageId;
	}
	public void setLastMessageId(Long lastMessageId) {
		this.lastMessageId = lastMessageId;
	}
	public UserDto getLastMessageFrom() {
		return lastMessageFrom;
	}
	public void setLastMessageFrom(UserDto lastMessageFrom) {
		this.lastMessageFrom = lastMessageFrom;
	}
	public UserDto getLastMessageTo() {
		return lastMessageTo;
	}
	public void setLastMessageTo(UserDto lastMessageTo) {
		this.lastMessageTo = lastMessageTo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public UserDto getFirstFriend() {
		return firstFriend;
	}
	public void setFirstFriend(UserDto firstFriend) {
		this.firstFriend = firstFriend;
	}
	public UserDto getSecondFriend() {
		return secondFriend;
	}
	public void setSecondFriend(UserDto secondFriend) {
		this.secondFriend = secondFriend;
	}
	public String getLastMessageBody() {
		return lastMessageBody;
	}
	public void setLastMessageBody(String lastMessageBody) {
		this.lastMessageBody = lastMessageBody;
	}
	public Long getLastTimeStamp() {
		return lastTimeStamp;
	}
	public void setLastTimeStamp(Long lastTimeStamp) {
		this.lastTimeStamp = lastTimeStamp;
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
