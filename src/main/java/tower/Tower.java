package tower;

import arena.logic.Arena;
import static arena.logic.ArenaConstants.*;
import monster.*;


import java.util.HashMap;
import java.lang.Math;

/**
 * <p>
 * Class for general towers. 
 * @author REN Jiming
 *
 */
public class Tower {
    private int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int attackPower;
    private int buildingCost;
    private int shootingRange;
    private int x;
    private int y;
    private String type;
    private int upgradeCost;

    public Tower() {}

    /**
     * Tower Constructor
     * @param attackPower The attack power of the tower
     * @param buildingCost The building cost of the tower
     * @param shootingRange The shooting range of the tower
     * @param x The x-coordinate of the cell where the tower locates
     * @param y The y-coordinate of the cell where the tower locates
     * @param type The type of the tower, i.e. Basic, Catapult, Ice, Laser
     */
    public Tower(int attackPower, int buildingCost, int shootingRange, int x, int y, String type) {
        this.attackPower = attackPower;
        this.buildingCost = buildingCost;
        this.shootingRange = shootingRange;
        this.x = x;
        this.y = y;
        this.type = type;
        this.upgradeCost = buildingCost;
    }

    public Tower(Tower t) {
        this.attackPower = t.attackPower;
        this.buildingCost = t.buildingCost;
        this.shootingRange = t.shootingRange;
        this.x = t.x;
        this.y = t.y;
        this.type = t.type;
        this.upgradeCost = t.buildingCost;
    }
    
    /**
     * Getter function for the attack power of the tower
     * @return attackPower The attack power of the tower
     */
    public int getAttackPower() {
        return attackPower;
    }
    
    /**
     * Setter function for the attack power of the tower
     * @param attackPower The attack power of the tower
     */
    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }
    
    /**
     * Getter function for the building cost of the tower
     * @return buildingCost The building cost of the tower
     */
    public int getBuildingCost() {
        return buildingCost;
    }
    
    /**
     * Setter function for the building cost of the tower
     * @param buildingCost The building cost of the tower
     */
    public void setBuildingCost(int buildingCost) {
        this.buildingCost = buildingCost;
    }
    
    /**
     * Getter function for the shooting range of the tower
     * @return shootingRange The shooting range of the tower
     */
    public int getShootingRange() {
        return shootingRange;
    }
    
    /**
     * Setter function for the shooting range of the tower
     * @param shootingRange The shooting range of the tower
     */
    public void setShootingRange(int shootingRange) {
        this.shootingRange = shootingRange;
    }
    
    /**
     * Getter function for the x-coordinate of the cell where the tower locates 
     * @return x The x-coordinate of the cell
     */
    public int getX() {
        return x;
    }

    /**
     * Getter function for the y-coordinate of the cell where the tower locates 
     * @return y The y-coordinate of the cell
     */
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUpgradeCost(int upgradeCost) {
        this.upgradeCost = upgradeCost;
    }

    /**
     * Getter function for the type of the tower, i.e. Basic, Catapult, Ice, Laser
     * @return type The type of the tower
     */
    public String getType() {
        return type;
    }

    /**
     * Getter function of the upgrade cost of the tower
     * @return upgradeCost The upgrade cost of the tower
     */
    public int getUpgradeCost() {
        return upgradeCost;
    }
    
    /**
     * Overrided Method to decrease the HP of the monster
     * @param target The monster whose HP to be decreased
     */
    public void implement(Monster target) {}
    
    /**
     * Overrided Mehod for the tower upgrading
     */
    public void upgrade() {}

    /**
     * Method for determining whether the Tower can attack a given pixel,
     * i.e. whether the pixel is inside its range.
     * Notice that LaserTower can always attack all the coordinates.
     * @param xPx The x-coordinate(in pixels) of the pixel.
     * @param yPx The y-coordinate(in pixels) of the pixel.
     * @return Boolean value showing whether the Tower can attack the given pixel.
     */
    public boolean canAttack(int xPx, int yPx) {
    	return true;
    }
    
    /**
     * Method for the tower to target the monster
     * <p>
     * Always target the nearest monster to the up-left corner of the end-zone 
     */
    public void shoot() {
    	if(Arena.getMonsterNum() > 0){
    		HashMap<Monster, Double> map = new HashMap<Monster, Double>();
    		for(Monster m: Arena.getMonsters()){
    			double distanceToTower = Math.hypot(
    					m.getxPx() - (this.x * GRID_WIDTH + GRID_WIDTH/2),
    					m.getyPx() - (this.y * GRID_HEIGHT + GRID_HEIGHT/2));
    			double distanceToEndZone = Math.hypot(	
    					m.getxPx() - (MAX_V_NUM_GRID + 0.5) * GRID_WIDTH,
    					m.getyPx() - (MAX_H_NUM_GRID + 0.5) * GRID_HEIGHT );
    			if(distanceToTower <= shootingRange){
    				if(!map.isEmpty()){
    					HashMap.Entry<Monster, Double> set =  map.entrySet().iterator().next();
    					if(distanceToEndZone < set.getValue()){
    						map.remove(set);
    						map.put(m, distanceToEndZone);
    					}
    				}
    				else map.put(m,distanceToEndZone);
    			}
    		}
    		if(map.isEmpty()) return;
    		Monster target = map.entrySet().iterator().next().getKey();
    		
//    		//rank the distance to monsters
//    		List<Map.Entry<Monster, Double>> list = new ArrayList<Map.Entry<Monster,Double>>(map.entrySet());
//    		list.sort(Map.Entry.comparingByValue());
//    		HashMap<Monster,Double> sorted = new HashMap<>();
//    		for (Map.Entry<Monster, Double> entry : list) {
//    			sorted.put(entry.getKey(), entry.getValue());
//    		}
//    		Iterator<Map.Entry<Monster,Double>> iter = sorted.entrySet().iterator();
//    		Map.Entry<Monster, Double> element = iter.next();
//    		Monster target = element.getKey();
    		
//    		double distance = element.getValue();
//    		//fixed: Stuck in this while loop
//    		do{
//    			if(iter.hasNext()){
//    				element = iter.next();
//    				if(element.getValue() == distance)
//    					if(element.getKey().getYPx()<target.getYPx())
//    						target=element.getKey();
//    			}
//    			else break;
//    		}while(element.getValue() == distance);
    		
    		Arena.logAttack(this,target);
    		target.setHit(true);
    		Arena.setTowerShot(x,y);
    		implement(target);
    	}
    }
}

