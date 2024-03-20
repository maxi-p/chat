package com.encryptedmessenger.services.impl;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import com.encryptedmessenger.dao.impl.MySqlJdbcFriendshipDao;
import com.encryptedmessenger.dao.impl.MySqlJdbcMessageDao;
import com.encryptedmessenger.dao.impl.MySqlJdbcUserDao;
import com.encryptedmessenger.dto.FriendshipDto;
import com.encryptedmessenger.dto.MessageDto;
import com.encryptedmessenger.dto.UserDto;
import com.encryptedmessenger.dto.converter.FriendshipDtoToFriendshipConverter;
import com.encryptedmessenger.dto.converter.MessageDtoToMessageConverter;
import com.encryptedmessenger.dto.converter.UserDtoToUserConverter;
import com.encryptedmessenger.enteties.Friendship;
import com.encryptedmessenger.enteties.Message;
import com.encryptedmessenger.enteties.User;
import com.encryptedmessenger.enteties.impl.DefaultFriendship;
import com.encryptedmessenger.services.FriendshipManagementService;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonWriter;

public class MySqlFriendshipManagementService implements FriendshipManagementService {
	private static MySqlFriendshipManagementService instance;
	public MySqlJdbcUserDao userDao = MySqlJdbcUserDao.getInstance();
	public MySqlJdbcMessageDao messageDao = MySqlJdbcMessageDao.getInstance();
	public MySqlJdbcFriendshipDao friendshipDao = MySqlJdbcFriendshipDao.getInstance();

	public UserDtoToUserConverter userConverter = UserDtoToUserConverter.getInstance();
	public MessageDtoToMessageConverter messageConverter = MessageDtoToMessageConverter.getInstance();
	public FriendshipDtoToFriendshipConverter friendshipConverter = FriendshipDtoToFriendshipConverter.getInstance();

	public static MySqlFriendshipManagementService getInstance() {
		if (instance == null) {
			instance = new MySqlFriendshipManagementService();
		}
		return instance;
	}

	@Override
	public String getLastTimeById(Long friendshipId) {
		return friendshipDao.getLastTimeById(friendshipId);
	}

	@Override
	public User getFriendByUser(Friendship friendship, User user) {
		FriendshipDto friendshipDto = friendshipConverter.convertFriendshipToFriendshipDto(friendship);
		UserDto userDto = userConverter.convertUserToUserDto(user);
		UserDto returnDto = friendshipDao.getFriendByUser(friendshipDto, userDto);
		if (returnDto != null)
			return userConverter.convertUserDtoToUser(returnDto);
		else
			return null;
	}

	@Override
	public Friendship getFriendshipById(Long friendshipId) {
		FriendshipDto friendship = friendshipDao.getFriendshipById(friendshipId);
		if (friendship != null)
			return friendshipConverter.convertFriendshipDtoToFriendship(friendship);
		else
			return null;
	}

	@Override
	public boolean readMessage(Friendship friendship, User user) {
		if (friendshipDao.isFirst(friendshipConverter.convertFriendshipToFriendshipDto(friendship),
				userConverter.convertUserToUserDto(user))) {
			return friendshipDao.setNotification(friendshipConverter.convertFriendshipToFriendshipDto(friendship), 0,
					1);
		} else {
			return friendshipDao.setNotification(friendshipConverter.convertFriendshipToFriendshipDto(friendship), 0,
					2);
		}
	}

	@Override
	public boolean readMessageByFriendshipId(Long friendshipId, String userNickName) {
		if (friendshipDao.isFirstById(friendshipId, userNickName)) { // QUICK
			return friendshipDao.setNotificationById(friendshipId, 0, 1); // QUICK
		} else {
			return friendshipDao.setNotificationById(friendshipId, 0, 2); // QUICK
		}
	}

	@Override
	public Integer getNotification(Friendship friendship, User user) {
		FriendshipDto frinedshipDto = friendshipConverter.convertFriendshipToFriendshipDto(friendship);
		if (friendshipDao.isFirst(frinedshipDto, userConverter.convertUserToUserDto(user))) {
			int notificiation = friendshipDao.getNotification(frinedshipDto, 1);
			return notificiation;
		} else {
			int notificiation = friendshipDao.getNotification(frinedshipDto, 2);
			friendshipDao.setNotification(frinedshipDto, notificiation, 2);
			return notificiation;
		}
	}

	@Override
	public Friendship getFriendship(User firstUser, User secondUser) {
		FriendshipDto friendship = friendshipDao.getFriendshipBetween(userConverter.convertUserToUserDto(firstUser),
				userConverter.convertUserToUserDto(secondUser));
		if (friendship != null)
			return friendshipConverter.convertFriendshipDtoToFriendship(friendship);
		else
			return null;
	}

	@Override
	public Long getFriendshipId(String firstUser, String secondUser) {
		Long friendshipId = friendshipDao.getFriendshipIdBetween(firstUser, secondUser);
		if (friendshipId != null) {
			return friendshipId;
		} else
			return null;
	}

