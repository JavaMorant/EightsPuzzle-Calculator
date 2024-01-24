#!/bin/bash

# When we know what the optimal number of moves is but don't care about the solution
# Appropriate if there are multiple optimal solutions

./astar < /dev/stdin | head -1 | awk '{print $1 " " $2}'
