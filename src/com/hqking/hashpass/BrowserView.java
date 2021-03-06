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
import javax.swing.JOptionPane;
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
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.JPopupMenu;

public class BrowserView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private JPasswordField masterKeyField;
	private JTextField searchField;
	private JLabel lblCheck;
	private JLabel lblTotal;
	private JLabel lblMatch;

	private JList<String> tagList;
	private String tagSelected;
	
	private static final String NO_MASTER_KEY = "no master key"; 
	
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
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				Hashpass.exit();
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewSite = new JMenuItem("New");
		mntmNewSite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createSite();
			}
		});
		mntmNewSite.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNewSite);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmImport = new JMenuItem("Import");
		mntmImport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
		mnFile.add(mntmImport);
		
		JMenuItem mntmExport = new JMenuItem("Export");
		mntmExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mnFile.add(mntmExport);
		
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Hashpass.exit();
			}
		});
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFile.add(mntmExit);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenuItem mntmPreferences = new JMenuItem("Preferences");
		mnTools.add(mntmPreferences);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AboutView.display();
			}
		});
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
		
		tagList = new JList<String>();
		tagList.setModel(Hashpass.tagdb);
		scrollPaneTags.setViewportView(tagList);
		
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(tagList, popupMenu);
		
		JMenuItem mntmDeleteTag = new JMenuItem("Delete");
		mntmDeleteTag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Hashpass.deleteTag(tagSelected);
			}
		});
		popupMenu.add(mntmDeleteTag);
		
		JScrollPane scrollPaneSites = new JScrollPane();
		splitPane.setRightComponent(scrollPaneSites);
		
		table = new JTable(Hashpass.db);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 &&
						e.getClickCount() == 2) {
					int row = table.rowAtPoint(e.getPoint());
					
					if (row != -1) {
						Site site = Hashpass.db.getSitebyRow(row); 
					
						new SiteController(site);
					}
				}
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneSites.setViewportView(table);
		
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JButton btnNewSite = new JButton("New");
		btnNewSite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createSite();
			}
		});
		toolBar.add(btnNewSite);
		
		JButton btnDel = new JButton("Del");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
	        	if (row != -1)
	        		Hashpass.delete(Hashpass.db.getSitebyRow(row));
			}
		});
		toolBar.add(btnDel);
		
		JButton btnAddtag = new JButton("Add");
		toolBar.add(btnAddtag);
		btnAddtag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tag = JOptionPane.showInputDialog(null, "new tag", "Add tag", JOptionPane.PLAIN_MESSAGE);
				Hashpass.addTag(tag);
			}
		});
		
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
		masterKeyField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				char[] input = masterKeyField.getPassword();
	            Generator.setKey(new String(input));
	            
	            // for security reason, clear bytes to zero
	            for (int i = 0; i < input.length; i++) {
	            	input[i] = 0;
	            }
	            
	            if (Generator.isKeySet()) {
	            	Site test = new Site(Generator.TABLE_PRINTALBE_ASCII);
	            	test.description = "test";
	            	lblCheck.setText(Generator.password(test));
	            } else {
	            	lblCheck.setText(NO_MASTER_KEY);
	            }
			}
		});
		lblMasterKey.setLabelFor(masterKeyField);
		masterKeyField.setHorizontalAlignment(SwingConstants.LEFT);
		masterKeyField.setColumns(24);
		toolBar.add(masterKeyField);
		
		JPanel statusBar = new JPanel();
		statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		contentPane.add(statusBar, BorderLayout.SOUTH);
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
		
		lblCheck = new JLabel(NO_MASTER_KEY);
		lblCheck.setHorizontalAlignment(SwingConstants.LEFT);
		statusBar.add(lblCheck);
		
		JSeparator separator_3 = new JSeparator();
		statusBar.add(separator_3);
		
		lblTotal = new JLabel("Total 100 sites");
		statusBar.add(lblTotal);
		
		JSeparator separator_4 = new JSeparator();
		statusBar.add(separator_4);
		
		lblMatch = new JLabel("Match 50 sites");
		statusBar.add(lblMatch);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{searchField, masterKeyField}));
	}
	public JTable getTable() {
		return table;
	}
	protected JLabel getLblCheck() {
		return lblCheck;
	}
	
	private void createSite() {		
		if (Generator.isKeySet())
			new SiteController(new Site(Generator.TABLE_PRINTALBE_ASCII));
		else
			JOptionPane.showMessageDialog(this, "no master key has been set", "error", JOptionPane.ERROR_MESSAGE);
	}
	public JLabel getLblTotal() {
		return lblTotal;
	}
	public JLabel getLblMatch() {
		return lblMatch;
	}
	private void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				int index = tagList.locationToIndex(e.getPoint());
				tagList.setSelectedIndex(index);
				tagSelected = tagList.getSelectedValue();
				
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
