package com.encryptedmessenger.services;

import com.encryptedmessenger.enteties.User;

public interface UserManagementService {

	public String registerUser(User user);
	public User[] getUsers();
	public User getUserByNickName(String userNickName);

}