package tower;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import arena.logic.*;
import monster.*;

import org.junit.Test;

public class LaserTowerTest {

	private LaserTower tower = null;
	//private LinkedList<Monster> monsterList = null;
	
	@Before
	public void setUp() throws Exception {
		Arena.initArena();
		tower = new LaserTower(6,6);
	}
	
	@After
	public void tearDown() throws Exception {
		Arena.initArena();
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
	public void testShoot_Vertical(){
		Resource.setResourceAmount(10);
		Monster m1 = new Monster(260, 420, 0, 1000, "Unicorn");
		Monster m2 = new Monster(260, 400, 0, 1000, "Unicorn");
		Monster m3 = new Monster(263, 300, 0, 1000, "Unicorn");
		Monster m4 = new Monster(250, 300, 0, 1000, "Unicorn");
		Monster m5 = new Monster(260, 100, 0, 1000,"Unicorn");
		Arena.getMonsters().add(m1);
		Arena.getMonsters().add(m2);
		Arena.getMonsters().add(m3);
		Arena.getMonsters().add(m4);
		Arena.getMonsters().add(m5);
		tower.shoot();
		assertEquals(m1.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m2.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m3.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m4.getHP(), 1000);
		assertEquals(m5.getHP(), 1000);
		Arena.getMonsters().clear();
	}
	
	@Test
	public void testShoot_Horizental(){
		Resource.setResourceAmount(10);
		Monster m1 = new Monster(420, 260, 0, 1000, "Unicorn");
		Monster m2 = new Monster(400, 260, 0, 1000, "Unicorn");
		Monster m3 = new Monster(300, 263, 0, 1000, "Unicorn");
		Monster m4 = new Monster(300, 250, 0, 1000, "Unicorn");
		Monster m5 = new Monster(100, 260, 0, 1000,"Unicorn");
		Arena.getMonsters().add(m1);
		Arena.getMonsters().add(m2);
		Arena.getMonsters().add(m3);
		Arena.getMonsters().add(m4);
		Arena.getMonsters().add(m5);
		tower.shoot();
		assertEquals(m1.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m2.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m3.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m4.getHP(), 1000);
		assertEquals(m5.getHP(), 1000);
		Arena.getMonsters().clear();
	}
	
	@Test
	public void testShoot_Diagonal(){
		Resource.setResourceAmount(10);
		Monster m1 = new Monster(420, 420, 0, 1000, "Unicorn");
		Monster m2 = new Monster(380, 380, 0, 1000, "Unicorn");
		Monster m3 = new Monster(360, 364, 0, 1000, "Unicorn");
		Monster m4 = new Monster(360, 365, 0, 1000, "Unicorn");
		Monster m5 = new Monster(100, 100, 0, 1000,"Unicorn");
		Arena.getMonsters().add(m1);
		Arena.getMonsters().add(m2);
		Arena.getMonsters().add(m3);
		Arena.getMonsters().add(m4);
		Arena.getMonsters().add(m5);
		tower.shoot();
		assertEquals(m1.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m2.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m3.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m4.getHP(), 1000);
		assertEquals(m5.getHP(), 1000);
		Arena.getMonsters().clear();
	}
	
	@Test
	public void testUpgrade(){
		
	}
}
