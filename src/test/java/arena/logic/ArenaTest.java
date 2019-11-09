package arena.logic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class ArenaTest {
    public Arena arena = null;

    @Before
    public void setUp() throws Exception {
        arena = new Arena();
    }

    @After
    public void tearDown() throws Exception {
        arena = null;
    }

    @Test
    public void loggingCoverage() {

    }

    @Test
    public void logMonsterCreated() {
    }

    @Test
    public void logTowerUpgrade() {
    }

    @Test
    public void logTowerUpgradeFailed() {
    }

    @Test
    public void getTower() {
    }

    @Test
    public void towerBuilt() {
    }

    @Test
    public void towerBuiltType() {
    }

    @Test
    public void setTowerBuilt() {
    }

    @Test
    public void buildTower() {
        boolean success = Arena.buildTower(0,0,"Basic");
        assert(success);
        System.out.println(Arena.getTower(0, 0).getType());
        assert(Arena.getTower(0, 0).getType().equals("Basic"));
        success = Arena.buildTower(0,0,"Catapult");
        assert(!success);
        success = Arena.buildTower(-1,0,"Ice");
        assert(!success);
        success = Arena.buildTower(1,0,"Ice");
        assert(success);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildIllegalTower() {
        Arena.buildTower(0,0,"");
    }
    @Test
    public void getMonsters() {
    }

    @Test
    public void getResource() {
    }

    @Test
    public void getTowers() {
    }

    @Test
    public void getMonsterNum() {
    }

    @Test
    public void getStage() {

    }

    @Test
    public void addMonster() {
        boolean success = Arena.addMonster(100,100,"Unicorn");
        assert(success);
        success = Arena.addMonster(100,100,"Penguin");
        assert(success);
        success = Arena.addMonster(100,100,"Fox");
        assert(success);
        Arena.buildTower(0,0,"Laser");
        success = Arena.addMonster(20,20,"Fox");
        assert(!success);
    }
    @Test
    public void deleteTowerAt() {
        Arena.buildTower(0,0,"Catapult");
        Arena.deleteTowerAt(0,0);
        assert(Arena.getTower(0,0)==null);
    }

    @Test
    public void upgradeTowerAt() {
        Arena.upgradeTowerAt(0,0);
        Arena.buildTower(0,0,"Basic");
        Arena.upgradeTowerAt(0,0);
    }

    @Test
    public void nextFrame_and_startGame() {
        assert(Arena.getFrameCount()==-1);
        Arena.nextFrame();
        assert(!Arena.isGameStarted());
        Arena.startGame();
        assert(Arena.isGameStarted());
        assert(Arena.getFrameCount()==-1);
//        for(int i=0;i<50;i++) {
//            Arena.nextFrame();
//            assert(Arena.getFrameCount()==i);
//        }
    }


}