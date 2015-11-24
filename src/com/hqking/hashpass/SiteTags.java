package com.hqking.hashpass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.AbstractListModel;

class SiteTags extends AbstractListModel<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int MAX_TAG_ALLOWED = 5;
	
	private Connection connection;
	private PreparedStatement tagSelect;
	private PreparedStatement tagInsert;
	private PreparedStatement tagDelete;
	private PreparedStatement tagCount;
	
	private Site site;
	private String[] tags = new String[MAX_TAG_ALLOWED];
	private int count;
	
	public SiteTags(Site site) {
		this.site = site;
		
		try {
			connection = Hashpass.getConnection();

			tagSelect = connection.prepareStatement("select tag from siteTags where site == ?;");
			tagInsert = connection.prepareStatement("insert into siteTags (site, tag) values (?, ?);");
			tagDelete = connection.prepareStatement("delete from siteTags where site = ? and tag = ?;");

			tagSelect.setString(1, site.description);

			ResultSet rs = tagSelect.executeQuery();
			
			count = 0;
			while (rs.next()) {
				tags[count] = rs.getString("tag");
				count++;
			}
				
			rs.close();

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveTags() {
		
	}
	
	public void addTag(String tag) {
		if (count < MAX_TAG_ALLOWED) {
			int i;
			
			for (i = 0; i < count; i++) {
				if (tags[i].equals(tag))
					break;
			}
			
			if (i == count) {
				tags[count] = tag;
				count++;
			
				fireIntervalAdded(this, 0, count);
				
				try {
					tagInsert.setString(1, site.description);
					tagInsert.setString(2, tag);
					tagInsert.execute();

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void deleteTag(String tag) {
		int i;
		for (i = 0; i < count; i++) {
			if (tags[i].equals(tag)) {
				break;
			}
		}

		if (i < count) {
			try {
				tagDelete.setString(1, site.description);
				tagDelete.setString(2, tag);
				tagDelete.execute();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		for (int j = i + 1; j < count; j++, i++) {
			tags[i] = tags[j];
		}
		
		count = i;
		
		fireIntervalRemoved(this, 0, count);
	}
	
	@Override
	public String getElementAt(int arg0) {
		return tags[arg0];
	}

	@Override
	public int getSize() {
		return count;
	}

}
