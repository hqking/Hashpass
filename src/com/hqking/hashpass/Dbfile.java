package com.hqking.hashpass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class Dbfile {
	private Connection connection;
	
	public Dbfile(String file) {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + "test.db");
			Statement stat = connection.createStatement();
			stat.setQueryTimeout(10);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Connection getConnection() {
		return connection;
	}
}
