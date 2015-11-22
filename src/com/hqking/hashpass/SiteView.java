package com.hqking.hashpass;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.KeyEvent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class SiteView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textDescription;
	private JLabel lblPassword;
	private JLabel lblEntropyData;
	private JLabel lblScoreData;
	private JLabel lblCmtData;
	private JSlider sliderLength;
	private JComboBox<String> comboPatternSelector;
	private JSpinner spinner;
	private SiteController controller;
	private JToggleButton tglbtnShow;
	private JButton btnCopy;
	private JButton btnSave;
	private JButton btnQuit;
	private JList list;
	private JComboBox<String> comboTags;
	private JScrollPane scrollPane;
	private JButton btnAddTag;
	private JLabel lblBumpDesc;
	
	/**
	 * Create the panel.
	 */
	public SiteView(SiteController ctl) {
		controller = ctl;
		
    	InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    	ActionMap am = getActionMap();

    	final String CMD_QUIT = "COMMAND_QUIT"; 
    	im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), CMD_QUIT);
    	am.put(CMD_QUIT, new AbstractAction() {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.btnQuitPressed();
			}
    	});

		setPreferredSize(new Dimension(500, 300));
		
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelParams = new JPanel();
		add(panelParams, BorderLayout.NORTH);
		panelParams.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Parameters", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)), new EmptyBorder(5, 5, 5, 5)));
		GridBagLayout gbl_panelParams = new GridBagLayout();
		gbl_panelParams.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panelParams.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelParams.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panelParams.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelParams.setLayout(gbl_panelParams);
		
		JLabel lblDescription = new JLabel("Description:");
		GridBagConstraints gbc_lblDescription = new GridBagConstraints();
		gbc_lblDescription.anchor = GridBagConstraints.EAST;
		gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescription.gridx = 0;
		gbc_lblDescription.gridy = 0;
		panelParams.add(lblDescription, gbc_lblDescription);
		
		textDescription = new JTextField();
		textDescription.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.descriptionChanged();
			}
		});
		textDescription.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				controller.descriptionChanged();
			}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				controller.descriptionChanged();
			}
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				controller.descriptionChanged();
			}
		});
		lblDescription.setLabelFor(textDescription);
		GridBagConstraints gbc_textDescription = new GridBagConstraints();
		gbc_textDescription.gridwidth = 3;
		gbc_textDescription.insets = new Insets(0, 0, 5, 0);
		gbc_textDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_textDescription.gridx = 1;
		gbc_textDescription.gridy = 0;
		panelParams.add(textDescription, gbc_textDescription);
		textDescription.setColumns(10);
		
		JLabel lblLength = new JLabel("Length:");
		GridBagConstraints gbc_lblLength = new GridBagConstraints();
		gbc_lblLength.anchor = GridBagConstraints.EAST;
		gbc_lblLength.insets = new Insets(0, 0, 5, 5);
		gbc_lblLength.gridx = 0;
		gbc_lblLength.gridy = 1;
		panelParams.add(lblLength, gbc_lblLength);
		
		sliderLength = new JSlider(JSlider.HORIZONTAL, 4, 24, 12);
		sliderLength.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				controller.lengthChanged();
			}
		});
		sliderLength.setMajorTickSpacing(4);
		sliderLength.setSnapToTicks(true);
		sliderLength.setPaintTicks(true);
		sliderLength.setPaintLabels(true);
		sliderLength.setMinorTickSpacing(1);
		GridBagConstraints gbc_sliderLength = new GridBagConstraints();
		gbc_sliderLength.gridwidth = 3;
		gbc_sliderLength.fill = GridBagConstraints.HORIZONTAL;
		gbc_sliderLength.anchor = GridBagConstraints.EAST;
		gbc_sliderLength.insets = new Insets(0, 0, 5, 0);
		gbc_sliderLength.gridx = 1;
		gbc_sliderLength.gridy = 1;
		panelParams.add(sliderLength, gbc_sliderLength);
		
		JLabel lblPattern = new JLabel("Pattern:");
		GridBagConstraints gbc_lblPattern = new GridBagConstraints();
		gbc_lblPattern.anchor = GridBagConstraints.EAST;
		gbc_lblPattern.insets = new Insets(0, 0, 5, 5);
		gbc_lblPattern.gridx = 0;
		gbc_lblPattern.gridy = 2;
		panelParams.add(lblPattern, gbc_lblPattern);
		panelParams.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{textDescription, sliderLength, comboPatternSelector, spinner}));
		
		comboPatternSelector = new JComboBox<String>();
		comboPatternSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.patternChanged();
			}
		});
		comboPatternSelector.setModel(new DefaultComboBoxModel<String>(new String[] {Generator.TABLE_PRINTALBE_ASCII, Generator.TABLE_ALPHA_NUMERIC, Generator.TABLE_NUMBERS_ONLY}));
		GridBagConstraints gbc_comboPatternSelector = new GridBagConstraints();
		gbc_comboPatternSelector.gridwidth = 3;
		gbc_comboPatternSelector.insets = new Insets(0, 0, 5, 0);
		gbc_comboPatternSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboPatternSelector.gridx = 1;
		gbc_comboPatternSelector.gridy = 2;
		panelParams.add(comboPatternSelector, gbc_comboPatternSelector);
		
		JLabel lblBump = new JLabel("Bump:");
		GridBagConstraints gbc_lblBump = new GridBagConstraints();
		gbc_lblBump.anchor = GridBagConstraints.EAST;
		gbc_lblBump.insets = new Insets(0, 0, 5, 5);
		gbc_lblBump.gridx = 0;
		gbc_lblBump.gridy = 3;
		panelParams.add(lblBump, gbc_lblBump);
		
		spinner = new JSpinner();
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				controller.bumpChanged();
			}
		});
		lblBump.setLabelFor(spinner);
		spinner.setPreferredSize(new Dimension(60, 20));
		spinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner.insets = new Insets(0, 0, 5, 5);
		gbc_spinner.anchor = GridBagConstraints.WEST;
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 3;
		panelParams.add(spinner, gbc_spinner);
		
		lblBumpDesc = new JLabel("Adjust bump number to change result");
		lblBumpDesc.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblBumpDesc = new GridBagConstraints();
		gbc_lblBumpDesc.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblBumpDesc.anchor = GridBagConstraints.WEST;
		gbc_lblBumpDesc.gridwidth = 2;
		gbc_lblBumpDesc.insets = new Insets(0, 0, 5, 5);
		gbc_lblBumpDesc.gridx = 2;
		gbc_lblBumpDesc.gridy = 3;
		panelParams.add(lblBumpDesc, gbc_lblBumpDesc);
		
		JLabel lblTags = new JLabel("Tags:");
		GridBagConstraints gbc_lblTags = new GridBagConstraints();
		gbc_lblTags.anchor = GridBagConstraints.EAST;
		gbc_lblTags.insets = new Insets(0, 0, 0, 5);
		gbc_lblTags.gridx = 0;
		gbc_lblTags.gridy = 4;
		panelParams.add(lblTags, gbc_lblTags);
		
		comboTags = new JComboBox(Hashpass.listTags());
		comboTags.setPreferredSize(new Dimension(96, 24));
		comboTags.setEditable(true);
		GridBagConstraints gbc_comboTags = new GridBagConstraints();
		gbc_comboTags.insets = new Insets(0, 0, 0, 5);
		gbc_comboTags.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboTags.gridx = 1;
		gbc_comboTags.gridy = 4;
		panelParams.add(comboTags, gbc_comboTags);
		
		btnAddTag = new JButton("+");
		btnAddTag.setPreferredSize(new Dimension(25, 25));
		GridBagConstraints gbc_btnAddTag = new GridBagConstraints();
		gbc_btnAddTag.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddTag.gridx = 2;
		gbc_btnAddTag.gridy = 4;
		panelParams.add(btnAddTag, gbc_btnAddTag);
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 3;
		gbc_scrollPane.gridy = 4;
		panelParams.add(scrollPane, gbc_scrollPane);
		
		JList listTags = new JList();
		scrollPane.setViewportView(listTags);
		listTags.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		listTags.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list = listTags;
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		tglbtnShow = new JToggleButton("Show");
		tglbtnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.btnShowPressed();
			}
		});
		tglbtnShow.setSelected(true);
		buttonPane.add(tglbtnShow);
		
		btnCopy = new JButton("Copy");
		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.btnCopyPressed();
			}
		});
		btnCopy.setMnemonic('C');
		buttonPane.add(btnCopy);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.btnSavePressed();
			}
		});
		btnSave.setMnemonic('S');
		buttonPane.add(btnSave);
		
		btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.btnQuitPressed();
			}
		});
		btnQuit.setMnemonic('Q');
		buttonPane.add(btnQuit);
		
		JPanel qualityPane = new JPanel();
		qualityPane.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Quality", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)), new EmptyBorder(5, 5, 5, 5)));
		add(qualityPane, BorderLayout.WEST);
		GridBagLayout gbl_qualityPane = new GridBagLayout();
		gbl_qualityPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_qualityPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_qualityPane.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_qualityPane.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		qualityPane.setLayout(gbl_qualityPane);
		
		JLabel lblEntropy = new JLabel("Entropy:");
		GridBagConstraints gbc_lblEntropy = new GridBagConstraints();
		gbc_lblEntropy.insets = new Insets(0, 0, 5, 5);
		gbc_lblEntropy.anchor = GridBagConstraints.EAST;
		gbc_lblEntropy.gridx = 0;
		gbc_lblEntropy.gridy = 0;
		qualityPane.add(lblEntropy, gbc_lblEntropy);
		
		lblEntropyData = new JLabel("32 bits");
		GridBagConstraints gbc_lblEntropyData = new GridBagConstraints();
		gbc_lblEntropyData.anchor = GridBagConstraints.WEST;
		gbc_lblEntropyData.insets = new Insets(0, 0, 5, 5);
		gbc_lblEntropyData.gridx = 1;
		gbc_lblEntropyData.gridy = 0;
		qualityPane.add(lblEntropyData, gbc_lblEntropyData);
		
		JLabel lblScore = new JLabel("Score:");
		GridBagConstraints gbc_lblScore = new GridBagConstraints();
		gbc_lblScore.anchor = GridBagConstraints.EAST;
		gbc_lblScore.insets = new Insets(0, 0, 5, 5);
		gbc_lblScore.gridx = 0;
		gbc_lblScore.gridy = 1;
		qualityPane.add(lblScore, gbc_lblScore);
		
		lblScoreData = new JLabel("95");
		GridBagConstraints gbc_lblScoreData = new GridBagConstraints();
		gbc_lblScoreData.anchor = GridBagConstraints.WEST;
		gbc_lblScoreData.insets = new Insets(0, 0, 5, 5);
		gbc_lblScoreData.gridx = 1;
		gbc_lblScoreData.gridy = 1;
		qualityPane.add(lblScoreData, gbc_lblScoreData);
		
		JLabel lblComment = new JLabel("Comment:");
		GridBagConstraints gbc_lblComment = new GridBagConstraints();
		gbc_lblComment.anchor = GridBagConstraints.EAST;
		gbc_lblComment.insets = new Insets(0, 0, 0, 5);
		gbc_lblComment.gridx = 0;
		gbc_lblComment.gridy = 2;
		qualityPane.add(lblComment, gbc_lblComment);
		
		lblCmtData = new JLabel("Very Strong");
		lblCmtData.setSize(new Dimension(84, 15));
		GridBagConstraints gbc_lblCmtData = new GridBagConstraints();
		gbc_lblCmtData.anchor = GridBagConstraints.WEST;
		gbc_lblCmtData.insets = new Insets(0, 0, 0, 5);
		gbc_lblCmtData.gridx = 1;
		gbc_lblCmtData.gridy = 2;
		qualityPane.add(lblCmtData, gbc_lblCmtData);
		
		JPanel pswdPane = new JPanel();
		pswdPane.setBorder(new CompoundBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Password", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)), new EmptyBorder(5, 5, 5, 5)));
		add(pswdPane, BorderLayout.CENTER);
		GridBagLayout gbl_pswdPane = new GridBagLayout();
		gbl_pswdPane.columnWidths = new int[]{120, 0};
		gbl_pswdPane.rowHeights = new int[]{19, 0};
		gbl_pswdPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_pswdPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		pswdPane.setLayout(gbl_pswdPane);
		
		lblPassword = new JLabel("Pass#wo3r.@d");
		lblPassword.setSize(new Dimension(105, 15));
		lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 16));
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 0;
		pswdPane.add(lblPassword, gbc_lblPassword);

	}
	JLabel getLblPassword() {
		return lblPassword;
	}
	JLabel getLblEntropyData() {
		return lblEntropyData;
	}
	JLabel getLblScoreData() {
		return lblScoreData;
	}
	JLabel getLblCmtData() {
		return lblCmtData;
	}
	JSlider getSliderLength() {
		return sliderLength;
	}
	JComboBox<String> getComboPatternSelector() {
		return comboPatternSelector;
	}
	JSpinner getSpinner() {
		return spinner;
	}
	JTextField getTextDescription() {
		return textDescription;
	}
	public JToggleButton getTglbtnShow() {
		return tglbtnShow;
	}
	public JButton getBtnCopy() {
		return btnCopy;
	}
	public JButton getBtnSave() {
		return btnSave;
	}
	public JButton getBtnQuit() {
		return btnQuit;
	}
}
