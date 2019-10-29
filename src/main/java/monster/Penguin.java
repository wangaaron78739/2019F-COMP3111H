package monster;

public class Penguin extends Monster {
    private static final int defaultHP = 50;
    private static final int defaultRestoreHP = 10;
    private static final int defaultSpeed = 2;
    
    private int restoreHP = defaultRestoreHP;

    /*public Penguin(int x, int y, int stage, int speed, int maxHP, String type) {
        super(x, y, speed, maxHP, type);
    }

    public Penguin(int x, int y, int stage, int speed) {
        super(x, y, speed, defaultHP, "Penguin");
    }*/

    public Penguin(int x, int y, int stage) {
        super(x, y, defaultSpeed, defaultHP, "Penguin");
        restoreHP = defaultRestoreHP * stage; // stronger as more restore HP
    }
    
    @Override
    public void move() {
    	super.move();
    	if (getHP()<defaultHP) {
    		setHP(Math.max(getHP()+restoreHP, defaultHP));
    	}
    }
}
