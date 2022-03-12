import processing.core.PApplet;

/**
 * Checks each position of a tile
 */
public class Tile {
    private PApplet p;
    public Tile(PApplet p) {
        this.p= p;
    }
    /**
     * If a tile is a bomb
     */
    private boolean bomb;
    /**
     * If a tile is flagged
     */
    private boolean flagged;
    /**
     * If a tile is open
     */
    private boolean open;
    /**
     * True/False if there is a bomb on X tile
     */
    public boolean isBomb () {
        return bomb;
    }
    /**
     * True/False if there is a flag on X tile
     */
    public boolean isFlagged () {
        return flagged;
    }
    /**
     * True/False if X tile is covered up or uncovered
     */
    public boolean isOpen () {return open;}

    public boolean fFlag() {
        return flagged=false;
    }
    public boolean fBomb() {
        return bomb=false;
    }
    public boolean fOpen() {
        return open=false;
    }
    public boolean switchFlag() {
        return flagged=!flagged;
    }
    public boolean makeOpen(Tile[][]tiles,int r, int c) {
        if (!tiles[r][c].isFlagged()&&!tiles[r][c].isOpen()) {
            return tiles[r][c].open=true;
        } else {
            return tiles[r][c].isOpen();
        }
    }
    public void makeBomb(/*Tile[][]tiles,int r, int c*/) {
        this.bomb = true;
        //return tiles[r][c].bomb=!tiles[r][c].bomb;
    }
}