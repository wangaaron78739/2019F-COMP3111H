package tower;

import arena.logic.Resource;
import monster.*;

public class BasicTower extends Tower {
    private static final int baseAttackPower = 100;
    private static final int baseBuildingCost = 100;
    private static final int baseShootingRange = 65;
    private static final int baseAttackCooldown = 50;
    private static final String typeName = "Basic";

    public BasicTower(int x, int y) {
        super(baseAttackPower, baseBuildingCost, baseShootingRange, baseAttackCooldown, x, y, typeName);
    }
    
    @Override
    public void upgrade(){
    	 setAttackPower(this.getAttackPower() + 60);
    	 Resource.setResourceAmount(Resource.getResourceAmount()-this.getUpgradeCost()); 
    }
    
    @Override
    public void implement(Monster target){
    	target.setHP(target.getHP()-this.getAttackPower());
    }
}