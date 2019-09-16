package tower;

public class Projectile {
    private final int xSource;
    private final int ySource;
    private int xPos;
    private int yPos;
    private final String towerSource;

    public Projectile(int xSource, int ySource, String towerSource) {
        this.xSource = xSource;
        this.ySource = ySource;
        this.xPos = xSource;
        this.yPos = ySource;
        this.towerSource = towerSource;
    }

    public int getXSource() {
        return xSource;
    }

    public int getYSource() {
        return ySource;
    }

    public String getTowerSource() {
        return towerSource;
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
}
