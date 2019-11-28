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
		Arena.initArena(); // create the arena with the same attributes as the one in the real game
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
		Arena.initArena();
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
	
	/**
	 * <p>
     * Method for tesing whether a Fox can be generated correctly given another monster. 
     */
	@Test
	public void givenAnotherMonster_whenCreateFox_assertAttributesAllDefaultValue() {
		Monster monster  = new Monster(0,0,5,200,"Fox");
		fox = new Fox(monster);
		assertEquals(0, fox.getXGrid());
		assertEquals(0, fox.getYGrid());
		assertEquals(5, fox.getSpeed());
		assertEquals(200, fox.getHP());
		assertEquals("Fox", fox.getType());
		assertEquals(200, fox.getMaxHP());
		assertEquals("Left", fox.getDirection());
		assertEquals(0, fox.getCoolDown());
		for (int i=0; i<12; ++i) {
			for (int j=0; j<12; ++j) {
				assertTrue(fox.getGridInArenaFox()[i][j].getValue()<=Monster.defaultCount);
			}
		}
		monster = null;
	}
	
	/**
	 * <p>
     * Method for tesing whether the array gridsInArena can be successfully updated, 
     * for correctly updating the Cell above and on the left of the end zone, 
     * given the Arena with no Tower built.
     */
	@Test
	public void givenArenaWithNoTower_whenUpdateGridFox_assertCellBesideEndZoneCanBeUpdated() {
		Arena.getMonsters().add(fox);
		fox.updateGridsFox();
		assertEquals("Down", fox.getGridInArenaFox()[12-1][11-1].getFromCell());
		assertEquals("Right", fox.getGridInArenaFox()[11-1][12-1].getFromCell());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether the array gridsInArena can be successfully updated, 
     * for correctly updating the Cell above and on the left of the end zone, 
     * given the Arena with one Tower built on the Cell above the end zone
     */
	@Test
	public void givenArenaWithOneTowerCase1_whenUpdateGridFox_assertCellBesideEndZoneCanBeUpdated() {
		Arena.buildTower(12-1, 11-1, "Basic");
		Arena.getMonsters().add(fox);
		Fox.updateTowerCount();
		assertEquals(1, fox.getTowerCount());
		fox.updateGridsFox();
		assertEquals("Left", fox.getGridInArenaFox()[12-1][11-1].getFromCell()); // no update
		assertEquals("Right", fox.getGridInArenaFox()[11-1][12-1].getFromCell());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether the array gridsInArena can be successfully updated, 
     * for correctly updating the Cell above and on the left of the end zone, 
     * given the Arena with one Tower built on the Cell on the left of the end zone
     */
	@Test
	public void givenArenaWithOneTowerCase2_whenUpdateGridFox_assertCellBesideEndZoneCanBeUpdated() {
		Arena.buildTower(11-1, 12-1, "Basic");
		Arena.getMonsters().add(fox);
		Fox.updateTowerCount();
		assertEquals(1, fox.getTowerCount());
		fox.updateGridsFox();
		assertEquals("Down", fox.getGridInArenaFox()[12-1][11-1].getFromCell()); 
		assertEquals("Left", fox.getGridInArenaFox()[11-2][12-1].getFromCell()); // no update
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether the array gridsInArena can be successfully updated, 
     * for correctly a Cell with a BasicTower on the Cell on the right of it, separated by 1 more Cell inside, 
     * we would only consider the Cell at (0,0) for this case, 
     * the correct update should update the fromCell to "Down", as it shouldn't go right.
     */
	@Test
	public void givenArenaWithOneTowerCase3_whenUpdateGridFox_assertCellBesideEndZoneCanBeUpdated() {
		Arena.buildTower(3, 0, "Basic");
		Arena.getMonsters().add(fox);
		Fox.updateTowerCount();
		assertEquals(1, fox.getTowerCount());
		fox.updateGridsFox();
		assertEquals("Down", fox.getGridInArenaFox()[0][0].getFromCell());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether the array gridsInArena can be successfully updated, 
     * for correctly a Cell with a BasicTower on the Cell below it, separated by 1 more Cell inside, 
     * we would only consider the Cell at (0,0) for this case, 
     * the correct update should update the fromCell to "Right", as it shouldn't go down.
     */
	@Test
	public void givenArenaWithOneTowerCase4_whenUpdateGridFox_assertCellBesideEndZoneCanBeUpdated() {
		Arena.buildTower(0, 2, "Basic");
		Arena.getMonsters().add(fox);
		Fox.updateTowerCount();
		assertEquals(1, fox.getTowerCount());
		fox.updateGridsFox();
		assertEquals("Right", fox.getGridInArenaFox()[0][0].getFromCell());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether the array gridsInArena can be successfully updated, 
     * for correctly a Cell with a BasicTower on the Cell below it, on the right of it, and on the left of it
     * all separated by 1 more Cell inside.
     * We would only consider the Cell at (6,6) for this case, 
     * the correct update should update the fromCell to "Up", as it should only go this way.
     */
	@Test
	public void givenArenaWithThreeTowerCase1_whenUpdateGridFox_assertCellBesideEndZoneCanBeUpdated() {
		Arena.buildTower(4, 6, "Basic");
		Arena.buildTower(6, 8, "Basic");
		Arena.buildTower(8, 6, "Basic");
		Arena.getMonsters().add(fox);
		Fox.updateTowerCount();
		assertEquals(3, fox.getTowerCount());
		fox.updateGridsFox();
		assertEquals("Up", fox.getGridInArenaFox()[6][6].getFromCell());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether the array gridsInArena can be successfully updated, 
     * for correctly a Cell with a BasicTower on the Cell below it, on the right of it, and above it
     * all separated by 1 more Cell inside.
     * We would only consider the Cell at (6,6) for this case, 
     * the correct update should update the fromCell to "Left", as it should only go this way.
     */
	@Test
	public void givenArenaWithThreeTowerCase2_whenUpdateGridFox_assertCellBesideEndZoneCanBeUpdated() {
		Arena.buildTower(6, 4, "Basic");
		Arena.buildTower(6, 8, "Basic");
		Arena.buildTower(8, 6, "Basic");
		Arena.getMonsters().add(fox);
		Fox.updateTowerCount();
		assertEquals(3, fox.getTowerCount());
		fox.updateGridsFox();
		assertEquals("Left", fox.getGridInArenaFox()[6][6].getFromCell());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Fox can succeed in not choosing Left or Down as the moving direction,
     * when the Fox can't move left or down from its current grid.
     * <p>
     * We would build no Tower in this case.
     * <p>
     * The reason why we only use one case is that other possibilities are tested in MonsterTest.
     */
	@Test
	public void givenFoxCoordCanNotGoLeftOrDownWithEmptyArena_whenDetermineDirection_assertNotGoLeftOrDown() {
		fox.setxPx(0);
		fox.setyPx(479);
		Arena.getMonsters().add(fox);
		Fox.updateGrids();
		Fox.updateTowerCount();
		assertEquals(0, fox.getTowerCount());
		fox.updateGridsFox();
		String direction = fox.determineWhichDirectionAtCenter(fox.getXGrid(), fox.getYGrid());
		assertFalse(direction.equals("Left"));
		assertFalse(direction.equals("Down"));
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Fox can succeed in not choosing Left or Down as the moving direction,
     * when the Fox can't move left or down from its current grid.
     * <p>
     * We would build one Tower in this case.
     * <p>
     * The reason why we only use one case is that other similar possibilities are tested in MonsterTest.
     */
	@Test
	public void givenFoxCoordCanNotGoLeftOrDownWithOneTower_whenDetermineDirection_assertNotGoLeftOrDown() {
		fox.setxPx(0);
		fox.setyPx(479);
		Arena.buildTower(0, 0, "Basic");
		Arena.getMonsters().add(fox);
		Fox.updateGrids();
		Fox.updateTowerCount();
		assertEquals(1, fox.getTowerCount());
		fox.updateGridsFox();
		String direction = fox.determineWhichDirectionAtCenter(fox.getXGrid(), fox.getYGrid());
		assertFalse(direction.equals("Left"));
		assertFalse(direction.equals("Down"));
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
}
