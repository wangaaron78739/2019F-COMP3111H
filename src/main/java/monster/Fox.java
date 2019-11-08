package monster;

import java.util.ArrayList;
import java.util.List;

import arena.logic.Arena;
import tower.Tower;

/**
 * 
 * Class implement one type of Monster, Fox.
 * Fox has the fastest speed among all three kinds of Monster.
 * Along the time elapsed, Fox would become stronger as they would have higher HP.
 * @author CHIU Ka Ho
 * 
 */

//import arena.logic.Arena;
//import monster.Monster.Cell;

public class Fox extends Monster {
    private static final int defaultHP = 500;
    private static final int defaultSpeed = 5;
    
	private static int numStepsEachCell = 8;
    private static Cell[][] gridsInArenaFox; // an array for representing grids in the arena
    private static Cell currentCellFox;
    private static final List<Cell> checkedNodesFox = new ArrayList<Cell>(); // an array for finding the path with least attacks
    private static final List<Cell> frontierNodesFox = new ArrayList<Cell>();
    
    /**
     * Fox Constructor.
     * @param x The x-coordinate (in pixels) of the Fox
     * @param y The y-coordinate (in pixels) of the Fox
     * @param stage The current stage of the game
     */
    public Fox(int x, int y, int stage) {
        super(x, y, defaultSpeed, defaultHP*stage, "Fox"); // stronger as the speed is faster
        // initialize the array gridsInArenaFox
        if (gridsInArenaFox == null) {
        	gridsInArenaFox = new Cell[Arena.MAX_H_NUM_GRID][Arena.MAX_V_NUM_GRID];
        	for (int i=0; i<Arena.MAX_H_NUM_GRID; ++i) {
        		for (int j=0; j<Arena.MAX_V_NUM_GRID; ++j) {
        			gridsInArenaFox[i][j] = new Cell(i, j);
            	}
        	}
        }
    }
    
