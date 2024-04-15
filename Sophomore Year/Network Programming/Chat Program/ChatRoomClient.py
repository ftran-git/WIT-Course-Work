# -*- coding: utf-8 -*-
"""
Created on Tue Oct 12 19:13:21 2021

@author: Fabio Tran
"""

import socket
 
s = socket.socket()
server_host = socket.gethostname()
ip = socket.gethostbyname(server_host)

port = 9999
 
print('This is your IP address: ', ip)
server_host = input('Enter friend\'s IP address:')
name = input('Enter Friend\'s name: ')
 
s.connect((server_host, port))
 
s.send(name.encode())
server_name = s.recv(1024)
server_name = server_name.decode()
 
print(server_name,' has joined...')
while True:
    message = (s.recv(1024)).decode()
    print(server_name, ":", message)
    message = input("Me : ")
    if message == 'exit' :
        message = "DONE"
        s.send(message.encode())
        break;
    s.send(message.encode())   
    
print("DONE")
s.close()