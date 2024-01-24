#!/bin/bash

# When we only care that we want our program to tell us if the instance is solvable or not

./manhattan < /dev/stdin | head -1 | awk '{print $1}'
