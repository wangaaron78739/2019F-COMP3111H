package monster;

import org.junit.Test;

import arena.logic.Arena;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;
import static arena.logic.ArenaConstants.*;

/**
 * 
 * Class implement testing for Monster.
 * @author CHIU Ka Ho
 * 
 */

public class MonsterTest {
	public Arena arena = null;
	public Monster monster = null;
	
	/**
	 * <p>
     * Setup before any testing.
     * <p>
     * Initialize Fox with both x-coordinate and y-coordinate (in pixels) 0, speed 2, maxHP 1000, and type Unicorn.
     * <p>
     * We are using Unicorn as it's the only one that doesn't have move() overrided.
     * <p>
     * Also Initializa arena with the same attributes as the arena in the game.
     */
	@Before
	public void setUp() {
		arena = new Arena(); // create the arena with the same attributes as the one in the real game
		monster = new Monster(0,0,2,1000,"Unicorn"); // we are using Unicorn as it's the only one that doesn't have move() overrided.
	}
	
	/**
	 * <p>
     * Cleanup after all testing.
     * <p>
     * Set monster and arena to null.
     */
	@After
	public void tearDown() {
		monster = null;
		arena = null;
	}
	
	/**
	 * <p>
     * Method for tesing whether a Monster can be generated correctly when no parameters are. 
     */
	@Test
	public void NotGivenParametes_whenCreateMonster_assertAttributesAllDefaultValue() {
		assertEquals(0, monster.getXGrid());
		assertEquals(0, monster.getYGrid());
		assertEquals(2, monster.getSpeed());
		assertEquals(1000, monster.getHP());
		assertEquals("Unicorn", monster.getType());
		assertEquals(1000, monster.getMaxHP());
		assertEquals("Left", monster.getDirection());
		assertEquals(0, monster.getCoolDown());
	}

	/**
	 * <p>
     * Method for tesing whether a Monster can be generated correctly given the parameters. 
     */
	@Test
	public void givenParametes_whenCreateMonster_assertAttributesAllDefaultValue() {
		assertEquals(0, monster.getXGrid());
		assertEquals(0, monster.getYGrid());
		assertEquals(2, monster.getSpeed());
		assertEquals(1000, monster.getHP());
		assertEquals("Unicorn", monster.getType());
		assertEquals(1000, monster.getMaxHP());
		assertEquals("Left", monster.getDirection());
		assertEquals(0, monster.getCoolDown());
	}
	
	/**
	 * <p>
     * Method for tesing whether a Monster can be generated correctly given another monster. 
     */
	@Test
	public void givenAnotherMonster_whenCreateMonster_assertAttributesAllDefaultValue() {
		Monster another = new Monster(monster);
		assertEquals(0, another.getXGrid());
		assertEquals(0, another.getYGrid());
		assertEquals(2, another.getSpeed());
		assertEquals(1000, another.getHP());
		assertEquals("Unicorn", another.getType());
		assertEquals(1000, another.getMaxHP());
		assertEquals("Left", another.getDirection());
		assertEquals(0, another.getCoolDown());
		another = null;
	}
	
	/**
	 * <p>
     * Method for tesing whether a Monster's attributes can be modified correctly with the setter functions.
     * <p>
     * Notice as attribute coolDown may affect getSpeed(), we will use a seperate test case for that.
     */
	@Test
	public void createdMonster_whenSetAttributes_assertAttributesAllSettedValue() {
		monster.setxPx(40);
		monster.setyPx(40);
		monster.setSpeed(1);
		monster.setHP(2000);
		monster.setTypeDeath();
		
		assertEquals(1, monster.getXGrid());
		assertEquals(1, monster.getYGrid());
		assertEquals(1, monster.getSpeed());
		assertEquals(2000, monster.getHP());
		assertEquals("Death", monster.getType());
	}
	
