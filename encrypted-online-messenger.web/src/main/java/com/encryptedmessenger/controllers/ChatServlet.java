package com.encryptedmessenger.controllers;

import java.io.IOException;
import java.io.StringWriter;

import com.encryptedmessenger.Configurations;
import com.encryptedmessenger.enteties.Friendship;
import com.encryptedmessenger.enteties.Message;
import com.encryptedmessenger.enteties.User;
import com.encryptedmessenger.enteties.impl.DefaultUser;
import com.encryptedmessenger.services.FriendshipManagementService;
import com.encryptedmessenger.services.MessageManagementService;
import com.encryptedmessenger.services.UserManagementService;
import com.encryptedmessenger.services.impl.MySqlFriendshipManagementService;
import com.encryptedmessenger.services.impl.MySqlMessageManagementService;
import com.encryptedmessenger.services.impl.MySqlUserManagementService;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/chats")
public class ChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	UserManagementService userManagementService = MySqlUserManagementService.getInstance();
	MessageManagementService messageManagementService = MySqlMessageManagementService.getInstance();
	FriendshipManagementService friendshipManagementService = MySqlFriendshipManagementService.getInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getServletContext().getContextPath();

		Message messages[];
		User currUser = (DefaultUser) request.getSession().getAttribute("loggedInUser");
		String jsonMessages;
		User friend;
		String currUserNickName;
		String friendNickName = request.getParameter("nickName");
		Long friendshipId;
		
		if (friendNickName != null && currUser != null) {
			currUserNickName = currUser.getNickName(); // QUICK
			friend = userManagementService.getUserByNickName(request.getParameter("nickName")); // QUICK
			friendshipId = friendshipManagementService.getFriendshipId(currUserNickName, friendNickName); // QUICK!
			friendshipManagementService.readMessageByFriendshipId(friendshipId, currUserNickName); // QUICK
			jsonMessages = messageManagementService.getLastNJsonMessagesBetween(friendshipId, currUserNickName, friendNickName, 10); // QUICK
			
			request.getSession().setAttribute("messages", jsonMessages);
			request.getSession().setAttribute("friend", friend);
		} 
		
		else {
			response.sendRedirect(baseUrl);
			return;
		}

		request.getRequestDispatcher("/homepage").forward(request, response);
	}

//	private String buildJsonData(Message[] messages, String loggedUser, String friendshipId, String friend) {
//		JsonObjectBuilder jsonRootBuilder = Json.createObjectBuilder();
//		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
//		for (int i = 0; i < messages.length; i++) {
//			Message message = messages[i];
//			JsonObjectBuilder messageBuilder = Json.createObjectBuilder();
//			System.out.println("getting time: " + message.getTime());
//			JsonObject msg = messageBuilder.add("sender", message.getSender().getNickName())
//					.add("msg", message.getBody()).add("time", message.getTime()).build();
//
//			jsonArrayBuilder.add(msg);
//		}
//
//		JsonObject root = jsonRootBuilder
//				.add("messages", jsonArrayBuilder)
//				.add("loggeduser", loggedUser)
//				.add("selectedFriend", friend)
//				.add("friendshipId", friendshipId)
//				.build();
//
//		StringWriter stringWriter = new StringWriter();
//		try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
//			jsonWriter.write(root);
//		}
//		return stringWriter.toString();
//	}

}
