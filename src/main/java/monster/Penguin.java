package monster;

public class Penguin extends Monster {
    private static final int defaultHP = 50;
    private static final int defaultRestoreHP = 10;
    private static final int defaultSpeed = 5;
    
    private int restoreHP = defaultRestoreHP;

    public Penguin(int x, int y, int stage) {
        super(x, y, defaultSpeed, defaultHP*stage, "Penguin");
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
