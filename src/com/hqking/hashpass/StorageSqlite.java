package com.hqking.hashpass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

class StorageSqlite implements Storage {
	private Connection connection = null;
	private PreparedStatement select = null;
	private PreparedStatement insert = null;
	
	StorageSqlite(String file) {	
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + file);
			Statement stat = connection.createStatement();
			stat.setQueryTimeout(10);
			
			String table = "create table if not exists site (" +
					"description text primary key," +
					"bump integer," +
					"length integer," +
					"generator text)";

			stat.executeUpdate(table);
			
			select = connection.prepareStatement("select * from site where description = ?");
			
			insert = connection.prepareStatement("insert or replace into site " +
					"(description, bump, length, generator)" +
					"values (?, ?, ?, ?);");
			
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public int save(Site site) {
		try {
			insert.setString(1, site.description);
			insert.setInt(2, site.bump);
			insert.setInt(3, site.length);
			insert.setString(4, "test");
			
			insert.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public Set<Site> search(String pattern) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Site find(String desc) {
		Site site = null;
		
		try {
			select.setString(1, desc);
			
			ResultSet rs = select.executeQuery();

			int length = rs.getInt("length");
			String type = rs.getString("generator");
				
			site = new Site(type, length);
				
			site.bump = rs.getInt("bump");
			site.description = rs.getString("description");
				
						
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return site;
	}

}
