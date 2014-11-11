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
        size += 16 - size%16 if size%16 is not 0 else 0 
        data = s.recv(size)
	while len(data) < size:
	    data += s.recv(size-len(data))
        return data

    def construct(self, sock):
        debug = False
        if debug: print self
        self.state = self.rcvTypeOfCommand(sock)
        if debug: print self
        self.toIP = self.rcvIP(sock)
        if debug: print self
        self.returnIP = self.rcvIP(sock)
        if debug: print self
        self.length = self.rcvSizeOfCommand(sock)
        if debug: print self
        self.data = self.rcvData(sock,self.length)
        if debug: print self

    def padData(self):
        import os
        while len(self.data)%16 is not 0:
            self.data += '\x0a'

    def encryptData(self,key):
        from Crypto.Cipher import AES
        cipher = AES.new(key,AES.MODE_ECB)
        self.padData()
        self.data = cipher.encrypt(self.data)

    def decryptData(self,key):
        from Crypto.Cipher import AES
        cipher = AES.new(key,AES.MODE_ECB)
        self.data = cipher.decrypt(self.data)

    def RSAEncryptData(self,key):
        from Crypto.PublicKey import RSA
        from Crypto.Cipher import PKCS1_OAEP
        rsakey = RSA.importKey(key)
        rsakey = PKCS1_OAEP.new(rsakey)
        self.data = rsakey.encrypt(self.data)

    def RSADecryptData(self,key):
        from Crypto.PublicKey import RSA 
        from Crypto.Cipher import PKCS1_OAEP 
        from base64 import b64decode 
        rsakey = RSA.importKey(key) 
        rsakey = PKCS1_OAEP.new(rsakey) 
        self.data = rsakey.decrypt(b64decode(self.data)) 

    def send(self,sock):
        sock.send(str(self))
