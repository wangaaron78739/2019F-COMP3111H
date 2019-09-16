package monster;

public class Penguin extends Monster {
    private static final int defaultHP = 100;
    private static final int defaultSpeed = 100;

    public Penguin(int x, int y, int speed, int maxHP, String type) {
        super(x, y, speed, maxHP, type);
    }

    public Penguin(int x, int y, int speed) {
        super(x, y, speed, defaultHP, "Penguin");
    }
    public Penguin(int x, int y) {
        super(x, y, defaultSpeed, defaultHP, "Penguin");
    }
}
