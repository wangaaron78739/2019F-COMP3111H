package arena.logic;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test Class for Arena Constants
 */
public class ArenaConstantsTest {
    /**
     * Test ArenaConstants to make sure that the properties are correct.
     */
    @Test
    public void assert_ArenaConstants_AssertPropertiesCorrect() {
         assert(ArenaConstants.ARENA_WIDTH == 480);
         assert(ArenaConstants.ARENA_HEIGHT == 480);
         assert(ArenaConstants.GRID_WIDTH == 40);
         assert(ArenaConstants.GRID_HEIGHT == 40);
         assert(ArenaConstants.MAX_H_NUM_GRID == 12);
         assert(ArenaConstants.MAX_V_NUM_GRID == 12);
         assert(ArenaConstants.INITIAL_RESOURCE_NUM == 1000);
         assert(ArenaConstants.UPDATE_INTERVAL == 50);
         assert(ArenaConstants.MONSTER_HEIGHT == 15);
         assert(ArenaConstants.MONSTER_WIDTH == 15);
    }

}