package tower;


public class IceTower extends Tower {
    private static final int baseAttackPower = 100;
    private static final int baseBuildingCost = 100;
    private static final int baseShootingRange = 100;
    private static final String typeName = "IceTower";

    public IceTower(int x, int y) {
        super(baseAttackPower, baseBuildingCost, baseShootingRange, x, y, typeName);
    }
}