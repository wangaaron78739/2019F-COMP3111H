package monster;

import arena.logic.Arena;

/**
 * 
 * Class implement one type of Monster, Unicorn
 * This type of monster has the largest HP
 * @author CHIU Ka Ho
 * 
 */
public class Unicorn extends Monster {
    private static final int defaultHP = 150;
    private static final int defaultSpeed = 2;

    /**
     * Unicorn Constructor
     * @param x The x-coordinate (in pixels) of the Unicorn
     * @param y The y-coordinate (in pixels) of the Unicorn
     * @param stage The stage of the game now
     */
    public Unicorn(int x, int y, int stage) {
        super(x, y, defaultSpeed, defaultHP*stage, "Unicorn"); // stronger as more HP
    }

}
