# -*- coding: utf-8 -*-
"""
Created on Fri Mar 18 16:45:36 2022

BFS algorithm using adjacency list 

@author: Fabio Tran
"""

import collections

# Bfs algorithm
def bfs(graph, start):

    # Creating queue 
    visited, queue = set(), collections.deque([start])
    visited.add(start)

    # While loop to traverse the graph using BFS 
    while queue:

        # Dequeues a vertex
        vertex = queue.popleft()
        print(str(vertex) + " ", end = "")

        # If not visited enqueue it and mark as visited
        for neighbour in graph[vertex]:
            if neighbour not in visited:
                visited.add(neighbour)
                queue.append(neighbour)

# Creating a graph using adjacency list
graph = {1:[4], 2:[4,5], 3:[5], 4:[1,2,5], 5:[3,2,4]}

# Testing bfs algorithm
start = 1
print("Breadth First Traversal Starting at " + str(start) + ": ")
bfs(graph, start)
    
