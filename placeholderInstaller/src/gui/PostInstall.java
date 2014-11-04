package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JButton;

public class PostInstall extends JPanel {

    private static final long serialVersionUID = 9043469927619052834L;

    /**
	 * Create the panel.
	 */
	public PostInstall() {
		setLayout(null);
		this.setBounds(0, 0, 640, 480);
		
		JPanel javaLogo = new JPanel();
		javaLogo.setBackground(Color.RED);
		javaLogo.setBounds(0, 0, 550, 70);
		add(javaLogo);
		
		JButton closeButton = new JButton("Close");
		closeButton.setBounds(400, 250, 89, 23);
		closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.closeWindow();
			}
		});
		add(closeButton);
	}
}