package com.encryptedmessenger.controllers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.encryptedmessenger.enteties.Message;
import com.encryptedmessenger.enteties.User;
import com.encryptedmessenger.enteties.impl.DefaultUser;
import com.encryptedmessenger.services.MessageManagementService;
import com.encryptedmessenger.services.UserManagementService;
import com.encryptedmessenger.services.impl.MySqlMessageManagementService;
import com.encryptedmessenger.services.impl.MySqlUserManagementService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/draft")
public class DraftServlet extends HttpServlet {
	UserManagementService userManagementService = MySqlUserManagementService.getInstance();
	MessageManagementService messageManagementService = MySqlMessageManagementService.getInstance();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String messagesBody = request.getParameter("draft");
		Message messages[];

		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getServletContext().getContextPath();
		
		User currUser = (DefaultUser) request.getSession().getAttribute("loggedInUser");
		User friend = userManagementService.getUserByNickName(request.getParameter("receiver"));
		if (currUser != null) {
			messageManagementService.sendMessage(currUser, friend, messagesBody);
			messages = messageManagementService.getMessagesBetween(currUser, friend);
			request.getSession().setAttribute(friend.getNickName(), messages);

		}
		response.sendRedirect(baseUrl + "/chat");
	}

}
