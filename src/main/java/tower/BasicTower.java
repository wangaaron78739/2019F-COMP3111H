package tower;

import static arena.logic.ArenaConstants.*;
import arena.logic.Resource;
import monster.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * <p>
 * Class for BasicTower
 * @author REN Jiming
 *
 */

public class BasicTower extends Tower {
    private static final int baseAttackPower = 30;
    private static final int upgradeAttackPower = 15;
    private static final int baseBuildingCost = 100;
    private static final int baseShootingRange = 65;
    private static final String typeName = "Basic";
    
    /**
     * BasicTower Constructor
     * @param x The x-coordinate of the cell where the BasicTower locates
     * @param y The y-coordinate of the cell where the BasicTower locates
     */
    public BasicTower(int x, int y) {
        super(baseAttackPower, baseBuildingCost, baseShootingRange, x, y, typeName);
    }
    
   /**
    * Overriding method to decrease HP of the monster
    * @param target The monster whose HP to be decreased
    */
    @Override
    public void implement(Monster target){
    	target.setHP(target.getHP() - getAttackPower());
    }
    
    /**
     * Overriding method to upgrade the BasicTower
     * <p>
     * Increase the attack power of the tower and consume certain amount of resource.
     * <p>
     * Check the remained resource beforehand; if there is no enough resource to exploit, give alert
     */
    @Override
    public void upgrade(){
    	if(Resource.canDeductAmount(getUpgradeCost())){
    	 setAttackPower(getAttackPower() + upgradeAttackPower);
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
     * Method for determining whether the BasicTower can attack a given pixel,
     * i.e. whether the pixel is inside its range.
     * @param xPx The x-coordinate(in pixels) of the pixel.
     * @param yPx The y-coordinate(in pixels) of the pixel.
     * @return Boolean value showing whether the BasicTower can attack the given pixel.
     */
    @Override
    public boolean canAttack(int xPx, int yPx) {
    	return (Math.hypot(
    			xPx - (getX() * GRID_WIDTH + GRID_WIDTH/2), 
    			yPx - (getY() * GRID_HEIGHT + GRID_HEIGHT/2))
    			<= getShootingRange());
    }
}