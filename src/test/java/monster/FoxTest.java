package monster;

import org.junit.Test;

import arena.logic.Arena;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * 
 * Class implement testing for one type of Monster, Fox.
 * @author CHIU Ka Ho
 * 
 */

public class FoxTest {
	public Arena arena = null;
	public Fox fox = null;

	/**
	 * <p>
     * Setup before any testing.
     * <p>
     * Initialize fox with both x-coordinate and y-coordinate (in pixels) 0 and stage 1.
     * <p>
     * Also Initializa arena with the same attributes as the arena in the game.
     */
	@Before
	public void setUp() {
		arena = new Arena(); // create the arena with the same attributes as the one in the real game
		fox = new Fox(0, 0, 1);
	}
	
	/**
	 * <p>
     * Cleanup after all testing.
     * <p>
     * Set penguin and arena to null.
     */
	@After
	public void tearDown() {
		fox = null;
		arena = null;
	}
	
	/**
	 * <p>
     * Method for tesing whether a Fox can be generated correctly given the parameters. 
     */
	@Test
	public void givenParametes_whenCreateFox_assertAttributesAllDefaultValue() {
		assertEquals(0, fox.getXGrid());
		assertEquals(0, fox.getYGrid());
		assertEquals(5, fox.getSpeed());
		assertEquals(200, fox.getHP());
		assertEquals("Fox", fox.getType());
	}
}
