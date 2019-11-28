package arena.logic;

/**
 * Class to store the GameData using hibernate
 * @author Aaron WANG
 */
public class GameData {
    private int frameCount;
    private int resourceAmt;
    private int id;
    private String gameState;

    /**
     * Return the game state
     * @return State of the game: {not started, play, simulate, ended}
     */
    public String getGameState() {
        return gameState;
    }

    /**
     * Setter method for gameState
     * @param gameState new game state
     */
    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    /**
     * Getter method for Id
     * @return Id of game
     */
    public int getId() {
        return id;
    }

    /**
     * Setter method for Id
     * @param id Id of game
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Default constructor for GameData
     */
    public GameData() { }

    /**
     * Constructor for GameData
     * @param frameCount Current Frame Count
     * @param resourceAmt Resource Amount
     * @param gameState Current Game State
     */
    public GameData(int frameCount, int resourceAmt, String gameState) {
        this.frameCount = frameCount;
        this.resourceAmt = resourceAmt;
        this.gameState = gameState;
    }

    /**
     * Getter method for frameCount
     * @return frameCount
     */
    public int getFrameCount() {
        return frameCount;
    }

    /**
     * Setter method for frameCount
     * @param frameCount frameCount of the game
     */
    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    /**
     * Getter method for resourceAmt
     * @return amount of resource in the game
     */
    public int getResourceAmt() {
        return resourceAmt;
    }

    /**
     * Setter method for resourceAmt
     * @param resourceAmt ammount of resource for the game
     */
    public void setResourceAmt(int resourceAmt) {
        this.resourceAmt = resourceAmt;
    }
}
