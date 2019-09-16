package tower;

public class CatapultProjectile {
    private final int xStart;
    private final int yStart;

    public CatapultProjectile(int xStart, int yStart) {
        this.xStart = xStart;
        this.yStart = yStart;
    }

    public int getXStart() {
        return xStart;
    }

    public int getYStart() {
        return yStart;
    }
}
