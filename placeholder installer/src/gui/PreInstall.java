package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JButton;

import main.DownloaderMain;

public class PreInstall extends JPanel {

private static final long serialVersionUID = 8876418829563396182L;

    /**
	 * Create the panel.
	 */
	public PreInstall() {
		setLayout(null);
		this.setBounds(0, 0, 550, 420);
		
		JPanel javaLogo = new JPanel();
		javaLogo.setBackground(Color.RED);
		javaLogo.setBounds(0, 0, 550, 70);
		add(javaLogo);
		
		JButton acceptButton = new JButton("OK");
		acceptButton.setBounds(400, 250, 89, 23);
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
		cancelButton.setBounds(301, 250, 89, 23);
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GUI.closeWindow();
				DownloaderMain.startDownload();
			}
		});
		add(cancelButton);
	}
}