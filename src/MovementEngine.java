import javax.swing.JFrame; // Required for live input

public class MovementEngine {
    private Player player;
    private Grid grid;
    private JFrame frame;
    private boolean listening = false;

    public MovementEngine(Player player, Grid grid) {
        this.player = player;
        this.grid = grid;
    }

    public boolean canMove(String direction) {
        int newRow = player.getRow();
        int newCol = player.getCol();
        switch (direction) {
            case "up":
                newRow++;
                break;
            case "down":
                newRow--;
                break;
            case "left":
                newCol--;
                break;
            case "right":
                newCol++;
                break;
            default:
                return false;
        }
        // Bounds check
        if (newRow < 0 || newRow >= grid.getRows() || newCol < 0 || newCol >= grid.getCols()) {
            return false;
        }
        // Tile check
        return grid.getTile(newRow, newCol).getTileType() == TileType.PATH;
    }
}
