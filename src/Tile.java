public class Tile {
    //Instance Vars (More added later)
    private TileType type;

    //Constructor, defaults to wall. Type can be changed via MazeCarver.
    public Tile () {
        this.type = TileType.WALL;
    }

    //Getters
    public TileType getTileType() {return type;}

    //Setters
    public void setTileType(TileType ntype) {this.type = ntype;}

}
