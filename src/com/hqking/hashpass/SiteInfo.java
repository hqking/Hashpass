package com.hqking.hashpass;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class SiteInfo extends JDialog 
implements 
ActionListener, 
ChangeListener, 
DocumentListener, 
ClipboardOwner,
WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6226076328845503856L;
	private static final String CMD_DESC = "description_command";
	private static final String CMD_PATTERN = "pattern_command";
	private static final String CMD_QUIT = "quite_command";
	private static final String CMD_SAVE = "save_command";
	private static final String CMD_COPY = "copy_command";
	private static final String CMD_PREV = "prev_command";
	private static final String CMD_NEXT = "next_command";
	private static final int INIT_BUMP = 0;
	private static final int INIT_LENGTH = 12;
	private static final String INIT_PATTERN = Generator.TABLE_PRINTALBE_ASCII; 
	
	private JLabel passwordLabel;
	private JSlider lengthSlider;
	private JSpinner bumpField;
	private JComboBox<String> patternSel;
	private JTextField descField;
	private JLabel entropyLabel;
	private JLabel scoreLabel;
	private JLabel commentLabel;
	private JButton saveButton;
	
	private Site site;
	private Site originalSite;
	private boolean inClipboard = false;
	
	public SiteInfo(Frame frame, Site site) {
		super(frame);

		this.site = site;
		originalSite = site.copy();
		
    	BorderLayout layout = new BorderLayout();
    	setLayout(layout);

    	add(addInputPane(), BorderLayout.PAGE_START);
    	
    	add(addQualityPane(), BorderLayout.LINE_END);

    	add(addButtonPane(), BorderLayout.PAGE_END);
    	
    	add(addPasswordPane(), BorderLayout.CENTER);
    	
    	pack();
    	setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    	addWindowListener(this);
    	setLocationRelativeTo(frame);
		setVisible(true);
	}
	
	public SiteInfo(Frame frame) {
		this(frame, new Site(INIT_PATTERN, INIT_LENGTH, INIT_BUMP));
	}

	private JPanel addInputPane() {
		descField = new JTextField(site.description, 30);
		descField.setActionCommand(CMD_DESC);
		descField.addActionListener(this);
		descField.getDocument().addDocumentListener(this);
		JLabel descLabel = new JLabel("Site description: ");
		descLabel.setLabelFor(descField);
		
		lengthSlider = new JSlider(JSlider.HORIZONTAL, 4, 24, site.length);
		lengthSlider.setMajorTickSpacing(4);
		lengthSlider.setMinorTickSpacing(1);
		lengthSlider.setPaintTicks(true);
		lengthSlider.setPaintLabels(true);
		lengthSlider.addChangeListener(this);
		JLabel lengthLabel = new JLabel("Length: ");
		lengthLabel.setLabelFor(lengthSlider);
		
		bumpField = new JSpinner(new SpinnerNumberModel(site.bump, 0, 65535, 1));
		bumpField.addChangeListener(this);
		JLabel bumpLabel = new JLabel("Retry: ");
		bumpLabel.setLabelFor(bumpField);
		
		JPanel inputPane = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		inputPane.setLayout(gridbag);
		
		String[] patterns = {
				Generator.TABLE_PRINTALBE_ASCII, 
				Generator.TABLE_ALPHA_NUMERIC,
				Generator.TABLE_NUMBERS_ONLY};
		patternSel = new JComboBox<String>(patterns);
		patternSel.setSelectedItem(site.type);
		patternSel.setActionCommand(CMD_PATTERN);
		patternSel.addActionListener(this);
		JLabel patternLabel = new JLabel("Patterns: ");
		
		JLabel[] labels = {descLabel, lengthLabel, patternLabel, bumpLabel};
		JComponent[] fields = {descField, lengthSlider, patternSel, bumpField};
		addLabelText(labels, fields, inputPane);
		
		inputPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Site infomation"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		return inputPane;
	}

	private JPanel addQualityPane() {
		entropyLabel = new JLabel("Entropy: %d bits");
    	scoreLabel = new JLabel("Score: %d");
    	commentLabel = new JLabel("Strong");
    	
    	JPanel qualityPane = new JPanel();
    	qualityPane.setLayout(new BoxLayout(qualityPane, BoxLayout.PAGE_AXIS));
    	qualityPane.add(entropyLabel);
    	qualityPane.add(scoreLabel);
    	qualityPane.add(commentLabel);
    	qualityPane.setBorder(
    			BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Quality"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		return qualityPane;
	}

	private JLabel addPasswordPane() {
		passwordLabel = new JLabel("calculated password");
		showPassword();
    	passwordLabel.setBorder(
    			BorderFactory.createCompoundBorder(
    					BorderFactory.createTitledBorder("Password"),
    					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		return passwordLabel;
	}

	private JPanel addButtonPane() {
    	JButton btn;
		
    	JPanel ctlPane = new JPanel();
    	ctlPane.setLayout(new FlowLayout());

    	btn = new JButton("Prev");
    	btn.setActionCommand(CMD_PREV);
    	btn.addActionListener(this);
    	btn.setMnemonic(KeyEvent.VK_P);
    	ctlPane.add(btn);
    	
    	btn = new JButton("Next");
    	btn.setActionCommand(CMD_NEXT);
    	btn.addActionListener(this);
    	btn.setMnemonic(KeyEvent.VK_N);
    	ctlPane.add(btn);
    	
    	btn = new JButton("Copy");
    	btn.setActionCommand(CMD_COPY);
    	btn.addActionListener(this);
    	btn.setMnemonic(KeyEvent.VK_C);
    	ctlPane.add(btn);
    	
    	saveButton = new JButton("Save");
    	saveButton.setActionCommand(CMD_SAVE);
    	saveButton.addActionListener(this);
    	saveButton.setMnemonic(KeyEvent.VK_S);
    	saveButton.setEnabled(false);
    	ctlPane.add(saveButton);
    	
    	btn = new JButton("Quit");
    	btn.setActionCommand(CMD_QUIT);
    	btn.addActionListener(this);
    	btn.setMnemonic(KeyEvent.VK_Q);
    	ctlPane.add(btn);
    	
    	InputMap im = ctlPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    	ActionMap am = ctlPane.getActionMap();

    	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), CMD_QUIT);
    	am.put(CMD_QUIT, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				quitAction();
			}
    	});
    	    	
		return ctlPane;
	}
	
	private void addLabelText(JLabel[] labels, JComponent[] fields, Container container) {
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		
		int length = labels.length;
		
		for (int i = 0; i < length; i++) {
			c.gridwidth = GridBagConstraints.RELATIVE;
			c.fill = GridBagConstraints.NONE;
			c.weightx = 0.0;
			container.add(labels[i], c);
			
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			container.add(fields[i], c);
		}
	}

	private void showPassword() {
		if (site.description == null || site.description.length() == 0) {
			passwordLabel.setText("Please input description");
			entropyLabel.setText("0 bit");
			scoreLabel.setText("0");
			commentLabel.setText("no password");
		} else {
			String pwd = Generator.password(site);
			passwordLabel.setText(pwd);
			
			entropyLabel.setText(String.format("entropy: %d bits", Generator.entropy(pwd, site.type)));
			
			Validator validator = new Validator(pwd);
			int score = validator.score();
			scoreLabel.setText(String.format("score: %d", score));
			if (score <= 20) {
				commentLabel.setText("very week");
			} else if (score <= 40) {
				commentLabel.setText("week");
			} else if (score <= 60) {
				commentLabel.setText("normal");
			} else if (score <= 80) {
				commentLabel.setText("strong");
			} else {
				commentLabel.setText("very strong");
			}
			
			if (site.compareTo(originalSite) != 0) {
				saveButton.setEnabled(true);
			} else {
				saveButton.setEnabled(false);
			}
		}
	}
	
	private void quitAction() {
		if (inClipboard) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(new StringSelection(""), null);
			inClipboard = false;
		}
		
		if (site.compareTo(originalSite) != 0) {
			String[] options = {"yes", "no", "cancel"};
			int n = JOptionPane.showOptionDialog(this, 
					"unsaved change, would you like to save before quit?", "unsaved site", 
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
					null, options, options[2]);
			switch (n) {
			case JOptionPane.YES_OPTION:
				Hashpass.save(site);
			case JOptionPane.NO_OPTION:
				dispose();
				break;
			case JOptionPane.CANCEL_OPTION:
			case JOptionPane.CLOSED_OPTION:
			default:
				;
			}
		} else {
			dispose();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if (cmd.equals(CMD_DESC)) {
			site.description = descField.getText();
			showPassword();
			
		} else if (cmd.equals(CMD_PATTERN)) {
			site.type = (String)patternSel.getSelectedItem();
			showPassword();
			
		} else if (cmd.equals(CMD_QUIT)) {
			quitAction();
			
		} else if (cmd.equals(CMD_SAVE)) {
			saveButton.setEnabled(false);
			originalSite = site.copy();
			Hashpass.save(site);
			
		} else if (cmd.equals(CMD_COPY)) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection selection = new StringSelection(passwordLabel.getText());
			clipboard.setContents(selection, selection);
			inClipboard = true;
			
		} else if (cmd.equals(CMD_PREV)) {
			
		} else if (cmd.equals(CMD_NEXT)) {
			
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Object src = e.getSource();
		
		if (src.equals(lengthSlider)) {
			if (lengthSlider.getValueIsAdjusting()) {
				site.length = lengthSlider.getValue();
				showPassword();
			}
		} else if (src.equals(bumpField)) {
			site.bump = (int)bumpField.getModel().getValue();
			showPassword();
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		site.description = descField.getText();
		showPassword();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		site.description = descField.getText();
		showPassword();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		site.description = descField.getText();
		showPassword();
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		inClipboard = false;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		quitAction();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
