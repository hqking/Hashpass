package com.hqking.hashpass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

class StorageSqlite extends AbstractTableModel
					implements Storage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
			
			if (rowNumber < PAGE_SIZE) {
				cells[rowNumber] = new String[columnName.length];
				
				cells[rowNumber][0] = site.description;
				cells[rowNumber][1] = String.format("%d", site.length);
				cells[rowNumber][2] = site.type;
				cells[rowNumber][3] = String.format("%d", site.bump);
		
				rowNumber++;

				fireTableRowsInserted(rowNumber - 1, rowNumber - 1);
				
			}
			
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
			
			rs.close();
			
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

	private final String[] columnName = {
			"Description", "Length", "Type", "Bump"
	};

	private int rowNumber;
	
	private String[][] cells;
	
	@Override
	public int getColumnCount() {
		return columnName.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnName[columnIndex];
	}

	@Override
	public int getRowCount() {
		return rowNumber;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return cells[arg0][arg1];
	}

	private static final int PAGE_SIZE = 50;
	
	public void sync() {
		try {
			select.setString(1, "%");
			ResultSet rs = select.executeQuery();
					
			cells = new String[PAGE_SIZE][];
			
			rowNumber = 0;
			while (rs.next()) {
				cells[rowNumber] = new String[columnName.length];
				
				cells[rowNumber][0] = rs.getString("description");
				cells[rowNumber][1] = String.format("%d", rs.getInt("length"));
				cells[rowNumber][2] = rs.getString("type");
				cells[rowNumber][3] = String.format("%d", rs.getInt("bump"));
				
				rowNumber++;
				if (rowNumber >= PAGE_SIZE)
					break;
			}
			
			rs.close();
		} catch (SQLException e) {
			System.out.println("sync failed" + e);
			
			rowNumber = 0;
		}
	}
}
