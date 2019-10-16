package arena.logic;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Arrays;

import monster.*;
import tower.*;

public class Arena {

    public static int ARENA_WIDTH;
    public static int ARENA_HEIGHT;
    public static int MAX_H_NUM_GRID;
    public static int MAX_V_NUM_GRID;
    public static int GRID_WIDTH;
    public static int GRID_HEIGHT;
    public static int INITIAL_RESOURCE_NUM;
    public static int UPDATE_INTERVAL;

    private static String towerBuilt[][];
//    private static LinkedList<LaserProjectile> projectiles = new LinkedList<LaserProjectile>();
    private static LinkedList<Monster> monsters = new LinkedList<Monster>();
    private static LinkedList<Tower> towers = new LinkedList<Tower>();
    private static Resource resource;

    private static int FrameCount = 0;

    public void initArena() {
        resource = new Resource(INITIAL_RESOURCE_NUM);
        towerBuilt = new String[GRID_HEIGHT][GRID_WIDTH];
        for (String[] row: towerBuilt) {
            Arrays.fill(row,"");
        }
    }

    /**
     * Arena Constructor
     * @param ARENA_WIDTH The width of the arena width in pixels
     * @param ARENA_HEIGHT The height of the arena width in pixels
     * @param MAX_H_NUM_GRID The number of grid cells in each row of the arena
     * @param MAX_V_NUM_GRID The number of grid cells in each column of the arena
     * @param GRID_WIDTH The width of each grid cell in pixels
     * @param GRID_HEIGHT The height of each grid cell in pixels
     * @param INITIAL_RESOURCE_NUM The initial amount of resource
     * @param UPDATE_INTERVAL The update interval of the game in ms
     */
    public Arena(int ARENA_WIDTH, int ARENA_HEIGHT, int MAX_H_NUM_GRID, int MAX_V_NUM_GRID, int GRID_WIDTH, int GRID_HEIGHT, int INITIAL_RESOURCE_NUM, int UPDATE_INTERVAL) {
        Arena.ARENA_WIDTH = ARENA_WIDTH;
        Arena.ARENA_HEIGHT = ARENA_HEIGHT;
        Arena.MAX_H_NUM_GRID = MAX_H_NUM_GRID;
        Arena.MAX_V_NUM_GRID = MAX_V_NUM_GRID;
        Arena.GRID_WIDTH = GRID_WIDTH;
        Arena.GRID_HEIGHT = GRID_HEIGHT;
        Arena.INITIAL_RESOURCE_NUM = INITIAL_RESOURCE_NUM;
        Arena.UPDATE_INTERVAL = UPDATE_INTERVAL;
    }

    private void logAttack(Tower tower, Monster mon) {
        System.out.printf("%s@(%d.%d) -> %s@(%d, %d)\n",tower,tower.getX(),tower.getY(),mon.getType(),mon.getYPx(),mon.getYPx());
    }

    private void logMonsterCreated(Monster mon) {
        System.out.printf("%s:%d generated\n",mon.getType(),mon.getMaxHP());
    }

    /**
     * Method to check whether building a tower in cel (x,y) is valid,
     * taking into account whether there's a tower or monster in that cell and if
     * building the tower will block off the end zone
     * @param x The x coordinate of the target cell
     * @param y The y coordinate of the target cell
     * @return true iff a tower can be built in the target cell
     */
    private boolean buildTowerPathValid(int x, int y) {
        boolean[][] reachable = new boolean[GRID_HEIGHT][GRID_WIDTH];
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
     * @return String tower typ eo ftower in cell (x,y)
     */
    public static String towerBuiltType(int x, int y) {
        return towerBuilt[y][x];
    }

    /**
     * Sets the
     * @param x The x coordinate of the target cell
     * @param y The y coordinate of the target cell
     * @return String tower typ eo ftower in cell (x,y)
     */
    public static void setTowerBuilt(int x, int y, String tower) {
        towerBuilt[y][x] = tower;
    }
    public boolean buildTower(int x, int y, String towerType) {
        if (towerBuilt(x,y)) return false;
        if (monsterNumInCell(x, y) != 0) return false;
        if (!buildTowerPathValid(x, y)) return false;
        if (x == MAX_H_NUM_GRID-1 && y == MAX_V_NUM_GRID-1) return false;
        Tower tower;
        switch (towerType) {
            case "BasicTower":
                tower = new BasicTower(x,y);
                break;
            case "Catapult":
                tower = new Catapult(x,y);
                break;
            case "IceTower":
                tower = new IceTower(x,y);
                break;
            case "LaserTower":
                tower = new LaserTower(x,y);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + towerType);
        }
        if (!Resource.canDeductAmount(tower.getBuildingCost())) return false;
        towers.add(tower);
        setTowerBuilt(x,y,towerType);
        Resource.deductAmount(tower.getBuildingCost());
        return true;
    }

    private int monsterNumInCell(int x, int y) {
        int total = 0;
        for (Monster m : monsters) {
            if (m.getXPx() / GRID_WIDTH == x && m.getYPx() / GRID_HEIGHT == y) {
                total++;
            }
        }
        return total;
    }

//    public static LinkedList<LaserProjectile> getProjectiles() {
//        return projectiles;
//    }

    public static LinkedList<Monster> getMonsters() {
        return monsters;
    }

    public static Resource getResource() {
        return resource;
    }

    public static LinkedList<Tower> getTowers() {
        return towers;
    }

//    public static int getProjectileNum() {
//        if (projectiles == null) return 0;
//        return projectiles.size();
//    }

    public static int getMonsterNum() {
        if (monsters == null) return 0;
        return monsters.size();
    }

    public static boolean addMonster(int x, int y, String monster) {
        if (Arena.getTower(x/GRID_WIDTH,y/GRID_HEIGHT) != null) {
            return false;
        }
        switch (monster) {
            case "Fox":
                monsters.add(new Fox(x,y));
                break;
            case "Penguin":
                monsters.add(new Penguin(x,y));
                break;
            case "Unicorn":
                monsters.add(new Unicorn(x,y));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + monster);
        }
        return true;
    }

    static boolean towerIsAt(int x, int y, Tower t) {
        return t.getX() == x && t.getY() == y;
    }

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
    public static void upgradeTowerAt(int x, int y) {
        if (towerBuilt(x,y)) {
            for (Tower t: towers) {
                if (towerIsAt(x,y,t)) {
                    t.upgrade();
                }
            }
        }
    }

    public static void nextFrame() {
        FrameCount++;
        towers.forEach(t -> t.shoot());
        // TODO: Give resource for killing monsters
        monsters.removeIf(m->m.getHP()<=0);
        monsters.forEach(m -> m.move());

    }

}