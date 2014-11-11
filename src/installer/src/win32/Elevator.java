/*
 * The code in this class is sourced from a Stack Overflow answer
 * written by the user 'prunge' and edited by the user 'Seda'.
 * 
 * The question can be seen here:
 * 
 * http://stackoverflow.com/questions/11041509/elevating-a-processbuilder-process-via-uac
 */

package win32;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;

public class Elevator
{
    public static void executeAsAdministrator(String command, String args)
    {
        Shell32X.SHELLEXECUTEINFO execInfo = new Shell32X.SHELLEXECUTEINFO();
        execInfo.lpFile = new WString(command);
        if (args != null)
            execInfo.lpParameters = new WString(args);
        execInfo.nShow = Shell32X.SW_HIDE;
        execInfo.fMask = Shell32X.SEE_MASK_NOCLOSEPROCESS;
        execInfo.lpVerb = new WString("runas");
        boolean result = Shell32X.INSTANCE.ShellExecuteEx(execInfo);

        if (!result)
        {
            int lastError = Kernel32.INSTANCE.GetLastError();
            String errorMessage = Kernel32Util.formatMessageFromLastErrorCode(lastError);
            throw new RuntimeException("Error performing elevation: " + lastError + ": " + errorMessage + " (apperror=" + execInfo.hInstApp + ")");
        }
    }
}