package monster;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * 
 * Class implement testing for one type of Monster, Unicorn.
 * This test is simple and don't need to put Unicorn into the game.
 * @author CHIU Ka Ho
 * 
 */

public class UnicornTest {
	public Unicorn unicorn = null;
	
	/**
	 * <p>
     * Setup before any testing.
     * <p>
     * Initialize unicorn with both x-coordinate and y-coordinate (in pixels) 0, and stage 1.
     */
	@Before
	public void setUp() {
		unicorn = new Unicorn(0,0,1);
	}
	
	/**
	 * <p>
     * Cleanup after all testing.
     * <p>
     * Set unicorn to null.
     */
	@After
	public void tearDown() {
		unicorn = null;
	}
	
	/**
	 * <p>
     * Method for tesing whether a Unicorn can be generated correctly given the parameters. 
     */
	@Test
	public void givenParametes_whenCreateUnicorn_assertAttributesAllDefaultValue() {
		assertEquals(0, unicorn.getXGrid());
		assertEquals(0, unicorn.getYGrid());
		assertEquals(2, unicorn.getSpeed());
		assertEquals(1000, unicorn.getHP());
		assertEquals("Unicorn", unicorn.getType());
	}
}
