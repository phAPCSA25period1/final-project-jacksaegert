public class Grid {
    //Instance Var
    private final Tile[][] grid;
    //Constructor
    public Grid(int rows, int cols) {
        //For DFS to work properly, Rows + Cols need to be odd
        //Makes sure rows are odd
        if (rows % 2 == 0) {rows++;}

        //Makes sure cols are odd
        if (cols % 2 == 0) {cols++;}

        //Create a grid of blank tile objects
        grid = new Tile[rows][cols];

        //Replace nulls with walls.
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Tile();
            }
        }
    }
    //Getters
    public Tile getTile (int row, int col) {
        return this.grid[row][col];
    }
    //Setters
    public void setTile (int row, int col, Tile newTile) {
        this.grid[row][col] = newTile;
    }

}
