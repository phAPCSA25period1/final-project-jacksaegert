import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MazeCarver {
    //Instance Vars
    private boolean[][] visited;

    public void carveMaze(Grid grid) {
        //Grabs dimensions of grid to pass to Carve method
        int rows = grid.getRows();
        int cols = grid.getCols();

        //Initializes Visited array to ensure no doublecarves happen
        visited = new boolean[rows][cols];

        //Calls carve method
        carveFrom(grid, 1, 1);
    }

    private void carveFrom(Grid grid, int row, int col) {
        //Marks the starting cell as visited (super important later) and sets it to a path
        visited[row][col] = true;
        grid.getTile(row, col).setTileType(TileType.PATH);

        //Gets the randomized offset deltas
        List<int[]> directions = getShuffledDirections();

        //COMPLEX STEP HERE! Uses an enhanced for loop to recursively call the method that the for loop is in, which calls another enhanced for loop. this behavior exploits the behavior of the Stack, a part of java memory that stores ongoing methods. that way, if the top of the stack is 'popped' (loop ends or is cut), because that loop was inside another loop, the previous loop resumes. This essentially allows backtracking to a previous cell if a cell returns no valid neighbors. However, this comes at the downside of using loads of memory, potentially crashing if the stack's max size is exceeded, causing a StackOverFlowError. Okay onto the method now.
        for (int[] direction : directions) {

            //Initializes nextRow and nextCol for recursice calls of carveFrom. Jumps 2 tiles instead of 1 so that a wall remains inbetween.
            int nextRow = row + direction[0] * 2;
            int nextCol = col + direction[1] * 2;

            //Checks if the next cell is due to be carved (within bounds and unvisited)
            if (isValidCell(nextRow, nextCol, grid.getRows(), grid.getCols()) && !visited[nextRow][nextCol]) {

                //Figures out where the wall inbetween the current position and the next position
                int wallRow = row + direction[0];
                int wallCol = col + direction[1];

                //Removes the wall. Due to the behavior of jumping 2 tiles rather than 1, it is not needed to be marked as visited
                grid.getTile(wallRow, wallCol).setTileType(TileType.PATH);

                //Calls the carveFrom method on the new tile. This will continue until all enhanced for loops run to completion, meaning every tile in the maze has been visited once.
                carveFrom(grid, nextRow, nextCol);
            }
        }
    }

    private boolean isValidCell(int targetrow, int targetcol, int rows, int cols) {

        //If the cell is within bounds, return true.
        return targetrow > 0 && targetrow < rows && targetcol > 0 && targetcol < cols;
    }

    private List<int[]> getShuffledDirections() {
        //Creates a new List with nothing
        List<int[]> directions = new ArrayList<>();

        //Adds the 4 "Direction" deltas to each (used to offset CarveFrom method via DFS)
        directions.add(new int[]{-1, 0});
        directions.add(new int[]{1, 0});
        directions.add(new int[]{0, -1});
        directions.add(new int[]{0, 1});

        //Shuffles List to make sure next step in carving maze is randomized
        Collections.shuffle(directions);

        //Returns randomized list of Deltas
        return directions;
    }
}

