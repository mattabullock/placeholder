package main;

import gui.ErrorPrinter;
import gui.ErrorPrinter.ErrorCode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import network.Uploader;

public class UploaderMain
{
    final static int PORT_NUMBER = 5723;
    private static String manifest;

    /**
     * Runs an uploader, which will send the files specified in a manifest file
     * to a connecting client
     * 
     * @param args Contains the name of the manifest file in the first argument
     * 
     * A default value of './manifest.txt' will be used if no arguments are given
     */
    public static void main(String args[])
    {
        if (args.length > 0)
            manifest = args[0];
        else manifest = "./manifest.txt";
        
        try
        {
            ServerSocket ss = new ServerSocket(PORT_NUMBER);
            Socket s;
            
            while ((s = ss.accept()) != null)
            {
                DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
                Uploader uploader = new Uploader(dOut);
                uploader.start();
            }
            
            ss.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            ErrorPrinter.printError(ErrorCode.uploadError, "IOException");
        }
    }

    public static String getManifestName()
    {
        return manifest;
    }
}