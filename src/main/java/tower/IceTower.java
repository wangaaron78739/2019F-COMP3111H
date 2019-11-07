package tower;

import arena.logic.Arena;
import arena.logic.Resource;
import monster.Monster;

public class IceTower extends Tower {
    private static int baseAttackPower = 100;
    private static final int baseBuildingCost = 100;
    private static final int baseShootingRange = 100;
    private static final int baseAttackCooldown = 100;
    private static final String typeName = "Ice";

    public IceTower(int x, int y) {
        super(baseAttackPower, baseBuildingCost, baseShootingRange, baseAttackCooldown, x, y, typeName);
    }
    
    @Override
    public boolean canAttack(int xPx, int yPx) {
    	return (Math.hypot(xPx - (getX()*Arena.GRID_WIDTH+Arena.GRID_WIDTH/2), yPx - (getY()*Arena.GRID_HEIGHT+Arena.GRID_HEIGHT/2))<=getShootingRange());
    }
    
    @Override
    public void upgrade(){
    	 baseAttackPower += 60;
    	 Resource.deductAmount(this.getUpgradeCost()); 
    }
    
    @Override
    public void implement(Monster target){
    	target.setCoolDown(baseAttackPower);
    }
}