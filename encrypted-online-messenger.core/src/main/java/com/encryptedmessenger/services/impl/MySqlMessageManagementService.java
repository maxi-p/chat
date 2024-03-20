package com.encryptedmessenger.services.impl;

import java.util.Collections;
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
import com.encryptedmessenger.enteties.impl.DefaultMessage;
import com.encryptedmessenger.services.MessageManagementService;

public class MySqlMessageManagementService implements MessageManagementService{
	private static MySqlMessageManagementService instance;
	public MySqlJdbcUserDao userDao = MySqlJdbcUserDao.getInstance();
	public MySqlJdbcFriendshipDao friendshipDao = MySqlJdbcFriendshipDao.getInstance();
	public MySqlJdbcMessageDao messageDao = MySqlJdbcMessageDao.getInstance();

	public UserDtoToUserConverter userConverter = UserDtoToUserConverter.getInstance();
	public FriendshipDtoToFriendshipConverter friendshipConverter = FriendshipDtoToFriendshipConverter.getInstance();
	public MessageDtoToMessageConverter messageConverter = MessageDtoToMessageConverter.getInstance();
	
	private MySqlFriendshipManagementService friendshipManagementService = MySqlFriendshipManagementService.getInstance();
	
	
	public static MySqlMessageManagementService getInstance() {
		if (instance == null) {
			instance = new MySqlMessageManagementService();
		}
		return instance;
	}
	
	@Override 
	public Message getMessageById(Long id) {
		return messageConverter.convertMessageDtoToMessage(messageDao.getMessageById(id));
	}
	
	@Override
	public Long sendMessageByIds(String senderNickName, String receiverNickName, Long friendshipId, String message){
		if(senderNickName == null || receiverNickName == null || message == null || senderNickName.equals("") || receiverNickName.equals("") || message.equals("")) {
			System.out.println("yup, null");
			return -1L;
		}
		
		if(friendshipManagementService.doesFriendshipExistIds(senderNickName, receiverNickName, friendshipId)) { // QUICK
			Message newMessage = new DefaultMessage();
			Long timeStamp = newMessage.getTimeStamp();
			Long send = messageDao.saveMessageIds(senderNickName, receiverNickName, timeStamp, message);
			if( send != null && !send.equals(-1L)) {
				System.out.println("returning a timestamp:  "+timeStamp);
				return timeStamp;
			}
		}
		return -1L;
	}
	
	@Override
	public Long sendMessage(User sender, User receiver, String messageBody) {
		if(sender == null || receiver == null || messageBody == null) {
			return null;
		}
		
		if(friendshipManagementService.doesFriendshipExist(sender, receiver)) {
			Friendship friendship = friendshipManagementService.getFriendship(sender, receiver);
			FriendshipDto frinedshipDto = friendshipConverter.convertFriendshipToFriendshipDto(friendship);
			Message message = new DefaultMessage(sender, receiver, messageBody);
			MessageDto msg = messageConverter.convertMessageToMessageDto(message);
			Long send = messageDao.saveMessage(msg);
			if(send != -1L || send != null) {
				if(friendshipDao.isFirst(frinedshipDto, userConverter.convertUserToUserDto(receiver))) {
					int notificiation = friendshipDao.getNotification(frinedshipDto, 1);
					System.out.println("BEFORE: "+notificiation);
					friendshipDao.setNotification(frinedshipDto, notificiation+1, 1);
					friendshipDao.setNotification(frinedshipDto, 0, 2);
				}
				else {
					int notificiation = friendshipDao.getNotification(frinedshipDto, 2);
					System.out.println("BEFORE: "+notificiation);
					friendshipDao.setNotification(frinedshipDto, notificiation+1, 2);
					friendshipDao.setNotification(frinedshipDto, 0, 1);
				}
			}
			return send;
			
		}
		return -1L;
		
	}

	@Override
	public Message[] getMessagesBetween(User firstUser, User secondUser) {
		UserDto first = userConverter.convertUserToUserDto(firstUser);
		UserDto second = userConverter.convertUserToUserDto(secondUser);
		List<MessageDto> messages = messageDao.getMessagesByUsers(first, second);
		List<Message> newM = messageConverter.convertMessageDtosToMessages(messages);
		Message[] result = new Message[newM.size()];
		newM.toArray(result);
		return result;
	}

	@Override
	public Message[] getLastNMessagesBetween(User firstUser, User secondUser, int N) {
		UserDto first = userConverter.convertUserToUserDto(firstUser);
		UserDto second = userConverter.convertUserToUserDto(secondUser);
		List<MessageDto> messages = messageDao.getLastNMessagesByUsers(first, second, N);
		List<Message> newM = messageConverter.convertMessageDtosToMessages(messages);
		Collections.reverse(newM);
		if(newM.size()<N) {
			N = newM.size();
		}
		Message[] result = new Message[N];
		newM.toArray(result);
		return result;
	}
	
	@Override
	public String getLastNJsonMessagesBetween(Long friendshipId, String firstUserNickName, String secondUserNickName, int N) {

		String messagesJson = messageDao.getLastNJsonMessagesByUsers(friendshipId, firstUserNickName, secondUserNickName, N);
		return messagesJson;
	}

}
