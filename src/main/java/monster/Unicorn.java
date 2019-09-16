package monster;

public class Unicorn extends Monster {
    private static final int defaultHP = 100;

    public Unicorn(int x, int y, int speed, int maxHP, String type) {
        super(x, y, speed, maxHP, type);
    }

    public Unicorn(int x, int y, int speed) {
        super(x, y, speed, defaultHP, "Unicorn");
    }
}
