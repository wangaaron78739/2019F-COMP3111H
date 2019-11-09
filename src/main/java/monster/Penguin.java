package monster;

/**
 * 
 * Class implement one type of Monster, Penguin.
 * Penguin has can replenish some HP (but not more than its initial value) each time it moves.
 * Along the time elapsed, Penguin would become stronger as they would have higher HP and higher HP restored in each movement.
 * @author CHIU Ka Ho
 * 
 */

public class Penguin extends Monster {
    private static final int defaultHP = 200;
    private static final int defaultRestoreHP = 30;
    private static final int defaultSpeed = 2;
    
    private int restoreHP = defaultRestoreHP;

    /**
     * Penguin Constructor.
     * @param x The x-coordinate (in pixels) of the Penguin
     * @param y The y-coordinate (in pixels) of the Penguin
     * @param stage The current stage of the game
     */
    public Penguin(int x, int y, int stage) {
        super(x, y, defaultSpeed, defaultHP*stage, "Penguin");
        restoreHP = defaultRestoreHP * stage; // stronger as more restore HP
    }
    
    /**
	 * <p>
     * Getter function for the parameter restoreHP.
     * @return Integer representing the restore HP (replenished HP per move) of the Monster.
     */
    public int getRestoreHP() {
        return restoreHP;
    }
    
    /**
     * Method to let penguin move,
     * Penguin will replenish some HP (but not more than its initial value) each time it moves.
     */
    @Override
    public void move() {
    	super.move();
    	if (getHP()<defaultHP) {
    		setHP(Math.max(getHP()+restoreHP, defaultHP));
    	}
    }
}
