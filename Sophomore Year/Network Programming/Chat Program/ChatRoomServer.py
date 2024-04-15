# -*- coding: utf-8 -*-
"""
Created on Tue Oct 12 19:12:17 2021

@author: Fabio Tran
"""

import socket

s = socket.socket()
host_name = socket.gethostname()
server_ip = socket.gethostbyname(host_name)
 
port = 9999
 
s.bind((host_name, port))
print("Binding successful!")
print("This is your IP: ", server_ip)
 
name = input('Enter name: ')
 
s.listen(1) 
 
conn, add = s.accept()
 
print("Received connection from ", add[0])
print('Connection Established. Connected From: ',add[0])
 
client = (conn.recv(1024)).decode()
print(client + ' has connected.')
 
conn.send(name.encode())
while True:
    message = input('Me : ')
    conn.send(message.encode())
    message = conn.recv(1024)
    message = message.decode()
    if message == 'exit' :
        break;
    print(client, ':', message)
    
s.close()