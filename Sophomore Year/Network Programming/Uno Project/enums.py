# -*- coding: utf-8 -*-
"""
Enums for card colors, numbers, and types

Created on Sun Nov 21 14:56:07 2021

@author: Fabio Tran
"""
from enum import Enum

# Enum for card colors 
class Colors(Enum):
    RED = "Red"
    YELLOW = "Yellow"
    GREEN = "Green"
    BLUE = "Blue"
    ANY = "Any"
    
# Enum for card numbers 
class Numbers(Enum):
    ZERO = ("Zero")
    ONE = ("One")
    TWO = ("Two")
    THREE = ("Three")
    FOUR = ("Four")
    FIVE = ("Five")
    SIX = ("Six")
    SEVEN = ("Seven")
    EIGHT = ("Eight")
    NINE = ("Nine")
    NONE = ("")
   
# Enum for action card types 
class Types(Enum):
    NUMBER = ("Number")
    DRAW2 = ("Draw-Two")
    REVERSE = ("Reverse")
    SKIP = ("Skip")
    WILD = ("Wild")
    WILD4 =("Wild Draw-Four")