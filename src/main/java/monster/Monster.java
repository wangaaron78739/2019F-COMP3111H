/**
 * monster is a group of files for monsters
 */
package monster;

import java.util.*;

import arena.logic.Arena;

/**
 * <p>
 * Class implement Monster.
 * <p>
 * There are three types of Monster, Fox, Penguin and Unicorn.
 * <p>
 * Along the time elapsed, different Monster would become stronger in different ways.
 * @author CHIU Ka Ho
 * 
 */
public class Monster {
    private float xPx;
    private float yPx;
    private int HP;
    private int speed;
    private final int maxHP;
    private String type;

    /**
     * <p>
     * A String representing the direction that monster in a cell should move to.
     * <p>
     * The default value is "Left", indicating that the monster in the cell should move to the cell to the next to it.
     */
    protected String direction = "Left";
    /**
     * <p>
     * An integer counting the total number of towers in current grids.
     * <p>
     * The default value is 0, indicating that no tower is built in current grids.
     */
    protected static int towerCount = 0;
    private static int currentValue = 0;
    /**
     * <p>
     * An integer representing default count for each cell,
     * i.e. the count of a cell if it hasn't been modified.
     * <p>
     * A cell with tower inside will always have count equal to this value.
     * <p>
     * The default value is 100000, indicating the count for the cell is 100000.
     */
    public static int defaultCount = 100000;
    
    private static Cell[][] gridsInArena; // an array for representing grids in the arena
    private static Cell currentCell;
    private static final List<Cell> checkedNodes = new ArrayList<Cell>(); // an array for finding the shortest path
    private static final List<Cell> frontierNodes = new ArrayList<Cell>();
    
    private int cooldown = 0; // number of cool downs left
    
    /**
     * <p>
     * Monster Constructor.
     * <p>
     * Monster have three types: Fox, Penguin and Unicorn
     * @param x The x-coordinate (in pixels) of the Monster
     * @param y The y-coordinate (in pixels) of the Monster
     * @param speed The speed (when not slowed down by IceTower) of the Monster
     * @param maxHP The maximum (and initial) HP of the Monster
     * @param type The type of the Monster
     */
    public Monster(float x, float y, int speed, int maxHP, String type) {
        this.xPx = x;
        this.yPx = y;
        this.HP = maxHP;
        this.speed = speed;
        this.maxHP = maxHP;
        this.type = type;
        // initialize the array gridsInArena
        if (gridsInArena == null) {
        	gridsInArena = new Cell[Arena.MAX_H_NUM_GRID][Arena.MAX_V_NUM_GRID];
        	for (int i=0; i<Arena.MAX_H_NUM_GRID; ++i) {
        		for (int j=0; j<Arena.MAX_V_NUM_GRID; ++j) {
        			gridsInArena[i][j] = new Cell(i, j);
            	}
        	}
        }
    }
    
    /**
	 * <p>
     * Getter function for the parameter xPx.
     * @return Integer representing the x-coordinate (in pixels) of the Monster.
     */
    public float getXPx() {
        return xPx;
    }

    /**
	 * <p>
     * Getter function for the parameter yPx.
     * @return Integer representing the y-coordinate (in pixels) of the Monster.
     */
    public float getYPx() {
        return yPx;
    }

    /**
	 * <p>
     * Getter function for the x-coordinate (in grids) of the Monster.
     * @return Integer representing the x-coordinate (in grids) of the Monster.
     */
    public int getXGrid() {
        return (int)(xPx/ Arena.GRID_WIDTH) ;
    }

    /**
	 * <p>
     * Getter function for the y-coordinate (in grids) of the Monster.
     * @return Integer representing the y-coordinate (in grids) of the Monster.
     */
    public int getYGrid() {
        return (int)(yPx/ Arena.GRID_HEIGHT) ;
    }
    
    /**
	 * <p>
     * Getter function for the speed (number of movements per move) of the Monster.
     * <p>
     * Notice if the Monster is hit by a IceTower and is in coolDown, its speed would be half of its default speed.
     * <p>
     * The monster would only resume normal speed when its coolDown is 0.
     * @return Integer representing the speed of the Monster.
     */
    public int getSpeed() {
    	if (cooldown==0) return speed;
    	else return speed/2;
    }
    
    /**
	 * <p>
     * Getter function for the HP of the Monster.
     * @return Integer representing the HP of the Monster.
     */
    public int getHP() {
        return HP;
    }

    /**
	 * <p>
     * Getter function for the type of the Monster.
     * <p>
     * A monster can have four types, the normal three ("Fox", "Penguin" and "Unicorn") and a "Death" type when it dies.
     * @return String representing the type of the Monster.
     */
    public String getType() {
        return type;
    }

