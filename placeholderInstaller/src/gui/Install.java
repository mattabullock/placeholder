package gui;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

import java.awt.Color;

import javax.swing.JSeparator;

public class Install extends JPanel {

    private static final long serialVersionUID = -1204752034106859639L;
    private JProgressBar fileProgressBar;
	private JProgressBar totalProgressBar;
	private JLabel status;
	
	/**
	 * Create the panel.
	 */
	public Install() {
		setBackground(new Color(230, 230, 210));
		setLayout(null);
		this.setBounds(0, 0, 500, 400);
		
		fileProgressBar = new JProgressBar(0, 1000);
		fileProgressBar.setBounds(20, 91, 460, 15);
		add(fileProgressBar);
		
		totalProgressBar = new JProgressBar(0, 1000);
		totalProgressBar.setBounds(20, 117, 460, 15);
		add(totalProgressBar);
		
		JLabel javaLogo = new JLabel(new ImageIcon(getClass().getResource("/javaLogoStrip.jpg")));
		javaLogo.setBackground(Color.RED);
		javaLogo.setBounds(0, 0, 500, 55);
		add(javaLogo);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 360, 500, 2);
		add(separator);
		
		JLabel advertisement = new JLabel(new ImageIcon(getClass().getResource("/threeBillion.jpeg")));
		advertisement.setBackground(Color.WHITE);
		advertisement.setBounds(0, 143, 500, 220);
		add(advertisement);
		
		status = new JLabel();
		status.setBounds(20, 66, 460, 14);
		add(status);
	}
	
	public JProgressBar getFileProgressBar()
	{
		return fileProgressBar;
	}
	
	public JProgressBar getTotalProgressBar()
	{
	    return totalProgressBar;
	}
	
	public JLabel getStatusLabel()
	{
	    return status;
	}
}