package com.hqking.hashpass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
					"type text)";

			stat.executeUpdate(table);
			
			select = connection.prepareStatement("select " +
					"description, length, type, bump" +
					" from site where description like ?");
			
			insert = connection.prepareStatement("insert or replace into site " +
					"(description, bump, length, type)" +
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
			insert.setString(4, site.type);
			
			insert.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public List<Site> search(String pattern) {
		try {
			ArrayList<Site> sites = new ArrayList<Site>();
			
			select.setString(1, pattern);
			ResultSet rs = select.executeQuery();
			
			while (rs.next()) {
				Site site = new Site(rs.getString("type"), rs.getInt("length"));
				site.bump = rs.getInt("bump");
				site.description = rs.getString("description");
				
				sites.add(site);
			}
			
			return sites;
		} catch (SQLException e) {
			System.out.println("can't find " + pattern);
			
			return null;
		}
	}

	@Override
	public Site find(String desc) {
		Site site = null;
		
		try {
			select.setString(1, desc);
			
			ResultSet rs = select.executeQuery();

			int length = rs.getInt("length");
			String type = rs.getString("type");
				
			site = new Site(type, length);
				
			site.bump = rs.getInt("bump");
			site.description = rs.getString("description");
				
						
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("cant find " + desc);
		}
		
		return site;
	}

}
