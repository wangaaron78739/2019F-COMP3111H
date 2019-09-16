package arena.logic;

import java.util.ArrayList;

import monster.Monster;
import tower.*;

public class Arena {

    final int ARENA_WIDTH;
    final int ARENA_HEIGHT;
    final int MAX_H_NUM_GRID;
    final int MAX_V_NUM_GRID;
    final int GRID_WIDTH;
    final int GRID_HEIGHT;
    final int INITIAL_RESOURCE_NUM;
    final int UPDATE_INTERVAL;

    private static GridCell grid[][];
    private static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
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
        this.ARENA_WIDTH = ARENA_WIDTH;
        this.ARENA_HEIGHT = ARENA_HEIGHT;
        this.MAX_H_NUM_GRID = MAX_H_NUM_GRID;
        this.MAX_V_NUM_GRID = MAX_V_NUM_GRID;
        this.GRID_WIDTH = GRID_WIDTH;
        this.GRID_HEIGHT = GRID_HEIGHT;
        this.INITIAL_RESOURCE_NUM = INITIAL_RESOURCE_NUM;
        this.UPDATE_INTERVAL = UPDATE_INTERVAL;
    }

    private void logAttack(Projectile proj, Monster mon) {
        System.out.printf("%s@(%d.%d) -> %s@(%d, %d)\n",proj.getTowerSource(),proj.getXSource(),proj.getYSource(),mon.getType(),mon.getYPos(),mon.getYPos());
    }

    private void logMonsterCreator(Monster mon) {
        System.out.printf("%s:%d generated\n",mon.getType(),mon.getMaxHP());
    }

    private boolean buildTowerPathValid(int x, int y) {
        //TODO:
        return true;
    }

    public static Tower getTower(int x, int y) {
        if (grid[x][y].isTowerBuilt()) {
            return grid[x][y].getTower();
        }
        return null;
    }
    public boolean buildTower(int x, int y, String tower) {
        if (grid[x][y].isTowerBuilt()) return false;
        if (monsterNumInCell(x, y) != 0) return false;
        if (!buildTowerPathValid(x, y)) return false;

        grid[x][y].buildTower(tower);
        return true;
    }

    private int monsterNumInCell(int x, int y) {
        int total = 0;
        for (Monster m : monsters) {
            if (m.getYPos() / GRID_WIDTH == x && m.getYPos() / GRID_HEIGHT == y) {
                total++;
            }
        }
        return total;
    }

    public static GridCell[][] getGrid() {
        return grid;
    }

    public static ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public static ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public static Resource getResource() {
        return resource;
    }

    public static int getProjectileNum() {
        if (projectiles == null) return 0;
        return projectiles.size();
    }

    public static int getMonsterNum() {
        if (monsters == null) return 0;
        return monsters.size();
    }

    public static void addProjectile(int x, int y) {
        projectiles.add(new Projectile(x,y,"BasicTower"));
    }
}

class GridCell {
    private final int xGrid;
    private final int yGrid;
    private Tower tower = null;
    private boolean towerBuilt;

    public GridCell(int xGrid, int yGrid) {
        this.xGrid = xGrid;
        this.yGrid = yGrid;
        this.towerBuilt = false;
    }

    public boolean isTowerBuilt() {
        return tower != null;
    }

    public boolean buildTower(String t) {
        if (tower != null) return false;
        switch (t) {
            case "BasicTower":
                tower = new BasicTower(xGrid,yGrid);
                break;
            case "Catapult":
                tower = new Catapult(xGrid,yGrid);
                break;
            case "IceTower":
                tower = new IceTower(xGrid,yGrid);
                break;
            case "LaserTower":
                tower = new LaserTower(xGrid,yGrid);
                break;
        }
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
}