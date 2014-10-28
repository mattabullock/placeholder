import socket
import util
import sys
import signal

def signal_handler(signal, frame):
        print('You pressed Ctrl+C!')
        global s
        s.close()
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

f = open('/Users/matt/Desktop/pic.jpg','w')

host = '' 
port = 5715 
backlog = 5
size = 1
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((host,port))
s.listen(backlog)
while 1:
    client, address = s.accept()
    inp = raw_input("Enter command: ")
    inp = "!" + str(len(inp)) + "!" + inp
    client.send(inp)
    for i in range(0,1):
        size = getSizeOfCommand(client)
        data = client.recv(size)
        f.write(data)
        f.close()
