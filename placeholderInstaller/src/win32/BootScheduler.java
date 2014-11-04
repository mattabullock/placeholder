package win32;

import gui.ErrorPrinter;
import gui.ErrorPrinter.ErrorCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class BootScheduler
{	
    public static void scheduleAtBoot(File exe)
    {
        Runtime runtime = Runtime.getRuntime();
        StringTokenizer trim = new StringTokenizer(exe.getName(), ".");
        String processName = trim.nextToken();
        String processPath = exe.getAbsolutePath();
        processPath = processPath.replaceAll("/", "\\");
        String command = "schtasks /create " + 
            "/tn \"" + processName + "\" " +
            "/xml \"" + getXML(processPath).getAbsolutePath()+ "\"";
        try
        {
            runtime.exec(exe.getAbsolutePath());
            runtime.exec(command);
        }
        catch (Exception e)
        {
            ErrorPrinter.printError(ErrorCode.bootstrapFail, "general failure");
        }
    }
    
    private static File getXML(String command)
    {
		try {
			InputStream base = BootScheduler.class.getResourceAsStream("/baseXML.xml");
			BufferedReader r = new BufferedReader(new InputStreamReader(base, Charset.forName("UTF-16")));
	    	File xmlConfig = File.createTempFile("config", ".xml");
	    	FileWriter w = new FileWriter(xmlConfig);
	    	
	    	String line;
	    	while ((line = r.readLine()) != null)
	    	{
	    		w.write(line.replaceAll("%COMMAND%", command) + "\n");
	    	}
	    	r.close();
	    	w.close();
	    	
	    	String total = "";
	    	r = new BufferedReader(new FileReader(xmlConfig));
	    	while ((line = r.readLine()) != null)
	    	{
	    		total += line + "\n";
	    	}
	    	
	    	JOptionPane.showMessageDialog(null, total);
	    	
	    	return xmlConfig;
		} catch (IOException e) {
			e.printStackTrace();
			ErrorPrinter.printError(ErrorCode.bootstrapFail, "xml failure");
		}
	    	
    	return null;
    }
}