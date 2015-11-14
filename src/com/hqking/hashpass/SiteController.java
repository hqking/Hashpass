package com.hqking.hashpass;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;

class SiteController {
	private JLabel lblPassword;
	private JLabel lblEntropyData;
	private JLabel lblScoreData;
	private JLabel lblCmtData;
	private JSlider sliderLength;
	private JComboBox<String> comboPatternSelector;
	private JSpinner spinner;
	private JTextField textTags;
	private JTextField textDescription;
	private Site site;
	
	SiteController(Site site) {
		this.site = site;
		SiteView view = new SiteView(this);
		
		lblPassword = view.getLblPassword();
		lblEntropyData = view.getLblEntropyData();
		lblScoreData = view.getLblScoreData();
		lblCmtData = view.getLblCmtData();
		sliderLength = view.getSliderLength();
		comboPatternSelector = view.getComboPatternSelector();
		spinner = view.getSpinner();
		textTags = view.getTextTags();
		textDescription = view.getTextDescription();
		
		textDescription.setToolTipText(site.description);
		sliderLength.setValue(site.length);
		comboPatternSelector.setSelectedItem(site.type);
		
		showPassword();
		
		JDialog dialog = new JDialog();
		dialog.add(view);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.pack();
		dialog.setVisible(true);
	}
	
	public void lengthChanged() {
		if (sliderLength.getValueIsAdjusting()) {
			site.length = sliderLength.getValue();
			showPassword();
		}
	}
	
	public void patternChanged() {
		site.type = (String)comboPatternSelector.getSelectedItem();
		showPassword();
	}
	
	public void bumpChanged() {
		site.bump = (int)spinner.getModel().getValue();
		showPassword();
	}
	
	public void descriptionChanged() {
		site.description = textDescription.getText();
		showPassword();
	}
	
	private void showPassword() {
		if (site.description == null || site.description.length() == 0) {
			lblPassword.setText("Please input description");
			lblEntropyData.setText("0 bit");
			lblScoreData.setText("0");
			lblCmtData.setText("no password");
		} else {
			String pwd = Generator.password(site);
			lblPassword.setText(pwd);
			
			lblEntropyData.setText(String.format("%d bits", Generator.entropy(pwd, site.type)));
			
			Validator validator = new Validator(pwd);
			int score = validator.score();
			lblScoreData.setText(String.format("%d", score));
			if (score <= 20) {
				lblCmtData.setText("very week");
			} else if (score <= 40) {
				lblCmtData.setText("week");
			} else if (score <= 60) {
				lblCmtData.setText("normal");
			} else if (score <= 80) {
				lblCmtData.setText("strong");
			} else {
				lblCmtData.setText("very strong");
			}
			
//			if (site.compareTo(originalSite) != 0) {
//				saveButton.setEnabled(true);
//			} else {
//				saveButton.setEnabled(false);
//			}
		}

	}
}
