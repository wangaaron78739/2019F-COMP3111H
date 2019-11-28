package arena.logic;

import java.util.LinkedList;
import java.util.Arrays;
import java.util.Random;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import monster.*;
import tower.*;


import static arena.logic.ArenaConstants.*;

/**
 * Handles the logic of the arena.
 * @author Aaron WANG
 */
public class Arena {

    private static String[][] towerBuilt = new String[MAX_H_NUM_GRID][MAX_V_NUM_GRID];
    private static boolean[][] towerShot = new boolean[MAX_H_NUM_GRID][MAX_V_NUM_GRID];
    private static LinkedList<Monster> monsters = new LinkedList<Monster>();
    private static LinkedList<Tower> towers = new LinkedList<Tower>();
    private static Random rand = new Random();
    
    /**
     * <p>
     * An integer representing the x-coordinate (in grids) of the Monster's start zone.
     * <p>
     * Monster would always be generated from that zone.
     */
    private static int MonsterStartXGrid = 2;
    /**
     * <p>
     * An integer representing the y-coordinate (in grids) of the Monster's start zone.
     * <p>
     * Monster would always be generated from that zone.
     */
    private static int MonsterStartYGrid = 0;

    private static int FrameCount = -1; // changed to -1, so it can generate monster from the starting time
    private static int EachStageCount = 500; // larger stage, stronger monster
    private static boolean gameStarted = false;

    private static final int monsterKillResource = 100;

    private Arena() {}

    /**
     * Initialize Arena
     */
    public static void initArena() {
        Resource.setResourceAmount(INITIAL_RESOURCE_NUM);
        towerBuilt = new String[MAX_H_NUM_GRID][MAX_V_NUM_GRID];
        towerShot = new boolean[MAX_H_NUM_GRID][MAX_V_NUM_GRID];
        for (String[] row: towerBuilt) {
            Arrays.fill(row,"");
        }
        monsters = new LinkedList<Monster>();
        towers = new LinkedList<Tower>();
    }

    /**
     * Log attack from tower to monster
     * @param tower the tower attacking
     * @param mon the monster being attacked
     */
    public static void logAttack(Tower tower, Monster mon) {
        if (mon.getType().equals("Death")) return;
        System.out.printf("%s@(%d.%d) -> %s@(%d, %d)\n",tower.getType(),tower.getX()*GRID_WIDTH+GRID_WIDTH/2,tower.getY()*GRID_HEIGHT+GRID_HEIGHT/2,mon.getType(),(int)mon.getyPx(),(int)mon.getyPx());
    }

    /**
     * Log monster created
     * @param mon the monster being created
     */
    public static void logMonsterCreated(Monster mon) {
        System.out.printf("%s:%d generated\n",mon.getType(),mon.getMaxHP());
    }

    /**
     * Log tower upgrade
     * @param tower the tower bring upgraded
     */
    public static void logTowerUpgrade(Tower tower) {
        System.out.printf("%s tower is being upgraded\n",tower.getType());
    }

    /**
     * Log tower upgrade failed message
     * @param tower the tower that failed to be upgraded
     */
    public static void logTowerUpgradeFailed(Tower tower) {
        System.out.printf("not enough resource to upgrade %s tower\n",tower.getType());
    }

    /**
     * Method to check whether building a tower in cel (x,y) is valid,
     * taking into account whether there's a tower or monster in that cell and if
     * building the tower will block off the end zone
     * @param x The x coordinate of the target cell
     * @param y The y coordinate of the target cell
     * @return true iff a tower can be built in the target cell
     */
    private static boolean buildTowerPathValid(int x, int y) {
        boolean[][] reachable = new boolean[MAX_H_NUM_GRID][MAX_V_NUM_GRID];
        class Cell {
            int _x; int _y;
            Cell(int _x, int _y) {this._x = _x; this._y = _y;}
            LinkedList<Cell> adjCells() {
                LinkedList<Cell> result = new LinkedList<Cell>();
                if (_x>0 && !reachable[_y][_x-1]) {
                    if (!towerBuilt(_x-1,_y) && !(_x == x && _y == y)) {
                        result.add(new Cell(_x-1,_y));
                    }
                }
                if (_y>0 && !reachable[_y-1][_x]) {
                    if (!towerBuilt(_x,_y-1) && !(_x == x && _y == y)) {
                        result.add(new Cell(_x,_y-1));
                    }
                }
                if (_x<MAX_H_NUM_GRID-1 && !reachable[_y][_x+1]) {
                    if (!towerBuilt(_x+1,_y) && !(_x == x && _y == y)) {
                        result.add(new Cell(_x+1,_y));
                    }
                }
                if (_y<MAX_H_NUM_GRID-1 && !reachable[_y+1][_x]) {
                    if (!towerBuilt(_x,_y+1) && !(_x == x && _y == y)) {
                        result.add(new Cell(_x,_y+1));
                    }
                }
                return result;
            }
        };
        //TODO:
        LinkedList<Cell> cellsToVisit = new LinkedList<>();
        Cell endZone = new Cell(MAX_H_NUM_GRID-1,MAX_V_NUM_GRID-1);
        cellsToVisit.add(endZone);
        while (cellsToVisit.size() > 0) {
            Cell c = cellsToVisit.get(0);
            reachable[c._y][c._x] = true;
            cellsToVisit.remove(0);
            cellsToVisit.addAll(c.adjCells());
        }
        for (Monster m: monsters) {
            System.out.println(m);
            int m_x = m.getXGrid();
            int m_y = m.getYGrid();
            assert(m_y >= 0 && m_y < MAX_V_NUM_GRID);
            assert(m_x >= 0 && m_x < MAX_H_NUM_GRID);
            if (!reachable[m_y][m_x]) return false;
        }
        return true;
    }

