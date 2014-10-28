def getSizeOfCommand(s):
    sizearray = []
    size = 1
    data = s.recv(1)
    if data == "!":
        data = None
        while data != "!":
            data = s.recv(size)
            print "getSize: " + str(data) 
            if data != "!" and data != "\n":
                sizearray.append(data)
    print str(sizearray)
    size = 0
    for i in range(0,len(sizearray)):
        size += int(sizearray[i]) * pow(10,len(sizearray)-i-1)
    print size
    return size

def sendMessage(sock,msg):
    sock.send(msg+"\n")