package gui;

import javax.swing.JOptionPane;

public class ErrorPrinter
{
	public enum ErrorCode
	{
		unrecognizedSymbol,
        invalidArgument,
        unspecifiedValue,
        invalidConfigFile,
        noArguments,
        alreadyInstalled,
        fileNotFound,
        downloadError,
        uploadError,
        unsupportedOS,
        bootstrapFail,
	};
	
	public static void printError(ErrorCode errorCode, String additional)
	{
		if (additional == null) additional = "no additional information";
		String message;
		switch (errorCode)
        {
            case unrecognizedSymbol:
            	message = "An unrecognized symbol was inputted as an argument: " + additional;
                break;
            case invalidArgument:
            	message = "An invalid symbol was inputted as an argument: " + additional;
                break;
            case unspecifiedValue:
            	message = "At least one value was not specified: " + additional;
                break;
            case invalidConfigFile:
            	message = "The supplied config file is incorrectly formatted: " + additional;
                break;
            case noArguments:
            	message = "No arguments were specified: " + additional;
                break;
            case alreadyInstalled:
            	message = "The specified file is already installed: " + additional;
            	break;
            case fileNotFound:
            	message = "The specified file was not found: " + additional;
            	break;
            case downloadError:
            	message = "An error occured while downloading: " + additional;
            	break;
            case uploadError:
            	message = "An error occured while uploading: " + additional;
            	break;
            case unsupportedOS:
            	message = "Your operating system is unsupported: " + additional;
            	break;
            case bootstrapFail:
            	message = "Bootstrapping failure: " + additional;
            	break;
            default:
            	message = "An unknown error has occured: " + additional;
                break;
        }
		JOptionPane.showMessageDialog(null, message);
        System.exit(0);
	}
}