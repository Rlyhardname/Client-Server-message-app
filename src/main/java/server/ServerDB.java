package server;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.ArrayList;

public class ServerDB {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/girrafe";
	static final String USER = "root";
	static final String PASS = "dCBZXTf49PcL3L97lWXP";
	Connection conn;
	static Statement stmt;
	PreparedStatement prep;
	String[] args;
	Enum task;

	ServerDB() {
		conn = null;
		stmt = null;
		prep = null;
		try {
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void closeConnection() {

		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
		System.out.println("Query has been executed and connection has been closed");

	}

	public ArrayList<String> selectRoomUsers() throws SQLException {

		ArrayList<String> users = new ArrayList<String>();
		String sql = "Select username from user where user = ?";
		prep = conn.prepareStatement(sql);
		ResultSet rs = prep.executeQuery();
		if (task.ordinal() == 1) {
			ResultSet resultSet = null;
			try {
				resultSet = stmt.getResultSet();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				while (resultSet.next()) {
					try {
						users.add(resultSet.getString(1));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return users;
	}

	private boolean isOnline(String user) {
		// proveri dali userera e online
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRegisteredUser(String username) {
		String sql = "SELECT username FROM User " + "WHERE username = ?";
		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, username);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				if (rs.getString(1) != null) {
					System.out.println("Takuv user sushtestvuva");
					return true;
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean passwordIsCorrect(String username, String password) {
		// TODO Auto-generated method stub
		String sql = "SELECT password from User where username = ?";
		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, username);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				if (rs.getString(1).equals(password)) {
					return true;
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e1) {
			e1.printStackTrace();
		}
		return false;
	}

	public boolean createUser(String username, String password) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO User " + "VALUES(?,?)";

		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, username);
			prep.setString(2, password);
			prep.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql = "SELECT username FROM User " + "WHERE username=?";

		ResultSet rs = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, username);
			rs = prep.executeQuery();
			if (!(rs.isBeforeFirst())) {

				return false;
			}

		} catch (

		SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public void createTables() {
		// TODO Auto-generated method stub

	}

	static boolean createTableUser() {
		String sql = "CREATE TABLE `user` (\n" + "  `username` varchar(25) NOT NULL,\n"
				+ "  `password` varchar(32) NOT NULL,\n" + "  PRIMARY KEY (`username`),\n"
				+ "  UNIQUE KEY `username_UNIQUE` (`username`)\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
		try {
			if (stmt.execute(sql)) {
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	static boolean createTableUserAdditionalInfo() {
		String sql = "CREATE TABLE `user_additional_info` (\n" + "  `username` varchar(25) NOT NULL,\n"
				+ "  `avatar` blob,\n" + "  `bio` varchar(200) DEFAULT NULL,\n" + "  KEY `username_idx` (`username`),\n"
				+ "  CONSTRAINT `user_additional_info_username_fk` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
		try {
			if (stmt.execute(sql)) {
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	static boolean createTableUserLog() {
		String sql = "CREATE TABLE user_log" + "(username VARCHAR(25) NOT NULL," + "login_time datetime DEFAULT NULL,"
				+ "logout_time datetime DEFAULT NULL," + "allMessagesSent TINYINT DEFAULT 1,"
				+ "FOREIGN KEY (username) REFERENCES user(username))";
		try {
			if (stmt.execute(sql)) {
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	static boolean createTableFriends() {
		String sql = "CREATE TABLE `friends` (\n" + "  `username` varchar(25) NOT NULL,\n"
				+ "  `friend` int NOT NULL,\n" + "  KEY `friends_username_idx` (`username`),\n"
				+ "  CONSTRAINT `friends_username` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
		try {
			if (stmt.execute(sql)) {
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	static boolean createTableChatRoom() {
		String sql = "CREATE TABLE `chat_room` (\n" + "  `chat_room_id` int NOT NULL AUTO_INCREMENT,\n"
				+ "  `room_name` varchar(45) DEFAULT 'New_Room',\n" + "  `room_theme` blob,\n"
				+ "  PRIMARY KEY (`chat_room_id`),\n" + "  UNIQUE KEY `chat_room_id_UNIQUE` (`chat_room_id`)\n"
				+ ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
		try {
			if (stmt.execute(sql)) {
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	static boolean createTableChatRoomWarehouse() {
		String sql = "CREATE TABLE `chat_room_warehouse` (\n" + "  `chat_room_id` int NOT NULL,\n"
				+ "  `username` varchar(25) NOT NULL,\n"
				+ "  KEY `chat_room_warehouse_chat_room_id_fk_idx` (`chat_room_id`),\n"
				+ "  KEY `chat_room_warehouse_username_fk_idx` (`username`),\n"
				+ "  CONSTRAINT `chat_room_warehouse_chat_room_id_fk` FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room` (`chat_room_id`) ON DELETE CASCADE ON UPDATE CASCADE,\n"
				+ "  CONSTRAINT `chat_room_warehouse_username_fk` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE\n"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
		try {
			if (stmt.execute(sql)) {
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	static boolean createTableMessageData() {
		String sql = "CREATE TABLE `message_data` (\n" + "  `message_id` int NOT NULL AUTO_INCREMENT,\n"
				+ "  `username` varchar(25) NOT NULL,\n" + "  `chat_room_id` int NOT NULL,\n"
				+ "  `message_text` varchar(500) DEFAULT NULL,\n" + "  `message_image` blob,\n"
				+ "  `time_log` datetime DEFAULT NULL,\n" + "  `user_state` int DEFAULT NULL,\n"
				+ "  PRIMARY KEY (`message_id`),\n" + "  UNIQUE KEY `message_id_UNIQUE` (`message_id`),\n"
				+ "  KEY `message_data_username_fk_idx` (`username`),\n"
				+ "  KEY `message_data_chat_room_id_fk_idx` (`chat_room_id`),\n"
				+ "  CONSTRAINT `message_data_chat_room_id_fk` FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room` (`chat_room_id`) ON DELETE CASCADE ON UPDATE CASCADE,\n"
				+ "  CONSTRAINT `message_data_username_fk` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE\n"
				+ ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
		try {
			if (stmt.execute(sql)) {
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void addChatRoom(String room) {
		String sql = "INSERT INTO chat_room " + "(room_name) " + "VALUES(?)";

		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, room);
			prep.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void fillRoom(int room, String user) {
		String sql = "INSERT INTO chat_room_warehouse " + "VALUES(?,?)";

		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, room);
			prep.setString(2, user);
			prep.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean storeMessage(String user, String msg, int room) {
		String selectSQL = "SELECT username " + "FROM chat_room_warehouse " + "WHERE chat_room_id=? AND username=?";
		String inputUser = "";
		try {
			prep = conn.prepareStatement(selectSQL);
			prep.setInt(1, room);
			prep.setString(2, user);
			ResultSet rs = prep.executeQuery();

			while (rs.next()) {
				inputUser = rs.getString(1);

			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (inputUser.toLowerCase().equals(user)) {

			String sql = "INSERT INTO Message_data " + "(chat_room_id,username,message_text) " + "VALUES(?,?,?)";
			try {
				prep = conn.prepareStatement(sql);
				prep.setInt(1, room);
				prep.setString(2, inputUser);
				prep.setString(3, msg);
				prep.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public void alterTable() {
		String sql = "ALTER TABLE message_data " + "ADD file BLOB DEFAULT NULL";
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void dropTable() {
		String sql = "DROP TABLE User_Log";
		try {
			stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getRoomUsers(int i) {
		String sql = "SELECT Username " + "FROM chat_room_warehouse " + "WHERE chat_room_id=?";
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, i);
			rs = prep.executeQuery();
			while (rs.next()) {
				String user = rs.getString(1).toLowerCase();
				list.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				prep.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		String[] usersInRoom = (String[]) list.toArray(new String[0]);
		return usersInRoom;
	}

	public void insertUserLogLogin(String username) {
		String sql = "INSERT INTO user_log " + "(username,login_time) " + "VALUES(?,NOW())";
		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, username);
			prep.executeLargeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertUserLogout(String username) {
		String sql = "INSERT INTO user_log " + "(username,logout_time) " + "VALUES(?,NOW())";
		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, username);
			prep.executeLargeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void StoreFile(String message, String username, int roomID, FileInputStream file) {
		// TODO Auto-generated method stub

		String sql = "INSERT INTO message_data " + "(message,username) " + "VALUES(?,NOW())";
		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, message);
			prep.setString(2, username);
			prep.setInt(3, roomID);
			prep.setBinaryStream(4, file);
			prep.executeLargeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String[] getUnsendMessages(String username) {
		// TODO Auto-generated method stub

		ResultSet rs = null;
		Timestamp timestamp = getTimestamp(username);
		boolean isUnsend = false;

		String tiniInt = "SELECT allMessagesSent FROM User_log " + "WHERE logout_time = ? and allMessagesSent = ? ";

		try {
			prep = conn.prepareStatement(tiniInt);
			prep.setTimestamp(1, timestamp);
			prep.setInt(2, 0);
			rs = prep.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) == 0) {
					isUnsend = true;
				}
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (isUnsend) {
			String sql = "SELECT message_text,username,chat_room_id " + "FROM message_data "
					+ "WHERE username != ? and timeLOG > ?";
			ArrayList<String> messages = new ArrayList<String>();
			try {
				prep = conn.prepareStatement(sql);
				prep.setString(1, username);
				prep.setTimestamp(2, timestamp);
				rs = prep.executeQuery();
				while (rs.next()) {
					String message = rs.getString(1);
					String user = rs.getString(2);
					int room = rs.getInt(3);
					messages.add(message + "," + user + "," + room + "," + "TextMessage");
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] batch = messages.toArray(new String[0]);
			return batch;
		}

		return new String[0];

	}

	public void deleteEntry() {
		String sql = "DELETE FROM chat_room_warehouse " + "WHERE chat_room_id=1";
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Timestamp getTimestamp(String username) {
		String sql = "SELECT Logout_time " + "FROM User_Log " + "WHERE username = ?";
		Timestamp dateTimeStamp = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setString(1, username);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {

				Timestamp timestamp = rs.getTimestamp(1);

				if (timestamp != null) {
					dateTimeStamp = timestamp;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateTimeStamp;
	}

	public void alterUserLogoutState(String username) {
		Timestamp dateTimeStamp = getTimestamp(username);
		String update = "UPDATE User_Log " + "SET allMessagesSent = ? " + "WHERE Logout_time = ? and username = ?";

		try {
			prep = conn.prepareStatement(update);
			prep.setInt(1, 0);
			prep.setTimestamp(2, dateTimeStamp);
			prep.setString(3, username);
			prep.executeLargeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}