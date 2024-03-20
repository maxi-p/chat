package com.encryptedmessenger.controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.encryptedmessenger.enteties.Friendship;
import com.encryptedmessenger.enteties.Message;
import com.encryptedmessenger.enteties.User;
import com.encryptedmessenger.enteties.impl.DefaultFriendship;
import com.encryptedmessenger.services.FriendshipManagementService;
import com.encryptedmessenger.services.MessageManagementService;
import com.encryptedmessenger.services.UserManagementService;
import com.encryptedmessenger.services.impl.MySqlFriendshipManagementService;
import com.encryptedmessenger.services.impl.MySqlMessageManagementService;
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
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/socket/{friendship}", configurator = FriendshipServerConfigurator.class)
public class FriendshipServerEndpoint {
	static Map<String, Set<Session>> friendships = (Map<String, Set<Session>>) Collections
			.synchronizedMap(new HashMap<String, Set<Session>>());
	UserManagementService userManagementService = MySqlUserManagementService.getInstance();
	MessageManagementService messageManagementService = MySqlMessageManagementService.getInstance();
	FriendshipManagementService friendshipManagementService = MySqlFriendshipManagementService.getInstance();


	public Set<Session> getFriendship(String friendshipId) {
		Set<Session> friendship = friendships.get(friendshipId);
		if (friendship == null) {
			friendship = Collections.synchronizedSet(new HashSet<Session>());
			friendships.put(friendshipId, friendship);
		}
		return friendship;
	}

	@OnOpen
	public void handleOpen(EndpointConfig config, Session userSession, @PathParam("friendship") String friendshipId, @PathParam("receiver") String receiver) {
		System.out.println("socket connection opening");
		if (friendshipId == null || friendshipId.equals("")) {
			System.out.println("no param...");
			return;
		}
		String userNickName = (String)config.getUserProperties().get("usernick");
		String friendNickName = friendshipManagementService.getFriendNickByFriendshipIdAndUserNick(friendshipId, userNickName);
		System.out.println(friendshipId);
		userSession.getUserProperties().put("usernick", config.getUserProperties().get("usernick"));
		userSession.getUserProperties().put("friend", friendNickName);
		userSession.getUserProperties().put("friendship", friendshipId);
		Set<Session> friendshipUsers = getFriendship(friendshipId);
		friendshipUsers.add(userSession);
	}

	@OnMessage
	public void handleMessage(String webMessage, Session userSession) throws IOException {
		System.out.println("socket server receives message!");
		
		String usernick = (String) userSession.getUserProperties().get("usernick");
		String friendnick = (String) userSession.getUserProperties().get("friend");
		String friendship = (String) userSession.getUserProperties().get("friendship");
		Long friendshipLong;
		
		String message = webMessage;;
		
		// ADMIN GREETING
		if(webMessage.contains("vluafdihsgfjm;vHCJASNCVIASCN_!(OCIDCNJZNELVr8yp2*&Y&*&SDVJESLAWD@!(&$@(#8417foaeltrurjw852qi0(*hnL&&^F#&I*F@ECSJZSfqvnlsnd23895dbkjanaks))))")) {
			String realMessage = webMessage.replace("vluafdihsgfjm;vHCJASNCVIASCN_!(OCIDCNJZNELVr8yp2*&Y&*&SDVJESLAWD@!(&$@(#8417foaeltrurjw852qi0(*hnL&&^F#&I*F@ECSJZSfqvnlsnd23895dbkjanaks))))", "");
			message = realMessage;
			friendnick = usernick;
			usernick = "admin";
			
		}
		
		// REGULAR MESSAGE PROCESS
		if(friendship != null) {
			friendshipLong = Long.valueOf(friendship);
		}
		else {
			return;
		}
		
		Long messageTimeStamp = messageManagementService.sendMessageByIds(usernick, friendnick, friendshipLong, message); // QUICK
		String builtJson = friendshipManagementService.setLastMessageByIdsJson(messageTimeStamp, message, friendshipLong, usernick, friendnick); // REALY QUICK
		Integer notificationsSender = 0;
		System.out.println("JSON!!!!!");
		System.out.println(builtJson);
		
		
		Set<Session> friendshipUsers = getFriendship(friendship);
		friendshipUsers.stream().forEach(x -> {
			try {
				x.getBasicRemote().sendText(builtJson);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@OnClose
	public void handleClose(Session userSession) {
		System.out.println("socket connection closing");
		String friendship = (String) userSession.getUserProperties().get("friendship");
		Set<Session> friendshipUsers = getFriendship(friendship);
		friendshipUsers.remove(userSession);
	}

//	private String buildJsonData(String receiver, String sender, String message, String messageTime, Integer notification, Integer notificationSender) {
//		JsonObject jsonObject = Json.createObjectBuilder()
//				.add("sender", sender)
//				.add("time", messageTime)
//				.add("msg", message)
//				.add("receiver",receiver)
//				.add("notification", notification)
//				.add("notificationSender", notificationSender)
//				.build();
//		
//		StringWriter stringWriter = new StringWriter();
//		try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
//			jsonWriter.write(jsonObject);
//		}
//		return stringWriter.toString();
//	}

	@OnError
	public void handleError(Throwable t) {
		System.out.println("socket connection error!!");
		t.printStackTrace();
	}

}
