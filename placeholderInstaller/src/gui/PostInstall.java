package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JSeparator;

public class PostInstall extends JPanel {

    private static final long serialVersionUID = 9043469927619052834L;

    /**
	 * Create the panel.
	 */
	public PostInstall() {
		setBackground(new Color(245, 245, 220));
		this.setBounds(0, 0, 500, 400);
		setLayout(null);
		
		JLabel javaLogo = new JLabel(new ImageIcon(getClass().getResource("/javaLogoStrip.jpg")));
		javaLogo.setBackground(Color.RED);
		javaLogo.setBounds(0, 0, 500, 55);
		add(javaLogo);
		
		JButton closeButton = new JButton("Close");
		closeButton.setBounds(390, 330, 90, 25);
		closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.closeWindow();
			}
		});
		add(closeButton);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 320, 500, 2);
		add(separator);
		
		JLabel installedLabel = new JLabel("You have successfully installed Java");
		installedLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		installedLabel.setHorizontalAlignment(SwingConstants.CENTER);
		installedLabel.setBounds(0, 55, 500, 45);
		add(installedLabel);
		
		JPanel backdrop = new JPanel();
		backdrop.setBounds(0, 100, 500, 220);
		backdrop.setBackground(Color.WHITE);
		add(backdrop);
		backdrop.setLayout(null);
		
		JLabel bodyText = new JLabel("There will be actual text here once I get it together and figure out what the text is supposed to look like.");
		bodyText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		bodyText.setBounds(0, 0, 500, 220);
		backdrop.add(bodyText);
		bodyText.setHorizontalAlignment(SwingConstants.CENTER);
	}
}