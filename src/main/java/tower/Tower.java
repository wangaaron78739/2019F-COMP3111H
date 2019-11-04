package tower;

import arena.logic.Arena;
import monster.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;


public class Tower {
    private int attackPower;
    private int buildingCost;
    private int shootingRange;
    private final String type;
    private final int x;
    private final int y;
    private final int cooldown;
    private int prevShot = 0;
    private int upgradeCost;

    public Tower(int attackPower, int buildingCost, int shootingRange, int cooldown, int x, int y, String type) {
        this.attackPower = attackPower;
        this.buildingCost = buildingCost;
        this.shootingRange = shootingRange;
        this.x = x;
        this.y = y;
        this.type = type;
        this.cooldown = cooldown;
        this.upgradeCost = buildingCost;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getBuildingCost() {
        return buildingCost;
    }

    public void setBuildingCost(int buildingCost) {
        this.buildingCost = buildingCost;
    }

    public int getShootingRange() {
        return shootingRange;
    }

    public void setShootingRange(int shootingRange) {
        this.shootingRange = shootingRange;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return type;
    }

    public void upgrade() {
        
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }
    
    public void implement(Monster target){
    	
    }
    
    // helper function for checking whether a pixel is in the range, would be overrride
    public boolean canAttack(int xPx, int yPx) {
    	return true;
    }

    public void shoot() {
        if (Arena.getFrameCount()-prevShot < cooldown) return;
      if(Arena.getMonsterNum() > 0){
    	  HashMap<Monster, Double> map = new HashMap<Monster, Double>();
    	  for(Monster m: Arena.getMonsters()){
    		  double distance = Math.hypot(m.getXPx() - (this.x*Arena.GRID_WIDTH+Arena.GRID_WIDTH/2), m.getYPx() - (this.y*Arena.GRID_HEIGHT+Arena.GRID_HEIGHT/2));
    		  if(distance <= shootingRange)
    		  map.put(m, distance);
    	  }
    	  if(map.isEmpty()) return;
    	  //rank the distance to monsters
    	  List<Map.Entry<Monster, Double>> list = new ArrayList<Map.Entry<Monster,Double>>(map.entrySet());
    	  list.sort(Map.Entry.comparingByValue());
    	  HashMap<Monster,Double> sorted = new HashMap<>();
          for (Map.Entry<Monster, Double> entry : list) {
              sorted.put(entry.getKey(), entry.getValue());
          }
    	  //find the up-left monster among the nearest monsters
         Iterator<Map.Entry<Monster,Double>> iter = sorted.entrySet().iterator();
         Map.Entry<Monster, Double> element = iter.next();
         Monster target = element.getKey();
         double distance = element.getValue();
         //fixed: Stuck in this while loop
         do{
        	 if(iter.hasNext()){
        		 element = iter.next();
        		 if(element.getValue() == distance)
        			 if(element.getKey().getYPx()<target.getYPx())
        				 target=element.getKey();
        	 }
        	 else break;
         }while(element.getValue() == distance);
         //Implement
         Arena.logAttack(this,target);
//         target.setHP( target.getHP()-attackPower);
         this.implement(target);
         prevShot = Arena.getFrameCount();
      }
    }
    
}
    
    