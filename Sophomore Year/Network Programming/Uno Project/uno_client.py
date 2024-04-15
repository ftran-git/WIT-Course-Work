# -*- coding: utf-8 -*-
"""
Created on Tue Oct 12 16:57:12 2021
"""

import socket
import sys

PORT = 11111

HOST = input("Input the server address: ")
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST, PORT))

username = input("Enter your username: ")
username = "Username: " + username if len(username) > 0 else ""
s.sendall(("CONNECT\n" + username).encode())
message = s.recv(50).decode()   
if(message.splitlines()[0] != "ACCEPTED"):
    print("Connection refused: " + message)
    sys.exit()

print(message)
message = s.recv(50).decode()
print(message)
    
s.close()

def send_card():
        
def receive_card():