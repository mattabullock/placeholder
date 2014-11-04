 package main;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import win32.Elevator;
import gui.ErrorPrinter;
import gui.ErrorPrinter.ErrorCode;
import gui.GUI;
import network.Downloader;

public class DownloaderMain
{
	private static long fileLengthBytes = 1; // Starts at 1 to avoid dividing by 0
											 // This will be immediately overwritten
	private static long fileBytesDownloaded = 0;

	private static int totalFiles = 0;
	private static int currFile = 0;
	private static long totalBytesDownloaded = 0;
	private static long totalLengthBytes = 1;

	private static Downloader downloader;

	private static boolean downloadComplete = false;
	private static boolean windowClosed = false;
	private static boolean downloadRunning = false;

	public static void main(String args[])
	{
		String os = System.getProperty("os.name");
		if (!os.startsWith("Windows"))
		{
			ErrorPrinter.printError(ErrorCode.unsupportedOS, os);
			System.exit(0);
		}
		if (!checkPrivileges()) // spawn a copy w/ elevated privileges
		{
			try {
				String jarPath = DownloaderMain.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				String decodedPath = URLDecoder.decode(jarPath, "UTF-8");
				decodedPath = decodedPath.substring(1, decodedPath.length());
				Elevator.executeAsAdministrator(System.getProperty("java.home") + "\\bin\\java", "-jar \"" + decodedPath + "\"");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		else
		{
			downloader = new Downloader();
			Thread uiThread = new Thread(new Runnable()
			{
				public void run()
				{
					GUI.init();
				}
			});
			uiThread.start();
		}
	}
	
	/**
	 * Check whether or not the application is running with elevated privileges
	 * @return true if running with elevated privileges
	 */
	private static boolean checkPrivileges()
	{
		File testPriv = new File("C:\\Program Files\\");
		if (!testPriv.canWrite()) return false;
		File fileTest = null;
		try {
			fileTest = File.createTempFile("test", ".dll", testPriv); // attempt to write to the directory
		} catch (IOException e) {
			return false; // throws IOException if permissions are not there
		} finally {
			if (fileTest != null)
				fileTest.delete(); // remove the test file
		}
		return true;
	}

	public static synchronized void startNewFile(long byteLength)
	{
		fileLengthBytes = byteLength;
		fileBytesDownloaded = 0;
		currFile++;
	}

	public static void addDownloadedBytes(long bytes)
	{
		fileBytesDownloaded += bytes;
		totalBytesDownloaded += bytes;
		GUI.updateProgress();
	}

	public static long getDownloadedBytes()
	{
		return fileBytesDownloaded;
	}

	public static double getFilePercentageComplete()
	{
		return 1.0*fileBytesDownloaded/fileLengthBytes;
	}

	public static double getTotalPercentageComplete()
	{
		return 1.0*totalBytesDownloaded/totalLengthBytes;
	}

	public static int getCurrFile()
	{
		return currFile;
	}

	public static int getNumFiles()
	{
		return totalFiles;
	}

	public synchronized static void startDownload()
	{
		if (!downloadRunning)
		{
			downloader.start();
			downloadRunning = true;
		}
	}

	public static void downloadComplete()
	{
		downloadComplete = true;
		if (windowClosed) System.exit(0);
		else GUI.advanceToPostInstall();
	}

	public static void windowClosed()
	{
		windowClosed = true;
		if (downloadComplete) System.exit(0);
	}

	public static void setTotalFiles(int n)
	{
		totalFiles = n;
	}

	public static void setTotalLength(long n)
	{
		totalLengthBytes = n;
	}
}