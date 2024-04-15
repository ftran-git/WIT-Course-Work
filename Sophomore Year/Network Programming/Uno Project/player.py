# -*- coding: utf-8 -*-
"""
Class to create player objects

Created on Wed Oct 27 10:46:49 2021

@author: Fabio Tran
"""

# Class to create player objects
class Player():
    
    def __init__(self, ip_address, name):
        self.hand = []
        self.ip_address = ip_address
        self.name = name 
        
    def add_card(self, card):
        self.hand.append(card)
        
    def __str__(self):
        result = ""
        for card in self.hand:
            result += self.hand[card]
        return result
            


