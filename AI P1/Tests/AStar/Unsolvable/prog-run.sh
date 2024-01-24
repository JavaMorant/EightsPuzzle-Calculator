#!/bin/bash


# When we only care that we want our program to tell us if the instance is solvable or not

# Note that for unsolvable answer has to be 0, -1 will not be accepted

./astar < /dev/stdin | head -1 | awk '{print $1}' 
