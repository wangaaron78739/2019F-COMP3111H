package monster;

import arena.logic.Arena;

public class Unicorn extends Monster {
    private static final int defaultHP = 150;
    private static final int defaultSpeed = 5;

    public Unicorn(int x, int y, int stage) {
        super(x, y, defaultSpeed, defaultHP*stage, "Unicorn"); // stronger as more HP
    }

}
