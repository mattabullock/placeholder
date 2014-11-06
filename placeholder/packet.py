class Packet:

    def __init__(self):
        self.toIP = ""
        self.returnIP = ""
        self.state = ""
        self.length = ""
        self.data = ""

    def __str__(self):
        return "!" + str(self.state) + "!" + self.toIP + "!" + self.returnIP \
            + "!" + str(self.length) + "!" + self.data

    def rcvSizeOfCommand(self,s):
        sizearray = []
        size = 1
        data = None
        while data != "!":
            data = s.recv(size)
            if data != "!":
                sizearray.append(data)
        size = 0
        for i in range(0,len(sizearray)):
            size += int(sizearray[i]) * pow(10,len(sizearray)-i-1)
        return size

    def rcvIP(self,s):
        iparray = []
        size = 1
        data = None
        while data != "!":
            data = s.recv(size)
            if data != "!":
                iparray.append(data)
        return "".join(iparray)

    def rcvTypeOfCommand(self,s):
        data = s.recv(1)
        if data == "!":
            data = None
            data = s.recv(3)
            exc = s.recv(1)
            if exc == "!":
                return data

    def rcvData(self,s,size):
        if size is 0:
            return ""
        data = s.recv(size)
	while len(data) < size:
	    data += s.recv(size-len(data))
        return data

    def construct(self, sock):
        self.state = self.rcvTypeOfCommand(sock)
        self.toIP = self.rcvIP(sock)
        self.returnIP = self.rcvIP(sock)
        self.length = self.rcvSizeOfCommand(sock)
        self.data = self.rcvData(sock,self.length)

    def send(self,sock):
        sock.send(str(self))
