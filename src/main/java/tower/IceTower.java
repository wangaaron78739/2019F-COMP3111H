package tower;

import static arena.logic.ArenaConstants.*;
import arena.logic.Resource;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import monster.Monster;

/**
 * Class for IceTower, which can slow down the movement of the monster for a period of time
 * @author REN Jiming
 *
 */

public class IceTower extends Tower {
    private static final int baseSlowDownTime = 100;
    private static final int upgradeSlowDownTime = 50;
    private static final int baseBuildingCost = 100;
    private static final int baseShootingRange = 100;
    private static final String typeName = "Ice";

    /**
     * IceTower Constructor
     * @param x The x-coordinate of the cell where the BasicTower locates
     * @param y The y-coordinate of the cell where the BasicTower locates
     */
    public IceTower(int x, int y) {
        super(baseSlowDownTime, baseBuildingCost, baseShootingRange, x, y, typeName);
    }
    
    /**
     * Overriding method to slow down the movement of the target monster
     * @param target The monster whose speed to be decreased
     */
    @Override
    public void implement(Monster target){
    	target.setCoolDown(getAttackPower());
    }
    
    /**
     * Overriding method to upgrade the IceTower
     * <p>
     * Increase the time to freeze the target monster and consume certain amount of resource.
     * <p>
     * Check the remained resource beforehand; if there is no enough resource to exploit, give alert
     */
    @Override
    public void upgrade(){
    	if(Resource.canDeductAmount(getUpgradeCost())){
    	 setAttackPower(getAttackPower() + upgradeSlowDownTime);
    	 Resource.deductAmount(getUpgradeCost());
    	}
    	else{
    		Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            alert.setTitle("Cannot upgrade");
            alert.setHeaderText("No enough resource, you need " + getUpgradeCost() + " resource to upgrade");
            alert.showAndWait();
    	}
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
    	return (Math.hypot(
    			xPx - (getX() * GRID_WIDTH + GRID_WIDTH/2), 
    			yPx - (getY() * GRID_HEIGHT + GRID_HEIGHT/2))
    			<= getShootingRange());
    }
}