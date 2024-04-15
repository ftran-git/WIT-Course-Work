# -*- coding: utf-8 -*-
"""
Class to create card objects
Created on Fri Oct 22 12:46:28 2021

@author: Fabio Tran
"""

# Class to create card objects
class Card:
    
    # Constructor
    def __init__(self, color, number, type):
        self.color = color
        self.number = number
        self.type = type
        self.wild_color = None
        
    # Sets color of wild card once it is played 
    def set_wild_color(self, color):
        self.wild_color = color

     # Used to compare color or number between two cards, self is the last card
     # played and other_card is the next card attempting to be played 
    def match_card(self, other_card):
        # checks if other card is a wild card
        if(other_card.color == "Any"):
            return True
        # checks if colors match
        if other_card.color == self.color or other_card.color == self.wild_color:
            return True
        # checks if numbers match
        if other_card.type == "Number" and self.type == "Number":
            if other_card.number == self.number:
                return True
        # checks if action matches
        if other_card.type == self.type:
            return True
        # otherwise no match
        return False
        
    # Formatted string to print to console 
    def __str__(self):
        # if card is a wild card
        if self.color == "Any":
            return str(self.type)
        # if card is an action card 
        if self.number == "":
            return str(self.color) + " " + str(self.type)
        # otherwise card is a number card
        return str(self.color) + " " + str(self.number)

# Testing creation of card objects
#print("Testing constructor and string return:")
#blue_one = Card("Blue","One","Number")
#print(blue_one)
#wild_card = Card("Any", "None", "Wild")
#print(wild_card)
#yellow_reverse = Card("Yellow", "None", "Reverse")
#print(yellow_reverse)
#blue_reverse = Card("Blue", "None", "Reverse")
#print(blue_reverse)
#print("\n")

# Testing def match_card
#print("Testing def match_card:")
#print(blue_one.match_card(wild_card))
#print(blue_one.match_card(yellow_reverse))
#print(yellow_reverse.match_card(blue_reverse))
#print(blue_reverse.match_card(blue_one))
#print("\n")

# Testing setting wild color
#print("Testing set_wild_color:")
#wild_card.set_wild_color("Blue")
#print(wild_card.match_card(blue_one))
#print(wild_card.match_card(yellow_reverse))



    
        
