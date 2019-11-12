package tower;

import static arena.logic.ArenaConstants.*;
import arena.logic.Resource;
import monster.Monster;


public class IceTower extends Tower {
    private static final int baseAttackPower = 100;
    private static final int baseBuildingCost = 100;
    private static final int baseShootingRange = 100;
    private static final int baseAttackCooldown = 100;
    private static final String typeName = "Ice";

    public IceTower(int x, int y) {
        super(baseAttackPower, baseBuildingCost, baseShootingRange, x, y, typeName);
    }
    
    /**
     * Method for determining whether the IceTower can attack a given pixel,
     * i.e. whether the pixel is inside its range.
     * @param xPx The x-coordinate(in pixels) of the pixel.
     * @param yPx The y-coordinate(in pixels) of the pixel.
     * @return Boolean value showing whether the IceTower can attack the given pixel.
     */
    @Override
    public boolean canAttack(int xPx, int yPx) {
    	return (Math.hypot(xPx - (getX()*GRID_WIDTH+GRID_WIDTH/2), yPx - (getY()*GRID_HEIGHT+GRID_HEIGHT/2))<=getShootingRange());
    }
    
    @Override
    public void upgrade(){
        setAttackPower(this.getAttackPower() + 60);
    	 Resource.deductAmount(this.getUpgradeCost()); 
    }
    
    @Override
    public void implement(Monster target){
    	target.setCoolDown(baseAttackPower);
    }
}