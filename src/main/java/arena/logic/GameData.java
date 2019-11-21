package arena.logic;

public class GameData {
    private int frameCount;
    private int resourceAmt;
    private int id;
    private String gameState;

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GameData() { }

    public GameData(int frameCount, int resourceAmt, String gameState) {
        this.frameCount = frameCount;
        this.resourceAmt = resourceAmt;
        this.gameState = gameState;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public int getResourceAmt() {
        return resourceAmt;
    }

    public void setResourceAmt(int resourceAmt) {
        this.resourceAmt = resourceAmt;
    }
}