    /**
	 * <p>
     * Method for the updating an array for representing grids in the arena, based on the current grids in the game.
     * <p>
     * Notice that this is not overriding, but simply change the way of updating the array.
     * <p>
     * It would update the value associated with each cells, 
     * which can be used in determining where a Fox in a particular Cell should move.
     */
    public void updateGridsFox() {
    	// reset the arrays
    	checkedNodesFox.clear();
    	frontierNodesFox.clear();
    	
    	// update the end zone
    	currentCellFox = gridsInArenaFox[Arena.MAX_H_NUM_GRID-1][Arena.MAX_V_NUM_GRID-1]; 
    	currentCellFox.setValue(0); // to make the end zone not "reachable" from any node that reduce the number of attack
    	
    	// update the two reachable cells from the end zone
    	int cellCount = 0;
    	int xPx = 0;
    	int yPx = 0;
    	
    	// update the cell above the end zone
    	cellCount = 0;
        numStepsEachCell = Arena.GRID_HEIGHT/getSpeed();
    	currentCellFox = gridsInArenaFox[Arena.MAX_H_NUM_GRID-1][Arena.MAX_V_NUM_GRID-2]; 
    	for (int i=0; i<numStepsEachCell/2+1; ++i) {
    		// the coordinates of the pixels the fox would go through
    		xPx = currentCellFox.getXGrid()*Arena.GRID_WIDTH + (int)(0.5*Arena.GRID_WIDTH) - 1;
        	yPx = currentCellFox.getYGrid()*Arena.GRID_HEIGHT + (int)(0.5*Arena.GRID_HEIGHT) - 1 + i*defaultSpeed;
    		// iterate through all the towers
    		for (Tower tower: Arena.getTowers()) {
    			if (tower.canAttack(xPx, yPx)) {
    				++cellCount;
    			}
    		}
    	}
    	currentCellFox.setValue(cellCount);
    	currentCellFox.setFromCell("Down"); // since the cell is above the end zone, it needs to go down to reach the end zone
    	checkedNodesFox.add(currentCellFox);
    	frontierNodesFox.add(currentCellFox);
    	
    	// update the cell on the left of the end zone
    	cellCount = 0;
    	currentCellFox = gridsInArenaFox[Arena.MAX_H_NUM_GRID-2][Arena.MAX_V_NUM_GRID-1]; 
    	for (int i=0; i<numStepsEachCell/2+1; ++i) {
    		// the coordinates of the pixels the fox would go through
    		xPx = currentCellFox.getXGrid()*Arena.GRID_WIDTH + (int)(0.5*Arena.GRID_WIDTH) - 1 + i*defaultSpeed;
        	yPx = currentCellFox.getYGrid()*Arena.GRID_HEIGHT + (int)(0.5*Arena.GRID_HEIGHT) - 1;
    		// iterate through all the towers
    		for (Tower tower: Arena.getTowers()) {
    			if (tower.canAttack(xPx, yPx)) {
    				++cellCount;
    			}
    		}
    	}
    	currentCellFox.setValue(cellCount);
    	currentCellFox.setFromCell("Right"); // since the cell is on the left the end zone, it needs to go right to reach the end zone
    	checkedNodesFox.add(currentCellFox);
    	frontierNodesFox.add(currentCellFox);
    	
    	// for the rest of the nodes
    	while (checkedNodesFox.size()<Arena.MAX_H_NUM_GRID*Arena.MAX_V_NUM_GRID-towerCount) {
    		List<Cell> NodesFoxThisStep = new ArrayList<Cell>();
    		for (Cell node : frontierNodesFox) { // for all the checked nodes
    			// Left
    			if (node.getXGrid()!=0) { // can move left
    				currentCellFox = gridsInArenaFox[node.getXGrid()-1][node.getYGrid()]; // get the left node
    				if (Arena.getTower(currentCellFox.getXGrid(),currentCellFox.getYGrid()) == null) { // not tower cell
    					cellCount = node.getValue();
    					for (int i=0; i<numStepsEachCell; ++i) {
    			    		// the coordinates of the pixels the fox would go through
    			    		xPx = currentCellFox.getXGrid()*Arena.GRID_WIDTH + (int)(0.5*Arena.GRID_WIDTH) - 1 + i*defaultSpeed;
    			        	yPx = currentCellFox.getYGrid()*Arena.GRID_HEIGHT + (int)(0.5*Arena.GRID_HEIGHT) - 1;
    			    		// iterate through all the towers
    			    		for (Tower tower: Arena.getTowers()) {
    			    			if (tower.canAttack(xPx, yPx)) {
    			    				++cellCount;
    			    			}
    			    		}
    			    	}
	    				if (!checkedNodesFox.contains(currentCellFox) && !NodesFoxThisStep.contains(currentCellFox)) { // unfilled stuff before
	    					currentCellFox.setValue(cellCount);
	    					currentCellFox.setFromCell("Right");
	    					NodesFoxThisStep.add(currentCellFox);
	    				}
	    				else { // already filled
	    					if (cellCount < currentCellFox.getValue()) {
	    						currentCellFox.setValue(cellCount); // only update when we find a path receiving smaller attack
	    						currentCellFox.setFromCell("Right");
	    					}
	    				}
	    			}
    			}
    			// Right, also need to check it's not the end zone!
    			if (node.getXGrid()!=Arena.MAX_H_NUM_GRID-1) { // can move right
    				currentCellFox = gridsInArenaFox[node.getXGrid()+1][node.getYGrid()]; // get the right node
    				if (Arena.getTower(currentCellFox.getXGrid(),currentCellFox.getYGrid()) == null) { // not tower cell
    					cellCount = node.getValue();
    					for (int i=0; i<numStepsEachCell; ++i) {
    			    		// the coordinates of the pixels the fox would go through
    			    		xPx = currentCellFox.getXGrid()*Arena.GRID_WIDTH + (int)(0.5*Arena.GRID_WIDTH) - 1 - i*defaultSpeed;
    			        	yPx = currentCellFox.getYGrid()*Arena.GRID_HEIGHT + (int)(0.5*Arena.GRID_HEIGHT) - 1;
    			    		// iterate through all the towers
    			    		for (Tower tower: Arena.getTowers()) {
    			    			if (tower.canAttack(xPx, yPx)) {
    			    				++cellCount;
    			    			}
    			    		}
    			    	}
	    				if (!checkedNodesFox.contains(currentCellFox) && !NodesFoxThisStep.contains(currentCellFox)) { // unfilled stuff before
	    					currentCellFox.setValue(cellCount);
	    					currentCellFox.setFromCell("Left");
	    					NodesFoxThisStep.add(currentCellFox);
	    				}
	    				else { // already filled
	    					if (cellCount < currentCellFox.getValue()) {
	    						currentCellFox.setValue(cellCount); // only update when we find a path receiving smaller attack
	    						currentCellFox.setFromCell("Left");
	    					}
	    				}
	    			}
    			}
    			// Up
    			if (node.getYGrid()!=0) { // can move up
    				currentCellFox = gridsInArenaFox[node.getXGrid()][node.getYGrid()-1]; // get the up node
    				if (Arena.getTower(currentCellFox.getXGrid(),currentCellFox.getYGrid()) == null) { // not tower cell
    					cellCount = node.getValue();
    					for (int i=0; i<numStepsEachCell; ++i) {
    			    		// the coordinates of the pixels the fox would go through
    			    		xPx = currentCellFox.getXGrid()*Arena.GRID_WIDTH + (int)(0.5*Arena.GRID_WIDTH) - 1;
    			        	yPx = currentCellFox.getYGrid()*Arena.GRID_HEIGHT + (int)(0.5*Arena.GRID_HEIGHT) - 1 + i*defaultSpeed;
    			    		// iterate through all the towers
    			    		for (Tower tower: Arena.getTowers()) {
    			    			if (tower.canAttack(xPx, yPx)) {
    			    				++cellCount;
    			    			}
    			    		}
    			    	}
	    				if (!checkedNodesFox.contains(currentCellFox) && !NodesFoxThisStep.contains(currentCellFox)) { // unfilled stuff before
	    					currentCellFox.setValue(cellCount);
	    					currentCellFox.setFromCell("Down");
	    					NodesFoxThisStep.add(currentCellFox);
	    				}
	    				else { // already filled
	    					if (cellCount < currentCellFox.getValue()) {
	    						currentCellFox.setValue(cellCount); // only update when we find a path receiving smaller attack
	    						currentCellFox.setFromCell("Down");
	    					}
	    				}
	    			}
    			}
    			// Down, also need to check it's not the end zone!
    			if (node.getYGrid()!=Arena.MAX_V_NUM_GRID-1) { // can move right
    				currentCellFox = gridsInArenaFox[node.getXGrid()][node.getYGrid()+1]; // get the down node
    				if (Arena.getTower(currentCellFox.getXGrid(),currentCellFox.getYGrid()) == null) { // not tower cell
    					cellCount = node.getValue();
    					for (int i=0; i<numStepsEachCell; ++i) {
    			    		// the coordinates of the pixels the fox would go through
    			    		xPx = currentCellFox.getXGrid()*Arena.GRID_WIDTH + (int)(0.5*Arena.GRID_WIDTH) - 1;
    			        	yPx = currentCellFox.getYGrid()*Arena.GRID_HEIGHT + (int)(0.5*Arena.GRID_HEIGHT) - 1 - i*defaultSpeed;
    			    		// iterate through all the towers
    			    		for (Tower tower: Arena.getTowers()) {
    			    			if (tower.canAttack(xPx, yPx)) {
    			    				++cellCount;
    			    			}
    			    		}
    			    	}
	    				if (!checkedNodesFox.contains(currentCellFox) && !NodesFoxThisStep.contains(currentCellFox)) { // unfilled stuff before
	    					currentCellFox.setValue(cellCount);
	    					currentCellFox.setFromCell("Up");
	    					NodesFoxThisStep.add(currentCellFox);
	    				}
	    				else { // already filled
	    					if (cellCount < currentCellFox.getValue()) {
	    						currentCellFox.setValue(cellCount); // only update when we find a path receiving smaller attack
	    						currentCellFox.setFromCell("Up");
	    					}
	    				}
    				}
    			}
    		}
    		frontierNodesFox.clear();
    		for (Cell node : NodesFoxThisStep) {
    			frontierNodesFox.add(node);
    			checkedNodesFox.add(node);
    		}
    	}
    }
    
    /**
	 * <p>
     * Method for determining where a Fox in a cell's center should move to.
     * @param xGrid The current x-coordinate (in grids) of the Fox.
     * @param yGrid The current y-coordinate (in grids) of the Fox.
     * @return String representing the moving direction of the Fox.
     */
    @Override
    public String determineWhichDirectionAtCenter(int xGrid, int yGrid) {
    	if (towerCount==0) {
    		return super.determineWhichDirectionAtCenter(xGrid, yGrid);
    	}
    	else { // some tower has been built
    		updateGridsFox();
    		return gridsInArenaFox[xGrid][yGrid].getFromCell();
    	}
    }
}
