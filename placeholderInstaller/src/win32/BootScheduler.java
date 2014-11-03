package win32;

import gui.ErrorPrinter;
import gui.ErrorPrinter.ErrorCode;

import java.io.File;
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
        String command = "schtasks /create /ru SYSTEM " + 
            "/tn \"" + processName + "\" " +
            "/tr \"" + processPath + "\" " +
            "/sc onlogon";
        try
        {
            runtime.exec(command);
        }
        catch (Exception e)
        {
            ErrorPrinter.printError(ErrorCode.bootstrapFail, "");
        }
    }
}