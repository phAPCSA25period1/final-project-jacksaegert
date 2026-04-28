/**
 * Represents a single cell (tile) within the maze {@link Grid}.
 * <p>
 * Each tile has a {@link TileType} that indicates whether it is a wall,
 * a path, or the target destination.
 */
public class Tile {

    /** The type of this tile (WALL, PATH, or TARGET). */
    private TileType type;

    /**
     * Constructs a new Tile with the default type of {@link TileType#WALL}.
     * <p>
     * The type can be changed later via the {@link #setTileType(TileType)} method,
     * typically by the {@link MazeCarver} during maze generation.
     */
    public Tile() {
        this.type = TileType.WALL;
    }

    /**
     * Returns the current type of this tile.
     *
     * @return the {@link TileType} of this tile
     */
    public TileType getTileType() {
        return type;
    }

    /**
     * Sets the type of this tile.
     *
     * @param newType the new {@link TileType} to assign to this tile
     */
    public void setTileType(TileType newType) {
        this.type = newType;
    }
}

