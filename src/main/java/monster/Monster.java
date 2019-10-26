package monster;

import arena.logic.Arena;

public class Monster {
    private float xPx;
    private float yPx;
    private int HP;
    private int speed;
    private final int maxHP;
    private final String type;
    private static int monsterNum;

    public Monster(float x, float y, int speed, int maxHP, String type) {
        this.xPx = x;
        this.yPx = y;
        this.HP = maxHP;
        this.speed = speed;
        this.maxHP = maxHP;
        this.type = type;
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

    public int getXGrid() {
        return (int)(xPx/ Arena.GRID_WIDTH) ;
    }

    public int getYGrid() {
        return (int)(yPx/ Arena.GRID_HEIGHT) ;
    }
    public void setYPx(int yPx) {
        this.yPx = yPx;
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

    public void move() {

    }
}
