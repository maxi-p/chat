package com.encryptedmessenger.dao.impl;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.encryptedmessenger.dao.MessageDao;
import com.encryptedmessenger.dto.MessageDto;
import com.encryptedmessenger.dto.UserDto;
import com.encryptedmessenger.utils.db.DBUtils;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonWriter;

public class MySqlJdbcMessageDao implements MessageDao{
	private static MySqlJdbcMessageDao instance;
	private MySqlJdbcUserDao userDao;

	public static MySqlJdbcMessageDao getInstance() {
		if (instance == null) {
			instance = new MySqlJdbcMessageDao();
		}
		return instance;
	}
	

	
	public MessageDto getLastMessageByFriendshipId(Long friendshipId) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement("SELECT * FROM friendship WHERE id = ?")) {

			ps.setLong(1, friendshipId);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					MessageDto messageDto = new MessageDto();
					messageDto.setId(rs.getLong("last_message_id"));
					messageDto.setMessageBody(rs.getString("last_message"));
					messageDto.setSender(userDao.getInstance().getUserById(rs.getLong("last_message_from")));
					messageDto.setReceiver(userDao.getInstance().getUserById(rs.getLong("last_message_to")));
					messageDto.setTimeStamp(rs.getLong("last_time_stamp"));
					return messageDto;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public MessageDto getMessageById(Long id) {
		try (var conn = DBUtils.getConnection(); 
				var ps = conn.prepareStatement("SELECT * FROM message WHERE id = ?")) {

			ps.setLong(1, id);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					MessageDto message = new MessageDto();
					message.setId(rs.getLong("id"));
					message.setMessageBody(rs.getString("message_body"));
					message.setSender(userDao.getInstance().getUserById(rs.getLong("sender")));
					message.setReceiver(userDao.getInstance().getUserById(rs.getLong("receiver")));
					message.setTimeStamp(rs.getLong("time_stamp"));
					return message;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public Long saveMessageIds(String senderNickName, String receiverNickName, Long timeStamp, String message){
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement("INSERT INTO message (message_body, time_stamp, sender, receiver) VALUES (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, message);
				ps.setLong(2, timeStamp);
				ps.setString(3, senderNickName);
				ps.setString(4, receiverNickName);
				
				ps.executeUpdate();
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
		            if (generatedKeys.next()) {
		            	System.out.println("returning!");
		                return generatedKeys.getLong(1);
		            }
		            else {
		                throw new SQLException("Creating user failed, no ID obtained.");
		            }
		        }
				
			} catch (SQLException e) {
				e.printStackTrace();
				return -1L;
			}
	}
	
	@Override
	public Long saveMessage(MessageDto message) {
		try (var conn = DBUtils.getConnection();
			var ps = conn.prepareStatement("INSERT INTO message (message_body, time_stamp, sender, receiver) VALUES (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, message.getMessageBody());
			ps.setLong(2, message.getTimeStamp());
			ps.setInt(3, message.getSender().getId());
			ps.setInt(4, message.getReceiver().getId());
			
			ps.executeUpdate();
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                return generatedKeys.getLong(1);
	            }
	            else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
			
		} catch (SQLException e) {
			e.printStackTrace();
			return -1L;
		}
	}
	
	
	
	@Override
	public List<MessageDto> getMessagesByUsers(UserDto first, UserDto second) {
		try (var conn = DBUtils.getConnection();
			var ps = conn.prepareStatement("SELECT * FROM message WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) ORDER BY id")) {
			ps.setInt(1, first.getId());
			ps.setInt(2, second.getId());
			ps.setInt(3, second.getId());
			ps.setInt(4, first.getId());

			try (var rs = ps.executeQuery()) {
				List<MessageDto> messages = new ArrayList<>();

				while (rs.next()) {
					MessageDto message = new MessageDto();
					message.setId(rs.getLong("id"));
					message.setMessageBody(rs.getString("message_body"));
					message.setTimeStamp(rs.getLong("time_stamp"));
					message.setSender(userDao.getInstance().getUserById(rs.getLong("sender")));
					message.setReceiver(userDao.getInstance().getUserById(rs.getLong("receiver")));
					messages.add(message);
				}

				return messages;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<MessageDto> getLastNMessagesByUsers(UserDto first, UserDto second, int N) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement("SELECT * FROM message WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) ORDER BY id DESC LIMIT ?")) {
				ps.setInt(1, first.getId());
				ps.setInt(2, second.getId());
				ps.setInt(3, second.getId());
				ps.setInt(4, first.getId());
				ps.setInt(5, N);

				try (var rs = ps.executeQuery()) {
					List<MessageDto> messages = new ArrayList<>();

					while (rs.next()) {
						MessageDto message = new MessageDto();
						message.setId(rs.getLong("id"));
						message.setMessageBody(rs.getString("message_body"));
						System.out.println("getting time stamp: "+rs.getLong("time_stamp"));
						message.setTimeStamp(rs.getLong("time_stamp"));
						message.setSender(userDao.getInstance().getUserById(rs.getLong("sender")));
						message.setReceiver(userDao.getInstance().getUserById(rs.getLong("receiver")));
						messages.add(message);
					}

					return messages;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
	}
	
	@Override
	public String getLastNJsonMessagesByUsers(Long friendshipId, String firstNickName, String secondNickName, int N) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement("SELECT * FROM (SELECT * FROM message WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) ORDER BY id DESC LIMIT ?) s ORDER BY id ASC")) {
				ps.setString(1, firstNickName);
				ps.setString(2, secondNickName);
				ps.setString(3, secondNickName);
				ps.setString(4, firstNickName);
				ps.setInt(5, N);

				try (var rs = ps.executeQuery()) {
					JsonObjectBuilder jsonRootBuilder = Json.createObjectBuilder();
					JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

					while (rs.next()) {
						JsonObjectBuilder messageBuilder = Json.createObjectBuilder();
						JsonObject msg = messageBuilder
								.add("sender", rs.getString("sender"))
								.add("msg", rs.getString("message_body"))
								.add("time", new Date(rs.getLong("time_stamp")).toString())
								.build();
			
						jsonArrayBuilder.add(msg);
					}
					JsonObject root = jsonRootBuilder
							.add("messages", jsonArrayBuilder)
							.add("loggeduser", firstNickName)
							.add("selectedFriend", secondNickName)
							.add("friendshipId", friendshipId.toString())
							.build();
			
					StringWriter stringWriter = new StringWriter();
					try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
						jsonWriter.write(root);
					}
					return stringWriter.toString();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
	}
}