    /**
     * Returns the Tower object in the cell (x,y), returns null if no tower is
     * built in that cell
     * @param x The x coordinate of the target cell
     * @param y The y coordinate of the target cell
     * @return Tower The Tower object in target cell (null if no tower)
     */
    public static Tower getTower(int x, int y) {
        if (x < 0 || x >=GRID_HEIGHT || y < 0 || y >= GRID_WIDTH) return null;
        if (towerBuilt(x,y)) {
            for (Tower t: towers) {
                if (towerIsAt(x,y,t)) return t;
            }
        }
        return null;
    }

    /**
     * Checks whether the there is a tower in cell (x,y)
     * @param x The x coordinate of the target cell
     * @param y The y coordinate of the target cell
     * @return boolean Returns true iff there a tower in the target cell
     */
    public static boolean towerBuilt(int x, int y) {
        return !towerBuilt[y][x].equals("");
    }

    /**
     * Returns the tower type of the tower in cell (x,y)
     * @param x The x coordinate of the target cell
     * @param y The y coordinate of the target cell
     * @return String tower type of tower in cell (x,y)
     */
    public static String towerBuiltType(int x, int y) {
        return towerBuilt[y][x];
    }

    /**
     * Returns the shot status of tower in cell (x,y)
     * @param x The x coordinate of the target cell
     * @param y The y coordinate of the target cell
     * @return boolean shot status of tower in cell (x,y)
     */
    public static boolean towerShot(int x, int y) {
        return towerShot[y][x];
    }

    /**
     * Set the shot status of tower in cell (x,y)
     * @param x The x coordinate of the target cell
     * @param y The y coordinate of the target cell
     */
    public static void setTowerShot(int x, int y) {
        towerShot[y][x] = true;
    }

    /**
     * Reset all tower shot to false (to display regular image of towers)
     */
    private static void resetShot() {
        towerShot = new boolean[MAX_H_NUM_GRID][MAX_V_NUM_GRID];
    }

    /**
     * Sets the tower type of the tower built in cell (x,y)
     * @param x The x coordinate of the target cell
     * @param y The y coordinate of the target cell
     * @param tower The Tower type of tower in cell (x,y)
     */
    public static void setTowerBuilt(int x, int y, String tower) {
        towerBuilt[y][x] = tower;
    }

