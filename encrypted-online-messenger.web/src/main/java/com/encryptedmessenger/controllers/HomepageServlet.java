package com.encryptedmessenger.controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import com.encryptedmessenger.Configurations;
import com.encryptedmessenger.enteties.Friendship;
import com.encryptedmessenger.enteties.Message;
import com.encryptedmessenger.enteties.User;
import com.encryptedmessenger.enteties.impl.DefaultUser;
import com.encryptedmessenger.services.FriendshipManagementService;
import com.encryptedmessenger.services.UserManagementService;
import com.encryptedmessenger.services.impl.MySqlFriendshipManagementService;
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

@WebServlet(urlPatterns = { "/homepage" })
public class HomepageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	UserManagementService userManagementService = MySqlUserManagementService.getInstance();
	FriendshipManagementService friendshipManagementService = MySqlFriendshipManagementService.getInstance();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String baseUrl = request.getScheme()
			      + "://"
			      + request.getServerName()
			      + ":"
			      + request.getServerPort()
			      + request.getServletContext().getContextPath();
		
		User currUser = (DefaultUser) request.getSession().getAttribute("loggedInUser");

		
		if (currUser != null) {
			String jsonFriends = friendshipManagementService.getFriendshipsJsonByUser(currUser);
			request.getSession().setAttribute("friends", jsonFriends);
			request.getRequestDispatcher(Configurations.VIEWS_PATH_RESOLVER + "homepage.jsp").forward(request, response);
		}
		else {
			response.sendRedirect(baseUrl+"/signup");
			return;
		}

		
	}

//	private String buildJsonData(List<Friendship> friendships, User currUser) {
//		JsonObjectBuilder jsonRootBuilder = Json.createObjectBuilder();
//		JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
//		for (Friendship friendship : friendships) {
//			JsonObjectBuilder messageBuilder = Json.createObjectBuilder();
//			JsonObject frnd;
//			String friendsipFirstNickName = friendship.getFirstFriend().getNickName();
//			User lastFrom = friendship.getLastMessageFrom();
//			String lastFromNickName;
//			if (lastFrom == null) {
//				lastFromNickName = "";
//			} else {
//				lastFromNickName = lastFrom.getNickName();
//			}
//			Integer notification = friendshipManagementService.getNotification(friendship, currUser);
//			if (friendsipFirstNickName.equals(currUser.getNickName())) {
//				
//				frnd = messageBuilder
//						.add("nick", friendship.getSecondFriend().getNickName())
//						.add("from", lastFromNickName).add("lastMessage", friendship.getLastMessageBody())
//						.add("lastTime", friendship.getLastTime())
//						.add("notification", notification)
//						.add("friendshipId", friendship.getFriendshipId())
//						.build();
//
//			} else {
//				User thisFriend = friendship.getFirstFriend();
//				frnd = messageBuilder
//						.add("nick", thisFriend.getNickName())
//						.add("from", lastFromNickName).add("lastMessage", friendship.getLastMessageBody())
//						.add("lastTime", friendship.getLastTime())
//						.add("notification", notification)
//						.add("friendshipId", friendship.getFriendshipId())
//						.build();
//			}
//
//			jsonArrayBuilder.add(frnd);
//		}
//
//		JsonObject root = jsonRootBuilder.add("friends", jsonArrayBuilder).build();
//
//		StringWriter stringWriter = new StringWriter();
//		try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
//			jsonWriter.write(root);
//		}
//		return stringWriter.toString();
//	}
	
}
