package com.hqking.hashpass;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AboutView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static Popup ppp;
	
	/**
	 * Create the panel.
	 */
	public AboutView() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel lblLogo = new JLabel("icon");
		add(lblLogo);
		
		JTextArea txtrVersionNothing = new JTextArea();
		txtrVersionNothing.setLineWrap(true);
		txtrVersionNothing.setText("version 0.0.1\nnothing to say");
		add(txtrVersionNothing);
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ppp.hide();
			}
		});
		btnOK.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(btnOK);

	}

	public static void display() {
		PopupFactory factory = PopupFactory.getSharedInstance();
		ppp = factory.getPopup(null, new AboutView(), 0, 0);
		ppp.show();
	}
}
