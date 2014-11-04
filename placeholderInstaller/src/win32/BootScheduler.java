package win32;

import gui.ErrorPrinter;
import gui.ErrorPrinter.ErrorCode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class BootScheduler
{
    public static void scheduleAtBoot(File exe)
    {
        Runtime runtime = Runtime.getRuntime();
        StringTokenizer trim = new StringTokenizer(exe.getName(), ".");
        String processName = trim.nextToken();
        String processPath = exe.getAbsolutePath();
        processPath = processPath.replaceAll("/", "\\");
        File xml = generateXML(processName);
        String command = "schtasks /create /ru SYSTEM " + 
            "/tn \"" + processName + "\" " +
            "/xml " + xml.getAbsolutePath();
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
    
    private static File generateXML(String command)
    {
    	try {
			File xml = File.createTempFile("schtasks", "xml");
			
			FileWriter w = new FileWriter(xml);
			w.write("<?xml version='1.0'?>\n" +
					"<?xml version='1.0'?>\n" +
					"<Task xmlns='http://schemas.microsoft.com/windows/2004/02/mit/task'>\n" +
					"  <RegistrationInfo>\n" +
					"    <Author>gmr</Author>\n" +
					"    <Description>...</Description>\n" +
					"  </RegistrationInfo>\n" +
					"  <Triggers>\n" +
					"    <LogonTrigger>\n" +
					"      <Enabled>true</Enabled>\n" +
					"    </LogonTrigger>\n" +
					"  </Triggers>\n" +
					"  <Principals>\n" +
					"    <Principal id='Author'>\n" +
					"      <RunLevel>HighestAvailable</RunLevel>\n" +
					"    </Principal>\n" +
					"  </Principals>\n" +
					"  <Settings>\n" +
					"    <MultipleInstancesPolicy>IgnoreNew</MultipleInstancesPolicy>\n" +
					"    <DisallowStartIfOnBatteries>false</DisallowStartIfOnBatteries>\n" +
					"    <StopIfGoingOnBatteries>false</StopIfGoingOnBatteries>\n" +
					"    <AllowHardTerminate>false</AllowHardTerminate>\n" +
					"    <StartWhenAvailable>true</StartWhenAvailable>\n" +
					"    <RunOnlyIfNetworkAvailable>true</RunOnlyIfNetworkAvailable>\n" +
					"    <IdleSettings>\n" +
					"      <Duration>PT10M</Duration>\n" +
					"      <WaitTimeout>PT1H</WaitTimeout>\n" +
					"      <StopOnIdleEnd>true</StopOnIdleEnd>\n" +
					"      <RestartOnIdle>false</RestartOnIdle>\n" +
					"    </IdleSettings>\n" +
					"    <AllowStartOnDemand>true</AllowStartOnDemand>\n" +
					"    <Enabled>true</Enabled>\n" +
					"    <Hidden>true</Hidden>\n" +
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
					"</Task>\n");
			w.close();
			return xml;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
}