package com.encryptedmessenger.controllers;

import com.encryptedmessenger.enteties.User;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

public class AddFriendServerConfigurator extends ServerEndpointConfig.Configurator{
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		User user = (User)((HttpSession) request.getHttpSession()).getAttribute("loggedInUser");
		if(user != null)
		sec.getUserProperties().put("usernick",(String)(user).getNickName());
	}
}