	public boolean setLastMessage(Long friendshipId, Message message) {
		return friendshipDao.setLastMessage(friendshipId, messageConverter.convertMessageToMessageDto(message));
	}

	@Override
	public String setLastMessageByIdsJson(Long messageTimeStamp, String message, Long friendshipLong, String senderNickName, String receiverNickName) {
		if (messageTimeStamp != null && !messageTimeStamp.equals(0L) && !messageTimeStamp.equals(-1L)) {
			System.out.println("getting there!");
			Integer notificiation;
			int flag;
			if (friendshipDao.isFirstById(friendshipLong, receiverNickName)) {
				notificiation = friendshipDao.getNotificationIds(friendshipLong, 1) + 1;
				flag = 1;
			} else {
				notificiation = friendshipDao.getNotificationIds(friendshipLong, 2) + 1;
				flag = 2;
			}
			if(friendshipDao.setLastMessageByIds(messageTimeStamp, message, friendshipLong, senderNickName, receiverNickName, notificiation, flag)) {
				
				JsonObject jsonObject = Json.createObjectBuilder()
						.add("sender", senderNickName)
						.add("time", new Date(messageTimeStamp).toString())
						.add("msg", message)
						.add("receiver",receiverNickName)
						.add("notification", notificiation)
						.add("notificationSender", 0)
						.build();
				
				StringWriter stringWriter = new StringWriter();
				try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
					jsonWriter.write(jsonObject);
				}
				System.out.println("actually got here! "+stringWriter.toString());
				return stringWriter.toString();
			}
			System.out.println("false!");
			
			
		}
		return new String("");
	}

	public Message getLastMessage(Long friendshipId) {
		MessageDto messageDto = messageDao.getLastMessageByFriendshipId(friendshipId);
		return messageConverter.convertMessageDtoToMessage(messageDto);
	}

	@Override
	public boolean doesFriendshipExist(User firstUser, User secondUser) { // QUICK
		return friendshipDao.areFriends(userConverter.convertUserToUserDto(firstUser),
				userConverter.convertUserToUserDto(secondUser));
	}

	@Override
	public boolean doesFriendshipExistIds(String firstUser, String secondUser, Long friendshipId) { // QUICKEST
		return friendshipId.equals(friendshipDao.areFriendsIds(firstUser, secondUser));
	}
	
	@Override
	public boolean doesFriendshipExistIds(String firstUser, String secondUser) { // QUICKEST
		Long id =  friendshipDao.areFriendsIds(firstUser, secondUser);
		return (id != null && !id.equals(0L) && !id.equals(-1L));
	}
	
	@Override
	public String createFriendshipIds(String firstUser, String secondUser) {
		if (firstUser == null || secondUser == null) {
			return new String("");
		}

		if (!doesFriendshipExistIds(firstUser, secondUser)) {
			Friendship friendship = new DefaultFriendship();
			Long lastTimeStamp = friendship.getLastTimeStamp();

			String created = friendshipDao
					.saveFriendshipIds(firstUser, secondUser, lastTimeStamp);
			if (created.equals("")) {
				return "";
			} else {
				return created;
			}

		}
		return "";
	}

	@Override
	public String createFriendship(User firstUser, User secondUser) {
		if (firstUser == null || secondUser == null) {
			return "Error";
		}

		if (!doesFriendshipExist(firstUser, secondUser)) {
			Friendship friendship = new DefaultFriendship(firstUser, secondUser);

			String created = friendshipDao
					.saveFriendship(friendshipConverter.convertFriendshipToFriendshipDto(friendship));
			if (created.equals("")) {
				return "";
			} else {
				return created;
			}

		}
		return "";
	}

	@Override
	public User[] getFriendsByUser(User user) {
		List<UserDto> friends = friendshipDao.getFriendsByUser(userConverter.convertUserToUserDto(user));
		List<User> usrs = userConverter.convertUserDtosToUsers(friends);
		User[] result = new User[usrs.size()];
		usrs.toArray(result);
		return result;

	}

	@Override
	public String getFriendNickByFriendshipIdAndUserNick(String FriendshipId, String userNickName) {
		String friendNickName = friendshipDao.getFriendNickByFriendshipIdAndUserNick(FriendshipId, userNickName);
		if (friendNickName != null)
			return friendNickName;
		else
			return new String("");
	}

	@Override
	public List<User> getFriendsListByUser(User user) {
		List<UserDto> friends = friendshipDao.getFriendsByUser(userConverter.convertUserToUserDto(user));
		List<User> usrs = userConverter.convertUserDtosToUsers(friends);
		return usrs;

	}

	@Override
	public List<Friendship> getFriendshipsListByUser(User user) {
		List<FriendshipDto> friendshipDtos = friendshipDao
				.getFriendshipsByUser(userConverter.convertUserToUserDto(user));
		List<Friendship> friendships = friendshipConverter.convertFriendshipDtosToFriendships(friendshipDtos);
		return friendships;

	}

	@Override
	public String getFriendshipsJsonByUser(User user) {
		String friendshipsJson = friendshipDao.getFriendshipsJsonByUser(userConverter.convertUserToUserDto(user));
		return friendshipsJson;

	}

}
