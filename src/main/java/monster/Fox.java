package monster;

public class Fox extends Monster {
    private static final int defaultHP = 100;
    private static final int defaultSpeed = 100;

    public Fox(int x, int y, int speed, int maxHP, String type) {
        super(x, y, speed, maxHP, type);
    }

    public Fox(int x, int y, int speed) {
        super(x, y, speed, defaultHP, "Fox");
    }
    public Fox(int x, int y) {
        super(x, y, defaultSpeed, defaultHP, "Fox");
    }
}
