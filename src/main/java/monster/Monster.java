package monster;

public class Monster {
    private int xPos;
    private int yPos;
    private int HP;
    private int speed;
    private final int maxHP;
    private final String type;
    private static int monsterNum;

    public Monster(int x, int y, int speed, int maxHP, String type) {
        this.xPos = x;
        this.yPos = y;
        this.HP = maxHP;
        this.speed = speed;
        this.maxHP = maxHP;
        this.type = type;
    }

    public int getXPos() {
        return xPos;
    }

    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public void setYPos(int yPos) {
        this.yPos = yPos;
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
}
