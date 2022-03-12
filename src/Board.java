import processing.core.PApplet;

/**
 * Board class which has the main elements of the game that is displayed
 */
public class Board {
    private PApplet p;
    private Tile t;
    private Tile[][] tiles;
    private int[] tileX;
    public int[] tileY;
    public int boardSize = 10;
    public int tileSize = 50;
    private boolean rClicked = false;
    private boolean lClicked = false;
    private boolean gameWin = false;
    private boolean gameLoss = false;
    private boolean firstClick = false;
    boolean resetGame= false;

    /**
     * Creates and sets up the 2D array (tiles)
     * @param p
     */
    public Board(PApplet p) {
        tileX = new int[boardSize];
        tileY = new int[boardSize];
        this.p = p;
        t = new Tile(p);
        tiles = new Tile[boardSize][boardSize];
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                tiles[r][c] = new Tile(p);
            }
        }
    }

    /**
     * Set number of bombs on the board
     */
    public int numBombs = 10;

    /**
     * Calculates the amount of bombs-flags on board
     * @return bombs-flags
     */
    public int bombCounter() {
        int count = 0;
        for (int i = 0; i < numBombs; i++) {
            count = numBombs - countFlags();
        }
        return count;
    }
    /**
     * Counts the ammount of flags that are placed by the player on the board
     * @return #of flags on the board
     */
    public int countFlags() {
        int count = 0;
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[r].length; c++) {
                if (tiles[r][c].isFlagged()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * draws bombs-flags
     */
    public void fCount () {
        p.fill(100);
        p.text(bombCounter(), 20, 60);
    }

    /**
     * draws board (tiles), number of bombs nearby, flag count
     */
    public void draw() {
        leftClicked();
        drawFace();
        loseGame();
        winGame();
        rightClicked();
        drawTiles();
        drawNums();
        fCount();
    }

    /**
     * Creates the grid for the board and changes the tile colors depending on if they are a bomb or open or flagged
     */
    public void drawTiles() {
        for (int i = 0; i < boardSize; i++) {
            tileX[i] = 10 + i * 50;
            tileY[i] = 90 + i * 50;
        }


        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (tiles[r][c].isOpen() && tiles[r][c].isBomb()) {
                    p.fill(255, 0, 0);
                }else if (tiles[r][c].isFlagged() && !tiles[r][c].isOpen()) {
                    p.fill(0, 255, 0);
                }else if (tiles[r][c].isOpen() && !tiles[r][c].isBomb()) {
                    p.fill(0, 0, 255);
                } else {
                    p.fill(100);
                }
                p.rect(tileX[r], tileY[c], tileSize, tileSize);
            }
        }
    }

    /**
     * Draws the nearby bombs around a tile
     */
    public void drawNums() {
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (tiles[r][c].isOpen()&&!tiles[r][c].isBomb()) {
                    p.fill(255);
                    p.text(nearbyBombs(tiles,r,c),tileX[r]+10,tileY[c]+45);
                }
            }
        }
    }



    /**
     * Counts the bombs nearby a tile in a 3X3 grid
     * @param tiles 2D array
     * @param row The row of the tile that you are checking
     * @param col The column of the tile that you are checking
     * @return the number of bombs nearby in a 3X3 grid of the tile you are checking
     */
    public int nearbyBombs (Tile[][] tiles, int row, int col) {
        int count = 0;
        int rowMin = Math.max(0,row-1);
        int rowMax = Math.min (row+1,9);
        int colMin = Math.max(0,col-1);
        int colMax = Math.min (col+1,9);
        for (int r = rowMin; r <= rowMax; r++) {
            for (int c = colMin; c <= colMax; c++) {
                if (tiles[r][c].isBomb()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Opens up the '0' tiles around the clicked tile
     * @param r
     * @param c
     */
    public void recurTile (int r, int c) {
        int rowMin = Math.max(0, r - 1);
        int rowMax = Math.min(r + 1, 9);
        int colMin = Math.max(0, c - 1);
        int colMax = Math.min(c + 1, 9);
        for (int i = rowMin; i <= rowMax; i++) {
            for (int j = colMin; j <= colMax; j++) {
                if (!tiles[i][j].isOpen() && nearbyBombs(tiles, i, j)== 0&&!tiles[i][j].isBomb()&&!tiles[i][j].isFlagged()) {
                    tiles[i][j].makeOpen(tiles,i,j);
                    recurTile(i, j);
                }
                if (!tiles[i][j].isOpen() && nearbyBombs(tiles, i, j)> 0&&!tiles[i][j].isBomb()&&!tiles[i][j].isFlagged()) {
                    tiles[i][j].makeOpen(tiles,i,j);
                }
            }
        }
    }

    /**
     * If the tile has x bombs next to it and has x flags next to it it will open the 8 tiles around it
     * @param r
     * @param c
     */
    public void numberRecur (int r, int c) {
        int rowMin = Math.max(0, r - 1);
        int rowMax = Math.min(r + 1, 9);
        int colMin = Math.max(0, c - 1);
        int colMax = Math.min(c + 1, 9);
        if (nearbyBombs(tiles,r,c)==numFlags(r,c)) {
            for (int i = rowMin; i <= rowMax; i++) {
                for (int j = colMin; j <= colMax; j++) {
                    if (!tiles[i][j].isFlagged()&&!tiles[i][j].isOpen()) {
                        if (nearbyBombs(tiles,i,j)==0) {
                            recurTile(i,j);
                        }
                        tiles[i][j].makeOpen(tiles,i,j);
                    }
                }
            }
        }
    }

    /**
     * Checks for how many tiles are flagged around a tile
     * @param r
     * @param c
     * @return number of flags around a tile
     */
    private int numFlags (int r, int c) {
        int count = 0;
        int rowMin = Math.max(0, r - 1);
        int rowMax = Math.min(r + 1, 9);
        int colMin = Math.max(0, c - 1);
        int colMax = Math.min(c + 1, 9);
        for (int i = rowMin; i <= rowMax; i++) {
            for (int j = colMin; j <= colMax; j++) {
                if (tiles[i][j].isFlagged()) {
                    count++;
                }
            }
        }
        return count;
    }


    /**
     * The right click function that flags a tile, if it is not open (TOGGLEABLE)
     */
    public void rightClicked() {
        if (p.mouseButton == p.RIGHT) {
            if (!rClicked) {
                for (int r = 0; r < boardSize; r++) {
                    for (int c = 0; c < boardSize; c++) {
                        if (p.mouseX >= tileX[r] && p.mouseX < tileX[r] + tileSize && p.mouseY >= tileY[c] && p.mouseY < tileY[c] + tileSize && !tiles[r][c].isOpen()) {
                            tiles[r][c].switchFlag();
                        }
                    }
                }
            }
            rClicked = true;
        } else {
            rClicked = false;
        }
    }

    /**
     * Places bombs randomly once the first click is done
     */
    public void placeBombs(int r, int c) {
        int j = 0;
        while (j<numBombs) {
            int a = (int)p.random(0,numBombs);
            int b = (int)p.random(0,numBombs);
            if (a!=r&&b!=c&&!tiles[a][b].isBomb()) {
                tiles[a][b].makeBomb();
                j++;
            }
        }
    }
    /**
     * The left click function that opens a tile, and changes it state to open
     */
    public void leftClicked() {
        if (p.mouseButton == p.LEFT) {
            if (!lClicked) {
                for (int r = 0; r < boardSize; r++) {
                    for (int c = 0; c < boardSize; c++) {
                        if (!firstClick) {
                            placeBombs(r,c);
                        }
                        firstClick=true;
                        if (p.mouseX >=235 && p.mouseX < 285 && p.mouseY >= 20 && p.mouseY < 70&&gameLoss||gameWin) {
                            resetGame();
                        }
                        if (p.mouseX >= tileX[r] && p.mouseX < tileX[r] + tileSize && p.mouseY >= tileY[c] && p.mouseY < tileY[c] + tileSize&&nearbyBombs(tiles,r,c)>0) {
                            numberRecur(r,c);
                        }
                        if (p.mouseX >= tileX[r] && p.mouseX < tileX[r] + tileSize && p.mouseY >= tileY[c] && p.mouseY < tileY[c] + tileSize && !tiles[r][c].isFlagged()&&!tiles[r][c].isOpen()) {
                            tiles[r][c].makeOpen(tiles,r,c);
                            if (nearbyBombs(tiles,r,c)==0) {
                                recurTile(r,c);
                            }
                        }
                    }
                }
            }

            lClicked = true;
        } else {
            lClicked = false;
        }
    }

    /**
     * The game is lost when a bomb is opened; Losing screen appears
     */
    public void loseGame() {
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (tiles[r][c].isOpen() && tiles[r][c].isBomb()) {
                    for (int i=0;i<boardSize;i++) {
                        for (int j = 0;j<boardSize;j++) {
                            if (tiles[i][j].isBomb()) {
                                if (tiles[i][j].isFlagged()) {
                                    tiles[i][j].switchFlag();
                                }
                                tiles[i][j].makeOpen(tiles,i,j);
                            }
                        }
                    }
                    gameLoss=true;
                }
            }
        }
    }

    /**
     * The game is won when all tiles except for the bombs are opened
     */
    public void winGame() {
        int count = 0;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (!tiles[r][c].isBomb() && tiles[r][c].isOpen()) {
                    count++;
                }
            }
        }
        if (count ==90) {
            gameWin=true;

        }
    }

    public void drawFace() {
        if (!gameWin&&!gameLoss) {

            p.rect(235,20,50,50);
        }
        if (gameWin) {
            p.fill(0,0,255);
            p.ellipse(260,50,50,50);
        }
        if (gameLoss) {
            p.fill(255,0,0);
            p.rect(235, 20, 50, 50);
        }
    }

    /**
     * Sets all the values to their default state. (RESETS)
     */
    public void resetGame() {
        resetGame=true;
        gameWin=false;
        gameLoss=false;
        firstClick=false;
        rClicked=false;
        lClicked=false;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                tiles[r][c].fBomb();
                tiles[r][c].fFlag();
                tiles[r][c].fOpen();
            }
        }
    }
}