    /**
	 * <p>
     * Getter function for the type of the Monster.
     * <p>
     * A monster can have four types, the normal three ("Fox", "Penguin" and "Unicorn") and a "Death" type when it dies.
     * @return String representing the type of the Monster.
     */
    public int getMaxHP() {
        return maxHP;
    }
    
    /**
	 * <p>
     * Getter function for the moving direction of the Monster.
     * <p>
     * A monster can have four directions to move, "Up", "Down", "Left" and "Right".
     * @return String representing the direction that the Monster would move towards.
     */
    public String getDirection() {
    	return direction;
    }
    
    /**
	 * <p>
     * Getter function for the coolDown of the Monster.
     * @return String representing the direction that the Monster would move towards.
     */
    public int getCoolDown() {
    	return cooldown;
    }   

    /**
	 * <p>
     * Setter function for the x-coordinate (in pixels) of the Monster.
     * @param xPx The x-coordinate (in pixels) that we want the Monster to have.
     */
    public void setXPx(int xPx) {
        this.xPx = xPx;
    }

    /**
	 * <p>
     * Setter function for the y-coordinate (in pixels) of the Monster.
     * @param yPx The y-coordinate (in pixels) that we want the Monster to have.
     */
    public void setYPx(int yPx) {
        this.yPx = yPx;
    }
    
    /**
	 * <p>
     * Setter function for the speed (number of movements per move) of the Monster.
     * @param speed The speed that we want the Monster to have.
     */
    public void setSpeed(int speed) {
    	this.speed = speed;
    }
    
    /**
	 * <p>
     * Setter function for the HP of the Monster.
     * @param HP The HP that we want the Monster to have.
     */
    public void setHP(int HP) {
        this.HP = HP;
    }
    
    /**
	 * <p>
     * Special setter function for the type of the Monster.
     * <p>
     * Note that this function can only set the type of the Monster to "Death".
     * <p>
     * It would be used only when the Monster's HP reaches 0 and is dead.
     */
    public void setTypeDeath() {
        type = "Death";
    }
    
    /**
	 * <p>
     * Setter function for the rounds of coolDown of the Monster.
     * @param cooldown The number of rounds the Monster has to wait for the effect of IceTower to gone and it can resume normal speed.
     */
    public void setCoolDown(int cooldown) {
    	this.cooldown = cooldown;
    }
    
    /**
	 * <p>
     * Method for the updating an array for representing grids in the arena, based on the current grids in the game.
     * <p>
     * It would update all the locations where there is a tower, and count the total number of towers currently.
     */
    public static void updateTowerCount() {
    	towerCount = 0;
    	// update index by search for grids of towers
    	for (int i=0; i<Arena.MAX_H_NUM_GRID; ++i) {
    		for (int j=0; j<Arena.MAX_V_NUM_GRID; ++j) {
    			if (Arena.getTower(i,j) != null) {
    				gridsInArena[i][j].setValue(defaultCount);
    				++towerCount;
    			}
        	}
    	}
    }
    
