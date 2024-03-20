package com.encryptedmessenger.dao;

import java.util.List;

import com.encryptedmessenger.dto.UserDto;

public interface UserDao {
	
	public boolean saveUser(UserDto user);
	
	public List<UserDto> getUsers();

	public UserDto getUserByNickName(String userNickName);

	public UserDto getUserById(long id);

}
