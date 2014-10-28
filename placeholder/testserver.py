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
    print inp
    client.send(inp)
    for i in range(0,1):
        size = util.getSizeOfCommand(client)
        data = client.recv(size)
        f.write(data)
        print data 

    client.close()