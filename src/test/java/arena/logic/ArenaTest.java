package arena.logic;

import monster.Fox;
import monster.Monster;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tower.BasicTower;
import tower.Tower;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;

import static org.junit.Assert.*;

public class ArenaTest {
    public Arena arena = null;
    OutputStream os = null;

    @Before
    public void setUp() throws Exception {
        arena = new Arena();
        os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
    }

    @After
    public void tearDown() throws Exception {
        arena = null;
        PrintStream originalOut = System.out;
        System.setOut(originalOut);
    }

    @Test
    public void logMonsterCreated() {
        Arena.addMonster(100,100,"Penguin");
        Monster mon = Arena.getMonsters().get(0);
        assertEquals(String.format("%s:%d generated\n",mon.getType(),mon.getMaxHP()), os.toString());
    }

    @Test
    public void logTowerUpgrade() {
        Tower tower = new BasicTower(1,1);
        Arena.logTowerUpgrade(tower);
        assertEquals(String.format("%s tower is being upgraded\n",tower.getType()),os.toString());
    }

    @Test
    public void logTowerUpgradeFailed() {
        Tower tower = new BasicTower(1,1);
        Arena.logTowerUpgradeFailed(tower);
        assertEquals(String.format("not enough resource to upgrade %s tower\n",tower.getType()),os.toString());
    }

    @Test
    public void buildTower() {
        boolean success = Arena.buildTower(0,0,"Basic");
        assert(success);
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
        assert(Arena.getTower(0,0)!=null);
        assert(Arena.getTower(0,0).getAttackPower()==45);
    }

    @Test
    public void nextFrame_and_startGame() {
        assert(Arena.getFrameCount()==-1);
        Arena.nextFrame();
        assert(!Arena.isGameStarted());
        Arena.startGame();
        assert(Arena.isGameStarted());
        assert(Arena.getFrameCount()==-1);
    }


}