    /**
     * Method to build tower in cell (x,y) of type towerType
     * @param x x coordinate of the target cell
     * @param y y coordinate of the target cell
     * @param towerType towerType of the desired tower
     * @return boolean of whether successful (true if successfully built)
     */
    public static boolean buildTower(int x, int y, String towerType) {
        if (x<0 || y < 0) return false;
        Tower tower;
        switch (towerType) {
            case "Basic":
                tower = new BasicTower(x,y);
                break;
            case "Catapult":
                tower = new Catapult(x,y);
                break;
            case "Ice":
                tower = new IceTower(x,y);
                break;
            case "Laser":
                tower = new LaserTower(x,y);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + towerType);
        }
        if (!Resource.canDeductAmount(tower.getBuildingCost())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            alert.setTitle("Not Enough Resources");
            alert.setHeaderText("You do not have enough resources.");
            alert.showAndWait();
            return false;
        }
        if (towerBuilt(x,y)) return false;
        if (monsterNumInCell(x, y) != 0) return false;
        if (!buildTowerPathValid(x, y)) return false;
        if (x == MAX_H_NUM_GRID-1 && y == MAX_V_NUM_GRID-1) return false;
        if (x == 2 && y == 0) return false;
        towers.add(tower);
        setTowerBuilt(x,y,towerType);
        Resource.deductAmount(tower.getBuildingCost());
        return true;
    }

    private static int monsterNumInCell(int x, int y) {
        int xLow = x*GRID_WIDTH-MONSTER_WIDTH/2;
        int xHigh = (x+1)*GRID_WIDTH+MONSTER_WIDTH/2;
        int yLow = y*GRID_HEIGHT-MONSTER_HEIGHT/2;
        int yHigh = (y+1)*GRID_HEIGHT+MONSTER_HEIGHT/2;
        int total = 0;
        for (Monster m : monsters) {
            if (m.getxPx()>xLow && m.getxPx()<xHigh && m.getyPx()>yLow && m.getyPx()<yHigh) {
                total++;
            }
        }
        return total;
    }

    /**
     * Gets the list of monsters in the Arena
     * @return List of monsters in the arena
     */
    public static LinkedList<Monster> getMonsters() {
        return monsters;
    }

    /**
     * Gets the list of towers in the arena
     * @return List of towers in the arena
     */
    public static LinkedList<Tower> getTowers() {
        return towers;
    }

    /**
     * Gets the number of monsters in the arena
     * @return The number of monsters in the arena
     */
    public static int getMonsterNum() {
        if (monsters == null) return 0;
        return monsters.size();
    }
    
    /**
     * Function for getting the current stage of difficulty.
     * There would be five stages in this game.
     * @return The number of stage represent the difficulty.
     */
    public static int getStage() { 
    	int index = FrameCount / EachStageCount;
    	switch (index) {
    		case 0:
    		case 1:
    		case 2:
    		case 3:
    			return index+1;
    		default:
    			return 5;
    	}
    }

    /**
     * Function for adding Monster to the game.
     * @param x The x-coordinate (in pixels) that we want the Monster to be at.
     * @param y The y-coordinate (in pixels) that we want the Monster to be at.
     * @param monster The type of the Monster.
     * @return A boolean value represent whether we can add the monster successfully.
     */
    public static boolean addMonster(int x, int y, String monster) {
    	if (Arena.getTower(x/GRID_WIDTH,y/GRID_HEIGHT) != null) {
    		return false;
        }
        Monster m;
        switch (monster) {
            case "Fox":
                m = new Fox(x,y,getStage());
                break;
            case "Penguin":
                m = new Penguin(x,y,getStage());
                break;
            case "Unicorn":
                m = new Unicorn(x,y,getStage());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + monster);
        }
        logMonsterCreated(m);
        monsters.add(m);
        return true;
    }

    private static boolean towerIsAt(int x, int y, Tower t) {
        return t.getX() == x && t.getY() == y;
    }

    /**
     * Delete tower at cell (x,y)
     * @param x X coordinate of tower to be deleted
     * @param y Y coordinate of tower to be deleted
     */
    public static void deleteTowerAt(int x, int y) {
        if (towerBuilt(x,y)) {
            for (Tower t: towers) {
                if (towerIsAt(x,y,t)) {
                    towers.remove(t);
                    setTowerBuilt(x,y,"");
                    return;
                }
            }
        }
    }

    /**
     * Upgrade tower at cell (x,y)
     * @param x X coordinate of tower to be deleted
     * @param y Y coordinate of tower to be deleted
     */
    public static void upgradeTowerAt(int x, int y) {
        if (towerBuilt(x,y)) {
            for (Tower t: towers) {
                if (towerIsAt(x,y,t)) {
                    if (Resource.canDeductAmount(t.getUpgradeCost())) {
                        t.upgrade();
                        logTowerUpgrade(t);
                    }else {
                        logTowerUpgradeFailed(t);
                    }
                }
            }
        }
    }

    /**
     * Method to increment the frame count and resolve the actions in the frame
     */
    public static void nextFrame() {
        if (!gameStarted) return;
        FrameCount++;
        final String[] names = {"Fox", "Penguin", "Unicorn"};
        // Create random monster
        if ((FrameCount%50)==0) {
        	for (int i=0; i<=rand.nextInt(2); ++i) // one or three monster
        		addMonster(MonsterStartXGrid*GRID_WIDTH+(int)(0.5*GRID_WIDTH)-1,MonsterStartYGrid*GRID_HEIGHT+(int)(0.5*GRID_HEIGHT)-1, names[rand.nextInt(names.length)]);
        		//addMonster(60,60,"Fox");
        }
        resetShot();
        monsters.forEach(Monster::resetHit);
        towers.forEach(Tower::shoot);
        monsters.removeIf(m->m.getType()=="Death"); // remove all the currently dead monsters
        monsters.forEach(m-> {
            if (m.getHP() <= 0) {
                Resource.addResourceAmount(monsterKillResource);
                m.setTypeDeath(); // image of monster replaced by collision.png
            }
        });
        Monster.updateGrids();
        monsters.forEach(Monster::move);
        if (Monster.gameEnds())
        	System.out.println("Game over!");
    }

    /**
     * Method to start the game
     */
    public static void startGame() {
        gameStarted = true;
    }

    /**
     * Get the current frame count
     * @return Frame Count
     */
    public static int getFrameCount() {
        return FrameCount;
    }

    /**
     * Check wheter the game has started
     * @return bool of whether the game has started (true = started)
     */
    public static boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Setter method for frame count
     * @param frameCount FrameCount value to be set
     */
    public static void setFrameCount(int frameCount) {
        FrameCount = frameCount;
    }

}
