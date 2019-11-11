package arena.logic;

/**
 * Class to store constants related to the arena.
 * @author Aaron WANG
 */
public final class ArenaConstants {
    public static final int ARENA_WIDTH = 480;
    public static final int ARENA_HEIGHT = 480;
    public static final int GRID_WIDTH = 40;
    public static final int GRID_HEIGHT = 40;
    public static final int MAX_H_NUM_GRID = 12;
    public static final int MAX_V_NUM_GRID = 12;
    public static final int INITIAL_RESOURCE_NUM = 1000;
    public static final int UPDATE_INTERVAL = 50;

    public static final int MONSTER_HEIGHT = 15;
    public static final int MONSTER_WIDTH = 15;

    /**
     * Private Constructor of ArenaConstants, should not be instantiated.
     */
    private ArenaConstants(){
        throw new AssertionError();
    }
}
