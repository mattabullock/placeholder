class Packet:

    def __init__(self,returnip="",state="",length="",data=""):
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
        return s.recv(size)

    def construct(self, sock):
        self.state = self.rcvTypeOfCommand(sock)
        print self
        self.toIP = self.rcvIP(sock)
        print self
        self.returnIP = self.rcvIP(sock)
        print self
        self.length = self.rcvSizeOfCommand(sock)
        print self
        self.data = self.rcvData(sock,self.length)
        print len(self)

    def send(self,sock):
        sock.send(str(self))