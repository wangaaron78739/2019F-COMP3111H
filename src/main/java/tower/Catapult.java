package tower;

/**
 * <p>
 * Class for Catapult, which takes sometime to reload after each attack.
 * <p>
 * Catapult throws a stone (attacks) to a coordinate less than 150 px
 * but more than 50 px away from the center of the it.
 * <p>
 * All monsters placed at the radius of 25px of where the stone drop receive damage.
 * 
 * @author REN Jiming
 */
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import arena.logic.Arena;
import arena.logic.Resource;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import monster.Monster;
import static arena.logic.ArenaConstants.*;


public class Catapult extends Tower {
    private static final int baseAttackPower = 50;
    private static final int baseBuildingCost = 100;
    private static final int baseShootingRange = 100;
    private static final int upperShootingRange = 150;
    private static final int lowerShootingRange = 50;
    private static final int baseAttackRadius = 25;
    private static final String typeName = "Catapult";
    private static final int upgradeCooldownTime = 5;
    private int cooldown;
    private int prevShot;
    
    /**
     * Catapult Constructer
     * @param x The x-coordinate of the cell where the Catapult locates
     * @param y The y-coordinate of the cell where the Catapult locates
     */
    public Catapult(int x, int y) {
        super(baseAttackPower, baseBuildingCost, baseShootingRange, x, y, typeName);
        cooldown = 50;
        prevShot = 0;
    }
    
    /**
     * Getter function for the upper shooting range of the catapult
     * @return upperShootingRange The upper shooting range of the catapult  
     */
    public int getUpperShootingRange() {
    	return upperShootingRange;
    }
    
    /**
     * Getter function for the lower shooting range of the catapult
     * @return upperShootingRange The lower shooting range of the catapult  
     */
    public int getLowerShootingRange() {
    	return lowerShootingRange;
    }
    
    /**
     * Getter function for the radius of the catapult's shooting range
     * @return baseAttackRadius The radius of the catapult's shooting range
     */
    public int getBaseAttackRadius() {
    	return baseAttackRadius;
    }
    
    /**
     * Getter function for the cooldown interval able to be upgraded
     * @return upgradeCooldownTime Cooldown interval able to be upgraded
     */
    public int getUpgradeCooldownTime(){
    	return upgradeCooldownTime;
    }
    
    /**
     * Getter function for the cooldown period
     * @return cooldown Cooldown interval
     */
    public int getCooldown(){
    	return cooldown;
    }
    
    /**
     * Method for determining whether the Catapult can attack a given pixel,
     * i.e. whether the pixel is inside its range.
     * @param xPx The x-coordinate(in pixels) of the pixel.
     * @param yPx The y-coordinate(in pixels) of the pixel.
     * @return Boolean value showing whether the Catapult can attack the given pixel.
     */
    @Override
    public boolean canAttack(int xPx, int yPx) {
    	double distance = Math.hypot(
    			xPx - (getX() * GRID_WIDTH + GRID_WIDTH/2), 
    			yPx - (getY() * GRID_HEIGHT + GRID_HEIGHT/2));
    	return (distance >= getLowerShootingRange() - getBaseAttackRadius() &&
    			distance <= getUpperShootingRange() + getBaseAttackRadius());
    }
    
    /**
     * Pixel
     */
    class Pixel{
    	int CoordX, CoordY;
    	
    	Pixel(int X, int Y){
    		this.CoordX = X;
    		this.CoordY = Y;
    	}
    	
//    	int NumMonstersInRange(){
//    		for(Monster m : Arena.getMonsters()){
//    			double distance = Math.hypot(m.getXPx() - CoordX, m.getYPx() - CoordY);
//    			if(distance <= 25) {
//    				MonstersInRange++;
//    				if(!monstersAttacked.contains(m))
//    					monstersAttacked.add(m);
//    			}
//    		}
//    		return MonstersInRange;
//    	}
    	
    	LinkedList<Monster> getMonsterList(){
    		LinkedList<Monster> monstersAttacked = new LinkedList<Monster>();
    		for(Monster m : Arena.getMonsters()){
    			double distance = Math.hypot(m.getxPx() - CoordX, m.getyPx() - CoordY);
    			if(distance <= 25) {
    				monstersAttacked.add(m);
    			}
    		}
    		return monstersAttacked;
    	}
    };
    
    /**
     * Overriding method to decrease HP of the monster
     * @param target The monster whose HP to be decreased
     */
    @Override
    public void implement(Monster target){
    	target.setHP(target.getHP() - getAttackPower());
    }
    
    /**
     * Overriding method to upgrade the Catapult
     * <p>
     * Shorten the cooldown time
     * <p>
     * Check the remained resource beforehand; 
     * if there is no enough resource to exploit, give alert
     */
    @Override
    public void upgrade(){
    	if(Resource.canDeductAmount(getUpgradeCost())){
    		if(cooldown >= 25){
    			cooldown -= upgradeCooldownTime;
    			Resource.deductAmount(getUpgradeCost()); 
    		}
    		else {
    			Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
    			alert.setTitle("Cannot upgrade any more");
    			alert.setHeaderText("Cannot decrease the cooldown time span");
    			alert.showAndWait();
    		}
    	}
    	else{
    		Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            alert.setTitle("Cannot upgrade");
            alert.setHeaderText("No enough resource, you need " + getUpgradeCost() + " resource to upgrade");
            alert.showAndWait();
    	}
    }
    
