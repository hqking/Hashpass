package com.hqking.hashpass;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;

public class BrowserView extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JPasswordField masterKeyField;
	private JTextField searchField;

	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BrowserView frame = new BrowserView();
					frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BrowserView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewSite = new JMenuItem("New");
		mntmNewSite.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNewSite);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmImport = new JMenuItem("Import");
		mnFile.add(mntmImport);
		
		JMenuItem mntmExport = new JMenuItem("Export");
		mnFile.add(mntmExport);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFile.add(mntmExit);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmPreferences = new JMenuItem("Preferences");
		mnTools.add(mntmPreferences);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.3);
		contentPane.add(splitPane);
		
		JScrollPane scrollPaneTags = new JScrollPane();
		scrollPaneTags.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitPane.setLeftComponent(scrollPaneTags);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"test", "mama", "finance", "research", "personal", "shopping"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPaneTags.setViewportView(list);
		
		JScrollPane scrollPaneSites = new JScrollPane();
		splitPane.setRightComponent(scrollPaneSites);
		
		table = new JTable(Hashpass.db);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneSites.setViewportView(table);
		
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton btnNewSite = new JButton("New");
		toolBar.add(btnNewSite);
		
		JButton btnDel = new JButton("Del");
		toolBar.add(btnDel);
		
		JLabel lblSearch = new JLabel("Search:");
		toolBar.add(lblSearch);
		
		searchField = new JTextField();
		searchField.setText("search");
		toolBar.add(searchField);
		searchField.setColumns(20);
		
		JSeparator separator_2 = new JSeparator();
		toolBar.add(separator_2);
		
		JLabel lblMasterKey = new JLabel("MasterKey:");
		toolBar.add(lblMasterKey);
		
		masterKeyField = new JPasswordField();
		lblMasterKey.setLabelFor(masterKeyField);
		masterKeyField.setHorizontalAlignment(SwingConstants.LEFT);
		masterKeyField.setColumns(24);
		toolBar.add(masterKeyField);
	}
}
