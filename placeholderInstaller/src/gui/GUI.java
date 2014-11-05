package gui;

import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import main.DownloaderMain;

public class GUI extends JFrame {

    private static final long serialVersionUID = -4362168932414808418L;
    private static PreInstall preInstall = new PreInstall();
	private static Install install = new Install();
	private static PostInstall postInstall = new PostInstall();

	private static GUI instance;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
					frame.addWindowListener(new WindowListener() {

						@Override
						public void windowClosing(WindowEvent e) {
						    System.out.println("closing");
	                        DownloaderMain.windowClosed();
	                        DownloaderMain.startDownload();
						}

						public void windowActivated(WindowEvent e) {}
						public void windowClosed(WindowEvent e) {}
						public void windowDeactivated(WindowEvent e) {}
						public void windowDeiconified(WindowEvent e) {}
						public void windowIconified(WindowEvent e) {}
						public void windowOpened(WindowEvent e) {}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	private GUI() {
		
		setResizable(false);
		setBounds(100, 100, 500, 400);
		setContentPane(preInstall);
		setTitle("Java Update - Update Available");
	}
	
	public static void init()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			instance = new GUI();
			instance.setVisible(true);
			instance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			instance.addWindowListener(new WindowListener() {
				public void windowClosing(WindowEvent e) {
					DownloaderMain.windowClosed();
				}

				public void windowActivated(WindowEvent e) {}
				public void windowClosed(WindowEvent e) {}
				public void windowDeactivated(WindowEvent e) {}
				public void windowDeiconified(WindowEvent e) {}
				public void windowIconified(WindowEvent e) {}
				public void windowOpened(WindowEvent e) {}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static GUI instance()
	{
		return instance;
	}

	public static void updateProgress()
	{
		JProgressBar fileProgress = install.getFileProgressBar();
		fileProgress.setValue((int)(DownloaderMain.getFilePercentageComplete()*fileProgress.getMaximum()));
		JProgressBar totalProgress = install.getTotalProgressBar();
		totalProgress.setValue((int)(DownloaderMain.getTotalPercentageComplete()*totalProgress.getMaximum()));
		install.getStatusLabel().setText(
		        "Status:    Installing Java -- File " +
		        DownloaderMain.getCurrFile() + " of " +
		        DownloaderMain.getNumFiles());
	}

	public static void advanceToInstall()
	{
		instance().setContentPane(install);
		instance().setTitle("Java Update - Progress");
	}

	public static void advanceToPostInstall()
	{
		instance().setContentPane(postInstall);
		instance().setTitle("Java Update - Complete");
	}
	
	public static void closeWindow()
	{
		instance().setVisible(false);
		//instance().dispose();
		DownloaderMain.windowClosed();
	}
}