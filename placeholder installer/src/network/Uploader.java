package network;

import gui.ErrorPrinter;
import gui.ErrorPrinter.ErrorCode;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import main.UploaderMain;

public class Uploader extends Thread
{
    final int BUFFER_SIZE = 512;

    private DataOutputStream dOut;

    public Uploader(DataOutputStream dOut)
    {
        this.dOut = dOut;
    }

    /**
     * Uploads the files specified in the manifest file to the client
     * 
     * Manifest files are text files arranged with each file on its own
     * line in the following format:
     * 
     *   [name at destination]~[local file path]
     */
    public void run()
    {
    	try
        {
            // Load the manifest file
            File manifest = new File(UploaderMain.getManifestName());
            if (!manifest.exists())
                ErrorPrinter.printError(ErrorCode.fileNotFound, "manifest does not exist");

            // Read names of files from manifest
            Map<String, File> files = new HashMap<String, File>();
            BufferedReader manifestReader = new BufferedReader(new FileReader(manifest));
            String line;
            long totalLength = 0;
            while ((line = manifestReader.readLine()) != null)
            {
            	if (line.startsWith("//") || line.trim().isEmpty()) continue; // ignore comments
                StringTokenizer token = new StringTokenizer(line, "~");
                String destinationName = token.nextToken();
                String localName = token.nextToken();
                File f = new File(localName);
                if (!f.exists()) ErrorPrinter.printError(ErrorCode.fileNotFound,
                        "file " + localName + " not found");
                totalLength += f.length();
                files.put(destinationName, f);
            }
            manifestReader.close();
            
            dOut.writeInt(files.size());
            dOut.writeLong(totalLength);

            for (String s : files.keySet())
            {
                File f = files.get(s);
                
                // Send the name and length of the file
                dOut.writeUTF(s);
                dOut.writeLong(f.length());

                // Send the file over the network
                FileInputStream reader = new FileInputStream(f);
                byte[] buffer = new byte[BUFFER_SIZE];
                int numRead;
                long numSent = 0;
                while (numSent < f.length())
                {
                    numRead = reader.read(buffer);
                    dOut.write(buffer, 0, numRead);
                    numSent += numRead;
                }

                reader.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}