package arena.logic;

import java.util.ArrayList;
import java.util.LinkedList;

import monster.*;
import tower.*;

public class Arena {

    private static int ARENA_WIDTH;
    private static int ARENA_HEIGHT;
    private static int MAX_H_NUM_GRID;
    private static int MAX_V_NUM_GRID;
    private static int GRID_WIDTH;
    private static int GRID_HEIGHT;
    private static int INITIAL_RESOURCE_NUM;
    private static int UPDATE_INTERVAL;

    private static GridCell grid[][];
    private static ArrayList<LaserProjectile> projectiles = new ArrayList<LaserProjectile>();
    private static ArrayList<Monster> monsters = new ArrayList<Monster>();
    private static Resource resource;

    public void initArena() {
        resource = new Resource(INITIAL_RESOURCE_NUM);
        grid = new GridCell[GRID_HEIGHT][GRID_WIDTH];
        for (int i = 0; i < MAX_V_NUM_GRID; i++)
            for (int j = 0; j < MAX_H_NUM_GRID; j++) {
                grid[i][j] = new GridCell(i, j);
            }
    }


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

//    private void logAttack(LaserProjectile proj, Monster mon) {
//        System.out.printf("%s@(%d.%d) -> %s@(%d, %d)\n",proj.getTowerSource(),proj.getXStart(),proj.getYStart(),mon.getType(),mon.getYPos(),mon.getYPos());
//    }

    private void logMonsterCreated(Monster mon) {
        System.out.printf("%s:%d generated\n",mon.getType(),mon.getMaxHP());
    }

    private boolean buildTowerPathValid(int x, int y) {
        boolean[][] reachable = new boolean[GRID_HEIGHT][GRID_WIDTH];
        class Cell {
            int _x; int _y;
            Cell(int _x, int _y) {this._x = _x; this._y = _y;}
            ArrayList<Cell> adjCells() {
                ArrayList<Cell> result = new ArrayList<Cell>();
                if (_x>0 && !reachable[_y][_x-1]) {
                    if (Arena.getTower(_x-1,_y) == null && !(_x == x && _y == y)) {
                        result.add(new Cell(_x-1,_y));
                    }
                }
                if (_y>0 && !reachable[_y-1][_x]) {
                    if (Arena.getTower(_x,_y-1) == null && !(_x == x && _y == y)) {
                        result.add(new Cell(_x,_y-1));
                    }
                }
                if (_x<MAX_H_NUM_GRID-1 && !reachable[_y][_x+1]) {
                    if (Arena.getTower(_x+1,_y) == null && !(_x == x && _y == y)) {
                        result.add(new Cell(_x+1,_y));
                    }
                }
                if (_y<MAX_H_NUM_GRID-1 && !reachable[_y+1][_x]) {
                    if (Arena.getTower(_x,_y+1) == null && !(_x == x && _y == y)) {
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
            int m_y = m.getYPos() / GRID_HEIGHT;
            int m_x = m.getXPos() / GRID_WIDTH;
            assert(m_y >= 0 && m_y < MAX_V_NUM_GRID);
            assert(m_x >= 0 && m_x < MAX_H_NUM_GRID);
            if (!reachable[m_y][m_x]) return false;
        }
        return true;
    }

    public static Tower getTower(int x, int y) {
        if (grid[y][x].isTowerBuilt()) {
            return grid[y][x].getTower();
        }
        return null;
    }
    public static GridCell getCell(int x, int y) {
        return grid[y][x];
    }

    public boolean buildTower(int x, int y, String towerType) {
        if (grid[y][x].isTowerBuilt()) return false;
        if (monsterNumInCell(x, y) != 0) return false;
        if (!buildTowerPathValid(x, y)) return false;
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
        grid[y][x].buildTower(tower);
        Resource.deductAmount(tower.getBuildingCost());
        return true;
    }

    private int monsterNumInCell(int x, int y) {
        int total = 0;
        for (Monster m : monsters) {
            if (m.getXPos() / GRID_WIDTH == x && m.getYPos() / GRID_HEIGHT == y) {
                total++;
            }
        }
        return total;
    }

    public static GridCell[][] getGrid() {
        return grid;
    }

    public static ArrayList<LaserProjectile> getProjectiles() {
        return projectiles;
    }

    public static ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public static Resource getResource() {
        return resource;
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

    public static void deleteTowerAt(int x, int y) {
        grid[y][x].deleteTower();
    }
    public static void upgradeTowerAt(int x, int y) {
        grid[y][x].upgradeTower();
    }

    public static void nextFrame() {

    }
}

class GridCell {
    private final int xGrid;
    private final int yGrid;
    private Tower tower = null;

    public GridCell(int xGrid, int yGrid) {
        this.xGrid = xGrid;
        this.yGrid = yGrid;
    }

    public boolean isTowerBuilt() {
        return tower != null;
    }

    public boolean buildTower(Tower t) {
        if (tower != null) return false;
        tower = t;
        return true;
    }

    public int getxGrid() {
        return xGrid;
    }

    public int getyGrid() {
        return yGrid;
    }

    public Tower getTower() {
        return tower;
    }

    public void deleteTower() {
        tower = null;
    }
    public void upgradeTower() {
        if (tower != null) tower.upgrade();
    }
}