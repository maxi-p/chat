package com.encryptedmessenger.dao.impl;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.encryptedmessenger.dao.FriendshipDao;
import com.encryptedmessenger.dto.FriendshipDto;
import com.encryptedmessenger.dto.MessageDto;
import com.encryptedmessenger.dto.UserDto;
import com.encryptedmessenger.utils.db.DBUtils;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonWriter;

public class MySqlJdbcFriendshipDao implements FriendshipDao {

	private static MySqlJdbcFriendshipDao instance;
	private MySqlJdbcUserDao userDao = MySqlJdbcUserDao.getInstance();

	public static MySqlJdbcFriendshipDao getInstance() {
		if (instance == null) {
			instance = new MySqlJdbcFriendshipDao();
		}
		return instance;
	}

	@Override
	public String getFriendNickByFriendshipIdAndUserNick(String FriendshipId, String userNickName) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement(
						"SELECT * FROM friendship WHERE id = ? AND (first_friend = ? OR second_friend = ?)")) {
			ps.setLong(1, Long.valueOf(FriendshipId));
			ps.setString(2, userNickName);
			ps.setString(3, userNickName);

			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					String one = rs.getString("first_friend");
					String two = rs.getString("second_friend");
					UserDto curr;
					if (userNickName.equals(one)) {
						return two;
					} else {
						return one;
					}
				}
				return new String("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String("");
		}
	}

	@Override
	public String getLastTimeById(Long friendshipId) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement("SELECT * FROM friendship WHERE id = ?")) {
			ps.setLong(1, friendshipId);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					Long lastTime = rs.getLong("last_time_stamp");
					if (!rs.wasNull()) {
						return (new Date(lastTime)).toString();
					}
				}
				return new String("");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String("");
		}
	}

	@Override
	public UserDto getFriendByUser(FriendshipDto friendshipDto, UserDto userDto) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement(
						"SELECT * FROM friendship WHERE id = ? AND (first_friend = ? OR second_friend = ?)")) {
			ps.setLong(1, friendshipDto.getId());
			ps.setInt(2, userDto.getId());
			ps.setInt(3, userDto.getId());

			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					UserDto friend = new UserDto();
					int one = rs.getInt("first_friend");
					int two = rs.getInt("second_friend");
					int him;
					UserDto curr;
					if (userDto.getId() == one) {
						him = two;
					} else {
						him = one;
					}
					curr = userDao.getUserById(him);
					friend.setId(him);
					friend.setNickName(curr.getNickName());
					friend.setPassword(curr.getPassword());
					return friend;
				}
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Integer getNotification(FriendshipDto friendshipDto, int flag) {
		if (flag == 1) {
			try (var conn = DBUtils.getConnection();
					var ps = conn.prepareStatement("SELECT * FROM friendship WHERE (id = ?)")) {
				ps.setLong(1, friendshipDto.getId());

				try (var rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt("notifications_first");
					}
					return -1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return -1;
			}
		} else {
			try (var conn = DBUtils.getConnection();
					var ps = conn.prepareStatement("SELECT * FROM friendship WHERE (id = ?)")) {
				ps.setLong(1, friendshipDto.getId());

				try (var rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt("notifications_second");
					}
					return -1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return -1;
			}
		}
	}

	@Override
	public Integer getNotificationIds(Long friendshipId, int flag) {
		if (flag == 1) {
			try (var conn = DBUtils.getConnection();
					var ps = conn.prepareStatement("SELECT * FROM friendship WHERE (id = ?)")) {
				ps.setLong(1, friendshipId);

				try (var rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt("notifications_first");
					}
					return -1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return -1;
			}
		} else {
			try (var conn = DBUtils.getConnection();
					var ps = conn.prepareStatement("SELECT * FROM friendship WHERE (id = ?)")) {
				ps.setLong(1, friendshipId);

				try (var rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt("notifications_second");
					}
					return -1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return -1;
			}
		}
	}

	@Override
	public boolean isFirst(FriendshipDto friendshipDto, UserDto userDto) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement("SELECT * FROM friendship WHERE (id = ?) AND (first_friend = ?)")) {
			ps.setLong(1, friendshipDto.getId());
			ps.setInt(2, userDto.getId());

			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					System.out.println(userDto.getNickName() + " is first...");
					return true;
				}
				System.out.println(userDto.getNickName() + " is second");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean isFirstById(Long friendshipId, String userNickName) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement("SELECT * FROM friendship WHERE (id = ?) AND (first_friend = ?)")) {
			ps.setLong(1, friendshipId);
			ps.setString(2, userNickName);

			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					return true;
				}
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean setNotification(FriendshipDto friendshipDto, int val, int flag) {
		if (flag == 1) {
			try (var conn = DBUtils.getConnection();
					var ps = conn.prepareStatement("UPDATE friendship SET notifications_first = ? WHERE (id = ?)")) {
				ps.setLong(1, val);
				ps.setLong(2, friendshipDto.getId());

				ps.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			try (var conn = DBUtils.getConnection();
					var ps = conn.prepareStatement("UPDATE friendship SET notifications_second = ? WHERE (id = ?)")) {
				ps.setLong(1, val);
				ps.setLong(2, friendshipDto.getId());

				ps.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}

	}

	@Override
	public boolean setNotificationById(Long friendshipId, int val, int flag) {
		if (flag == 1) {
			try (var conn = DBUtils.getConnection();
					var ps = conn.prepareStatement("UPDATE friendship SET notifications_first = ? WHERE (id = ?)")) {
				ps.setLong(1, val);
				ps.setLong(2, friendshipId);

				ps.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			try (var conn = DBUtils.getConnection();
					var ps = conn.prepareStatement("UPDATE friendship SET notifications_second = ? WHERE (id = ?)")) {
				ps.setLong(1, val);
				ps.setLong(2, friendshipId);

				ps.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}

	}

	@Override
	public boolean setLastMessageByIds(Long messageTimeStamp, String message, Long friendshipLong,
			String senderNickName, String receiverNickName, Integer notification, int flag) {
		if (flag == 1) {
			try (var conn = DBUtils.getConnection();
					var ps = conn.prepareStatement(
							"UPDATE friendship SET notifications_first = ?, notifications_second = ?, last_message = ?, last_time_stamp = ?, last_message_from = ?, last_message_to = ? WHERE (id = ?)")) {
				ps.setLong(1, notification);
				ps.setLong(2, 0L);
				ps.setString(3, message);
				ps.setLong(4, messageTimeStamp);
				ps.setString(5, senderNickName);
				ps.setString(6, receiverNickName);
				ps.setLong(7, friendshipLong);
				ps.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			try (var conn = DBUtils.getConnection();
					var ps = conn.prepareStatement(
							"UPDATE friendship SET notifications_second = ?, notifications_first = ?, last_message = ?, last_time_stamp = ?, last_message_from = ?, last_message_to = ? WHERE (id = ?)")) {
				ps.setLong(1, notification);
				ps.setLong(2, 0L);
				ps.setString(3, message);
				ps.setLong(4, messageTimeStamp);
				ps.setString(5, senderNickName);
				ps.setString(6, receiverNickName);
				ps.setLong(7, friendshipLong);
				ps.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	@Override
	public boolean setLastMessage(Long FriendshipId, MessageDto messageDto) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement(
						"UPDATE friendship SET last_message_id = ?, last_message = ?, last_time_stamp = ?, last_message_from = ?, last_message_to = ? WHERE (id = ?)")) {
			ps.setLong(1, messageDto.getId());
			ps.setString(2, messageDto.getMessageBody());
			ps.setLong(3, messageDto.getTimeStamp());
			ps.setInt(4, messageDto.getSender().getId());
			ps.setInt(5, messageDto.getReceiver().getId());
			ps.setLong(6, FriendshipId);

			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public FriendshipDto getFriendshipBetween(UserDto firstUser, UserDto secondUser) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement(
						"SELECT * FROM friendship WHERE (first_friend = ? AND second_friend = ?) OR (first_friend = ? AND second_friend = ?)")) {
			ps.setString(1, firstUser.getNickName());
			ps.setString(2, secondUser.getNickName());
			ps.setString(3, secondUser.getNickName());
			ps.setString(4, firstUser.getNickName());

			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					FriendshipDto friendship = new FriendshipDto();
					friendship.setId(rs.getLong("id"));
					friendship.setFirstFriend(userDao.getUserByNickName(rs.getString("first_friend")));
					friendship.setSecondFriend(userDao.getUserByNickName(rs.getString("second_friend")));
					friendship.setLastTimeStamp(rs.getLong("last_time_stamp"));
					return friendship;
				}
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Long getFriendshipIdBetween(String firstUserId, String secondUserId) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement(
						"SELECT * FROM friendship WHERE (first_friend = ? AND second_friend = ?) OR (first_friend = ? AND second_friend = ?)")) {
			ps.setString(1, firstUserId);
			ps.setString(2, secondUserId);
			ps.setString(3, secondUserId);
			ps.setString(4, firstUserId);

			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("id");
				}
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public FriendshipDto getFriendshipById(long id) {

		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement("SELECT * FROM friendship WHERE id = ?")) {

			ps.setLong(1, id);
			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					FriendshipDto friendship = new FriendshipDto();
					friendship.setId(rs.getLong("id"));
					friendship.setFirstFriend(userDao.getUserById(rs.getInt("first_friend")));
					friendship.setSecondFriend(userDao.getUserById(rs.getInt("second_friend")));
					return friendship;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean areFriends(UserDto first, UserDto second) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement(
						"SELECT * FROM friendship WHERE (first_friend = ? AND second_friend = ?) OR (first_friend = ? AND second_friend = ?)")) {
			ps.setString(1, first.getNickName());
			ps.setString(2, second.getNickName());
			ps.setString(3, second.getNickName());
			ps.setString(4, first.getNickName());

			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					return true;
				}
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Long areFriendsIds(String firstUser, String secondUser) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement(
						"SELECT * FROM friendship WHERE (first_friend = ? AND second_friend = ?) OR (first_friend = ? AND second_friend = ?)")) {
			ps.setString(1, firstUser);
			ps.setString(2, secondUser);
			ps.setString(3, secondUser);
			ps.setString(4, firstUser);

			try (var rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getLong("id");
				}
				return 0L;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0L;
		}
	}
	
	@Override
	public String saveFriendshipIds(String firstUser, String secondUser, Long lastTimeStamp) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement(
						"INSERT INTO friendship (first_friend, second_friend, last_time_stamp, notifications_first, notifications_second) VALUES (?, ?, ?, ?, ?);",
						Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, firstUser);
			ps.setString(2, secondUser);
			ps.setLong(3, lastTimeStamp);
			ps.setInt(4, 0);
			ps.setInt(5, 0);

			ps.executeUpdate();

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return String.valueOf(generatedKeys.getLong(1));
				} else {
					throw new SQLException("Creating user failed, no ID obtained.");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return new String("");
		}
	}
	
	@Override
	public String saveFriendship(FriendshipDto friendship) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement(
						"INSERT INTO friendship (first_friend, second_friend, last_time_stamp, notifications_first, notifications_second) VALUES (?, ?, ?, ?, ?);",
						Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, friendship.getFirstFriend().getNickName());
			ps.setString(2, friendship.getSecondFriend().getNickName());
			ps.setLong(3, friendship.getLastTimeStamp());
			ps.setInt(4, 0);
			ps.setInt(5, 0);

			ps.executeUpdate();

			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					return String.valueOf(generatedKeys.getLong(1));
				} else {
					throw new SQLException("Creating user failed, no ID obtained.");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return new String("");
		}
	}
