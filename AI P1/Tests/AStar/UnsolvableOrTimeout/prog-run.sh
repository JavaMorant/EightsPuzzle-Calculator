#!/bin/bash

# When we accept an unknown answer for unsolvable instances

# Note that runs killed by stacscheck will be regarded as fails 

./astar < /dev/stdin | head -1 | awk '{print $1}' | sed 's/-1/0/'
