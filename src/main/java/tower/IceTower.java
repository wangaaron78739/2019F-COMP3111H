package tower;

import arena.logic.Resource;
import monster.Monster;

public class IceTower extends Tower {
    private static final int baseAttackPower = 100;
    private static final int baseBuildingCost = 100;
    private static final int baseShootingRange = 100;
    private static final int baseAttackCooldown = 100;
    private static final String typeName = "Ice";

    public IceTower(int x, int y) {
        super(baseAttackPower, baseBuildingCost, baseShootingRange, baseAttackCooldown, x, y, typeName);
    }
    
}