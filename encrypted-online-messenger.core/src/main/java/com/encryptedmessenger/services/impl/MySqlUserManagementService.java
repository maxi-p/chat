package com.encryptedmessenger.services.impl;

import java.util.List;

import com.encryptedmessenger.dao.impl.MySqlJdbcUserDao;
import com.encryptedmessenger.dto.converter.UserDtoToUserConverter;
import com.encryptedmessenger.enteties.User;
import com.encryptedmessenger.services.UserManagementService;

public class MySqlUserManagementService implements UserManagementService {
	private static MySqlUserManagementService instance;
	public MySqlJdbcUserDao userDao = MySqlJdbcUserDao.getInstance();
	public UserDtoToUserConverter userConverter = UserDtoToUserConverter.getInstance();
	
	public static MySqlUserManagementService getInstance() {
		if (instance == null) {
			instance = new MySqlUserManagementService();
		}
		return instance;
	}
	
	private MySqlUserManagementService() {
	}
	
	@Override
	public String registerUser(User user) {
		
		boolean created = userDao.saveUser(userConverter.convertUserToUserDto(user));
		if(created) {
			return "Sucessfully registered";
		}
		else {
			return "Error";
		}
	}
	
	@Override
	public User[] getUsers() {
		List<User> users = userConverter.convertUserDtosToUsers(userDao.getUsers());
		User[] result = new User[users.size()];
		users.toArray(result);
		return result;
	}
	
	@Override
	public User getUserByNickName(String userNickName) {
		return userConverter.convertUserDtoToUser(userDao.getUserByNickName(userNickName));
	}
	
	
}
