package tower;

import java.util.HashMap;

import static arena.logic.ArenaConstants.GRID_HEIGHT;
import static arena.logic.ArenaConstants.GRID_WIDTH;
import static arena.logic.ArenaConstants.MAX_H_NUM_GRID;
import static arena.logic.ArenaConstants.MAX_V_NUM_GRID;

import java.util.ArrayList;
import arena.logic.Arena;
import arena.logic.Resource;
import arena.logic.ArenaConstants;
import monster.Monster;
import javafx.scene.shape.Line;

public class LaserTower extends Tower {
    private static final int baseAttackPower = 100;
    private static final int baseBuildingCost = 100;
    private static final int baseShootingRange = 100;
    private static final int baseAttackCooldown = 100;
    private static final String typeName = "Laser";
    private Line attackTrace = new Line();
    
    
    public LaserTower(int x, int y) {
        super(baseAttackPower, baseBuildingCost, baseShootingRange, x, y, typeName);
    }
    
    public Line getAttackTrace(){
		return attackTrace;
	} 

	@Override
	public void upgrade(){
		setAttackPower(this.getAttackPower() + 60);
		Resource.deductAmount(this.getUpgradeCost()); 
	}

	@Override
	public void implement(Monster target){
		target.setHP(target.getHP() - this.getAttackPower());
	}


	@Override
	public void shoot(){
		if(Arena.getMonsterNum() > 0 && Resource.canDeductAmount(10)){
			HashMap<Monster, Double> map = new HashMap<Monster, Double>();
			for(Monster m: Arena.getMonsters()){
				double distanceToEndZone = Math.hypot(	
    					m.getxPx() - (MAX_V_NUM_GRID + 0.5) * GRID_WIDTH,
    					m.getyPx() - (MAX_H_NUM_GRID + 0.5) * GRID_HEIGHT);
//				if(!map.isEmpty() && distanceToEndZone < map.entrySet().iterator().next().getValue()){
//					map.remove(map.entrySet().iterator().next());
//					map.put(m, distanceToEndZone);
//				}
//				else map.put(m, distanceToEndZone);
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
			double targetMonX = (map.entrySet().iterator().next().getKey().getXGrid() + 0.5) * ArenaConstants.GRID_WIDTH;
    		double targetMonY = (map.entrySet().iterator().next().getKey().getYGrid() + 0.5) * ArenaConstants.GRID_HEIGHT;
			int towerX = this.getX() * ArenaConstants.GRID_WIDTH + ArenaConstants.GRID_WIDTH/2;
			int towerY = this.getY() * ArenaConstants.GRID_HEIGHT + ArenaConstants.GRID_HEIGHT/2;
    		double slopeReciprocal = (targetMonX - towerX)/(targetMonY - towerY); 
    		double frameX = 0, frameY = 0;
    		if(targetMonY > towerY){
    			frameY = (ArenaConstants.MAX_H_NUM_GRID + 1) * ArenaConstants.GRID_HEIGHT;
    			frameX = ((ArenaConstants.MAX_H_NUM_GRID + 1) * ArenaConstants.GRID_HEIGHT - towerY) * slopeReciprocal + towerX;
			}
    		if(targetMonY <= towerY){
    			frameX = (0 - towerY) * slopeReciprocal + towerX;
    		}
			attackTrace = new Line(towerX, towerY, frameX, frameY);
			double slope = 1 / slopeReciprocal;
			double deltaY = 3 * Math.hypot(1 , slope);
			ArrayList<Monster> targetMonList = new ArrayList<Monster>();
			for(Monster m : Arena.getMonsters()){
				if(m.getyPx() > slope * (m.getxPx() - towerX) - deltaY &&
				   m.getyPx() < slope * (m.getxPx() - towerX) + deltaY){
					System.out.printf("$$$In attack range$$$\n");
					targetMonList.add(m);
					//if(targetMonY > towerY && m.getYPx() > towerY) targetMonList.add(m);
					//if(targetMonY < towerY && m.getYPx() < towerY) targetMonList.add(m);
				}
			}
			for(Monster tm : targetMonList){
				Arena.logAttack(this, tm);
    			this.implement(tm);
    			Resource.deductAmount(10);
			}
		}
	}	
} 