	/**
	 * <p>
     * Method for tesing whether a Monster's coolDown attributes can be modified correctly with the setter functions.
     */
	@Test
	public void createdMonster_whenSetAttributesForCoolDown_assertAttributesAllSettedValue() {
		monster.setCoolDown(2);
		
		assertEquals(1, monster.getSpeed());
		assertEquals(2, monster.getCoolDown());
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster's towerCount attribute would be correctly 0 when there's no tower built in the arena.
     */
	@Test
	public void givenEmptyArena_whenUpdateTowerCount_assertZeroTowerCount() {
		Arena.getMonsters().add(monster);
		Monster.updateTowerCount();
		Monster thisMonster = Arena.getMonsters().peek();
		assertEquals(0, thisMonster.getTowerCount());
		assertEquals(Monster.defaultCount, thisMonster.getGridInArena()[0][0].getValue());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster's towerCount attribute would be correctly 1 when there's only one tower built in the arena.
     */
	@Test
	public void givenArenaWithOneTower_whenUpdateTowerCount_assertOneTowerCount() {
		Arena.buildTower(0, 0, "Basic");
		Arena.getMonsters().add(monster);
		Monster.updateTowerCount();
		Monster thisMonster = Arena.getMonsters().peek();
		assertEquals(1, thisMonster.getTowerCount());
		assertEquals(Monster.defaultCount, thisMonster.getGridInArena()[0][0].getValue());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether gameEnds() can correctly determine whether the game has ended when Monster is not in end zone.
     * <p>
     * This test would cover the case when both the Monster's x-coordinate and its y-coordinate are not the same as end zone's, 
     * and the case when one of the Monster's x-coordinate and its y-coordinate is not the same as end zone's.
     */
	@Test
	public void givenMonsterNotInEndZone_whenCheckGameEnds_assertFalse() {
		// case 1: both x_coord and y-coord are not the same as end-zone
		Arena.getMonsters().add(monster);
		assertFalse(Monster.gameEnds());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
		// case 2: only y_coord is not the same as end zone
		monster.setxPx(460);
		Arena.getMonsters().add(monster);
		assertFalse(Monster.gameEnds());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
		// case 2: only x_coord is not the same as end zone
		monster.setxPx(0);
		monster.setyPx(460);
		Arena.getMonsters().add(monster);
		assertFalse(Monster.gameEnds());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether gameEnds() can correctly determine whether the game has ended when Monster is in end zone.
     */
	@Test
	public void givenMonsterInEndZone_whenCheckGameEnds_assertFalse() {
		monster.setxPx(460);
		monster.setyPx(460);
		Arena.getMonsters().add(monster);
		assertTrue(Monster.gameEnds());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether the array gridsInArena can be correctly updated by Monster's updateGrids() when there's no tower built in the arena.
     */
	@Test
	public void givenEmptyArena_whenUpdateGrids_assertRealDistanceToEndZone() {
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		Monster thisMonster = Arena.getMonsters().peek();
		assertEquals(0, thisMonster.getTowerCount()); // this is as the function also calls updateTowerCount()
		for (int i=0; i<MAX_H_NUM_GRID; ++i) {
			for (int j=0; j<MAX_V_NUM_GRID; ++j) {
				assertEquals(MAX_H_NUM_GRID-i-1+MAX_V_NUM_GRID-j-1, thisMonster.getGridInArena()[i][j].getValue());
			}
		}
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether the array gridsInArena can be correctly updated by Monster's updateGrids() when there's one tower built in the arena.
     * <p>
     * In this test case we assume both the x-coodinate and the y-coodinate (in grids) of the tower built are 0.
     */
	@Test
	public void givenArenaWithOneTowerWithLocation_0_0_whenUpdateGrids_assertRealDistanceToEndZone() {
		Arena.buildTower(0, 0, "Basic");
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		Monster thisMonster = Arena.getMonsters().peek();
		assertEquals(1, thisMonster.getTowerCount()); // this is as the function also calls updateTowerCount()
		for (int i=0; i<MAX_H_NUM_GRID; ++i) {
			for (int j=0; j<MAX_V_NUM_GRID; ++j) {
				if (!(i==0&&j==0)) // not the grids where the tower is built
					assertEquals(MAX_H_NUM_GRID-i-1+MAX_V_NUM_GRID-j-1, thisMonster.getGridInArena()[i][j].getValue());
				else assertEquals(Monster.defaultCount, thisMonster.getGridInArena()[i][j].getValue());
			}
		}
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether the array gridsInArena can be correctly updated by Monster's updateGrids() when there's one tower built in the arena.
     * <p>
     * In this test case we assume both the x-coodinate and the y-coodinate (in grids) of the tower built are 1.
     */
	@Test
	public void givenArenaWithOneTowerWithLocation_1_1_whenUpdateGrids_assertRealDistanceToEndZone() {
		Arena.buildTower(1, 1, "Basic");
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		Monster thisMonster = Arena.getMonsters().peek();
		assertEquals(1, thisMonster.getTowerCount()); // this is as the function also calls updateTowerCount()
		for (int i=0; i<MAX_H_NUM_GRID; ++i) {
			for (int j=0; j<MAX_V_NUM_GRID; ++j) {
				if (!(i==1&&j==1)) // not the grids where the tower is built
					assertEquals(MAX_H_NUM_GRID-i-1+MAX_V_NUM_GRID-j-1, thisMonster.getGridInArena()[i][j].getValue());
				else assertEquals(Monster.defaultCount, thisMonster.getGridInArena()[i][j].getValue());
			}
		}
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster can succeed in not choosing Left or Down as the moving direction,
     * when the Monster can't move left or down from its current grid.
     */
	@Test
	public void givenMonsterCoordCanNotGoLeftOrDownWithEmptyArena_whenDetermineDirection_assertNotGoLeftOrDown() {
		monster.setxPx(0);
		monster.setyPx(479);
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		String direction = monster.determineWhichDirectionAtCenter(monster.getXGrid(), monster.getYGrid());
		assertFalse(direction.equals("Left"));
		assertFalse(direction.equals("Down"));
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster can succeed in not choosing Up or Right as the moving direction,
     * when the Monster can't move up or right from its current grid.
     */
	@Test
	public void givenMonsterCoordCanNotGoUpOrRightWithEmptyArena_whenDetermineDirection_assertNotGoUpOrRight() {
		monster.setxPx(479);
		monster.setyPx(0);
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		String direction = monster.determineWhichDirectionAtCenter(monster.getXGrid(), monster.getYGrid());
		assertFalse(direction.equals("Up"));
		assertFalse(direction.equals("Right"));
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster can succeed in not choosing Left as the moving direction,
     * when the Monster can move left although it's not the optimal direction.
     * <p>
     * We will be using the case that both the x-coodinate and the y-coodinate (in grids) of the tower built are 1.
     * <p>
     * We will build a Tower, both the x-coodinate and the y-coodinate (in grids) of the Tower built are 3.
     */
	@Test
	public void givenMonsterCoordCanMoveAnyDirectionWithLeftIsNotMinimumAndNonEmptyArena_whenDetermineDirection_assertNotGoLeft() {
		Arena.buildTower(3, 3, "Basic");
		monster.setxPx(60);
		monster.setyPx(60);
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		String direction = monster.determineWhichDirectionAtCenter(monster.getXGrid(), monster.getYGrid());
		assertFalse(direction.equals("Left"));
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster can succeed in not choosing Up as the moving direction,
     * when the Monster can move up although it's not the optimal direction.
     * <p>
     * We will be using the case that both the x-coodinate and the y-coodinate (in grids) of the tower built are 1.
     * <p>
     * We will build a Tower, both the x-coodinate and the y-coodinate (in grids) of the Tower built are 3.
     */
	@Test
	public void givenMonsterCoordCanMoveAnyDirectionWithUpIsNotMinimumAndNonEmptyArena_whenDetermineDirection_assertNotGoUp() {
		Arena.buildTower(3, 3, "Basic");
		monster.setxPx(60);
		monster.setyPx(60);
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		String direction = monster.determineWhichDirectionAtCenter(monster.getXGrid(), monster.getYGrid());
		assertFalse(direction.equals("Up"));
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster can succeed in not choosing Right as the moving direction,
     * when the Monster can move right although it's not the optimal direction.
     * <p>
     * We will be using the case that both the x-coodinate of the tower built is 1, the y-coordinate of the tower built is 2.
     * <p>
     * We will build three Towers with carefully chosen coordinates.
     */
	@Test
	public void givenMonsterCoordCanMoveAnyDirectionWithRightIsNotMinimumAndNonEmptyArena_whenDetermineDirection_assertNotGoRight() {
		Arena.buildTower(1, 2, "Basic");
		Arena.buildTower(2, 3, "Basic");
		Arena.buildTower(3, 2, "Basic");
		monster.setxPx(60);
		monster.setyPx(100);
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		String direction = monster.determineWhichDirectionAtCenter(monster.getXGrid(), monster.getYGrid());
		assertFalse(direction.equals("Right"));
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster can succeed in not choosing Down as the moving direction,
     * when the Monster can move down although it's not the optimal direction.
     * <p>
     * We will be using the case that both the x-coodinate of the tower built is 2, the y-coordinate of the tower built is 1.
     * <p>
     * We will build three Towers with carefully chosen coordinates.
     */
	@Test
	public void givenMonsterCoordCanMoveAnyDirectionWithDownIsNotMinimumAndNonEmptyArena_whenDetermineDirection_assertNotGoDown() {
		Arena.buildTower(2, 1, "Basic");
		Arena.buildTower(3, 2, "Basic");
		Arena.buildTower(2, 3, "Basic");
		monster.setxPx(100);
		monster.setyPx(60);
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		String direction = monster.determineWhichDirectionAtCenter(monster.getXGrid(), monster.getYGrid());
		assertFalse(direction.equals("Down"));
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster's move() function can successfully have not effect when the monster is already in end zone,
     * causing the game to end.
     * <p>
     * We treat "No change in coodinates" as the meaning of "no move".
     */
	@Test
	public void givenMonsterInEndedGame_whenMove_assertNoMove() {
		monster.setxPx(460);
		monster.setyPx(460);
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		monster.move();
		assertEquals(11, monster.getXGrid());
		assertEquals(11, monster.getYGrid());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster's move() function can successfully have not effect when the monster is already dead, and is not in end zone.
     * <p>
     * We treat "No change in coodinates" as the meaning of "no move".
     */
	@Test
	public void givenDeadMonsterNotInEndZone_whenMove_assertNoMove() {
		monster.setxPx(0);
		monster.setyPx(0);
		monster.setTypeDeath();
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		monster.move();
		assertEquals(0, monster.getXGrid());
		assertEquals(0, monster.getYGrid());
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster's move() function can successfully determine where the direction should be,
     * given monster is at the center of a cell.
     * <p>
     * As we have tested the function determineWhichDirectionAtCenter() before, we just need to check one cell,
     * e.g. the one with both x-coordinate and y-coordinate (in grids) 0.
     */
	@Test
	public void givenMonsterInMiddelOfCell_whenMove_assertCanDetermineDirection() {
		monster.setxPx(19);
		monster.setyPx(19); // middle of the cell (0,0)
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		monster.move();
		if (Math.abs(monster.getxPx()-19)<0.01) assertEquals(21, monster.getyPx(), 0.01);
		else assertEquals(21, monster.getxPx(), 0.01);
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster's move() function can successfully determine where the direction should be,
     * given monster is at the center of a cell, and there's tower in below and on the right
     * <p>
     * As we have tested the function determineWhichDirectionAtCenter() before, we just need to check one cell,
     * e.g. the one with both x-coordinate and y-coordinate (in grids) 1.
     */
	@Test
	public void givenMonsterInMiddleOfCellwithTowerCase1_whenMove_assertCanDetermineDirection() {
		monster.setxPx(59);
		monster.setyPx(59); // middle of the cell (1,1)
		Arena.buildTower(1, 2, "Basic");
		Arena.buildTower(2, 1, "Basic");
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		monster.move();
		if (Math.abs(monster.getxPx()-59)<0.01) assertEquals(57, monster.getyPx(), 0.01);
		else assertEquals(57, monster.getxPx(), 0.01);
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster's move() function can successfully determine to move right,
     * given monster is slightly on the left of the middle of a given cell, facing left, 
     * but there's also a tower built in the left cell.
     * <p>
     * As we have tested the function determineWhichDirectionAtCenter() before, we just need to check one cell,
     * e.g. the one with both x-coordinate and y-coordinate (in grids) 1.
     */
	@Test
	public void givenMonsterNotInMiddleOfCellAndFacingLeft_whenMove_assertCanMoveRight() {
		monster.setxPx(57);
		monster.setyPx(59); // slightly left of the middle of cell (1,1)
		monster.setDirection("Left"); // facing left
		Arena.buildTower(0, 1, "Basic"); // tower on the left cell
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		monster.move();
		assertEquals(59, monster.getxPx(), 0.01); // since by condition it should change direction to "Right"
		assertEquals(59, monster.getyPx(), 0.01); // no change would be made here
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster's move() function can successfully to move left,
     * given monster is slightly on the right of the middle of a given cell, facing right, 
     * but there's also a tower built in the right cell.
     * <p>
     * As we have tested the function determineWhichDirectionAtCenter() before, we just need to check one cell,
     * e.g. the one with both x-coordinate and y-coordinate (in grids) 1.
     */
	@Test
	public void givenMonsterNotInMiddleOfCellAndFacingRight_whenMove_assertCanMoveLeft() {
		monster.setxPx(61);
		monster.setyPx(59); // slightly right of the middle of cell (1,1)
		monster.setDirection("Right"); // facing right
		Arena.buildTower(2, 1, "Basic"); // tower on the right cell
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		monster.move();
		assertEquals(59, monster.getxPx(), 0.01); // since by condition it should change direction to "Left"
		assertEquals(59, monster.getyPx(), 0.01); // no change would be made here
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster's move() function can successfully to move down,
     * given monster is slightly above the middle of a given cell, facing upwards, 
     * but there's also a tower built in the above cell.
     * <p>
     * As we have tested the function determineWhichDirectionAtCenter() before, we just need to check one cell,
     * e.g. the one with both x-coordinate and y-coordinate (in grids) 1.
     */
	@Test
	public void givenMonsterNotInMiddleOfCellAndFacingUp_whenMove_assertCanMoveDown() {
		monster.setxPx(59);
		monster.setyPx(57); // slightly above the middle of cell (1,1)
		monster.setDirection("Up"); // facing up
		Arena.buildTower(1, 0, "Basic"); // tower on the up cell
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		monster.move();
		assertEquals(59, monster.getxPx(), 0.01); // no change would be made here
		assertEquals(59, monster.getyPx(), 0.01); // since by condition it should change direction to "Up"
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster's move() function can successfully to move up,
     * given monster is slightly below the middle of a given cell, facing downwards, 
     * but there's also a tower built in the below cell.
     * <p>
     * As we have tested the function determineWhichDirectionAtCenter() before, we just need to check one cell,
     * e.g. the one with both x-coordinate and y-coordinate (in grids) 1.
     */
	@Test
	public void givenMonsterNotInMiddleOfCellAndFacingDown_whenMove_assertCanMoveUp() {
		monster.setxPx(59);
		monster.setyPx(61); // slightly below the middle of cell (1,1)
		monster.setDirection("Down"); // facing down
		Arena.buildTower(1, 2, "Basic"); // tower on the down cell
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		monster.move();
		assertEquals(59, monster.getxPx(), 0.01); // no change would be made here
		assertEquals(59, monster.getyPx(), 0.01); // since by condition it should change direction to "Down"
		Arena.getMonsters().pop(); // since we would not this Monster in other tests
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster's coolDown would be correctly the same if it has coolDown 0 before move().
     */
	@Test
	public void givenNotCoolDOwnMonster_whenMove_assertNoChangeInCoolDown() {
		monster.setCoolDown(0);
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		monster.move();
		assertEquals(0, monster.getCoolDown());
	}
	
	/**
	 * <p>
     * Method for tesing whether Monster's coolDown would be correctly decreased if it has coolDown >0 before move().
     */
	@Test
	public void givenCoolDOwnMonster_whenMove_assertChangeInCoolDown() {
		monster.setCoolDown(2);
		Arena.getMonsters().add(monster);
		Monster.updateGrids();
		monster.move();
		assertEquals(1, monster.getCoolDown());
	}
}