    /**
     * Find the pixel whose range (+-25 pixels) include the input (x, y) as well as the largest number of monsters
     * @param tm x
     * @param tn y
     * @return
     */
    public Pixel findBestPixel(int tm, int tn){
    	Pixel bestTarget = new Pixel (0,0);
		int MonstersInRange = 0;
		
		for(int x = tm - baseAttackRadius; x <= tm + baseAttackRadius; x++){
			for(int y = tn - baseAttackRadius; y <= tn + baseAttackRadius; y++){
				//System.out.printf("Loop:%d,%d\n",x,y);
				double distance = Math.hypot(
						x - (getX() * GRID_WIDTH + GRID_WIDTH/2), 
						y - (getY() * GRID_HEIGHT + GRID_HEIGHT/2));
				double distanceToNearestMonster = Math.hypot(x - tm, y - tn);
				if(distance <= upperShootingRange && 
				   distance >= lowerShootingRange && 
				   distanceToNearestMonster <= baseAttackRadius){
					int num = 0;
					for(Monster mon : Arena.getMonsters()){
						double distanceToMonster = Math.hypot(mon.getxPx() - x, mon.getyPx() - y);
						if(distanceToMonster <= baseAttackRadius) {
							num = num + 1;
							//System.out.printf("A:%d",num);
						}
					}
					if(num > MonstersInRange) {
						MonstersInRange = num;
						bestTarget = new Pixel (x,y);
					}
				}
			}
		}		
		return bestTarget;
    }
    
    /**
     * Method for Catapults to attack the monster
     * <p>
     * The stone should be thrown to the coordinate that hits most monsters, 
     * among which the monster in the shooting range (pls 25 pixels) closed to the end-zone will be attacked. 
     */
    @Override    
    public void shoot() {
    	
    	//Check whether catapult is still in cooldown period
    	if(Arena.getFrameCount() - prevShot < cooldown) return;
    	
    	//find the monster closest to the end zone
    	if(Arena.getMonsterNum() > 0){
    		HashMap<Monster, Double> map = new HashMap<Monster, Double>();
    		for(Monster m: Arena.getMonsters()){
    			double distance = Math.hypot(
    					m.getxPx() - this.getX() * GRID_WIDTH - GRID_WIDTH/2, 
    					m.getyPx() - this.getY() * GRID_HEIGHT - GRID_HEIGHT/2);
    			double distanceToEndZone = Math.hypot(
    					m.getyPx() - (MAX_H_NUM_GRID - 0.5) * GRID_HEIGHT, 
    					m.getxPx() - (MAX_V_NUM_GRID - 0.5) * GRID_WIDTH);
    			if(distance <= (upperShootingRange + baseAttackRadius) && 
    			   distance >= (lowerShootingRange - baseAttackRadius)){
    				if(!map.isEmpty()){
    					HashMap.Entry<Monster, Double> set =  map.entrySet().iterator().next();
    					if(distanceToEndZone < set.getValue()){
    						map.clear();
    						map.put(m, distanceToEndZone);
    					}
    					else if(distanceToEndZone == set.getValue()){
    						map.put(m, distanceToEndZone);
    					}
    				}
    				else map.put(m,distanceToEndZone);
    			}
    		}
    		if(map.isEmpty()) return;
//    		//rank the distance to monsters
//    		List<Map.Entry<Monster, Double>> list = new ArrayList<Map.Entry<Monster,Double>>(map.entrySet());
//    		//list.sort(Map.Entry.comparingByValue());
//    		HashMap<Monster,Double> sorted = new HashMap<>();
//
//    		for (Map.Entry<Monster, Double> entry : list) {
//    			sorted.put(entry.getKey(), entry.getValue());
//    		}
    		
    		Pixel bestTarget = new Pixel (0,0);
    		int numberInRange = 0;
    		for (Iterator<Entry<Monster, Double>> it = map.entrySet().iterator(); it.hasNext();){
    			Entry<Monster, Double> en = it.next();
    			int targetMonX = (int)(en.getKey().getxPx());
    			int targetMonY = (int)(en.getKey().getyPx());
    			Pixel p = findBestPixel(targetMonX, targetMonY);
    			int num = p.getMonsterList().size();
    			if(num > numberInRange){
    				bestTarget = p;
    				numberInRange= num;
    			}
    		}
    		
    		
//    		//find the best pixel target
//    		Pixel bestTarget = new Pixel (0,0);
//    		int MonstersInRange = 0;
//    		
//    		for(int x = targetMonX - baseAttackRadius; x <= targetMonX + baseAttackRadius; x++){
//    			for(int y = targetMonY - baseAttackRadius; y <= targetMonY + baseAttackRadius; y++){
//    				//System.out.printf("Loop:%d,%d\n",x,y);
//    				double distance = Math.hypot(
//    						x - (getX() * GRID_WIDTH + GRID_WIDTH/2), 
//    						y - (getY() * GRID_HEIGHT + GRID_HEIGHT/2));
//    				double distanceToNearestMonster = Math.hypot(x - targetMonX, y - targetMonY);
//    				if(distance <= upperShootingRange && 
//    				   distance >= lowerShootingRange && 
//    				   distanceToNearestMonster <= baseAttackRadius){
//    					int num = 0;
//    					for(Monster m : Arena.getMonsters()){
//    						double distanceToMonster = Math.hypot(m.getxPx() - x, m.getyPx() - y);
//    						if(distanceToMonster <= baseAttackRadius) {
//    							num = num + 1;
//    							//System.out.printf("A:%d",num);
//    						}
//    					}
//    					if(num > MonstersInRange) {
//    						MonstersInRange = num;
//    						bestTarget = new Pixel (x,y);
//    					}
//    				}
//    			}
//    		}		
    		
    		//Implement`
    		for(Monster m: bestTarget.getMonsterList()){
    			Arena.logAttack(this,m);
    			m.setHit(true);
        		Arena.setTowerShot(getX(),getY());
    			this.implement(m);
    		}
    		prevShot = Arena.getFrameCount();
    	}
    }
    
}