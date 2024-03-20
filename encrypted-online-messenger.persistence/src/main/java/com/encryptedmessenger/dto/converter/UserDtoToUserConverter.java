package com.encryptedmessenger.dto.converter;

import java.util.ArrayList;
import java.util.List;

import com.encryptedmessenger.dto.UserDto;
import com.encryptedmessenger.enteties.User;
import com.encryptedmessenger.enteties.impl.DefaultUser;

public class UserDtoToUserConverter {
	private static UserDtoToUserConverter instance;
	
	public static UserDtoToUserConverter getInstance() {
		if(instance == null) {
			instance = new UserDtoToUserConverter();
		}
		return instance;
	}
	
	public UserDto convertUserIdToUserDtoWithOnlyId(int userId) {
		UserDto userDto = new UserDto();
		userDto.setId(userId);
		return userDto;
	}
	
	public User convertUserDtoToUser(UserDto userDto) {
		if(userDto == null) {
			return null;
		}
		
		User user = new DefaultUser();
		user.setNickName(userDto.getNickName());
		user.setPassword(userDto.getPassword());
		user.setId(userDto.getId());
		
		return user;
	}
	
	public UserDto convertUserToUserDto(User user) {
		UserDto userDto = new UserDto();
		
		userDto.setNickName(user.getNickName());
		userDto.setPassword(user.getPassword());
		userDto.setId(user.getId());
		
		return userDto;
	}
	
	public List<User> convertUserDtosToUsers(List<UserDto> userDtos){
		List<User> users = new ArrayList<>();
		
		for(UserDto userDto : userDtos) {
			users.add(convertUserDtoToUser(userDto));
		}
		return users;
	}
}
