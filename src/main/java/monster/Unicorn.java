package monster;

import arena.logic.Arena;

/**
 * 
 * Class implement one type of Monster, Unicorn.
 * Unicorn has more HP than other monsters.
 * Along the time elapsed, Unicorn would become stronger as they would have higher HP.
 * @author CHIU Ka Ho
 * 
 */
public class Unicorn extends Monster {
    private static final int defaultHP = 150;
    private static final int defaultSpeed = 2;

    /**
     * Unicorn Constructor.
     * @param x The x-coordinate (in pixels) of the Unicorn
     * @param y The y-coordinate (in pixels) of the Unicorn
     * @param stage The current stage of the game
     */
    public Unicorn(int x, int y, int stage) {
        super(x, y, defaultSpeed, defaultHP*stage, "Unicorn"); // stronger as more HP
    }

}
