package arena.logic;

/**
 * Class to store constants related to the arena.
 * @author Aaron WANG
 */
public final class ArenaConstants {
    /**
     * Arena width in px
     */
    public static final int ARENA_WIDTH = 480;
    /**
     * Arena height in px
     */
    public static final int ARENA_HEIGHT = 480;
    /**
     * Width of a cell
     */
    public static final int GRID_WIDTH = 40;
    /**
     * Height of a cell
     */
    public static final int GRID_HEIGHT = 40;
    /**
     * Number of cells in arena horizontally
     */
    public static final int MAX_H_NUM_GRID = 12;
    /**
     * Number of cells in arena vertically
     */
    public static final int MAX_V_NUM_GRID = 12;
    /**
     * Initial resource amount
     */
    public static final int INITIAL_RESOURCE_NUM = 1000;
    /**
     * Update interval in ms
     */
    public static final int UPDATE_INTERVAL = 50;
    /**
     * Monster height in px
     */
    public static final int MONSTER_HEIGHT = 15;
    /**
     * Monster width in px
     */
    public static final int MONSTER_WIDTH = 15;

    /**
     * Private Constructor of ArenaConstants, should not be instantiated.
     */
    private ArenaConstants(){
        throw new AssertionError();
    }
}
