package monster;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

public class PenguinTest {
	public Penguin penguin = null;
	
	/**
	 * <p>
     * Setup before any testing.
     * <p>
     * Initialize penguin with both x-coordinate and y-coordinate (in grids) 0, and stage 1.
     */
	@Before
	public void setUp() {
		penguin = new Penguin(0,0,1);
	}
	
	/**
	 * <p>
     * Cleanup after all testing.
     * <p>
     * Set penguin to null.
     */
	@After
	public void tearDown() {
		penguin = null;
	}
	
	/**
	 * <p>
     * Method for tesing whether a Penguin can be generated correctly given the parameters. 
     */
	@Test
	public void givenParametes_whenCreateUnicorn_assertAttributesAllDefaultValue() {
		assertEquals(penguin.getXGrid(), 0);
		assertEquals(penguin.getYGrid(), 0);
		assertEquals(penguin.getSpeed(), 2);
		assertEquals(penguin.getHP(), 200);
		assertEquals(penguin.getType(), "Penguin");
		assertEquals(penguin.getRestoreHP(), 30);
	}
}