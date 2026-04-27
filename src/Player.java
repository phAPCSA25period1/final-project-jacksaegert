public class Player {
    private int row;
    private int col;
    private Grid grid;

    public Player(Grid grid, int startRow, int startCol) {
        this.grid = grid;
        this.row = startRow;
        this.col = startCol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean moveUp() {
        return move(-1, 0);
    }

    public boolean moveDown() {
        return move(1, 0);
    }

    public boolean moveLeft() {
        return move(0, -1);
    }

    public boolean moveRight() {
        return move(0, 1);
    }

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

    private boolean isValidMove(int r, int c) {
        if (r < 0 || r >= grid.getRows() || c < 0 || c >= grid.getCols()) {
            return false;
        }
        return grid.getTile(r, c).getTileType() != TileType.WALL;
    }

    public boolean checkWin() {
        return (grid.getTile(row, col).getTileType() == TileType.TARGET);
    }
}
