package com.hqking.hashpass;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

class SiteInfo extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6226076328845503856L;

	public SiteInfo(Frame frame) {
		super(frame);
		
		JTextField descField = new JTextField(30);
		JLabel descLabel = new JLabel("Site description: ");
		descLabel.setLabelFor(descField);
		
		JSlider lengthSlider = new JSlider(JSlider.HORIZONTAL, 4, 24, 12);
		lengthSlider.setMajorTickSpacing(4);
		lengthSlider.setMinorTickSpacing(1);
		lengthSlider.setPaintTicks(true);
		lengthSlider.setPaintLabels(true);
		JLabel lengthLabel = new JLabel("Length: ");
		lengthLabel.setLabelFor(lengthSlider);
		
		JTextField bumpField = new JTextField(5);
		bumpField.setEditable(false);
		JLabel bumpLabel = new JLabel("Retry: ");
		bumpLabel.setLabelFor(bumpField);
		
		JPanel inputPane = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		inputPane.setLayout(gridbag);
		
		String[] patterns = {"Printable Ascii", "Alphanumeric", "Numbers only"};
		JComboBox<String> patternSel = new JComboBox<String>(patterns);
		JLabel patternLabel = new JLabel("Patterns: ");
		
		JLabel[] labels = {descLabel, lengthLabel, patternLabel, bumpLabel};
		JComponent[] fields = {descField, lengthSlider, patternSel, bumpField};
		addLabelText(labels, fields, inputPane);
		
		inputPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Site infomation"),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
    	BorderLayout layout = new BorderLayout();
    	setLayout(layout);

    	add(inputPane, BorderLayout.PAGE_START);
    	
    	JLabel passwordLabel = new JLabel("calculated password");
    	passwordLabel.setBorder(
    			BorderFactory.createCompoundBorder(
    					BorderFactory.createTitledBorder("Password"),
    					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    	add(passwordLabel, BorderLayout.CENTER);
    	
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
    	add(qualityPane, BorderLayout.LINE_END);
    	
    	JButton btnPrev = new JButton("Prev");
    	JButton btnNext = new JButton("Next");
    	JButton btnSave = new JButton("Save");
    	JButton btnQuit = new JButton("Quit");
    	
    	JPanel ctlPane = new JPanel();
    	ctlPane.setLayout(new FlowLayout());
    	ctlPane.add(btnPrev);
    	ctlPane.add(btnNext);
    	ctlPane.add(btnSave);
    	ctlPane.add(btnQuit);
    	add(ctlPane, BorderLayout.PAGE_END);
    	
    	setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    	pack();
    	setVisible(true);
	}
	
	void addLabelText(JLabel[] labels, JComponent[] fields, Container container) {
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
}
