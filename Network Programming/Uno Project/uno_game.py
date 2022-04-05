# -*- coding: utf-8 -*-
"""
Contains game logic for UNO
Created on Mon Nov 22 17:21:27 2021

@author: Fabio Tran
"""

from deck import Deck
from player import Player
from uno_server import *
import Stack

class Game():
    # constructor
    def __init__(self):
        # tracks who's player's turn it is 
        self.current_player
        
        # creates and shuffles deck
        self.deck = Deck()
        self.deck.shuffle_deck()
        
        # deals cards to each player
        for x in range(7):
            self.players[0].hand.put(self.deck.draw())
            self.players[1].hand.put(self.deck.draw())
            self.players[2].hand.put(self.deck.draw())
            self.players[3].hand.put(self.deck.draw())
            
    # runs rounds of uno until there is a winner 
    def run_game(self):
        winner = False
        
        # continues while winner hasn't been determined
        while not winner:
            # checks for a winner 
            winner = self.check_winner()
            
            # if no winner then proceed with round
            self.process_card()
            
        # TODO once the while loop ends, send a message to server to end game
    
    
    # receives card from player, if player can't send a card, make them draw 
    def receive_cards(self):
        None
    
    # processes events 
    def process_card(self):
        current_card = self.deck.last_played()
        type = current_card.type 
        color = current_card.color
        
        if type == 'Draw-Two':
            for x in range(2):
               None # TODO
        elif type == 'Reverse':
            self.reverse_order()
        elif type == 'Skip':
            current_player = (self.current_player + 2) % len(self.players)
        elif type == 'Wild 4':
            None # TODO 
            
        # updates current player to the next once this sequence is over
        current_player = (self.current_player + 1) % len(players)

        return 
        
    # checks to see if there is a winner 
    def check_winner(self):
        winner = False
        
        # checks if a player has no cards left
        for player in players:
            if player.hand.size() == 0:
                winner = True
                
        return winner
    
    # used in event of a skip card which will reverse the order of turns 
    def reverse_order(self):
        # used to temporarily store players in order to reverse order 
        temp_stack = []
    
        # moves every player from queue to a stack
        while(not players.empty()):
            temp_stack.append(players.get())
        # returns every player from stack to the queue now in reversed order
        while(not temp_stack.empty()):
            players.put(temp_stack.pop())
        
    # TODO sends data to server
    def send_data(self):
        data = str("")
        reply = self.net.send(data)
        return reply
    
    # TODO receives data from server
    def receive_data(self):
        message = s.recv(50).decode()
        print(message)
        
    
    
