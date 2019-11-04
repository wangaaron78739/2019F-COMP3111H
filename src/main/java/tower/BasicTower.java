package tower;

import arena.logic.Arena;
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
    	 Resource.deductAmount(this.getUpgradeCost()); 
    }
    
    @Override
    public void implement(Monster target){
    	target.setHP(target.getHP()-this.getAttackPower());
    }
    
    @Override
    public boolean canAttack(int xPx, int yPx) {
    	return (Math.hypot(xPx - (getX()*Arena.GRID_WIDTH+Arena.GRID_WIDTH/2), yPx - (getY()*Arena.GRID_HEIGHT+Arena.GRID_HEIGHT/2))<=getShootingRange());
    }
}