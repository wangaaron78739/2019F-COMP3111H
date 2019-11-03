package monster;

import java.util.*;

import arena.logic.Arena;

public class Monster {
    private float xPx;
    private float yPx;
    private int HP;
    private int speed;
    private final int maxHP;
    private final String type;

    protected String direction = "Left";
    protected static int towerCount = 0;
    private static int currentValue = 0;
    public static int defaultCount = 100000;
    
    private static Cell[][] gridsInArena; // an array for representing grids in the arena
    private static Cell currentCell;
    private static final List<Cell> checkedNodes = new ArrayList<Cell>(); // an array for finding the shortest path
    private static final List<Cell> frontierNodes = new ArrayList<Cell>();
    
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

    public float getXPx() {
        return xPx;
    }

    public void setXPx(int xPx) {
        this.xPx = xPx;
    }

    public float getYPx() {
        return yPx;
    }

    public void setYPx(int yPx) {
        this.yPx = yPx;
    }

    public int getXGrid() {
        return (int)(xPx/ Arena.GRID_WIDTH) ;
    }

    public int getYGrid() {
        return (int)(yPx/ Arena.GRID_HEIGHT) ;
    }
    
    public int getSpeed() {
    	return speed;
    }
    
    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public String getType() {
        return type;
    }

    public int getMaxHP() {
        return maxHP;
    }
    
    public String getDirection() {
    	return direction;
    }
    
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

    public void move() { // TODO: override this method
    	for (int i=0; i<speed; ++i) {
	    	int xPx = (int)getXPx();
	    	int yPx = (int)getYPx();

    		int xGrid = getXGrid();
    		int yGrid = getYGrid();
	    	// when the monster has reached the end zone, simply return (no move)
	    	if ((xGrid==Arena.MAX_H_NUM_GRID-1) && (yGrid==Arena.MAX_V_NUM_GRID-1)) {
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
    }
    
    // public inner class for representing cells in the grid
    protected static class Cell {
    	private int xGrid = 0;
    	private int yGrid = 0;
    	private int value = defaultCount; // represent minimal steps needed to reach from end zone
    	private String fromCell = "Left"; // the cell we used to get this cell, only used by Fox
    	
    	public Cell(int xGrid, int yGrid) {
    		this.yGrid = yGrid;
    		this.xGrid = xGrid;
    		this.value = defaultCount; // modified later
    	}
    	
    	public int getXGrid() {
    		return xGrid;
    	}
    	
    	public int getYGrid() {
    		return yGrid;
    	}
    	
    	public int getValue() {
    		return value;
    	}
    	
    	public void setValue(int value) {
    		this.value = value;
    	}
    	
    	public String getFromCell() {
    		return fromCell;
    	}
    	
    	public void setFromCell(String fromCell) {
    		this.fromCell = fromCell;
    	}
    }
}
