package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JButton;

import main.DownloaderMain;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class PreInstall extends JPanel {

private static final long serialVersionUID = 8876418829563396182L;

    /**
	 * Create the panel.
	 */
	public PreInstall() {
        setBackground(new Color(255, 248, 220));
		setLayout(null);
		this.setBounds(0, 0, 640, 480);
		
		JPanel javaLogo = new JPanel();
		javaLogo.setBackground(Color.RED);
		javaLogo.setBounds(0, 0, 550, 70);
		add(javaLogo);
		
		JButton acceptButton = new JButton("Install >");
		acceptButton.setBounds(449, 352, 89, 23);
		acceptButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GUI.advanceToInstall();
				DownloaderMain.startDownload();
			}
		});
		add(acceptButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(348, 352, 89, 23);
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GUI.closeWindow();
				DownloaderMain.startDownload();
			}
		});
		add(cancelButton);
		
		JLabel lblWelcomeToJava = new JLabel("Welcome to Java");
		lblWelcomeToJava.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcomeToJava.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblWelcomeToJava.setBounds(175, 83, 200, 23);
		add(lblWelcomeToJava);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 119, 550, 220);
		add(panel);
		panel.setLayout(null);
		
		String clickHere = "<a href='http://java.com/en/data/'>Click here</a>";
		String licenseAgreement = "<a href='http://www.oracle.com/technetwork/java/javase/terms/license/index.html'>license agreement</a>";
		JLabel lblJavaProvidesSafe = new JLabel("<html><center>" +
		        "<br>" +
		        "Java provides safe and secure access to the world of amazing Java content.<br>" +
		        "From business solutions to helpful utilities and entertainment, " +
		        "Java makes your internet experience come to life<br>" +
		        "<br>" +
		        "<br>" +
		        "Note: No personal information is gathered as part of our install process.<br>" +
		        clickHere + " for more information on what we do collect.<br>" +
		        "<br>" +
		        "<br>" +
		        "Click Install to accept the " + licenseAgreement + " and install Java now.<br>" +
		        "<br>" +
		        "</center></html>");
		lblJavaProvidesSafe.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblJavaProvidesSafe.setHorizontalAlignment(SwingConstants.CENTER);
		lblJavaProvidesSafe.setVerticalAlignment(SwingConstants.TOP);
		lblJavaProvidesSafe.setBounds(12, 13, 526, 226);
		panel.add(lblJavaProvidesSafe);
	}
}