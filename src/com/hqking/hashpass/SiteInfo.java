package com.hqking.hashpass;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class SiteInfo extends JDialog implements ActionListener, ChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6226076328845503856L;
	private static final String CMD_DESC = "description";
	private static final String CMD_PATTERN = "pattern";
	private static final int INIT_BUMP = 0;
	private static final int INIT_LENGTH = 12;
	private static final String INIT_PATTERN = "Printable Ascii"; 
	
	private JLabel passwordLabel;
	private JSlider lengthSlider;
	private JSpinner bumpField;
	
	private String inputDescription;
	private int inputBump = INIT_BUMP;
	private int inputLength = INIT_LENGTH;
	private String inputPattern = INIT_PATTERN;
	
	public SiteInfo(Frame frame) {
		super(frame);
				
    	BorderLayout layout = new BorderLayout();
    	setLayout(layout);

    	add(addInputPane(), BorderLayout.PAGE_START);
    	
    	add(addPasswordPane(), BorderLayout.CENTER);
    	
    	add(addQualityPane(), BorderLayout.LINE_END);
    	
    	add(addButtonPane(), BorderLayout.PAGE_END);
    	
    	setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    	pack();
    	setVisible(true);
	}

	private JPanel addInputPane() {
		JTextField descField = new JTextField(30);
		descField.setActionCommand(CMD_DESC);
		descField.addActionListener(this);
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
		
		String[] patterns = {"Printable Ascii", "Alphanumeric", "Numbers only"};
		JComboBox<String> patternSel = new JComboBox<String>(patterns);
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
		JLabel entropy = new JLabel("Entropy: %d bits");
    	JLabel score = new JLabel("Score: %d");
    	JLabel comment = new JLabel("Strong");
    	
    	JPanel qualityPane = new JPanel();
    	qualityPane.setLayout(new BoxLayout(qualityPane, BoxLayout.PAGE_AXIS));
    	qualityPane.add(entropy);
    	qualityPane.add(score);
    	qualityPane.add(comment);
    	qualityPane.setBorder(
    			BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Quality 质量"),
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
    		
    	JPanel ctlPane = new JPanel();
    	ctlPane.setLayout(new FlowLayout());
    	ctlPane.add(new JButton("Prev"));
    	ctlPane.add(new JButton("Next"));
    	ctlPane.add(new JButton("Copy"));
    	ctlPane.add(new JButton("Save"));
    	ctlPane.add(new JButton("Quit"));
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
		if (inputDescription == null || inputDescription.length() == 0) {
			passwordLabel.setText("Please input description");
		} else {
			passwordLabel.setText(String.format("%s + %s + %d + %d", 
					inputDescription, inputPattern, inputLength, inputBump));
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if (cmd.equals(CMD_DESC)) {
			JTextField src = (JTextField)e.getSource();
			inputDescription = src.getText();
			showPassword();
		} else if (cmd.equals(CMD_PATTERN)) {
			JComboBox<String> src = (JComboBox<String>)e.getSource();
			inputPattern = (String)src.getSelectedItem();
			showPassword();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Object src = e.getSource();
		
		if (src.equals(lengthSlider)) {
			if (lengthSlider.getValueIsAdjusting()) {
				inputLength = lengthSlider.getValue();
				showPassword();
			}
		} else if (src.equals(bumpField)) {
			inputBump = (int)bumpField.getModel().getValue();
			showPassword();
		}
	}
}
