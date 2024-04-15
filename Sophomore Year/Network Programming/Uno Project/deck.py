# -*- coding: utf-8 -*-
"""
Class to create and initalize an UNO deck
Created on Fri Oct 22 13:10:32 2021

@author: Fabio Tran
"""

from enums import Colors, Numbers, Types
from card import Card
import random 

# Class to create deck objects
class Deck():
    
    # Constructor to create deck objects
    def __init__(self,):
        # List to store cards 
        self.draw_pile = []
        # Tracks last card played
        self.last_played = None
        # Discard pile
        self.discard_pile = []
        
        # Creates all uno cards, 1 zero for each color, 2 of every number 1-9 
        # for each color, 2 of each action card for each color, and 4 of each 
        # type of wild cards
        for color in Colors:
            # creates wild cards
            if color.value == "Any":
                for type in Types:
                    if type.value == "Wild" or type.value == "Wild Draw-Four":
                        for x in range(4):
                            card = Card(color.value, "", type.value)
                            #print(card)
                            self.draw_pile.append(card)
                continue
            for number in Numbers:
                # creataes action cards
                if number.value == "":
                    for type in Types:
                        if type.value == "Skip" or type.value == "Reverse" or type.value == "Draw-Two":
                            for x in range(2):
                                card = Card(color.value, "", type.value)
                                #print(card)
                                self.draw_pile.append(card)   
                    continue
                # creates number cards
                if number.value == "Zero":
                    card = Card(color.value,number.value,"Number")
                    #print(card)
                    self.draw_pile.append(card)
                    continue
                for x in range(2):
                    card = Card(color.value,number.value,"Number")
                    #print(card)
                    self.draw_pile.append(card)
                      
    # Function to shuffle deck
    def shuffle_deck(self):
        random.shuffle(self.draw_pile)
    
    # Function to take a card from the draw pile 
    def draw(self):
        return self.draw_pile.pop()
    
    # Function to add to the discard pile
    def discard(self, card):
        self.discard_pile.append(card)
        self.last_played = self.discard_pile[-1]
        
    # Function to reshuffle discard pile into draw pile excluding last played card
    def reshuffle(self):
        for x in range(len(self.discard_pile) - 1):
                self.draw_pile.append(self.discard_pile[x])
        self.discard_pile.clear()
        self.discard_pile.append(self.last_played)
        random.shuffle(self.draw_pile)
    
# Testing creation of UNO deck
#print("Printing cards:")
#card_counter = 0
#uno_deck = Deck()

#uno_deck.shuffle_deck()
#for card in uno_deck.draw_pile:
#    card_counter += 1
#    print(card)
    
#print(str(card_counter) + " cards")
#print ("\n")

# Testing draw 
#print("Testing drawing:")
#print(uno_deck.draw())
#print(uno_deck.draw())
#print(str(len(uno_deck.draw_pile))+ " cards")
#print ("\n")

# Testing discard
#print("Testing discarding:")
#print("Drawing a card:")
#card = uno_deck.draw()
#print(str(len(uno_deck.draw_pile))+ " cards")
#print("Discarding " + str(card))
#uno_deck.discard(card)
#print("Discard pile contains " + str(uno_deck.discard_pile[0]))
#print ("\n")

# Checking last played 
#print("Last played:")
#print(uno_deck.last_played)
#print ("\n")

# Testing reshuffle 
#print("Discarding whole draw pile")
#for x in range(len(uno_deck.draw_pile)):
#   card = uno_deck.draw()
#   uno_deck.discard(card)
#   print(card)
#print(str(len(uno_deck.draw_pile))+ " cards")
#print ("\n")
#print("Reshuffling:")
#uno_deck.reshuffle()
#print("Printing draw pile")
#for card in uno_deck.draw_pile:
#    print(card)

#print ("\n")
#print(str(len(uno_deck.draw_pile))+ " cards")
#print("Last played:")
#print(uno_deck.last_played)
#print ("\n")
