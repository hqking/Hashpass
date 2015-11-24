package com.hqking.hashpass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	private PreparedStatement delete = null;
	
	StorageSqlite(String file) {	
		try {
			connection = Hashpass.getConnection();
						
			select = connection.prepareStatement("select " +
					"description, length, type, bump" +
					" from site where description like ?");
			
			insert = connection.prepareStatement("insert or replace into site " +
					"(description, bump, length, type)" +
					"values (?, ?, ?, ?);");
			
			delete = connection.prepareStatement("delete from site "
					+ "where description = ?");
			
			cells = new String[PAGE_SIZE][];
			for (int i = 0; i < PAGE_SIZE; i++) {
				cells[i] = new String[columnName.length];
			}
			
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
			
			sync();
			fireTableDataChanged();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	@Override
	public int delete(Site site) {
		try {
			delete.setString(1, site.description);
			
			delete.executeUpdate();
			
			sync();
			fireTableDataChanged();
			
		} catch (SQLException e) {
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
	
	public Site getSitebyRow(int row) {
		if (row < rowNumber)
			return find(cells[row][0]);
		else
			return null;
	}
	
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
					
			rowNumber = 0;
			while (rs.next()) {
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
