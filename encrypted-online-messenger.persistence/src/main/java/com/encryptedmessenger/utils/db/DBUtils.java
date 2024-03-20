package com.encryptedmessenger.utils.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils {
//	private static Path p;
//	private static BufferedReader s;
//	private static Properties prop;
//	static {
//        try{
//        	p = Path.of("db.properties");
//    		s = Files.newBufferedReader(p);
//    		prop = new Properties();
//    		prop.load(s);
//        } catch (IOException e){
//        	throw new RuntimeException(e);
//        }
//    }
	
	private static final String JDBC_MYSQL_HOST = "jdbc:mysql://messenger.crqyyeoi8nvx.us-east-1.rds.amazonaws.com/";
	private static final String DB_NAME = "encrypted_messenger";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "asdasdasd";
	
	private DBUtils() {
	}
	
	public static Connection getConnection() {
//		try {
//			return DriverManager.getConnection("jdbc:sqlite:test.sqlite");			
//		}
//		catch(SQLException e) {
//			throw new RuntimeException(e);
//		}
		
		// MySQL
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(JDBC_MYSQL_HOST + DB_NAME, USERNAME, PASSWORD);
		} catch (SQLException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}

