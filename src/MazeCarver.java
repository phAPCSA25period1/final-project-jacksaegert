import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generates mazes using a recursive backtracking (Depth-First Search) algorithm.
 * <p>
 * This class carves paths through a {@link Grid} by randomly selecting directions
 * and recursively visiting unvisited cells. It ensures that walls remain between
 * paths by jumping two cells at a time during carving.
 */
public class MazeCarver {

    /** Tracks which cells have already been visited during maze generation. */
    private boolean[][] visited;

    /**
     * Carves a maze into the provided grid starting from position (1, 1).
     * <p>
     * After carving, the bottom-right inner cell is marked as the target.
     *
     * @param grid the {@link Grid} to carve the maze into
     */
    public void carveMaze(Grid grid) {
        int rows = grid.getRows();
        int cols = grid.getCols();

        // Initialize visited array to prevent revisiting cells during DFS.
        visited = new boolean[rows][cols];

        // Begin recursive carving from the starting cell.
        carveFrom(grid, 1, 1);

        // Mark the target cell near the bottom-right corner.
        grid.getTile(rows - 2, cols - 2).setTileType(TileType.TARGET);
    }

    /**
     * Recursively carves paths from the specified cell using DFS.
     * <p>
     * For each direction, it checks two cells ahead. If that cell is valid and unvisited,
     * it removes the wall between the current cell and the next cell, then recurses.
     * This method exploits the call stack for backtracking when no valid neighbors remain.
     *
     * @param grid the {@link Grid} being carved
     * @param row  the current row index
     * @param col  the current column index
     */
    private void carveFrom(Grid grid, int row, int col) {
        // Mark the current cell as visited and set it to a path.
        visited[row][col] = true;
        grid.getTile(row, col).setTileType(TileType.PATH);

        // Get a shuffled list of directions to ensure random maze generation.
        List<int[]> directions = getShuffledDirections();

        // Explore each direction. The recursion naturally backtracks via the call stack
        // when a dead end is reached (no unvisited neighbors).
        for (int[] direction : directions) {
            // Calculate the position two cells away in the chosen direction.
            int nextRow = row + direction[0] * 2;
            int nextCol = col + direction[1] * 2;

            // Proceed only if the target cell is within bounds and unvisited.
            if (isValidCell(nextRow, nextCol, grid.getRows(), grid.getCols()) && !visited[nextRow][nextCol]) {

                // Determine the wall cell between the current position and the next position.
                int wallRow = row + direction[0];
                int wallCol = col + direction[1];

                // Remove the wall by converting it to a path.
                grid.getTile(wallRow, wallCol).setTileType(TileType.PATH);

                // Recursively carve from the next cell.
                carveFrom(grid, nextRow, nextCol);
            }
        }
    }

    /**
     * Checks if the specified cell is within the bounds of the grid.
     *
     * @param targetRow the row index to check
     * @param targetCol the column index to check
     * @param rows      the total number of rows in the grid
     * @param cols      the total number of columns in the grid
     * @return true if the cell is inside the grid boundaries, false otherwise
     */
    private boolean isValidCell(int targetRow, int targetCol, int rows, int cols) {
        return targetRow > 0 && targetRow < rows && targetCol > 0 && targetCol < cols;
    }

    /**
     * Returns a shuffled list of the four cardinal direction deltas.
     * <p>
     * Each direction is represented as an int array of size 2: {rowDelta, colDelta}.
     * Shuffling ensures randomized maze generation.
     *
     * @return a list of shuffled direction arrays
     */
    private List<int[]> getShuffledDirections() {
        List<int[]> directions = new ArrayList<>();

        // Add the four cardinal directions.
        directions.add(new int[]{-1, 0}); // Up
        directions.add(new int[]{1, 0});  // Down
        directions.add(new int[]{0, -1}); // Left
        directions.add(new int[]{0, 1});  // Right

        // Randomize the order to vary maze structure each time.
        Collections.shuffle(directions);

        return directions;
    }
}

