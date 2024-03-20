package com.encryptedmessenger.controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.ResourceBundle;

import com.encryptedmessenger.Configurations;
import com.encryptedmessenger.enteties.Friendship;
import com.encryptedmessenger.enteties.User;
import com.encryptedmessenger.enteties.impl.DefaultUser;
import com.encryptedmessenger.services.UserManagementService;
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

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserManagementService userManagementService = MySqlUserManagementService.getInstance();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(Configurations.VIEWS_PATH_RESOLVER 
				+ "signup.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String baseUrl = request.getScheme()
			      + "://"
			      + request.getServerName()
			      + ":"
			      + request.getServerPort()
			      + request.getServletContext().getContextPath();

		User user = new DefaultUser();
		
		user.setNickName(request.getParameter("nickName"));
		user.setPassword(request.getParameter("password"));
		
		User userByNickName = userManagementService.getUserByNickName(user.getNickName());
		
		if (userByNickName != null) {
			request.getSession().setAttribute("errMsg", "signup.err.msg.nickname.exists");
			response.sendRedirect(baseUrl + "/signup");
			return;
		}

		if (!user.getPassword().equals(request.getParameter("repeatPassword"))) {
			request.getSession().setAttribute("errMsg", "signup.err.msg.repeat.password");
			response.sendRedirect(baseUrl + "/signup");
			return;
		}
		
		userManagementService.registerUser(user);
		request.getSession().setAttribute("firstTimeUser", buildJsonData());
		response.sendRedirect(baseUrl + "/signin");
	}
	
	
	private String buildJsonData() {
		JsonObjectBuilder jsonRootBuilder = Json.createObjectBuilder();
			

		JsonObject root = jsonRootBuilder.add("isanewuser", "yes!").build();

		StringWriter stringWriter = new StringWriter();
		try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
			jsonWriter.write(root);
		}
		return stringWriter.toString();
	}

}


