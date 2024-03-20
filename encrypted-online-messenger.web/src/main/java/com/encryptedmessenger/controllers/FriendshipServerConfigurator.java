package com.encryptedmessenger.controllers;

import com.encryptedmessenger.enteties.User;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

public class FriendshipServerConfigurator extends ServerEndpointConfig.Configurator{
	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		sec.getUserProperties().put("usernick",(String)((User)((HttpSession) request.getHttpSession()).getAttribute("loggedInUser")).getNickName());
	}
}
