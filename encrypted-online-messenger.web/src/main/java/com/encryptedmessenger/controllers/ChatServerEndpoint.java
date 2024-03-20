package com.encryptedmessenger.controllers;

import java.io.IOException;

import com.encryptedmessenger.services.FriendshipManagementService;
import com.encryptedmessenger.services.MessageManagementService;
import com.encryptedmessenger.services.impl.MySqlFriendshipManagementService;
import com.encryptedmessenger.services.impl.MySqlMessageManagementService;

import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat", configurator = FriendshipServerConfigurator.class)
public class ChatServerEndpoint {
	MessageManagementService messageManagementService = MySqlMessageManagementService.getInstance();
	FriendshipManagementService friendshipManagementService = MySqlFriendshipManagementService.getInstance();

	@OnOpen
	public void handleOpen(EndpointConfig config, Session userSession) {
		System.out.println("socket connection opening");
	}

	@OnMessage
	public void handleMessage(String friendNick, Session userSession)
			throws IOException {
		System.out.println("socket server receives message!");
		String usernick = (String) userSession.getUserProperties().get("usernick");

		String jsonMessages;
		Long friendshipId;
		System.out.println(friendNick);
		System.out.println(usernick);
		if (friendNick != null && usernick != null && !friendNick.equals("") && !usernick.equals("")) {
			friendshipId = friendshipManagementService.getFriendshipId(usernick, friendNick); // QUICK!
			friendshipManagementService.readMessageByFriendshipId(friendshipId, usernick); // QUICK
			jsonMessages = messageManagementService.getLastNJsonMessagesBetween(friendshipId, usernick, friendNick, 10); // QUICK
			System.out.println(jsonMessages);
			try {
				userSession.getBasicRemote().sendText(jsonMessages);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		else {
			return;
		}
	}

	@OnClose
	public void handleClose(Session userSession) {
		System.out.println("socket connection closing");
	}

	@OnError
	public void handleError(Throwable t) {
		System.out.println("socket connection error!!");
		t.printStackTrace();
	}

}
