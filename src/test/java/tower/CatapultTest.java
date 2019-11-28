package tower;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import arena.logic.*;
import monster.*;

public class CatapultTest {

	private Arena arena = null;
	private Catapult tower = null;
	//private LinkedList<Monster> monsterList = null;
	
	@Before
	public void setUp() throws Exception {
		arena = new Arena();
		tower = new Catapult(6,6);
	}
	
	@After
	public void tearDown() throws Exception {
		arena = null;
		tower = null;
	}

	@Test
	public void assert_CatapultConstants_AssertPropertiesCorrect(){
		assertEquals(tower.getAttackPower(),50);
		assertEquals(tower.getBuildingCost(),100);
		assertEquals(tower.getShootingRange(),150);
		assertEquals(tower.getX(),6);
		assertEquals(tower.getY(),6);
		assertEquals(tower.getType(),"Catapult");
		assertEquals(tower.getUpperShootingRange(),150);
		assertEquals(tower.getLowerShootingRange(),50);
		assertEquals(tower.getBaseAttackRadius(),25);
		assertEquals(tower.getUpgradeCooldownTime(), 5);
		assertEquals(tower.getCooldown(), 50);
	} 
	
//	@Test
//	public void testShoot_IfInCooldownPeriod(){
//		Arena.setFrameCount(1);
//		
//	}
	
	@Test
	public void testShoot_TestUpperRange(){
		Arena.setFrameCount(55);
		Monster m1 = new Monster(265, 415, 0, 1000, "Unicorn");
		Monster m2 = new Monster(260, 410, 0, 1000, "Unicorn");
		Monster m3 = new Monster(260, 390, 0, 1000, "Unicorn");
		Monster m4 = new Monster(285, 390, 0, 1000, "Unicorn");
		Monster m5 = new Monster(310, 310, 0, 1000,"Unicorn");
		Arena.getMonsters().add(m1);
		Arena.getMonsters().add(m2);
		Arena.getMonsters().add(m3);
		Arena.getMonsters().add(m4);
		Arena.getMonsters().add(m5);
		tower.shoot();
		assertEquals(m1.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m2.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m3.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m4.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m5.getHP(), 1000);
		Arena.getMonsters().clear();
		Arena.setFrameCount(-1);
	}
	
	@Test
	public void testShoot_TestLowerRange(){
		for(int x = 260; x <= 320; x = x + 10){
			Monster m = new Monster(x, x, 0 , 500, "Unicorn");
			Arena.getMonsters().add(m);
		}
		for(int x = 0; x < 11; x++){
			Arena.setFrameCount(Arena.getFrameCount() + 55);
			tower.shoot();
		} 
		Arena.getMonsters().removeIf(m->m.getHP() <= 0);
		assertEquals(Arena.getMonsterNum(), 3);
		Arena.setFrameCount(-1); 
		Arena.getMonsters().clear();
	}
	
	@Test
	public void testShoot_TestTie(){
		Arena.setFrameCount(55);
		Monster m1 = new Monster(265, 415, 0, 1000, "Unicorn");
		Monster m2 = new Monster(260, 410, 0, 1000, "Unicorn");
		Monster m3 = new Monster(260, 390, 0, 1000, "Unicorn");
		Monster m4 = new Monster(285, 390, 0, 1000, "Unicorn");
		Monster m5 = new Monster(415, 265, 0, 1000,"Unicorn");
		Monster m6 = new Monster(410, 260, 0, 1000,"Unicorn");
		Monster m7 = new Monster(390, 260, 0, 1000,"Unicorn");
		Arena.getMonsters().add(m1);
		Arena.getMonsters().add(m2);
		Arena.getMonsters().add(m3);
		Arena.getMonsters().add(m4);
		Arena.getMonsters().add(m5);
		Arena.getMonsters().add(m6);
		Arena.getMonsters().add(m7);
		tower.shoot();
		assertEquals(m1.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m2.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m3.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m4.getHP(), (1000 - tower.getAttackPower()));
		assertEquals(m5.getHP(), 1000);
		assertEquals(m6.getHP(), 1000);
		assertEquals(m7.getHP(), 1000);
		Arena.getMonsters().clear();
		Arena.setFrameCount(-1);
	}
	
	@Test
	public void testUpgrade(){
		int resourceAmount = Resource.getResourceAmount();
		Resource.addResourceAmount(tower.getUpgradeCost());
		int ct = tower.getCooldown();
		tower.upgrade();
		assertEquals(resourceAmount, Resource.getResourceAmount());
		assertEquals(tower.getCooldown(), ct - tower.getUpgradeCooldownTime());
	}
	
	@Test
	public void testCanAttack(){
		assertEquals(tower.canAttack(310, 310), true);
	}
}
