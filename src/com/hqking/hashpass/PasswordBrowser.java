package com.hqking.hashpass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;


public class PasswordBrowser extends JFrame implements Runnable {
	private class PasswordEntryMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1 &&
					e.getClickCount() == 2) {
				int row = list.rowAtPoint(e.getPoint());
				
				Site site = Hashpass.db.getSitebyRow(row); 
				
				new SiteInfo(frame, site);
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1977703648719134928L;

	private JFrame frame;
	
	public PasswordBrowser() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

	public PasswordBrowser(GraphicsConfiguration arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public PasswordBrowser(String arg0) throws HeadlessException {
		super(arg0);

		frame = this;
	}

	public PasswordBrowser(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	private static final String CMD_KEYSAVE = "OK";
	private static final String CMD_ADD = "ADD";
	
	class Test implements ActionListener {
		public void actionPerformed(ActionEvent e) {
	        String cmd = e.getActionCommand();

	        if (cmd.equals(CMD_KEYSAVE)) { //Process the password.
	            char[] input = pwdField.getPassword();
	            System.out.println(input);
	        } else if (cmd.equals(CMD_ADD)) {
	        	new SiteInfo(frame);
	        }
	    }	
	}

	private JPasswordField pwdField;
	
	private void createAndShowUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addMenuBar();
		
		addToolBar();		
		
		add(addSiteList(), BorderLayout.CENTER);
		
		add(addTagTree(), BorderLayout.LINE_START);
		
		pack();
		setVisible(true);
	}

	private JScrollPane addTagTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Title 1");
		DefaultMutableTreeNode t2 = new DefaultMutableTreeNode("Title 2");
		DefaultMutableTreeNode t3 = new DefaultMutableTreeNode("Title 3");
		DefaultMutableTreeNode t21 = new DefaultMutableTreeNode("Title 2.1");
		top.add(t2);
		top.add(t3);
		t2.add(t21);
		
		JTree tree = new JTree(top);
		
		JScrollPane treeScroller = new JScrollPane(tree);
		return treeScroller;
	}
	
	private JTable list;
	private JScrollPane addSiteList() {
		list = new JTable(Hashpass.db);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFillsViewportHeight(true);
		list.addMouseListener(new PasswordEntryMouseListener());
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		return listScroller;
	}
	
//	void update() {
//		entries.fireTableDataChanged();
//		entries.fireTableRowsInserted(0, 10);
//	}

	private void addToolBar() {
		Test tt = new Test();
		
		pwdField = new JPasswordField(20);
		pwdField.setOpaque(true);
		pwdField.setActionCommand(CMD_KEYSAVE);
		pwdField.addActionListener(tt);
		JLabel label = new JLabel("Master key: ");
		label.setLabelFor(pwdField);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setActionCommand(CMD_ADD);
		btnAdd.addActionListener(tt);
		
		JButton btnSave = new JButton("Save");
		btnSave.setActionCommand(CMD_KEYSAVE);
		btnSave.addActionListener(tt);
		
		JToolBar toolBar = new JToolBar("tools");
		toolBar.add(btnAdd);
		toolBar.addSeparator(new Dimension(40, 0));
		toolBar.add(label);
		toolBar.add(pwdField);
		toolBar.add(btnSave);
		getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}

	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setOpaque(true);
		menuBar.setBackground(new Color(154, 165, 127));
		menuBar.setPreferredSize(new Dimension(200, 20));
		setJMenuBar(menuBar);
	}

	@Override
	public void run() {
		createAndShowUI();
	}
	
	public static void start() {
		javax.swing.SwingUtilities.invokeLater(new PasswordBrowser("Password Brower"));
	}
}
