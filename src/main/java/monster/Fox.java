package monster;

public class Fox extends Monster {
    private static final int defaultHP = 50;
    private static final int defaultSpeed = 20;

    /*public Fox(int x, int y, int stage, int speed, int maxHP, String type) {
        super(x, y, speed, maxHP, type);
    }

    public Fox(int x, int y, int stage, int speed) {
        super(x, y, speed, defaultHP, "Fox");
    }*/
    
    public Fox(int x, int y, int stage) {
        super(x, y, defaultSpeed*stage, defaultHP, "Fox"); // stronger as the speed is faster
    }
}
