# -*- coding: utf-8 -*-
"""
Created on Mon Nov 22 00:35:02 2021

@author: Peter Garrity
"""

import socket
import player
import queue

def host_players(numplayers, socket_, players):
    """"Waits for specified number of players to join and fills players with the player info"""
    connections = 0

    while(connections != numplayers):
        try:
            sc, address = socket_.accept()
            request = sc.recv(50).decode().splitlines()
            if request[0] == "CONNECT":
                username = "User" + str(connections)
                for line in request:
                    header = line.split(' ')
                    if(header[0] == "Username:"):
                        username = header[1]

                connections += 1
                usr = player.Player((sc, address), username)
                players.put(usr)
                print("Added " + username)
                sc.sendall(b"ACCEPTED")
        except socket.error as msg:
            print('Connection failed: ' + str(msg[0]) + ' Message ' + msg[1])
            sc.close()

HOST = ''
PORT = 11111

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print('Socket created')

s.bind((HOST, PORT))
print('Socket bound')
s.listen(10)

players = queue.Queue()
host_players(2, s, players)
while(not players.empty()):
    usr = players.get()
    connection = usr.ip_address[0]
    connection.sendall(b"Thanks for joining! The game is starting.")
# TODO: Implement game.
s.close()