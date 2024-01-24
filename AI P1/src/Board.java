import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int[][] puzzle; // The puzzle board represented as a 2D array
    private final int row; // Number of rows in the puzzle
    private final int col; // Number of columns in the puzzle
    private int manhattanDistance; // Manhattan distance of the puzzle
    private int movesMade;
    private ArrayList<Integer> moves; // List of numbers moved
    private int moveToMake; // Move to make

    // Constructor to initialize the board with the given puzzle
    public Board(int[][] puzzle, int manhattanDistance, int movesMade, ArrayList<Integer> moves, int moveToMake) { // numsmoved
        this.puzzle = puzzle;
        this.row = puzzle.length;
        this.col = puzzle[0].length;
        this.manhattanDistance = manhattanDistance;
        this.moves = moves;
        this.movesMade = movesMade;
        this.moveToMake = moveToMake;
    }

    // Get the value of the tile at the specified position (i, j)
    public int getTile(int i, int j) {
        return puzzle[i][j];
    }

    // Get the number of rows in the puzzle
    public int getRow() {
        return row;
    }

    // Get the number of columns in the puzzle
    public int getCol() {
        return col;
    }

    // Create a copy of the puzzle board ??
    public int[][] getPuzzle() {
        return this.puzzle;
    }

    // Get the number of moves made
    public int getMovesMade() {
        return this.movesMade;
    }

    // Get the Manhattan distance of the puzzle
    public int getManhattanDistance() {
        return this.manhattanDistance;
    }

    // Get the list of numbers moved
    public ArrayList<Integer> getMoves() {
        return this.moves;
    }

    // Get a string copy of the puzzle
    public String getGridString() {
        return Arrays.toString(Arrays.stream(puzzle).flatMapToInt(Arrays::stream).toArray());
    }

    // Set moves
    public void setMoves(ArrayList<Integer> moves) {
        this.moves = moves;
    }

    // Increment the number of moves made
    public void addMove() {
        this.movesMade++;
    }

    // Get the move to make
    public int getMoveToMake() {
        return this.moveToMake;
    }

    // Append the move made to a list of moves
    public void appendMove(int move) {
        if (this.moves == null) {
            this.moves = new ArrayList<>();
        }
        this.moves.add(move);
    }

    // Set the number of moves made
    public void setMovesMade(int movesMade) {
        this.movesMade = movesMade;
    }

    // Check if the puzzle is in the solved state
    public boolean isSolved(int[][] boardString) {
        if (calculateManhattanDistanceSum(boardString) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the neighboring board states, i.e. the states that can be reached by
     * moving the empty tile
     * @returns a list of neighboring boards
     */
    public ArrayList<Board> getNeighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();

        // Positions (Up, Down, Left, Right)
        int[] dx = { 0, 0, -1, 1 };
        int[] dy = { -1, 1, 0, 0 };

        // Find empty tile
        int i, j = 0;
        i = findEmptyTile(getPuzzle())[0];
        j = findEmptyTile(getPuzzle())[1];

        // Loop through the positions and add the neighbors to the list
        for (int k = 0; k < 4; k++) {
            int ni = i + dx[k];
            int nj = j + dy[k];

            // Check if the new position is within bounds
            if (ni >= 0 && ni < row && nj >= 0 && nj < col) {
                int[][] neighborPuzzle = new int[row][col];

                // Create a copy of the current state
                for (int x = 0; x < row; x++) {
                    for (int y = 0; y < col; y++) {
                        neighborPuzzle[x][y] = getPuzzle()[x][y];
                    }
                }
                // Swap the empty tile with the tile at the new position
                neighborPuzzle[i][j] = neighborPuzzle[ni][nj];
                moveToMake = neighborPuzzle[ni][nj];
                // Update moves in the neighbor board
                neighborPuzzle[ni][nj] = 0;
                // Create a new Board object for the neighbor
                Board neighborBoard = new Board(neighborPuzzle, calculateManhattanDistanceSum(neighborPuzzle),
                        0, getMoves(), moveToMake);

                neighborBoard.addMove(); // Increment the number of moves made

                // Add the neighbor to the list
                neighbors.add(neighborBoard);
            }
        }
        return neighbors;
    }

    // Find the empty tile in the puzzle
    public int[] findEmptyTile(int[][] puzzle) {
        int[] emptyTile = new int[2];
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[0].length; j++) {
                if (puzzle[i][j] == 0) {
                    emptyTile[0] = i;
                    emptyTile[1] = j;
                }

            }
        }
        return emptyTile;

    }

    /**
     * Calculates the total Manhattan distance sum for the puzzle
     * @param puzzle is the puzzle state to calculate the Manhattan distance sum for
     * @returns the total Manhattan distance sum as an integer
     */
    public int calculateManhattanDistanceSum(int[][] puzzle) {
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

}
