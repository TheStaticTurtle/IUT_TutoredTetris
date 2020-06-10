import socket

HOST = ''                 # Symbolic name meaning all available interfaces
PORT = 55553              # Arbitrary non-privileged port  must be > 1023
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(1)
while 1:
	conn, addr = s.accept()
	print 'Connected by', addr
	while 1:
		data = conn.recv(1024)
		print data
		if not data: break
		conn.send(data)
	conn.close()
