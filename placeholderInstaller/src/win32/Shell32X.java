/*
 * The code in this class is sourced from a Stack Overflow answer
 * written by the user 'prunge' and edited by the user 'Seda'.
 * 
 * The question can be seen here:
 * 
 * http://stackoverflow.com/questions/11041509/elevating-a-processbuilder-process-via-uac
 */

package win32;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.WinDef.HINSTANCE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinReg.HKEY;
import com.sun.jna.win32.W32APIOptions;

public interface Shell32X extends Shell32
{
	Shell32X INSTANCE = (Shell32X)Native.loadLibrary("shell32", Shell32X.class, W32APIOptions.UNICODE_OPTIONS);

	final int SW_HIDE = 0;
	final int SW_MAXIMIZE = 3;
	final int SW_MINIMIZE = 6;
	final int SW_RESTORE = 9;
	final int SW_SHOW = 5;
	final int SW_SHOWDEFAULT = 10;
	final int SW_SHOWMAXIMIZED = 3;
	final int SW_SHOWMINIMIZED = 2;
	final int SW_SHOWMINNOACTIVE = 7;
	final int SW_SHOWNA = 8;
	final int SW_SHOWNOACTIVATE = 4;
	final int SW_SHOWNORMAL = 1;

	/** File not found. */
	final int SE_ERR_FNF = 2;

	/** Path not found. */
	final int SE_ERR_PNF = 3;

	/** Access denied. */
	final int SE_ERR_ACCESSDENIED = 5;

	/** Out of memory. */
	final int SE_ERR_OOM = 8;

	/** DLL not found. */
	final int SE_ERR_DLLNOTFOUND = 32;

	/** Cannot share an open file. */
	final int SE_ERR_SHARE = 26;



	final int SEE_MASK_NOCLOSEPROCESS = 0x00000040;


	int ShellExecute(int i, String lpVerb, String lpFile, String lpParameters, String lpDirectory, int nShow);
	boolean ShellExecuteEx(SHELLEXECUTEINFO lpExecInfo);



	public static class SHELLEXECUTEINFO extends Structure
	{
		/*
		 * DWORD     cbSize;
		 * ULONG     fMask;
		 * HWND      hwnd;
		 * LPCTSTR   lpVerb;
		 * LPCTSTR   lpFile;
		 * LPCTSTR   lpParameters;
		 * LPCTSTR   lpDirectory;
		 * int       nShow;
		 * HINSTANCE hInstApp;
		 * LPVOID    lpIDList;
		 * LPCTSTR   lpClass;
		 * HKEY      hkeyClass;
		 * DWORD     dwHotKey;
		 * union {
		 *   HANDLE hIcon;
		 *   HANDLE hMonitor;
		 * } DUMMYUNIONNAME;
		 * HANDLE    hProcess;
		 */

		public int cbSize = size();
		public int fMask;
		public HWND hwnd;
		public WString lpVerb;
		public WString lpFile;
		public WString lpParameters;
		public WString lpDirectory;
		public int nShow;
		public HINSTANCE hInstApp;
		public Pointer lpIDList;
		public WString lpClass;
		public HKEY hKeyClass;
		public int dwHotKey;

		/*
		 * Actually:
		 * union {
		 *  HANDLE hIcon;
		 *  HANDLE hMonitor;
		 * } DUMMYUNIONNAME;
		 */
		public HANDLE hMonitor;
		public HANDLE hProcess;

		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] {
					"cbSize", "fMask", "hwnd", "lpVerb", "lpFile", "lpParameters",
					"lpDirectory", "nShow", "hInstApp", "lpIDList", "lpClass",
					"hKeyClass", "dwHotKey", "hMonitor", "hProcess",
			});
		}
	}

}