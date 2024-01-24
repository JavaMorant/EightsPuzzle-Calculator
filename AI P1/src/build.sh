# Compile  and build the project
javac *.java

echo "#!/bin/bash" > manhattan
echo "java manhattan < /dev/stdin" >> manhattan

echo "#!/bin/bash" > astar
echo "java astar < /dev/stdin" >> astar

# Make the files executable
chmod +x manhattan
chmod +x astar
