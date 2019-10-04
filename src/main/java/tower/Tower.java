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

    public Tower(int attackPower, int buildingCost, int shootingRange, int x, int y, String type) {
        this.attackPower = attackPower;
        this.buildingCost = buildingCost;
        this.shootingRange = shootingRange;
        this.x = x;
        this.y = y;
        this.type = type;
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
        System.out.printf("Upgraded %s at (%d,%d)\n", type, x,y);
    }
    public void shoot() {
      if(Arena.getMonsterNum() > 0){
    	  HashMap<Monster, Double> map = new HashMap<Monster, Double>();
    	  for(Monster m: Arena.getMonsters()){
    		  double distance = Math.hypot(m.getXPx() - this.x, m.getYPx() - this.y);
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
         do{
        	 if(iter.hasNext()){
        		 element = iter.next();
        		 if(element.getValue() == distance)
        			 if(element.getKey().getYPx()<target.getYPx())
        				 target=element.getKey();		 
        	 }
         }while(element.getValue() == distance);	 
         //shoot
         target.setHP( target.getHP()-attackPower);
      }
    }
}
    
    