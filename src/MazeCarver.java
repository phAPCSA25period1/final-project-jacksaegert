import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MazeCarver {
    // Instance Vars
    private boolean[][] visited;
    private Random random = new Random();

    // --- Difficulty Settings ---
    // Controls how many extra passages are punched through walls after carving.
    // Higher = more loops, more paths to any point, harder to solve.
    // 0.0 = perfect maze (no loops), 0.15 = light looping, 0.30+ = very loopy
    private static final double EXTRA_PASSAGE_RATIO = 0.01;

    // Controls how strongly the carver prefers to continue in its current direction
    // rather than turning. Higher = longer straight/winding runs, fewer sharp turns.
    // 1 = no bias (original behavior), 3 = moderate bias, 6+ = very biased
    private static final int DIRECTION_CONTINUATION_WEIGHT = 4;

    public void carveMaze(Grid grid) {
        int rows = grid.getRows();
        int cols = grid.getCols();

        visited = new boolean[rows][cols];

        // Pass null as the initial "last direction" since we have no prior direction yet
        carveFrom(grid, 1, 1, null);
        grid.getTile(rows - 2, cols - 2).setTileType(TileType.TARGET);

        // After the perfect maze is carved, punch extra holes to create loops
        addExtraPassages(grid, rows, cols);
    }

    private void carveFrom(Grid grid, int row, int col, int[] lastDirection) {
        visited[row][col] = true;
        grid.getTile(row, col).setTileType(TileType.PATH);

        // Get directions biased toward continuing in lastDirection
        List<int[]> directions = getBiasedDirections(lastDirection);

        for (int[] direction : directions) {
            int nextRow = row + direction[0] * 2;
            int nextCol = col + direction[1] * 2;

            if (isValidCell(nextRow, nextCol, grid.getRows(), grid.getCols()) && !visited[nextRow][nextCol]) {
                int wallRow = row + direction[0];
                int wallCol = col + direction[1];

                grid.getTile(wallRow, wallCol).setTileType(TileType.PATH);

                // Pass the current direction forward so the next call can bias toward it
                carveFrom(grid, nextRow, nextCol, direction);
            }
        }
    }

    /**
     * After the maze is fully carved, randomly removes interior walls that sit
     * between two PATH cells. Each removed wall creates a new loop, giving the
     * maze multiple valid routes and making it much harder to solve at a glance.
     */
    private void addExtraPassages(Grid grid, int rows, int cols) {
        // Collect all interior wall tiles that separate two path cells
        List<int[]> candidateWalls = new ArrayList<>();

        // Only check odd-coordinate walls (the ones placed between carved cells)
        for (int r = 1; r < rows - 1; r++) {
            for (int c = 1; c < cols - 1; c++) {
                if (grid.getTile(r, c).getTileType() != TileType.PATH) {
                    // A wall is a valid candidate if it has PATH neighbors on both sides
                    // along either axis (i.e., it's a wall between two open cells)
                    boolean horizontalBridge =
                        c - 1 >= 0 && c + 1 < cols &&
                        grid.getTile(r, c - 1).getTileType() == TileType.PATH &&
                        grid.getTile(r, c + 1).getTileType() == TileType.PATH;

                    boolean verticalBridge =
                        r - 1 >= 0 && r + 1 < rows &&
                        grid.getTile(r - 1, c).getTileType() == TileType.PATH &&
                        grid.getTile(r + 1, c).getTileType() == TileType.PATH;

                    if (horizontalBridge || verticalBridge) {
                        candidateWalls.add(new int[]{r, c});
                    }
                }
            }
        }

        // Shuffle so we pick random walls, not always the top-left ones
        Collections.shuffle(candidateWalls, random);

        // Remove a percentage of them based on EXTRA_PASSAGE_RATIO
        int toRemove = (int) (candidateWalls.size() * EXTRA_PASSAGE_RATIO);
        for (int i = 0; i < toRemove; i++) {
            int[] wall = candidateWalls.get(i);
            grid.getTile(wall[0], wall[1]).setTileType(TileType.PATH);
        }
    }

    /**
     * Returns a shuffled direction list that is weighted to favor continuing
     * in lastDirection. The current direction is added multiple times so it
     * appears more often when the list is shuffled, making the carver more
     * likely to keep going straight (or slightly curved) rather than turning.
     */
    private List<int[]> getBiasedDirections(int[] lastDirection) {
        List<int[]> directions = new ArrayList<>();

        directions.add(new int[]{-1, 0});
        directions.add(new int[]{1, 0});
        directions.add(new int[]{0, -1});
        directions.add(new int[]{0, 1});

        // If we have a prior direction, add it extra times to weight it more heavily
        if (lastDirection != null) {
            for (int i = 0; i < DIRECTION_CONTINUATION_WEIGHT; i++) {
                directions.add(new int[]{lastDirection[0], lastDirection[1]});
            }
        }

        Collections.shuffle(directions, random);
        return directions;
    }

    private boolean isValidCell(int targetrow, int targetcol, int rows, int cols) {
        return targetrow > 0 && targetrow < rows && targetcol > 0 && targetcol < cols;
    }
}
