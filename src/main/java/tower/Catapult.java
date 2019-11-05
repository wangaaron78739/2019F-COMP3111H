package tower;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import arena.logic.Arena;
import monster.Monster;

public class Catapult extends Tower {
    private static final int baseAttackPower = 100;
    private static final int baseBuildingCost = 100;
    private static final int baseShootingRange = 100;
    private static final int baseAttackCooldown = 100;
    private static final int upperShootingRange = 150;
    private static final int lowerShootingRange = 50;
    private static final int baseAttackRadius = 25;
    private int cooldown = 50;
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
    
    @Override
    public boolean canAttack(int xPx, int yPx) {
    	double distance = Math.hypot(xPx - (getX()*Arena.GRID_WIDTH+Arena.GRID_WIDTH/2), yPx - (getY()*Arena.GRID_HEIGHT+Arena.GRID_HEIGHT/2));
    	return (distance>=getLowerShootingRange()-getBaseAttackRadius() && distance<=getUpperShootingRange()+getBaseAttackRadius());
    }
   
    @Override    
    public void shoot() {
    	if(Arena.getFrameCount() - prevShot < this.cooldown) return;
    	if(Arena.getMonsterNum() > 0){
    		HashMap<Monster, Double> map = new HashMap<Monster, Double>();
    		for(Monster m: Arena.getMonsters()){
    			double distance = Math.hypot(m.getXPx() - this.getX() * Arena.GRID_WIDTH + Arena.GRID_WIDTH/2, m.getYPx() - this.getY() * Arena.GRID_HEIGHT + Arena.GRID_HEIGHT/2);
    			double distanceToEndZone = Math.hypot(m.getYPx() - (Arena.MAX_H_NUM_GRID - 0.5) * Arena.GRID_HEIGHT, m.getXPx() - (Arena.MAX_V_NUM_GRID - 0.5) * Arena.ARENA_WIDTH);
    			if(distance <= upperShootingRange && distance >= lowerShootingRange)
    				map.put(m, distanceToEndZone);
    		}
    		if(map.isEmpty()) return;
    		//rank the distance to monsters
    		List<Map.Entry<Monster, Double>> list = new ArrayList<Map.Entry<Monster,Double>>(map.entrySet());
    		list.sort(Map.Entry.comparingByValue());
    		HashMap<Monster,Double> sorted = new HashMap<>();
    		for (Map.Entry<Monster, Double> entry : list) {
    			sorted.put(entry.getKey(), entry.getValue());
    		}
    		//go through the adjacent pixels
    		int targetMonX = sorted.entrySet().iterator().next().getKey().getXGrid();
    		int targetMonY = sorted.entrySet().iterator().next().getKey().getYGrid();
    		class Pixel implements Comparable <Pixel>{
    			int CoordX; int CoordY;
    	    	Integer MonstersInRange;
    	    	double distance;
    	    	List<Monster> monstersAttacked = new LinkedList<Monster>();
    	    	Pixel(int X, int Y){
    	    		this.CoordX = X;
    	    		this.CoordY = Y;
    	    		this.MonstersInRange = 0;
    	    		this.distance = Math.hypot(X - targetMonX, Y - targetMonY);
    	    	}
    	    	Integer NumMonstersInRange(){
    	    		for(Monster m : Arena.getMonsters()){
    	    			double distance = Math.hypot(m.getXPx() - CoordX, m.getYPx() - CoordY);
    	    			if(distance <= 25) {
    	    				MonstersInRange++;
    	    				if(!monstersAttacked.contains(m))
    	    				monstersAttacked.add(m);
    	    			}
    	    		}
    	    		return MonstersInRange;
    	    	}
    	    	@Override
    	    	public int compareTo (Pixel p){
    	    		return p.NumMonstersInRange().compareTo(this.NumMonstersInRange());
    	    	}
    		};
    		List<Pixel> PixelList = new ArrayList<Pixel> ();
    		for(int x = targetMonX - 25; x <= targetMonX + 25; x++){
    			for(int y = targetMonY - 25; x <= targetMonY +25; y++){
    				PixelList.add(new Pixel(x, y));
    			}
    		}
    		PixelList.sort(new Comparator<Pixel>(){
                public int compare(Pixel p1, Pixel p2){
                    return p1.compareTo(p2);
            }});
    		//Implement
    		Pixel bestTarget = PixelList.iterator().next();
    		for(Monster m: bestTarget.monstersAttacked){
    			Arena.logAttack(this,m);
    			this.implement(m);
    		}
    		prevShot = Arena.getFrameCount();
    	}
    }

}