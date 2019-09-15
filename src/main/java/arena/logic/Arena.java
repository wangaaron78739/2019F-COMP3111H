package arena.logic;

import java.util.ArrayList;

import monster.Fox;
import monster.Monster;
import tower.*;


enum TowerType {
    BASIC_TOWER,
    CATAPULT_TOWER,
    ICE_TOWER,
    LASER_TOWER
};

enum MonsterType {
    FOX,
    MONSTER,
    PENGUIN,
    UNICORN
};

public class Arena {

    final int ARENA_WIDTH;
    final int ARENA_HEIGHT;
    final int MAX_H_NUM_GRID;
    final int MAX_V_NUM_GRID;
    final int GRID_WIDTH;
    final int GRID_HEIGHT;
    final int INITIAL_RESOURCE_NUM;

    private GridCell grid[][] ;
    private ArrayList<Monster> monsters;
    private Resource resource;



    public void initArena() {
        resource = new Resource(INITIAL_RESOURCE_NUM);
        grid = new GridCell[GRID_HEIGHT][GRID_WIDTH];
        for (int i = 0; i < MAX_V_NUM_GRID; i++)
            for (int j = 0; j < MAX_H_NUM_GRID; j++) {
                grid[i][j] = new GridCell(i,j);
            }
    }


    public Arena(int ARENA_WIDTH, int ARENA_HEIGHT, int MAX_H_NUM_GRID, int MAX_V_NUM_GRID, int GRID_WIDTH, int GRID_HEIGHT, int INITIAL_RESOURCE_NUM) {
        this.ARENA_WIDTH = ARENA_WIDTH;
        this.ARENA_HEIGHT = ARENA_HEIGHT;
        this.MAX_H_NUM_GRID = MAX_H_NUM_GRID;
        this.MAX_V_NUM_GRID = MAX_V_NUM_GRID;
        this.GRID_WIDTH = GRID_WIDTH;
        this.GRID_HEIGHT = GRID_HEIGHT;
        this.INITIAL_RESOURCE_NUM = INITIAL_RESOURCE_NUM;
    }

    private boolean buildTowerPathValid(int x, int y) {
        //TODO:
        return true;
    }

    public boolean buildTower(int x, int y, TowerType tower) {
        if (grid[x][y].isTowerBuilt()) return false;
        if (monsterNumInCell(x,y) != 0) return false;
        if (!buildTowerPathValid(x,y)) return false;

        grid[x][y].buildTower(tower);
        return true;
    }

    private int monsterNumInCell(int x, int y) {
        int total = 0;
        for (Monster m : monsters) {
            if (m.getX() / GRID_WIDTH == x && m.getY() / GRID_HEIGHT == y) {
                total++;
            }
        }
        return total;
    }

}

class GridCell {
    private final int xGrid;
    private final int yGrid;
    private Tower tower = null;
    private boolean towerBuilt;

    enum LoggingType {
        MONSTER_GENERATED,
        MONSTER_ATTACKED
    }

    public GridCell(int xGrid, int yGrid) {
        this.xGrid = xGrid;
        this.yGrid = yGrid;
        this.towerBuilt = false;
    }

    public boolean isTowerBuilt() {
        return towerBuilt;
    }

    public boolean buildTower(TowerType t) {
        if (tower != null) return false;
        switch (t) {
            case BASIC_TOWER:
                tower = new BasicTower();
                break;
            case CATAPULT_TOWER:
                tower = new CatapultTower();
                break;
            case ICE_TOWER:
                tower = new IceTower();
                break;
            case LASER_TOWER:
                tower = new LaserTower();
                break;
        }
        return true;
    }
    public void logging() {
        System.out.println("<tower_type>@(<x>.<y>) -> <monster_type>@(<x>, <y>)");
    }
}