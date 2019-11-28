package tower;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import arena.logic.*;
import monster.*;

public class BasicTowerTest {

	private BasicTower tower = null;
	//private LinkedList<Monster> monsterList = null;
	
	@Before
	public void setUp() throws Exception {
		Arena.initArena();
		tower = new BasicTower(6,6);
	}
	
	@After
	public void tearDown() throws Exception {
		Arena.initArena();
		tower = null;
	}
	
	@Test
	public void assert_BasicTowerConstants_AssertPropertiesCorrect(){
		assertEquals(tower.getAttackPower(),30);
		assertEquals(tower.getBuildingCost(),100);
		assertEquals(tower.getShootingRange(),65);
		assertEquals(tower.getX(),6);
		assertEquals(tower.getY(),6);
		assertEquals(tower.getUpgradeCost(),tower.getBuildingCost());
		assertEquals(tower.getType(),"Basic");
		assertEquals(tower.getUpgradeAttackPower(), 15);
	}
	
//	@Test
//	public void assert_CopyConstructor_AssertPropertiesCorrected(){
//		Tower tower2 = new Tower(tower);
//		assertEquals(tower2.getAttackPower(),30);
//		assertEquals(tower2.getBuildingCost(),100);
//		assertEquals(tower2.getShootingRange(),65);
//		assertEquals(tower2.getX(),6);
//		assertEquals(tower2.getY(),6);
//		assertEquals(tower2.getUpgradeCost(),tower.getBuildingCost());
//		assertEquals(tower2.getType(),"Basic");
//	}
		
	@Test
	public void assert_SetterFunctions_AssertPropertiesCorrected(){
		tower.setAttackPower(20);
		tower.setBuildingCost(90);
		tower.setShootingRange(60);
		tower.setType("Basic'");;
		tower.setUpgradeCost(90);
		tower.setX(5);
		tower.setY(5);
		
		assertEquals(tower.getAttackPower(),20);
		assertEquals(tower.getBuildingCost(),90);
		assertEquals(tower.getShootingRange(),60);
		assertEquals(tower.getX(),5);
		assertEquals(tower.getY(),5);
		assertEquals(tower.getUpgradeCost(),90);
		assertEquals(tower.getType(),"Basic'");
	}
	
	@Test
	public void testShoot(){
		Monster m1 = new Monster(325, 325, 0, 1000, "Unicorn");
		Monster m2 = new Monster(270, 300, 0, 1000, "Unicorn");
		Monster m3 = new Monster(300, 270, 0, 1000, "Unicorn");
		Monster m4 = new Monster(300, 300, 0, 1000, "Unicorn");
		Monster m5 = new Monster(305, 305, 0, 1000, "Unicorn");
		Arena.getMonsters().add(m1);
		Arena.getMonsters().add(m2);
		Arena.getMonsters().add(m3);
		Arena.getMonsters().add(m4);
		Arena.getMonsters().add(m5);
		tower.shoot();
		assertEquals(m1.getHP(), 1000);
		assertEquals(m2.getHP(), 1000);
		assertEquals(m3.getHP(), 1000);
		assertEquals(m4.getHP(), 1000);
		assertEquals(m5.getHP(), (1000 - tower.getAttackPower()));
		Arena.getMonsters().clear();
	}
	
	@Test
	public void testUpgrade(){
		int resourceAmount = Resource.getResourceAmount();
		Resource.addResourceAmount(tower.getUpgradeCost());
		tower.upgrade();
		assertEquals(resourceAmount, Resource.getResourceAmount());
		assertEquals(tower.getAttackPower(), (tower.getUpgradeAttackPower() + 30));
	}
	
	@Test
	public void testImplement(){
		Monster m1 = new Monster(0, 0, 0, 1000, "Unicorn");
		tower.implement(m1);
		assertEquals(m1.getHP(), (1000 - tower.getAttackPower()));
	}
	
	@Test
	public void testCanAttack(){
		assertEquals(tower.canAttack(270,270), true);
	}
}
