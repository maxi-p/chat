package com.encryptedmessenger.dto.converter;

import java.util.ArrayList;
import java.util.List;

import com.encryptedmessenger.dto.FriendshipDto;
import com.encryptedmessenger.enteties.Friendship;
import com.encryptedmessenger.enteties.impl.DefaultFriendship;

public class FriendshipDtoToFriendshipConverter {
	private static FriendshipDtoToFriendshipConverter instance = null;
	private UserDtoToUserConverter userConverter = UserDtoToUserConverter.getInstance();
	
	public static FriendshipDtoToFriendshipConverter getInstance() {
		if(instance == null) {
			instance = new FriendshipDtoToFriendshipConverter();
		}
		return instance;
	}
	
	public FriendshipDto convertFriendshipIdToFriendshipDtoWithOnlyId(long friendshipId) {
		FriendshipDto friendshipDto = new FriendshipDto();
		friendshipDto.setId(friendshipId);
		return friendshipDto;
	}
	
	public Friendship convertFriendshipDtoToFriendship(FriendshipDto friendshipDto) {
		if(friendshipDto == null) {
			return null;
		}
		
		Friendship friendship = new DefaultFriendship();
		friendship.setFirstFriend(userConverter.convertUserDtoToUser(friendshipDto.getFirstFriend()));
		friendship.setSecondFriend(userConverter.convertUserDtoToUser(friendshipDto.getSecondFriend()));
		friendship.setFriendshipId(friendshipDto.getId());
		
		if(friendshipDto.getLastMessageId() != null)
		friendship.setLastMessageId(friendshipDto.getLastMessageId());
		else friendship.setLastMessageId(-1L);
		
		if(friendshipDto.getLastMessageBody() != null)
		friendship.setLastMessageBody(friendshipDto.getLastMessageBody());
		else friendship.setLastMessageBody(new String(""));
		
		if(friendshipDto.getLastTimeStamp() != null)
		friendship.setLastTimeStamp(friendshipDto.getLastTimeStamp());
		else friendship.setLastTimeStamp(-1L);
		
		if(friendshipDto.getLastMessageFrom() != null)
		friendship.setLastMessageFrom(userConverter.convertUserDtoToUser(friendshipDto.getLastMessageFrom()));
		else friendship.setLastMessageFrom(null);
		
		if(friendshipDto.getLastMessageTo() != null)
		friendship.setLastMessageTo(userConverter.convertUserDtoToUser(friendshipDto.getLastMessageTo()));
		else friendship.setLastMessageTo(null);
		
		
		
		if(friendshipDto.getNotificationsFirst() != null)
			friendship.setNotificationsFirst(friendshipDto.getNotificationsFirst());
			else friendship.setNotificationsFirst(0);
		
		if(friendshipDto.getNotificationsSecond() != null)
			friendship.setNotificationsSecond(friendshipDto.getNotificationsSecond());
			else friendship.setNotificationsSecond(0);
		
		
		return friendship;
	}
	
	public FriendshipDto convertFriendshipToFriendshipDto(Friendship friendship) {
		FriendshipDto friendshipDto = new FriendshipDto();
		
		friendshipDto.setFirstFriend(userConverter.convertUserToUserDto(friendship.getFirstFriend()));
		friendshipDto.setSecondFriend(userConverter.convertUserToUserDto(friendship.getSecondFriend()));
		friendshipDto.setId(friendship.getFriendshipId());
		
		if(friendship.getLastMessageId() != null)
		friendshipDto.setLastMessageId(friendship.getLastMessageId());
		else friendshipDto.setLastMessageId(-1L);
		
		if(friendship.getLastMessageBody() != null)
		friendshipDto.setLastMessageBody(friendship.getLastMessageBody());
		else friendshipDto.setLastMessageBody(new String(""));
		
		if(friendship.getLastTimeStamp() != null)
		friendshipDto.setLastTimeStamp(friendship.getLastTimeStamp());
		else friendshipDto.setLastTimeStamp(-1L);
		
		if(friendship.getLastMessageFrom() != null)
		friendshipDto.setLastMessageFrom(userConverter.convertUserToUserDto(friendship.getLastMessageFrom()));
		else friendshipDto.setLastMessageFrom(null);
		
		if(friendship.getLastMessageTo() != null)
		friendshipDto.setLastMessageTo(userConverter.convertUserToUserDto(friendship.getLastMessageTo()));
		else friendshipDto.setLastMessageTo(null);
		
		
		if(friendship.getNotificationsFirst() != null)
			friendshipDto.setNotificationsFirst(friendship.getNotificationsFirst());
			else friendshipDto.setNotificationsFirst(0);
		
		if(friendship.getNotificationsSecond() != null)
			friendshipDto.setNotificationsSecond(friendship.getNotificationsSecond());
			else friendshipDto.setNotificationsSecond(0);
		
		return friendshipDto;
	}
	
	public List<Friendship> convertFriendshipDtosToFriendships(List<FriendshipDto> friendshipDtos){
		List<Friendship> friendships = new ArrayList<>();
		
		for(FriendshipDto friendshipDto : friendshipDtos) {
			friendships.add(convertFriendshipDtoToFriendship(friendshipDto));
		}
		return friendships;
	}
}