    /**
	 * <p>
     * Method for the updating an array for representing grids in the arena, based on the current grids in the game.
     * <p>
     * It would update the value associated with each cells, 
     * which can be used in determining where a Monster in a particular Cell should move.
     */
    public static void updateGrids() {
    	updateTowerCount();
    	
    	// reset the arrays
    	checkedNodes.clear();
    	currentValue = 0;
    	frontierNodes.clear();
    	// update value by search from the endzone
    	currentCell = gridsInArena[Arena.MAX_H_NUM_GRID-1][Arena.MAX_V_NUM_GRID-1]; 
    	currentCell.setValue(currentValue); // end zone can be reached by itself in 0 steps
    	checkedNodes.add(currentCell);
    	frontierNodes.add(currentCell);
    	++currentValue; // now reachable cells has currentValue one larger
    	
    	while (checkedNodes.size()<Arena.MAX_H_NUM_GRID*Arena.MAX_V_NUM_GRID-towerCount) {
    		List<Cell> NodesThisStep = new ArrayList<Cell>();
    		for (Cell node : frontierNodes) { // for all the checked nodes
    			// Left
    			if (node.xGrid!=0) { // can move left
    				currentCell = gridsInArena[node.xGrid-1][node.yGrid]; // get the left node
    				if (Arena.getTower(currentCell.xGrid,currentCell.yGrid) == null) { // not tower cell
	    				if (!checkedNodes.contains(currentCell) && !NodesThisStep.contains(currentCell)) { // unfilled stuff before
	    					currentCell.setValue(currentValue);
	    					NodesThisStep.add(currentCell);
	    				}
	    			}
    			}
    			// Right
    			if (node.xGrid!=Arena.MAX_H_NUM_GRID-1) { // can move right
    				currentCell = gridsInArena[node.xGrid+1][node.yGrid]; // get the right node
    				if (Arena.getTower(currentCell.xGrid,currentCell.yGrid) == null) { // not tower cell
	    				if (!checkedNodes.contains(currentCell) && !NodesThisStep.contains(currentCell)) { // unfilled stuff before
	    					currentCell.setValue(currentValue);
	    					NodesThisStep.add(currentCell);
	    				}
	    			}
    			}
    			// Up
    			if (node.yGrid!=0) { // can move up
    				currentCell = gridsInArena[node.xGrid][node.yGrid-1]; // get the up node
    				if (Arena.getTower(currentCell.xGrid,currentCell.yGrid) == null) { // not tower cell
	    				if (!checkedNodes.contains(currentCell) && !NodesThisStep.contains(currentCell)) { // unfilled stuff before
	    					currentCell.setValue(currentValue);
	    					NodesThisStep.add(currentCell);
	    				}
	    			}
    			}
    			// Down
    			if (node.yGrid!=Arena.MAX_V_NUM_GRID-1) { // can move down
    				currentCell = gridsInArena[node.xGrid][node.yGrid+1]; // get the down node
    				if (Arena.getTower(currentCell.xGrid,currentCell.yGrid) == null) { // not tower cell
	    				if (!checkedNodes.contains(currentCell) && !NodesThisStep.contains(currentCell)) { // unfilled stuff before
	    					currentCell.setValue(currentValue);
	    					NodesThisStep.add(currentCell);
	    				}
	    			}
    			}
    		}
    		frontierNodes.clear();
    		for (Cell node : NodesThisStep) {
    			frontierNodes.add(node);
    			checkedNodes.add(node);
    		}
    		++currentValue;
    	}
    }
    
    /**
	 * <p>
     * Method for determining where a Monster in a cell's center should move to.
     * @param xGrid The current x-coordinate (in grids) of the Monster.
     * @param yGrid The current y-coordinate (in grids) of the Monster.
     * @return String representing the moving direction of the Monster.
     */
    public String determineWhichDirectionAtCenter(int xGrid, int yGrid) {
    	int leftCount = defaultCount;
		int rightCount = defaultCount;
		int upCount = defaultCount;
		int downCount = defaultCount;
		Random rand = new Random();
		// update direction
		if (xGrid!=0) { // can move left
			leftCount = gridsInArena[xGrid-1][yGrid].getValue(); // get the left value
		}
		if (xGrid!=Arena.MAX_H_NUM_GRID-1) { // can move right
			rightCount = gridsInArena[xGrid+1][yGrid].getValue(); // get the right value
		}
		if (yGrid!=0) { // can move up
			upCount = gridsInArena[xGrid][yGrid-1].getValue(); // get the up value
		}
		if (yGrid!=Arena.MAX_V_NUM_GRID-1) { // can move down
			downCount = gridsInArena[xGrid][yGrid+1].getValue(); // get the down value
		}
		int[] counts = {leftCount, rightCount, upCount, downCount};
		int minimumCount = Arrays.stream(counts).min().getAsInt(); // get the minimal possible counts
		
		ArrayList<String> optimalDirections = new ArrayList<String>(); // array storing all the optimal directions
		if (leftCount==minimumCount) 
			optimalDirections.add("Left");
		if (rightCount==minimumCount) 
			optimalDirections.add("Right");
		if (upCount==minimumCount) 
			optimalDirections.add("Up");
		if (downCount==minimumCount) 
			optimalDirections.add("Down");
		
		return(optimalDirections.get(rand.nextInt(optimalDirections.size())));
    }
    
    /**
	 * <p>
     * Method for determining whether there's any Monster already in the end zone,
     * causing the game to end.
     * @return Boolean value representing whether the game has ended.
     */
    public static boolean gameEnds() {
    	for (Monster m: Arena.getMonsters()) {
    		if ((m.getXGrid()==Arena.MAX_H_NUM_GRID-1) && (m.getYGrid()==Arena.MAX_V_NUM_GRID-1)) {
	    		return true;
	    	}
    	}
    	return false;
    }

