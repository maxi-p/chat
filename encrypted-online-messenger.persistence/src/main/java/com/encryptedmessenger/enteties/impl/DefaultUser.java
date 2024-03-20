package com.encryptedmessenger.enteties.impl;

import com.encryptedmessenger.enteties.User;

public class DefaultUser implements User {
	
	private static int userCounter = 0;
	
	private int id;
	private String nickName;
	private String password;

	{
		id = ++userCounter;
	}
	
	public DefaultUser() {
	}
	
	public DefaultUser(String nickName, String password) {
		this.nickName = nickName;
		this.password = password;
	}
	
	public DefaultUser(String nickName) {
		this.nickName = nickName;
		this.password = new String("");
	}

	@Override
	public String getNickName() {
		return this.nickName;
	}

	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public String toString() {
		return "Nick-Name: " + this.getNickName() + "\t\t";
	}
	
	@Override
	public void setNickName(String nickName) {
		if(nickName == null) {
			return;
		}
		this.nickName = nickName;
	}

	@Override
	public void setPassword(String password) {
		if (password == null) {
			return;
		}
		this.password = password;
	}

	@Override
	public int getId() {
		return this.id;
	}
	
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	void clearState() {
		userCounter = 0;
	}
}
