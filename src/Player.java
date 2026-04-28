/**
 * Represents the player in the maze game.
 * <p>
 * Tracks the player's current position and provides methods for movement
 * in the four cardinal directions, as well as win condition checking.
 */
public class Player {

    /** The current row position of the player in the grid. */
    private int row;

    /** The current column position of the player in the grid. */
    private int col;

    /** Reference to the {@link Grid} the player is navigating. */
    private Grid grid;

    /**
     * Constructs a new Player at the specified starting position on the given grid.
     *
     * @param grid     the {@link Grid} the player will move on
     * @param startRow the starting row index
     * @param startCol the starting column index
     */
    public Player(Grid grid, int startRow, int startCol) {
        this.grid = grid;
        this.row = startRow;
        this.col = startCol;
    }

    /**
     * Returns the player's current row.
     *
     * @return the current row index
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the player's current column.
     *
     * @return the current column index
     */
    public int getCol() {
        return col;
    }

    /**
     * Attempts to move the player one cell up.
     *
     * @return true if the move was successful, false if blocked
     */
    public boolean moveUp() {
        return move(-1, 0);
    }

    /**
     * Attempts to move the player one cell down.
     *
     * @return true if the move was successful, false if blocked
     */
    public boolean moveDown() {
        return move(1, 0);
    }

    /**
     * Attempts to move the player one cell left.
     *
     * @return true if the move was successful, false if blocked
     */
    public boolean moveLeft() {
        return move(0, -1);
    }

    /**
     * Attempts to move the player one cell right.
     *
     * @return true if the move was successful, false if blocked
     */
    public boolean moveRight() {
        return move(0, 1);
    }

    /**
     * Attempts to move the player by the specified row and column deltas.
     * <p>
     * The move is only applied if the target cell is within bounds and not a wall.
     *
     * @param dRow the row offset (-1 for up, 1 for down)
     * @param dCol the column offset (-1 for left, 1 for right)
     * @return true if the move was valid and applied, false otherwise
     */
    private boolean move(int dRow, int dCol) {
        int newRow = row + dRow;
        int newCol = col + dCol;

        if (isValidMove(newRow, newCol)) {
            row = newRow;
            col = newCol;
            return true;
        }
        return false;
    }

    /**
     * Checks whether moving to the specified cell is valid.
     * <p>
     * A move is valid if the target cell is within grid bounds and is not a wall.
     *
     * @param r the target row index
     * @param c the target column index
     * @return true if the move is valid, false otherwise
     */
    private boolean isValidMove(int r, int c) {
        if (r < 0 || r >= grid.getRows() || c < 0 || c >= grid.getCols()) {
            return false;
        }
        return grid.getTile(r, c).getTileType() != TileType.WALL;
    }

    /**
     * Checks if the player has reached the target tile.
     *
     * @return true if the player is on the target tile, false otherwise
     */
    public boolean checkWin() {
        return (grid.getTile(row, col).getTileType() == TileType.TARGET);
    }
}

