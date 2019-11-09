package monster;

import org.junit.Test;

import arena.logic.Arena;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

public class PenguinTest {
	public Penguin penguin = null;
	public Arena arena = null;
	
	/**
	 * <p>
     * Setup before any testing.
     * <p>
     * Initialize penguin with both x-coordinate and y-coordinate (in grids) 0, and stage 1.
     * <p>
     * Also Initializa arena with the same attributes as the arena in the game.
     */
	@Before
	public void setUp() {
		penguin = new Penguin(0,0,1);
		arena = new Arena(480, 480, 15, 15, 12, 12, 40, 40, 1000, 50); // create the arena with the same attributes as the one in the real game
	}
	
	/**
	 * <p>
     * Cleanup after all testing.
     * <p>
     * Set penguin and arena to null.
     */
	@After
	public void tearDown() {
		penguin = null;
		arena = null;
	}
	
	/**
	 * <p>
     * Method for tesing whether a Penguin can be generated correctly given the parameters. 
     */
	@Test
	public void givenParametes_whenCreatePenguin_assertAttributesAllDefaultValue() {
		assertEquals(0, penguin.getXGrid());
		assertEquals(0, penguin.getYGrid());
		assertEquals(2, penguin.getSpeed());
		assertEquals(200, penguin.getHP());
		assertEquals("Penguin", penguin.getType());
		assertEquals(30, penguin.getRestoreHP());
	}
	
	/**
	 * <p>
     * Method for tesing whether a Penguin's HP would remain the same when it is already full. 
     */
	@Test
	public void givenFullHPPenguin_whenMove_assertNoHPChange() {
		penguin.setHP(200); // full HP
		Arena.getMonsters().add(penguin);
		Monster thisMonster = Arena.getMonsters().pop(); // since we would not this Monster in other tests
		penguin.move();
		assertEquals(200, thisMonster.getHP());
	}
	
	/**
	 * <p>
     * Method for tesing whether a Penguin would replenish some HP after move when its HP is not full. 
     */
	@Test
	public void givenNotFullHPPenguin_whenMove_assertNoHPChange() {
		penguin.setHP(100); // not full HP
		Arena.getMonsters().add(penguin);
		Monster thisMonster = Arena.getMonsters().pop(); // since we would not this Monster in other tests
		penguin.move();
		assertEquals(130, thisMonster.getHP());
	}
}