package arena.logic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

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
//    private static ArrayList<LaserProjectile> projectiles = new ArrayList<LaserProjectile>();
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

    private boolean buildTowerPathValid(int x, int y) {
        boolean[][] reachable = new boolean[GRID_HEIGHT][GRID_WIDTH];
        class Cell {
            int _x; int _y;
            Cell(int _x, int _y) {this._x = _x; this._y = _y;}
            ArrayList<Cell> adjCells() {
                ArrayList<Cell> result = new ArrayList<Cell>();
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

    public static Tower getTower(int x, int y) {
        if (towerBuilt(x,y)) {
            for (Tower t: towers) {
                if (towerIsAt(x,y,t)) return t;
            }
        }
        return null;
    }

    public static boolean towerBuilt(int x, int y) {
        return !towerBuilt[y][x].equals("");
    }
    public static String towerBuiltType(int x, int y) {
        return towerBuilt[y][x];
    }
    public static void setTowerBuilt(int x, int y, String tower) {
        towerBuilt[y][x] = tower;
    }
    public boolean buildTower(int x, int y, String towerType) {
        if (towerBuilt(x,y)) return false;
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

//    public static ArrayList<LaserProjectile> getProjectiles() {
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
        for(Tower t: towers) {
            t.shoot();
        }
        for(Monster m: monsters) {
            m.move();
        }
    }
}