package tower;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import arena.logic.*;
import monster.*;

import org.junit.Test;

public class LaserTowerTest {

	private Arena arena = null;
	private LaserTower tower = null;
	//private LinkedList<Monster> monsterList = null;
	
	@Before
	public void setUp() throws Exception {
		arena = new Arena();
		tower = new LaserTower(6,6);
	}
	
	@After
	public void tearDown() throws Exception {
		arena = null;
		tower = null;
	}

	@Test
	public void assert_LaserTowerConstants_AssertPropertiesCorrect(){
		assertEquals(tower.getAttackPower(),20);
		assertEquals(tower.getBuildingCost(),100);
		assertEquals(tower.getShootingRange(),100);
		assertEquals(tower.getX(),6);
		assertEquals(tower.getY(),6);
		assertEquals(tower.getType(),"Laser");
		assertEquals(tower.getUpgradeAttackPower(), 10);
	} 
	
	@Test
	public void testShoot(){
		
	}

}
