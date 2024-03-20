package com.encryptedmessenger.enteties;

public interface User {
	
	public int getId();
	public String getNickName();
	public String getPassword();
	
	public void setId(int id);
	public void setNickName(String newNickName);
	public void setPassword(String newPassword);
	
}