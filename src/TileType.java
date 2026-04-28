/**
 * Enumerates the possible types of tiles in the maze.
 * <p>
 * Each tile in the {@link Grid} is classified as one of the following:
 * a wall, a navigable path, or the target destination.
 */
public enum TileType {

    /** Represents an impassable wall in the maze. */
    WALL,

    /** Represents a navigable path that the player can move through. */
    PATH,

    /** Represents the target destination that the player must reach to win. */
    TARGET
}

