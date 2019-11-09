package monster;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

/**
 * 
 * Class implement testing for Cell in game.
 * @author CHIU Ka Ho
 * 
 */

public class MonsterCellTest {
	public Monster.Cell cell = null;
	
	/**
	 * <p>
     * Setup before any testing.
     * <p>
     * Initialize cell with both x-coordinate and y-coordinate (in grids) 0.
     */
	@Before
	public void setUp() {
		cell = new Monster.Cell(0,0);
	}

	/**
	 * <p>
     * Cleanup after all testing.
     * <p>
     * Set unicorn to null.
     */
	@After
	public void tearDown() {
		cell = null;
	}
	
	/**
	 * <p>
     * Method for tesing whether a Cell can be generated correctly given the parameters. 
     */
	@Test
	public void givenParametes_whenCreateMonsterCell_assertAttributesAllDefaultValue() {
		assertEquals(cell.getXGrid(), 0);
		assertEquals(cell.getYGrid(), 0);
		assertEquals(cell.getValue(), Monster.defaultCount);
		assertEquals(cell.getFromCell(), "Left");
	}
	
	/**
	 * <p>
     * Method for tesing whether a Cell's attributes can be modified correctly with the setter functions.
     */
	@Test
	public void createdMonsterCell_whenSetAttributes_assertAttributesAllSettedValue() {
		cell.setValue(1);
		cell.setFromCell("Up");
		
		assertEquals(cell.getValue(), 1);
		assertEquals(cell.getFromCell(), "Up");
	}
}
