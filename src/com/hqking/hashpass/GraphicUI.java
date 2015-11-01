package com.hqking.hashpass;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;


public class GraphicUI extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1977703648719134928L;

	private JFrame frame;
	private SiteInfo dialog;
	
	public GraphicUI() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

	public GraphicUI(GraphicsConfiguration arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public GraphicUI(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
		
		frame = this;
		dialog = new SiteInfo(frame);
	}

	public GraphicUI(String arg0, GraphicsConfiguration arg1) {
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
	        	dialog.setVisible(true);
	        }
	    }	
	}

	private JPasswordField pwdField;
	
	private void createAndShowUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setOpaque(true);
		menuBar.setBackground(new Color(154, 165, 127));
		menuBar.setPreferredSize(new Dimension(200, 20));
		setJMenuBar(menuBar);
		
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
		
		String test[] = {"jin yifeng", "hqking", "xiaodu", "papa", "qinqin"};
		JList<String> list = new JList<String>(test);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		getContentPane().add(listScroller, BorderLayout.CENTER);
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Title 1");
		DefaultMutableTreeNode t2 = new DefaultMutableTreeNode("Title 2");
		DefaultMutableTreeNode t3 = new DefaultMutableTreeNode("Title 3");
		DefaultMutableTreeNode t21 = new DefaultMutableTreeNode("Title 2.1");
		top.add(t2);
		top.add(t3);
		t2.add(t21);
		
		JTree tree = new JTree(top);
		
		JScrollPane treeScroller = new JScrollPane(tree);
		
		getContentPane().add(treeScroller, BorderLayout.LINE_START);
		
		pack();
		setVisible(true);
	}

	@Override
	public void run() {
		createAndShowUI();
	}
	
	public static void start() {
		javax.swing.SwingUtilities.invokeLater(new GraphicUI("Password Brower"));
	}
}