    /**
	 * <p>
     * Method for updating the location of the Monster base on their moving direction
     */
    public void move() {
    	for (int i=0; i<getSpeed(); ++i) {
	    	int xPx = (int)getXPx();
	    	int yPx = (int)getYPx();

    		int xGrid = getXGrid();
    		int yGrid = getYGrid();
	    	// when the monster has reached the end zone, simply return (no move)
	    	if (gameEnds()/*(xGrid==Arena.MAX_H_NUM_GRID-1) && (yGrid==Arena.MAX_V_NUM_GRID-1)*/ || getType()=="Death") {
	    		return;
	    	}
	    	
	    	// determine which direction to go to when reach the middle of a cell
	    	if (xPx % Arena.GRID_WIDTH == (int)(0.5*Arena.GRID_WIDTH)-1 && 
					yPx % Arena.GRID_HEIGHT == (int)(0.5*Arena.GRID_HEIGHT)-1 ) {
	    		direction = determineWhichDirectionAtCenter(xGrid, yGrid);
			}
	    	
	    	// handle the case when already on the way, but a tower is built
	    	if (direction == "Left" && (xPx % Arena.GRID_WIDTH < (int)(0.5*Arena.GRID_WIDTH)-1) && (Arena.getTower(xGrid-1,yGrid) != null)) 
	    		direction = "Right";
	    	if (direction == "Right" && (xPx % Arena.GRID_WIDTH > (int)(0.5*Arena.GRID_WIDTH)-1) && (Arena.getTower(xGrid+1,yGrid) != null)) 
	    		direction = "Left";
	    	if (direction == "Up" && (yPx % Arena.GRID_HEIGHT < (int)(0.5*Arena.GRID_HEIGHT)-1) && (Arena.getTower(xGrid,yGrid-1) != null)) 
	    		direction = "Down";
	    	if (direction == "Down" && (yPx % Arena.GRID_HEIGHT > (int)(0.5*Arena.GRID_HEIGHT)-1) && (Arena.getTower(xGrid,yGrid+1) != null)) 
	    		direction = "Up";
	    	
	    	// move according to the direction
	    	switch (direction) {
				case "Left":
					setXPx(xPx-1);
					break;
				case "Right":
					setXPx(xPx+1);
					break;
				case "Up":
					setYPx(yPx-1);
					break;
				case "Down":
					setYPx(yPx+1);
					break;
				default: break;
			}
    	}
    	
    	if (cooldown>0) --cooldown; // one less round of cooldown
    }
    
    /**
     * <p>
     * Nested class implement Cell in the game.
     * <p>
     * Used for getting the count for each Cell in the game.
     * <p>
     * The count would have different meanings for different classes, 
     * in Monster it means the minimum possible steps needed for a Monster to go from the current Cell to end zone,
     * in Fox it means the minimum possible number of attacks received for a Fox to go from the current Cell to end zone.
     * @author CHIU Ka Ho
     * 
     */
    protected static class Cell {
    	private int xGrid = 0;
    	private int yGrid = 0;
    	private int value = defaultCount; // represent minimal steps needed to reach from end zone
    	private String fromCell = "Left"; // the cell we used to get this cell, only used by Fox
    	
    	/**
    	 * <p>
         * Cell Constructor.
         * <p>
         * Cells would be constructed correspondingly to their positions in the map.
         * @param xGrid The x-coordinate (in grids) of the Cell
         * @param yGrid The y-coordinate (in grids) of the Cell
         */
    	public Cell(int xGrid, int yGrid) {
    		this.yGrid = yGrid;
    		this.xGrid = xGrid;
    		this.value = defaultCount; // modified later
    	}
    	
    	/**
    	 * <p>
         * Getter function for the parameter xGrid.
         * @return Integer representing the x-coordinate (in grids) of the Cell.
         */
    	public int getXGrid() {
    		return xGrid;
    	}
    	
    	/**
    	 * <p>
         * Getter function for the parameter yGrid.
         * @return Integer representing the y-coordinate (in grids) of the Cell.
         */
    	public int getYGrid() {
    		return yGrid;
    	}
    	
    	/**
    	 * <p>
         * Getter function for the parameter value.
         * @return Integer representing the current value of the Cell.
         */
    	public int getValue() {
    		return value;
    	}
	
    	/**
    	 * <p>
         * Getter function for the parameter fromCell.
         * @return String representing the direction from which the current Cell is pointed by another Cell.
         */
    	public String getFromCell() {
    		return fromCell;
    	}   	
    	
    	/**
    	 * <p>
         * Setter function for setting the parameter value.
         * @param value The value we want to associate with the current Cell.
         */
    	public void setValue(int value) {
    		this.value = value;
    	}
    	/**
    	 * <p>
         * Setter function for setting the parameter fromCell.
         * @param fromCell The String representing the direction from which the current Cell is pointed by another Cell.
         */
    	public void setFromCell(String fromCell) {
    		this.fromCell = fromCell;
    	}
    }
}
