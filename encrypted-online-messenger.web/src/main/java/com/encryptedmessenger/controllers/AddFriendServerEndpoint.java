package com.encryptedmessenger.controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.encryptedmessenger.enteties.Friendship;
import com.encryptedmessenger.enteties.User;
import com.encryptedmessenger.enteties.impl.DefaultUser;
import com.encryptedmessenger.services.FriendshipManagementService;
import com.encryptedmessenger.services.UserManagementService;
import com.encryptedmessenger.services.impl.MySqlFriendshipManagementService;
import com.encryptedmessenger.services.impl.MySqlUserManagementService;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonWriter;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/addFriendSocket", configurator = AddFriendServerConfigurator.class)
public class AddFriendServerEndpoint {
	static Map<String, Session> friendships = (Map<String, Session>) Collections
			.synchronizedMap(new HashMap<String, Session>());
	UserManagementService userManagementService = MySqlUserManagementService.getInstance();
	FriendshipManagementService friendshipManagementService = MySqlFriendshipManagementService.getInstance();

	public Session getFriend(String friendNick) {
		Session friendship = friendships.get(friendNick);
		return friendship;
	}

	@OnOpen
	public void handleOpen(EndpointConfig config, Session userSession) {
		System.out.println("socket connection opening");
		String userNick = (String) config.getUserProperties().get("usernick");
		userSession.getUserProperties().put("usernick", userNick);
		friendships.put(userNick, userSession);
	}

	@OnMessage
	public void handleMessage(String friendNick, Session userSession) throws IOException {
		System.out.println("socket server receives message!");
		String usernick = (String) userSession.getUserProperties().get("usernick");
		
		// ADMIN GREETING SCRIPT
		if (friendNick.equals("vluafdihsgfjm;vHCJASNCVIASCN_!(OCIDCNJZNELVr8yp2*&Y&*&SDVJESLAWD@!(&$@(#8417foaeltrurjw852qi0(*hnL&&^F#&I*F@ECSJZSfqvnlsnd23895dbkjanaks))))")) {
			String createdFriendshipId = friendshipManagementService.createFriendshipIds(usernick, "admin");
			String lastTime;
			if (!createdFriendshipId.equals("")) {
				lastTime = friendshipManagementService.getLastTimeById(Long.valueOf(createdFriendshipId));
			} else {
				return;
			}
			
			try {
				userSession.getBasicRemote().sendText(buildJsonData("admin", createdFriendshipId, lastTime));
				System.out.println("socket connection SENDING TO CLIENT!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		// REGULAR ADD FREIND
		else {

			User newFriend = userManagementService.getUserByNickName(friendNick); // FAIRLY QUICK
			if (newFriend == null) {
				// Error nick doesnt exist
				System.out.println("nick wrong!");
				return;
			} else if (friendshipManagementService.doesFriendshipExistIds(usernick, friendNick)) {
				// Error friendship exists
				return;
			}

			String createdFriendshipId = friendshipManagementService.createFriendshipIds(usernick, friendNick);
			String lastTime;
			if (!createdFriendshipId.equals("")) {
				lastTime = friendshipManagementService.getLastTimeById(Long.valueOf(createdFriendshipId));
			} else {
				return;
			}

			Session friendSession = getFriend(friendNick);

			try {
				userSession.getBasicRemote().sendText(buildJsonData(friendNick, createdFriendshipId, lastTime));
				System.out.println("socket connection SENDING TO CLIENT!");
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (friendSession != null)
					friendSession.getBasicRemote().sendText(buildJsonData(usernick, createdFriendshipId, lastTime));
				System.out.println("socket connection SENDING TO CLIENT!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@OnClose
	public void handleClose(Session userSession) {
		System.out.println("socket connection closing");
		friendships.remove((String) userSession.getUserProperties().get("usernick"));
	}

	private String buildJsonData(String username, String friendshipId, String lastTime) {
		JsonObject jsonObject = Json.createObjectBuilder().add("nick", username).add("time", lastTime)
				.add("notification", "0").add("friendshipId", friendshipId).build();
		StringWriter stringWriter = new StringWriter();
		try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
			jsonWriter.write(jsonObject);
		}
		return stringWriter.toString();
	}

	@OnError
	public void handleError(Throwable t) {
		System.out.println("socket connection error!!");
		t.printStackTrace();
	}

}
