package tower;

/**
 * <p>
 * Class for LaserTower, which consumes some resources to attack a monster.
 * <p>
 * The LaserTower draws a line from the center to the tower to the monster and extends beyond until it reaches the edge of the Arena. 
 * <p>
 * Furthermore, it brings damage to all monsters on the line or within 3px away from the line.
 * @author REN Jiming
 */

import java.util.HashMap;
import java.util.ArrayList;
import arena.logic.Arena;
import arena.logic.Resource;
import static arena.logic.ArenaConstants.*;
import monster.Monster;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.Line;

public class LaserTower extends Tower {
    private static final int baseAttackPower = 20;
    private static final int upgradeAttackPower = 10;
    private static final int baseBuildingCost = 100;
    private static final int baseShootingRange = 100;
    private static final String typeName = "Laser";
    private Line attackTrace = new Line();
    
    /**
     * LaserTower Constructor
     * @param x The x-coordinate of the cell where the LaserTower locates
     * @param y The y-coordinate of the cell where the LaserTower locates
     */
    public LaserTower(int x, int y) {
        super(baseAttackPower, baseBuildingCost, baseShootingRange, x, y, typeName);
    }
    
    /**
     * Getter function for the attack line extended from the center of the tower 
     * @return attackTrace The attack line
     */
    public Line getAttackTrace(){
		return attackTrace;
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
	 * Method for LaserTower to attack the monster
	 * <p>
	 * Draw a line from the center of the tower to the nearest monster to the End Zone and extends the frame of the game
	 * <p>
	 * Monsters on the line or within 3px away from the line get hurt
	 */
	@Override
	public void shoot(){
		if(Arena.getMonsterNum() > 0 && Resource.canDeductAmount(1)){
			HashMap<Monster, Double> map = new HashMap<Monster, Double>();
			for(Monster m: Arena.getMonsters()){
				double distanceToEndZone = Math.hypot(	
    					m.getXPx() - (MAX_V_NUM_GRID - 0.5) * GRID_WIDTH,
    					m.getYPx() - (MAX_H_NUM_GRID - 0.5) * GRID_HEIGHT);
				if(!map.isEmpty()){
					HashMap.Entry<Monster, Double> set =  map.entrySet().iterator().next();
					if(distanceToEndZone < set.getValue()){
						map.remove(set);
						map.put(m, distanceToEndZone);
					}
				}
				else map.put(m,distanceToEndZone);
			}
			if(map.isEmpty()) return;
			
			double targetMonX = map.entrySet().iterator().next().getKey().getXPx();
    		double targetMonY = map.entrySet().iterator().next().getKey().getYPx();
			int towerX = getX() * GRID_WIDTH + GRID_WIDTH/2;
			int towerY = getY() * GRID_HEIGHT + GRID_HEIGHT/2;
			ArrayList<Monster> targetMonList = new ArrayList<Monster>();
			
			//
			if(targetMonY != towerY){
				double slopeReciprocal = (targetMonX - towerX)/(targetMonY - towerY); 
				double frameX = 0, frameY = 0;
				if(targetMonY > towerY){
					frameY = MAX_H_NUM_GRID * GRID_HEIGHT;
					frameX = (MAX_H_NUM_GRID * GRID_HEIGHT - towerY) * slopeReciprocal + towerX;
				}
				if(targetMonY <= towerY){
					frameX = (0 - towerY) * slopeReciprocal + towerX;
				}
				attackTrace = new Line(towerX, towerY, frameX, frameY);
			}
			else {
				if(targetMonX > towerX) 
					attackTrace =  new Line(towerX, towerY, MAX_V_NUM_GRID * GRID_WIDTH, towerY);
				else attackTrace = new Line(towerX, towerY, 0, towerY);
			}
			
			//
			if(targetMonX != towerX){
				double slope = (targetMonY - towerY)/(targetMonX - towerX);
				double deltaY = 3 * Math.hypot(1 , slope);
				for(Monster m : Arena.getMonsters()){
					if(m.getYPx() > slope * (m.getXPx() - towerX) + towerY - deltaY &&
							m.getYPx() < slope * (m.getXPx() - towerX) + towerY + deltaY){
						//System.out.printf("$$$In attack range$$$\n");
						if(targetMonY > towerY && m.getYPx() > towerY) targetMonList.add(m);
						if(targetMonY < towerY && m.getYPx() < towerY) targetMonList.add(m);
					}
				}
			}
			else{
				for(Monster m : Arena.getMonsters()){
					if(m.getXPx() < towerX + 3 && m.getXPx() > towerX - 3){
						if(targetMonY > towerY && m.getYPx() > towerY) targetMonList.add(m);
						if(targetMonY < towerY && m.getYPx() < towerY) targetMonList.add(m);
					}
				}
			}
			//if the attackin trace is a horizontal line:
			for(Monster tm : targetMonList){
				Arena.logAttack(this, tm);
    			this.implement(tm);
    			tm.setHit(true);
        		Arena.setTowerShot(getX(),getY());
			}
			Resource.deductAmount(1);
		}
		else{
			attackTrace = null;
		}
	}
} 