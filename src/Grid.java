/**
 * Represents a 2D grid of {@link Tile} objects used for maze generation and gameplay.
 * <p>
 * The grid automatically adjusts dimensions to be odd numbers to ensure
 * proper maze generation via recursive backtracking algorithms.
 */
public class Grid {

    /** 2D array storing the tiles of the grid. */
    private final Tile[][] grid;

    /**
     * Constructs a new Grid with the specified number of rows and columns.
     * <p>
     * If either dimension is even, it is incremented by one to ensure odd dimensions,
     * which are required for proper maze generation.
     *
     * @param rows the desired number of rows (will be made odd if even)
     * @param cols the desired number of columns (will be made odd if even)
     */
    public Grid(int rows, int cols) {
        // Ensure odd dimensions for DFS maze generation.
        if (rows % 2 == 0) {
            rows++;
        }
        if (cols % 2 == 0) {
            cols++;
        }

        // Initialize the grid and populate it with default Wall tiles.
        grid = new Tile[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Tile();
            }
        }
    }

    /**
     * Retrieves the tile at the specified row and column.
     *
     * @param row the row index of the tile
     * @param col the column index of the tile
     * @return the {@link Tile} at the given position
     */
    public Tile getTile(int row, int col) {
        return this.grid[row][col];
    }

    /**
     * Returns the number of rows in the grid.
     *
     * @return the total number of rows
     */
    public int getRows() {
        return this.grid.length;
    }

    /**
     * Returns the number of columns in the grid.
     *
     * @return the total number of columns
     */
    public int getCols() {
        return this.grid[0].length;
    }

    /**
     * Replaces the tile at the specified position with a new tile.
     *
     * @param row     the row index of the tile to replace
     * @param col     the column index of the tile to replace
     * @param newTile the new {@link Tile} to place at the specified position
     */
    public void setTile(int row, int col, Tile newTile) {
        this.grid[row][col] = newTile;
    }
}

