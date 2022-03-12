import processing.core.PApplet;

/**
 * Whole game that uses processing PApplet p
 */
public class MineSweeperGame {
    private PApplet p;
    private Board b;

    /**
     * Takes in PApplet p so you can use processing
     *
     * @param p
     */
    public MineSweeperGame(PApplet p) {
        this.p = p;
        b = new Board(p);
    }

    /**
     * uses millis() from processing to count the milliseconds/1000 (seconds) after you press start; draws the seconds passed
     */
    public void timer() {
        p.fill(100);
        int sec=0;
        sec+=p.millis()/1000;
        if (b.resetGame) {
        }
        if (sec > 999) {
            sec = 999;
        }
        p.textSize(50);
        if (sec < 10) {
            p.text(sec, 460, 60);
        }
        if (sec >= 10 && sec < 100) {
            p.text(sec, 430, 60);
        }
        if (sec >= 100 && sec < 1000) {
            p.text(sec, 400, 60);
        }
    }
    /**
     * Draws the board from board class, timer, leftclicks, rightclicks, losing and winning screen
     */
    public void draw() {
        timer();
        b.draw();
    }

}