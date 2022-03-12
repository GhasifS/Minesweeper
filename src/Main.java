import processing.core.PApplet;

public class Main extends PApplet {
    MineSweeperGame g;

    public static void main(String[] args) {
        PApplet.main("Main", args);
    }

    /**
     * Sets the size of board
     */
    public void settings() {
        size(520, 600);
        g = new MineSweeperGame(this);
    }

    /**
     * Draws everything from the classes
     */
    public void draw() {
        background(0);
        g.draw();
    }
}