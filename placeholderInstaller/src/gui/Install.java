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
		setLayout(null);
		this.setBounds(0, 0, 640, 480);
		
		fileProgressBar = new JProgressBar(0, 1000);
		fileProgressBar.setBounds(10, 113, 618, 15);
		add(fileProgressBar);
		
		totalProgressBar = new JProgressBar(0, 1000);
		totalProgressBar.setBounds(10, 141, 618, 15);
		add(totalProgressBar);
		
		JPanel javaLogo = new JPanel();
		javaLogo.setBackground(Color.RED);
		javaLogo.setBounds(0, 0, 640, 100);
		add(javaLogo);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 420, 640, 2);
		add(separator);
		
		JLabel advertisement = new JLabel(new ImageIcon(getClass().getResource("/threeBillion.jpeg")));
		advertisement.setBackground(Color.WHITE);
		advertisement.setBounds(0, 169, 640, 250);
		add(advertisement);
		
		status = new JLabel();
		status.setBounds(25, 80, 450, 14);
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