//	private String buildJsonData(List<Friendship> friendships, User currUser) {
//	JsonObjectBuilder jsonRootBuilder = Json.createObjectBuilder();
//	JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
//	for (Friendship friendship : friendships) {
//		JsonObjectBuilder messageBuilder = Json.createObjectBuilder();
//		JsonObject frnd;
//		String friendsipFirstNickName = friendship.getFirstFriend().getNickName();
//		User lastFrom = friendship.getLastMessageFrom();
//		String lastFromNickName;
//		if (lastFrom == null) {
//			lastFromNickName = "";
//		} else {
//			lastFromNickName = lastFrom.getNickName();
//		}
//		Integer notification = friendshipManagementService.getNotification(friendship, currUser);
//		if (friendsipFirstNickName.equals(currUser.getNickName())) {
//			
//			frnd = messageBuilder
//					.add("nick", friendship.getSecondFriend().getNickName())
//					.add("from", lastFromNickName).add("lastMessage", friendship.getLastMessageBody())
//					.add("lastTime", friendship.getLastTime())
//					.add("notification", notification)
//					.add("friendshipId", friendship.getFriendshipId())
//					.build();
//
//		} else {
//			User thisFriend = friendship.getFirstFriend();
//			frnd = messageBuilder
//					.add("nick", thisFriend.getNickName())
//					.add("from", lastFromNickName).add("lastMessage", friendship.getLastMessageBody())
//					.add("lastTime", friendship.getLastTime())
//					.add("notification", notification)
//					.add("friendshipId", friendship.getFriendshipId())
//					.build();
//		}
//
//		jsonArrayBuilder.add(frnd);
//	}
//
//	JsonObject root = jsonRootBuilder.add("friends", jsonArrayBuilder).build();
//
//	StringWriter stringWriter = new StringWriter();
//	try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
//		jsonWriter.write(root);
//	}
//	return stringWriter.toString();
//}

	@Override
	public String getFriendshipsJsonByUser(UserDto user) {
		String currentUserNick = user.getNickName();
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement(
						"SELECT * FROM friendship WHERE first_friend = ? OR second_friend = ? ORDER BY last_time_stamp DESC")) {
			ps.setString(1, currentUserNick);
			ps.setString(2, currentUserNick);

			try (var rs = ps.executeQuery()) {
				JsonObjectBuilder jsonRootBuilder = Json.createObjectBuilder();
				JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
				while (rs.next()) {
					JsonObjectBuilder messageBuilder = Json.createObjectBuilder();
					JsonObject frnd;
					String friendsipFirstNickName = rs.getString("first_friend");

					String lastFrom = rs.getString("last_message_from");
					String lastFromNickName;
					if (rs.wasNull()) {
						lastFromNickName = new String("");
					} else {
						lastFromNickName = lastFrom;
					}

					Integer notification;
					if (currentUserNick.equals(rs.getString("first_friend"))) {
						notification = rs.getInt("notifications_first");
					} else {
						notification = rs.getInt("notifications_second");
					}
					String lastMessage = rs.getString("last_message");
					if (rs.wasNull()) {
						lastMessage = new String("");
					}
					if (currentUserNick.equals(friendsipFirstNickName)) {
						frnd = messageBuilder.add("nick", rs.getString("second_friend")).add("from", lastFromNickName)
								.add("lastMessage", lastMessage)
								.add("lastTime", (new Date(rs.getLong("last_time_stamp")).toString()))
								.add("notification", notification).add("friendshipId", String.valueOf(rs.getLong("id")))
								.build();
					} else {
						frnd = messageBuilder.add("nick", rs.getString("first_friend")).add("from", lastFromNickName)
								.add("lastMessage", lastMessage)
								.add("lastTime", (new Date(rs.getLong("last_time_stamp")).toString()))
								.add("notification", notification).add("friendshipId", String.valueOf(rs.getLong("id")))
								.build();
					}
					jsonArrayBuilder.add(frnd);

				}
				JsonObject root = jsonRootBuilder.add("friends", jsonArrayBuilder).build();
				StringWriter stringWriter = new StringWriter();
				try (JsonWriter jsonWriter = Json.createWriter(stringWriter)) {
					jsonWriter.write(root);
				}
				return stringWriter.toString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public List<FriendshipDto> getFriendshipsByUser(UserDto user) {
		try (var conn = DBUtils.getConnection();
				var ps = conn.prepareStatement(
						"SELECT * FROM friendship WHERE first_friend = ? OR second_friend = ? ORDER BY last_time_stamp DESC")) {
			ps.setInt(1, user.getId());
			ps.setInt(2, user.getId());

			try (var rs = ps.executeQuery()) {
				List<FriendshipDto> friendships = new ArrayList<>();

				while (rs.next()) {
					FriendshipDto friendship = new FriendshipDto();
					friendship.setId(rs.getLong("id"));
					friendship.setFirstFriend(userDao.getUserById(rs.getInt("first_friend")));
					friendship.setSecondFriend(userDao.getUserById(rs.getInt("second_friend")));

					friendship.setLastMessageId(rs.getLong("last_message_id"));
					friendship.setLastMessageBody(rs.getString("last_message"));
					friendship.setLastMessageFrom(userDao.getUserById(rs.getLong("last_message_from")));
					friendship.setLastMessageTo(userDao.getUserById(rs.getLong("last_message_to")));
					friendship.setLastTimeStamp(rs.getLong("last_time_stamp"));

					friendships.add(friendship);
				}

				return friendships;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<UserDto> getFriendsByUser(UserDto user) {
		try (var conn = DBUtils.getConnection();
				var ps = conn
						.prepareStatement("SELECT * FROM friendship WHERE first_friend = ? OR second_friend = ?")) {
			ps.setInt(1, user.getId());
			ps.setInt(2, user.getId());

			try (var rs = ps.executeQuery()) {
				List<UserDto> users = new ArrayList<>();

				while (rs.next()) {
					UserDto friend = new UserDto();
					int one = rs.getInt("first_friend");
					int two = rs.getInt("second_friend");
					int him;
					UserDto curr;
					if (user.getId() == one) {
						him = two;
					} else {
						him = one;
					}
					curr = userDao.getUserById(him);
					friend.setId(him);
					friend.setNickName(curr.getNickName());
					friend.setPassword(curr.getPassword());
					users.add(friend);
				}

				return users;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
