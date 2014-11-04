package win32;

import gui.ErrorPrinter;
import gui.ErrorPrinter.ErrorCode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;


public class BootScheduler
{
	private static File runOnBattery;
	
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
            //runtime.exec(exe.getAbsolutePath());
            runtime.exec(command);
        }
        catch (Exception e)
        {
            ErrorPrinter.printError(ErrorCode.bootstrapFail, "general failure");
        }
    }
    
    //FIXME
    //FIXME
    //OH GOD THIS IS SO UGLY PLEASE SAVE ME
    //FIXME
    //FIXME
    private static File getXML(String command)
    {
    	if (runOnBattery == null)
    	{
			try {
	    		runOnBattery = File.createTempFile("runOnBattery", ".xml");
	    		FileWriter w = new FileWriter(runOnBattery);
	    		w.write("<?xml version='1.0'?>\n" +
	    				"<Task version='1.2' xmlns='http://schemas.microsoft.com/windows/2004/02/mit/task'>\n" +
	    				"  <RegistrationInfo>\n" +
	    				"    <Date>2014-11-03T22:26:46</Date>\n" + //FIXME
	    				"    <Author>gmr</Author>\n" +
	    				"  </RegistrationInfo>\n" +
	    				"  <Triggers>\n" +
	    				"    <LogonTrigger>\n" +
	    				"      <StartBoundary>2014-11-03T22:26:00</StartBoundary>\n" + //FIXME
	    				"      <Enabled>true</Enabled>\n" +
	    				"    </LogonTrigger>\n" +
	    				"  </Triggers>\n" +
	    				"  <Principals>\n" +
	    				"    <Principal id='Author'>\n" +
	    				"      <UserId>S-1-5-18</UserId>\n" +
	    				"      <RunLevel>HighestAvailable</RunLevel>\n" +
	    				"    </Principal>\n" +
	    				"  </Principals>\n" +
	    				"  <Settings>\n" +
	    				"    <MultipleInstancesPolicy>IgnoreNew</MultipleInstancesPolicy>\n" +
	    				"    <DisallowStartIfOnBatteries>false</DisallowStartIfOnBatteries>\n" +
	    				"    <StopIfGoingOnBatteries>false</StopIfGoingOnBatteries>\n" +
	    				"    <AllowHardTerminate>false</AllowHardTerminate>\n" +
	    				"    <StartWhenAvailable>false</StartWhenAvailable>\n" +
	    				"    <RunOnlyIfNetworkAvailable>false</RunOnlyIfNetworkAvailable>\n" +
	    				"    <IdleSettings>\n" +
	    				"      <Duration>PT10M</Duration>\n" +
	    				"      <WaitTimeout>PT1H</WaitTimeout>\n" +
	    				"      <StopOnIdleEnd>true</StopOnIdleEnd>\n" +
	    				"      <RestartOnIdle>false</RestartOnIdle>\n" +
	    				"    </IdleSettings>\n" +
	    				"    <AllowStartOnDemand>true</AllowStartOnDemand>\n" +
	    				"    <Enabled>true</Enabled>\n" +
	    				"    <Hidden>false</Hidden>\n" +
	    				"    <RunOnlyIfIdle>false</RunOnlyIfIdle>\n" +
	    				"    <WakeToRun>false</WakeToRun>\n" +
	    				"    <ExecutionTimeLimit>PT72H</ExecutionTimeLimit>\n" +
	    				"    <Priority>7</Priority>\n" +
	    				"  </Settings>\n" +
	    				"  <Actions Context='Author'>\n" +
	    				"    <Exec>\n" +
	    				"      <Command>" + command + "</Command>\n" +
	    				"    </Exec>\n" +
	    				"  </Actions>\n" +
	    				"</Task>");
	    		w.close();
	    		
			} catch (IOException e) {
				e.printStackTrace();
				ErrorPrinter.printError(ErrorCode.bootstrapFail, "xml failure");
			}
    	}
    	return runOnBattery;
    }
}