package tower;


import java.util.HashMap;
import java.util.LinkedList;

import arena.logic.Arena;
import arena.logic.Resource;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import monster.Monster;

public class Catapult extends Tower {
    private static final int baseAttackPower = 100;
    private static final int baseBuildingCost = 100;
    private static final int baseShootingRange = 100;
    private static final int baseAttackCooldown = 100;
    private static final int upperShootingRange = 150;
    private static final int lowerShootingRange = 50;
    private static final int baseAttackRadius = 25;
    private static int cooldown = 50;
    private int prevShot = 0;
    private static final String typeName = "Catapult";

    public Catapult(int x, int y) {
        super(baseAttackPower, baseBuildingCost, baseShootingRange, baseAttackCooldown, x, y, typeName);
    }
    
    public int getUpperShootingRange() {
    	return upperShootingRange;
    }

    public int getLowerShootingRange() {
    	return lowerShootingRange;
    }

    public int getBaseAttackRadius() {
    	return baseAttackRadius;
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
    	double distance = Math.hypot(xPx - (getX()*Arena.GRID_WIDTH+Arena.GRID_WIDTH/2), yPx - (getY()*Arena.GRID_HEIGHT+Arena.GRID_HEIGHT/2));
    	return (distance>=getLowerShootingRange()-getBaseAttackRadius() && distance<=getUpperShootingRange()+getBaseAttackRadius());
    }
    
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
    			double distance = Math.hypot(m.getXPx() - CoordX, m.getYPx() - CoordY);
    			if(distance <= 25) {
    					monstersAttacked.add(m);
    			}
    		}
    		return monstersAttacked;
    	}
    };
    
    @Override
    public void implement(Monster target){
    	target.setHP(target.getHP()- this.baseAttackPower);
    }
    
    @Override
    public void upgrade(){
    	if(cooldown >= 20){
    		cooldown -= 5;
    		Resource.deductAmount(this.getUpgradeCost()); 
    	}
    	else {
    		Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.CLOSE);
            alert.setTitle("Cannot upgrade any more");
            alert.setHeaderText("Cannot decrease the cooldown time span");
            alert.showAndWait();
    	}
    }
    
    @Override    
    public void shoot() {
    	if(Arena.getFrameCount() - prevShot < Catapult.cooldown) return;
    	if(Arena.getMonsterNum() > 0){
    		HashMap<Monster, Double> map = new HashMap<Monster, Double>();
    		for(Monster m: Arena.getMonsters()){
    			double distance = Math.hypot(m.getXPx() - this.getX() * Arena.GRID_WIDTH + Arena.GRID_WIDTH/2, m.getYPx() - this.getY() * Arena.GRID_HEIGHT + Arena.GRID_HEIGHT/2);
    			double distanceToEndZone = Math.hypot(m.getYPx() - (Arena.MAX_H_NUM_GRID - 0.5) * Arena.GRID_HEIGHT, m.getXPx() - (Arena.MAX_V_NUM_GRID - 0.5) * Arena.ARENA_WIDTH);
    			if(distance <= upperShootingRange && distance >= lowerShootingRange){
    				if(!map.isEmpty() && distanceToEndZone < map.entrySet().iterator().next().getValue()){
    					map.remove(map.entrySet().iterator().next());
    					map.put(m, distanceToEndZone);
    				}
    				else map.put(m, distanceToEndZone);
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
    		//go through the adjacent pixels
    		int targetMonX = map.entrySet().iterator().next().getKey().getXGrid();
    		int targetMonY = map.entrySet().iterator().next().getKey().getYGrid();
    		
    		Pixel bestTarget = null;
    		int x = targetMonX - 25, y = targetMonY - 25;
    		int MonstersInRange = 0;
    		
    		for(; x <= targetMonX + 25; x++){
    			for(; y <= targetMonY + 25; y++){
    				double distance = Math.hypot(x - this.getX() * Arena.GRID_WIDTH + Arena.GRID_WIDTH/2, y - this.getY() * Arena.GRID_HEIGHT + Arena.GRID_HEIGHT/2);
    				if(distance <= upperShootingRange && distance >= lowerShootingRange){
    					int num = 0;
    					for(Monster m : Arena.getMonsters()){
    						double distanceToMonster = Math.hypot(m.getXPx() - x, m.getYPx() - y);
    						if(distanceToMonster <= 25) {
    							num++;
    						}
    					}
    					if(num > MonstersInRange) {
    						MonstersInRange = num;
    						bestTarget = new Pixel (x,y);
    					}
    				}
    			}
    		}		
    		//Implement
    		for(Monster m: bestTarget.getMonsterList()){
    			Arena.logAttack(this,m);
    			// this.implement(m);
    		}
    		prevShot = Arena.getFrameCount();
    	}
    }
    
}