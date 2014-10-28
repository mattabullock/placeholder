import socket
import signal
import sys
import util

def signal_handler(signal, frame):
        print('You pressed Ctrl+C!')
        global s
        s.close()
        global f
        f.close()
        sys.exit(0)
signal.signal(signal.SIGINT, signal_handler)

f = open('/Users/matt/Desktop/jain8cr.jpg', 'rb')
fread = f.read()

TCP_IP = 'localhost'
TCP_PORT = 5715
BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))
while 1:
    size = util.getSizeOfCommand(s)
    print "size: " + str(size)
    data = s.recv(1024).rstrip()
    print "data: " + data
    if not data:
        break
    if data == "100":
        output = "hello"
    elif data == "143":
        output = "!" + str(len(fread))
        util.sendMessage(s,output)
        output = "!" + fread
        util.sendMessage(s,output)
        print "done sending"
    elif data == "144":
        output = "other thing"
    elif data == "145":
        output = "last thing"
    else:
        output = "i don't know that command"
s.close()
