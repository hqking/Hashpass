package com.hqking.hashpass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.AbstractListModel;

class TagsModel extends AbstractListModel<String> implements Tags {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private PreparedStatement tagSelect;
	private PreparedStatement tagInsert;
	private PreparedStatement tagDelete;
	private PreparedStatement tagCount;
	
	private String[] list;
	private int count;
	
	public TagsModel(String dbFile) {
		try {
			connection = Hashpass.getConnection();

			tagSelect = connection.prepareStatement("select name from tag;");
			tagInsert = connection.prepareStatement("insert into tag (name) values (?);");
			tagDelete = connection.prepareStatement("delete from tag where name == ?;");
			tagCount = connection.prepareStatement("select count(name) from tag;");

			sync();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sync() {
		ResultSet rs;

		try {
			rs = tagCount.executeQuery();
			count = rs.getInt(1);
			rs.close();
			
			list = new String[count];
			rs = tagSelect.executeQuery();
			
			for (int i = 0; i < count; i++) {
				if (rs.next()) {
					list[i] = rs.getString("name");
				}
			}

			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			count = 0;
		}
	}
	
	@Override
	public String getElementAt(int arg0) {
		return list[arg0];
	}

	@Override
	public int getSize() {
		return count;
	}

	@Override
	public void addTag(String name) {
		try {
			tagInsert.setString(1, name);
			
			tagInsert.executeUpdate();
			
			sync();
			
			fireIntervalAdded(this, 0, count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteTag(String name) {
		try {
			tagDelete.setString(1, name);
			
			tagDelete.executeUpdate();
			
			sync();
			
			fireIntervalRemoved(this, 0, count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] listTags() {
		return list;
	}
}
