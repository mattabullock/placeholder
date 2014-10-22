import socket
import signal
import sys
def signal_handler(signal, frame):
        print('You pressed Ctrl+C!')
        sys.exit(0)
signal.signal(signal.SIGINT, signal_handler)

TCP_IP = 'localhost'
TCP_PORT = 5715
BUFFER_SIZE = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))
while 1:
    data = s.recv(BUFFER_SIZE).rstrip()
    if not data: break
    if data == "100":
        output = "hello"
    elif data == "143":
        output = "screenshot"
    elif data == "144":
        output = "other thing"
    elif data == "145":
        output = "last thing"
    else:
        output = "i don't know that command"
    s.send(output+"\n")  
s.close()
