package com.hqking.hashpass;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
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

class SiteInfo extends JDialog implements ActionListener, ChangeListener, DocumentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6226076328845503856L;
	private static final String CMD_DESC = "description_command";
	private static final String CMD_PATTERN = "pattern_command";
	private static final String CMD_QUITE = "quite_command";
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
	
	public SiteInfo(Frame frame, Site site) {
		super(frame);

		this.site = site;
		
    	BorderLayout layout = new BorderLayout();
    	setLayout(layout);

    	add(addInputPane(), BorderLayout.PAGE_START);
    	
    	add(addQualityPane(), BorderLayout.LINE_END);

    	JPanel panel = addButtonPane();
    	add(panel, BorderLayout.PAGE_END);
    	
    	add(addPasswordPane(), BorderLayout.CENTER);
    	
    	
    	
    	pack();
    	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
		
		lengthSlider = new JSlider(JSlider.HORIZONTAL, 4, 24, INIT_LENGTH);
		lengthSlider.setMajorTickSpacing(4);
		lengthSlider.setMinorTickSpacing(1);
		lengthSlider.setPaintTicks(true);
		lengthSlider.setPaintLabels(true);
		lengthSlider.addChangeListener(this);
		JLabel lengthLabel = new JLabel("Length: ");
		lengthLabel.setLabelFor(lengthSlider);
		
		bumpField = new JSpinner(new SpinnerNumberModel(INIT_BUMP, 0, 65535, 1));
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
    	btn.setActionCommand(CMD_QUITE);
    	btn.addActionListener(this);
    	btn.setMnemonic(KeyEvent.VK_Q);
    	ctlPane.add(btn);
    	
    	InputMap im = ctlPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    	ActionMap am = ctlPane.getActionMap();

    	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), CMD_QUITE);
    	am.put(CMD_QUITE, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
    	});
    	
    	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), CMD_SAVE);
    	am.put(CMD_SAVE, new AbstractAction() {
    		private static final long serialVersionUID = 1L;
    		@Override
    		public void actionPerformed(ActionEvent arg0) {
    			saveButton.setEnabled(false);
    			Hashpass.save(site);
    		}
    	});
    	
    	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), CMD_COPY);
    	am.put(CMD_COPY, new AbstractAction() {
    		private static final long serialVersionUID = 1L;
    		@Override
    		public void actionPerformed(ActionEvent arg0) {
    			
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
			
			saveButton.setEnabled(true);
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
			
		} else if (cmd.equals(CMD_QUITE)) {
			dispose();
			
		} else if (cmd.equals(CMD_SAVE)) {
			saveButton.setEnabled(false);
			Hashpass.save(site);
			
		} else if (cmd.equals(CMD_COPY)) {
			
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
}
