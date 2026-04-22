public class Player {
    //Instance Vars
    private int pRow;
    private int pCol;

    public Player() {
        this.pRow = 1;
        this.pCol = 1;
    }

    //Getters
    public int getRow() {
        return pRow;
    }

    public int getCol() {
        return pCol;
    }

    //Convinience Methods
    public void goUp() {
        this.pRow++;
    }

    public void goDown() {
        this.pRow--;
    }

    public void goRight() {
        this.pCol++;
    }

    public void goLeft() {
        this.pCol--;
    }

}
