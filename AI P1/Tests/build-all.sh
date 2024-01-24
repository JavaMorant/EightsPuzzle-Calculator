#!/bin/bash

find . -type f -name "*.class" -delete 

chmod a+x build.sh

./build.sh

chmod a+x astar >& /dev/null  # redirect error in case file doesn't exist
chmod a+x manhattan >& /dev/null  # redirect error in case file doesn't exist
chmod a+x price >& /dev/null  # redirect error in case file doesn't exist

exit 0

