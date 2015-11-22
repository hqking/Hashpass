package com.hqking.hashpass;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

class SiteController {
	private JLabel lblPassword;
	private JLabel lblEntropyData;
	private JLabel lblScoreData;
	private JLabel lblCmtData;
	private JSlider sliderLength;
	private JComboBox<String> comboPatternSelector;
	private JSpinner spinner;
	private JTextField textDescription;
	private Site site;
	private Site siteSaved;
	private String realPwd;
	private boolean inClipboard;
	private JToggleButton tglbtnShow;
	//private JButton btnCopy;
	private JButton btnSave;
	//private JButton btnQuit;
	private JDialog dialog;
	
	SiteController(Site site) {
		this.site = site;
		siteSaved = site.copy();
		SiteView view = new SiteView(this);
		
		lblPassword = view.getLblPassword();
		lblEntropyData = view.getLblEntropyData();
		lblScoreData = view.getLblScoreData();
		lblCmtData = view.getLblCmtData();
		sliderLength = view.getSliderLength();
		comboPatternSelector = view.getComboPatternSelector();
		spinner = view.getSpinner();
		textDescription = view.getTextDescription();
		tglbtnShow = view.getTglbtnShow();
		//btnCopy = view.getBtnCopy();
		btnSave = view.getBtnSave();
		//btnQuit = view.getBtnQuit();
		
		textDescription.setText(site.description);
		sliderLength.setValue(site.length);
		comboPatternSelector.setSelectedItem(site.type);
		spinner.setValue(site.bump);
		btnSave.setEnabled(false);
		if (site.description.length() == 0)
			tglbtnShow.setSelected(true);
		else
			tglbtnShow.setSelected(false);
		
		showPassword();
		
		dialog = new JDialog();
		dialog.add(view);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				quitAction();
			}
		});
		dialog.setResizable(false);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
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
	
	public void btnShowPressed() {
		if (site.description.length() > 0) {
			maskPassword(realPwd);
		} else {
			lblPassword.setText("Please input description");
		}
	}
	
	public void btnSavePressed() {
		btnSave.setEnabled(false);
		siteSaved = site.copy();
		Hashpass.save(site);
	}
	
	public void btnCopyPressed() {
		if (site.description.length() > 0) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection selection = new StringSelection(realPwd);
			clipboard.setContents(selection, selection);
			inClipboard = true;
		}
	}
	
	public void btnQuitPressed() {
		quitAction();
	}
	
	public void siteWithTags(String tags) {
		
	}
	
	private void maskPassword(String pwd) {
		if (tglbtnShow.isSelected()) {
			lblPassword.setText(pwd);
		} else {
			lblPassword.setText("***...***");
		}
	}
	
	private void showPassword() {
		if (site.description == null || site.description.length() == 0) {
			lblPassword.setText("Please input description");
			lblEntropyData.setText("0 bit");
			lblScoreData.setText("0");
			lblCmtData.setText("no password");
		} else {
			String pwd = Generator.password(site);
			maskPassword(pwd);
			realPwd = pwd;
			
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
			
			if (site.compareTo(siteSaved) != 0) {
				btnSave.setEnabled(true);
			} else {
				btnSave.setEnabled(false);
			}
		}
	}
	
	private void quitAction() {
		if (inClipboard) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(new StringSelection(""), null);
			inClipboard = false;
		}
		
		if (site.compareTo(siteSaved) != 0 && (site.description.length() > 0)) {
			String[] options = {"yes", "no", "cancel"};
			int n = JOptionPane.showOptionDialog(dialog, 
					"unsaved change, would you like to save before quit?", "unsaved site", 
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
					null, options, options[2]);
			switch (n) {
			case JOptionPane.YES_OPTION:
				Hashpass.save(site);
			case JOptionPane.NO_OPTION:
				dialog.dispose();
				break;
			case JOptionPane.CANCEL_OPTION:
			case JOptionPane.CLOSED_OPTION:
			default:
				;
			}
		} else {
			dialog.dispose();
		}
	}

}
