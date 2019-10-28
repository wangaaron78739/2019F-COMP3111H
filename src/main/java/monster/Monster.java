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
    private static int monsterNum;

    private static int towerCount = 0;
    private static int currentValue = 0;
    
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
        	this.gridsInArena = new Cell[Arena.MAX_H_NUM_GRID][Arena.MAX_V_NUM_GRID];
        	for (int i=0; i<Arena.MAX_H_NUM_GRID; ++i) {
        		for (int j=0; j<Arena.MAX_V_NUM_GRID; ++j) {
        			gridsInArena[i][j] = new Cell(i, j, 0);
            	}
        	}
        }
    }
    
    public static void updateGrids() {
    	towerCount = 0;
    	// update index by search for grids of towers
    	for (int i=0; i<Arena.MAX_H_NUM_GRID; ++i) {
    		for (int j=0; j<Arena.MAX_V_NUM_GRID; ++j) {
    			if (Arena.getTower(i,j) != null) {
    				gridsInArena[i][j].setIndex(1); // tower grid
    				gridsInArena[i][j].setValue(1000);
    				++towerCount;
    			}
        	}
    	}
    	
    	// update value by search from the endzone
    	checkedNodes.clear();
    	currentValue = 0;
    	frontierNodes.clear();
    	// end zone
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

    public void move() { // TODO: override this method
    	int initXGrid = getXGrid(); // initial means before the monster move for any steps
    	int initYGrid = getYGrid();
    	Random rand = new Random(); 
    	int leftCount = 1000;
    	int rightCount = 1000;
    	int upCount = 1000;
    	int downCount = 1000;
		String direction = "Left";
    	for (int i=0; i<speed; ++i) {
        	int xGrid = getXGrid();
        	int yGrid = getYGrid();
        	// int count = gridsInArena[xGrid][yGrid].getValue();
	    	// when the monster has reached the end zone, simply return (no move)
	    	if ((xGrid==Arena.MAX_H_NUM_GRID-1) && (yGrid==Arena.MAX_V_NUM_GRID-1)) {
	    		return;
	    	}
			
			if (i==0 || ((xGrid!=initXGrid) || (yGrid!=initYGrid))) { // the first move made by the monster
				// get all the four counts
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
				int minimumCount = Arrays.stream(counts).min().getAsInt(); // get the minimum count among all four directions
				
				ArrayList<String> optimalDirections = new ArrayList<String>();
				if (leftCount==minimumCount) optimalDirections.add("Left");
				if (rightCount==minimumCount) optimalDirections.add("Right");
				if (upCount==minimumCount) optimalDirections.add("Up");
				if (downCount==minimumCount) optimalDirections.add("Down");
				int numOfDirections = optimalDirections.size();
				
				direction = optimalDirections.get(rand.nextInt(numOfDirections));
			}	
			
			switch (direction) {
				case "Left":
					setXPx((int)(getXPx()-1));
					break;
				case "Right":
					setXPx((int)(getXPx()+1));
					break;
				case "Up":
					setYPx((int)(getYPx()-1));
					break;
				case "Down":
					setYPx((int)(getYPx()+1));
					break;
				default: break;
			}
    	}
    }
    
    // public inner class for representing cells in the grid
    public static class Cell {
    	private int xGrid = 0;
    	private int yGrid = 0;
    	private int index = 0; // 0 for empty, 1 for tower
    	private int value = 1000; // represent minimal steps needed to reach from end zone
    	
    	public Cell(int xGrid, int yGrid, int index) {
    		this.xGrid = xGrid;
    		this.yGrid = yGrid;
    		this.index = index;
    		this.value = 1000; // modified later
    	}
    	
    	public int getValue() {
    		return value;
    	}
    	
    	public void setIndex(int index) {
    		this.index = index;
    	}
    	
    	public void setValue(int value) {
    		this.value = value;
    	}
    }
}
