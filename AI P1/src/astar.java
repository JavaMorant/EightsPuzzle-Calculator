/*
 * Author: 210021783
 */

import java.util.*;

public class astar {
    private static Board initState;
    private static Board goalState;
    private static int moves = 0;
    private static ArrayList<Integer> movesMade;

    public static void main(String[] args) {
       // Take input
       Scanner input = new Scanner(System.in);
       String initStateString = input.nextLine();
       long startTime = System.currentTimeMillis(); // Record the start time


        input.close();

        // Create StringTokenizer to store user input
        StringTokenizer userInput = new StringTokenizer(initStateString, " ");

        // Validate sizes
        if (userInput.countTokens() < 3) {
            System.out.println("-1 (Too few arguments)");
            System.exit(0);
        }

        // Validate Row and Column sizes
        String n = userInput.nextToken();
        String m = userInput.nextToken();

        int row = 0;
        int col = 0;

        // Try parse the row and column sizes
        try {
            row = Integer.parseInt(n);
            col = Integer.parseInt(m);
        } catch (Exception e) {
            System.out.println("-1");
            System.exit(0);
        }

        if (row < 1 || col < 1) {
            System.out.println("-1");
            System.exit(0);
        }

        // Validate number of tiles
        if ((row * col) != userInput.countTokens()) {
            System.out.println("-1");
            System.exit(0);
        }

        // Create 2D array to store initial state
        int[][] initStateArray = new int[row][col];

        // Validate input and store in 2D array
        boolean isZero = false;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (userInput.hasMoreTokens()) {
                    String token = userInput.nextToken();
                    try {
                        Integer.parseInt(token);
                    } catch (Exception e) {
                        System.out.println("-1");
                        System.exit(0);
                    }
                    initStateArray[i][j] = Integer.parseInt(token);
                    if (initStateArray[i][j] == 0) {
                        isZero = true;
                    }
                    
                    if (initStateArray[i][j] < 0 || initStateArray[i][j] > (row * col) - 1) {
                        System.out.println("-1");
                        System.exit(0);
                    }
                } else {
                    // Handle the case where there are not enough tokens in the input
                    System.out.println("-1");
                    System.exit(0);
                }
            }
        }
        // Check if the puzzle contains a zero
        if (!isZero) {
            System.out.println("-1");
            System.exit(0);
        }
        movesMade = new ArrayList<>();

        // Create Board objects
        initState = new Board(initStateArray, calculateManhattanDistanceSum(initStateArray), 0, movesMade, -1);
        goalState = createGoalState(row, col);

        // Check if solvable
        if (!isSolvable()) {
            System.out.println("0");
            System.exit(0);
        }

        // Compare initial state to goal state and check if already sorted
        if (isSolved()) {
            System.out.println("1 " + moves);
            return;
        }
        // Create a priority queue for the astar algorithm to store the states
        PriorityQueue<Board> priorityQueue = new PriorityQueue<>(
                (a, b) -> (a.getManhattanDistance() + a.getMovesMade())
                        - (b.getManhattanDistance() + b.getMovesMade()));

        // Create a set to store visited states
        Set<String> visited = new HashSet<>();

        // Add the initial state to the priority queue
        priorityQueue.offer(initState);

        // Previous state
        int[][] previousState = initState.getPuzzle();
        int movedNumber = -1;

        // Main loop
        while (!priorityQueue.isEmpty()) {
            Board currentState = priorityQueue.poll();

            int moves = 0;

            if (currentState.getMoveToMake() != -1) {
                moves = currentState.getMovesMade() + 1;
                movesMade.add(currentState.getMoveToMake());
            }

            // Increment the number of moves made
            // Check if the state has already been visited
            if (visited.contains(currentState.getGridString())) {
                continue;
            }
            // Add the state to the visited set
            visited.add(currentState.getGridString());

            // Get the state with the lowest Manhattan distance sum
            previousState = currentState.getPuzzle();

            // Check if the goal state is reached
            if (currentState.isSolved(currentState.getPuzzle())) {
                System.out.println("1 " + currentState.getMovesMade());
                long endTime = System.currentTimeMillis(); // Record the end time
                long executionTime = endTime - startTime; // Calculate the execution time in milliseconds
                System.out.println("Execution Time: " + executionTime + " milliseconds");
                return;
            }

            // Get neighboring boards
            ArrayList<Board> neighborBoards = currentState.getNeighbors();

            // Loop through neighboring boards
            for (Board neighbor : neighborBoards) {
                // Set the number of moves for the neighbor
                neighbor.setMovesMade(moves);
                neighbor.appendMove(findMovedNumber(previousState, neighbor.getPuzzle()));

                // Add the neighbor to the priority queue
                priorityQueue.add(neighbor);
            }
            long currentTime = System.currentTimeMillis(); // Record the current time
            if (currentTime - startTime > 60000) { // Check if the execution time is greater than 1 minute
                System.out.println("-1");
                System.exit(0);
            }
        }

    }

    /**
     * Checks if a move was made by comparing the current board to the previous
     * puzzle state
     * 
     * @param currBoard  is the current board
     * @param prevPuzzle is the previous puzzle
     * @returns true if a move was made, false otherwise
     */
    private static boolean checkIfMoveMade(Board currBoard, int[][] prevPuzzle) {
        int[][] currPuzzle = currBoard.getPuzzle();
        if (!Arrays.deepEquals(currPuzzle, prevPuzzle)) {
            int x = currBoard.findEmptyTile(currPuzzle)[0];
            int y = currBoard.findEmptyTile(currPuzzle)[1];

            movesMade.add(prevPuzzle[x][y]);

            // System.out.println("Moved: " + prevPuzzle[x][y]);
            return true;
        }

        return false;
    }

    /**
     * Finds the number that was moved
     * 
     * @param previousState is the previous state (root node)
     * @param currentState  is the current state (child node)
     * @returns the number that was moved
     */
    private static int findMovedNumber(int[][] previousState, int[][] currentState) {
        for (int i = 0; i < previousState.length; i++) {
            for (int j = 0; j < previousState[0].length; j++) {
                if (previousState[i][j] != currentState[i][j]) {
                    return previousState[i][j];
                }
            }
        }
        return -1; // No number was moved
    }

    /**
     * Calculates the total Manhattan distance sum for the puzzle
     * 
     * @param puzzle is the puzzle to calculate the Manhattan distance sum for
     * @returns the total Manhattan distance sum as an integer
     */
    private static int calculateManhattanDistanceSum(int[][] puzzle) {
        int sum = 0;
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[0].length; j++) {
                int value = puzzle[i][j];
                if (value != 0) { // Exclude the empty tile
                    int goalX = (value - 1) / puzzle.length; // Calculate the expected row for the tile
                    int goalY = (value - 1) % puzzle[0].length; // Calculate the expected column for the tile
                    sum += Math.abs(goalX - i) + Math.abs(goalY - j);
                }
            }
        }
        return sum;
    }

    /**
     * Checks if the puzzle is solvable by counting the number of inversions
     * 
     * @returns true if the puzzle is solvable, false otherwise
     */
    private static boolean isSolvable() {
        int inversions = 0;
        int totalTiles = initState.getRow() * initState.getCol();

        for (int i = 0; i < totalTiles; i++) {
            for (int j = i + 1; j < totalTiles; j++) {
                int value1 = initState.getTile(i / initState.getCol(), i % initState.getCol());
                int value2 = initState.getTile(j / initState.getCol(), j % initState.getCol());

                if (value1 != 0 && value2 != 0 && value1 > value2) {
                    inversions++;
                }
            }
        }
        return (inversions % 2 == 0);
    }

    /**
     * Checks if the puzzle is solved by looping through the puzzle and comparing
     * each tile to the goal state
     * 
     * @returns true if the puzzle is solved, false otherwise
     */
    private static boolean isSolved() {
        for (int i = 0; i < initState.getPuzzle().length; i++) {
            for (int j = 0; j < initState.getPuzzle()[0].length; j++) {
                if (initState.getPuzzle()[i][j] != goalState.getPuzzle()[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Create the goal state
     * 
     * @param row is the length of the row
     * @param col is the length of the column
     * @returns the goal state as a Board object
     */
    private static Board createGoalState(int row, int col) {
        int[][] goalStateArray = new int[row][col];
        int value = 1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i == row - 1 && j == col - 1) {
                    goalStateArray[i][j] = 0; // Last tile should be empty
                } else {
                    goalStateArray[i][j] = value++;
                }
            }
        }
        return new Board(goalStateArray, 0, 0, null, -1);
    }
}
