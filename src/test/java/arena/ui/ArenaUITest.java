package arena.ui;
import arena.logic.Resource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import arena.logic.Arena;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import tower.Tower;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static arena.logic.ArenaConstants.MAX_V_NUM_GRID;
import static arena.logic.ArenaConstants.UPDATE_INTERVAL;
import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class ArenaUITest extends ApplicationTest {
    private ArenaUI arenaUI = null;
    private int activeCellX = -1;
    private int activeCellY = -1;
    private int hoveredCellX = -1;
    private int hoveredCellY = -1;
    private Button buttonUpgradeTower = null;
    private Button buttonDeleteTower = null;
    private Label labelResource = null;
    private Label labelHovered = null;
    private Label labelActive = null;
    private Method setActiveCellMethod = null;
    private Method setHoveredCellMethod = null;
    private Method upgradeActiveTowerMethod = null;
    private Method deleteActiveTowerMethod = null;
    private Field labelEndZone = null;
    private Field fieldResource = null;
    private Field fieldHovered = null;
    private Field fieldActive = null;
    private Field fieldButtonUpgrade = null;
    private Field fieldButtonDelete = null;
    private Field fieldActiveTowerx = null;
    private Field fieldActiveTowery = null;
    private Field fieldHoveredTowerx = null;
    private Field fieldHoveredTowery = null;
    private CountDownLatch lock = new CountDownLatch(1);;
    @Override
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/arena.fxml"));
        Parent root = loader.load();
        stage.setTitle("Tower Defence");
        stage.setScene(new Scene(root, 680, 480));
        stage.show();
        arenaUI = loader.getController();
        arenaUI.createArena();

        fieldResource = ArenaUI.class.getDeclaredField("labelResource");
        fieldHovered = ArenaUI.class.getDeclaredField("labelHovered");
        fieldActive = ArenaUI.class.getDeclaredField("labelActive");
        fieldButtonUpgrade = ArenaUI.class.getDeclaredField("buttonUpgradeTower");
        fieldButtonDelete = ArenaUI.class.getDeclaredField("buttonDeleteTower");
        fieldActiveTowerx = ArenaUI.class.getDeclaredField("activeCellX");
        fieldActiveTowery = ArenaUI.class.getDeclaredField("activeCellY");
        fieldHoveredTowerx = ArenaUI.class.getDeclaredField("hoveredCellX");
        fieldHoveredTowery = ArenaUI.class.getDeclaredField("hoveredCellY");
        fieldActiveTowerx.setAccessible(true);
        fieldActiveTowery.setAccessible(true);
        fieldHoveredTowerx.setAccessible(true);
        fieldHoveredTowery.setAccessible(true);
        fieldButtonDelete.setAccessible(true);
        fieldButtonUpgrade.setAccessible(true);
        fieldResource.setAccessible(true);
        fieldHovered.setAccessible(true);
        fieldActive.setAccessible(true);
        activeCellX = (int) fieldActiveTowerx.get(arenaUI);
        activeCellY = (int) fieldActiveTowery.get(arenaUI);
        hoveredCellX = (int) fieldHoveredTowerx.get(arenaUI);
        hoveredCellY = (int) fieldHoveredTowery.get(arenaUI);
        labelEndZone = ArenaUI.class.getDeclaredField("labelEndZone");
        labelEndZone.setAccessible(true);
        buttonUpgradeTower = (Button) fieldButtonUpgrade.get(arenaUI);
        buttonDeleteTower = (Button) fieldButtonDelete.get(arenaUI);
        labelResource = (Label) fieldResource.get(arenaUI);
        labelHovered = (Label) fieldHovered.get(arenaUI);
        labelActive = (Label) fieldActive.get(arenaUI);
        setActiveCellMethod = ArenaUI.class.getDeclaredMethod("setActiveCell",int.class,int.class);
        setHoveredCellMethod = ArenaUI.class.getDeclaredMethod("setHoveredCell",int.class,int.class);
        upgradeActiveTowerMethod = ArenaUI.class.getDeclaredMethod("upgradeActiveTower");
        deleteActiveTowerMethod = ArenaUI.class.getDeclaredMethod("deleteActiveTower");
        setActiveCellMethod.setAccessible(true);
        setHoveredCellMethod.setAccessible(true);
        upgradeActiveTowerMethod.setAccessible(true);
        deleteActiveTowerMethod.setAccessible(true);

    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        arenaUI = null;
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void testBuildUpgradeDeleteHoverandActiveTowers() throws InterruptedException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        testSpecificTowerAndCell("Catapult",1,1);
        testSpecificTowerAndCell("Basic",3,1);
        testSpecificTowerAndCell("Laser",0,3);
        testSpecificTowerAndCell("Ice",10,10);
        assert(ArenaUI.isEnableBuildTowers());
        ArenaUI.setEnableBuildTowers(false);
        assert(!ArenaUI.isEnableBuildTowers());
        ArenaUI.setEnableBuildTowers(true);
        Label temp = (Label) labelEndZone.get(arenaUI);
        assert(temp != null);
        assert(temp.getGraphic() != null);
    }


    public void testSpecificTowerAndCell(String towerType, int x, int y) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InterruptedException {
        lock.await(UPDATE_INTERVAL*2, TimeUnit.MILLISECONDS);
        Arena.buildTower(x,y,towerType);
        lock.await(UPDATE_INTERVAL*2, TimeUnit.MILLISECONDS);
        labelResource = (Label) fieldResource.get(arenaUI);
        assert(labelResource.getText().equals(String.format("Money: %d", Resource.getResourceAmount())));

        setActiveCellMethod.invoke(arenaUI,x,y);
        lock.await(UPDATE_INTERVAL*2, TimeUnit.MILLISECONDS);
        activeCellX = (int) fieldActiveTowerx.get(arenaUI);
        activeCellY = (int) fieldActiveTowery.get(arenaUI);
        assert(activeCellX == x && activeCellY == y);
        Tower t = Arena.getTower(activeCellX,activeCellY);
        assert( t != null);
        lock.await(UPDATE_INTERVAL*2, TimeUnit.MILLISECONDS);
        labelActive = (Label) fieldActive.get(arenaUI);
        assert(labelActive.getText().equals(String.format("Selected Tower: %s\n Attack: %d\n Cost: %d\n Range: %d",
                t.getType(), t.getAttackPower(), t.getBuildingCost(), t.getShootingRange())));
        buttonUpgradeTower = (Button) fieldButtonUpgrade.get(arenaUI);
        buttonDeleteTower = (Button) fieldButtonDelete.get(arenaUI);
        assert(buttonDeleteTower.isVisible());
        assert(buttonUpgradeTower.isVisible());

        setActiveCellMethod.invoke(arenaUI,-1,-1);
        activeCellX = (int) fieldActiveTowerx.get(arenaUI);
        activeCellY = (int) fieldActiveTowery.get(arenaUI);
        assert(activeCellX == -1 && activeCellY == -1);
        t = Arena.getTower(activeCellX,activeCellY);
        assert(t==null);
        lock.await(UPDATE_INTERVAL*2, TimeUnit.MILLISECONDS);

        labelActive = (Label) fieldActive.get(arenaUI);

        assert(labelActive.getText().equals("Selected Tower: None"));
        buttonUpgradeTower = (Button) fieldButtonUpgrade.get(arenaUI);
        buttonDeleteTower = (Button) fieldButtonDelete.get(arenaUI);
        assert(!buttonDeleteTower.isVisible());
        assert(!buttonUpgradeTower.isVisible());


        setHoveredCellMethod.invoke(arenaUI,x,y);
        lock.await(UPDATE_INTERVAL*2, TimeUnit.MILLISECONDS);

        hoveredCellX = (int) fieldHoveredTowerx.get(arenaUI);
        hoveredCellY = (int) fieldHoveredTowery.get(arenaUI);
        assert(hoveredCellX==x && hoveredCellY == y);
        labelHovered = (Label) fieldHovered.get(arenaUI);
        t = Arena.getTower(hoveredCellX,hoveredCellY);
        assert t != null;
        assert(labelHovered.getText().equals(String.format("Hovered Tower: %s\n Attack: %d\n Cost: %d\n Range: %d",
                t.getType(), t.getAttackPower(), t.getBuildingCost(), t.getShootingRange())));

        setHoveredCellMethod.invoke(arenaUI,-1,-1);
        lock.await(UPDATE_INTERVAL*2, TimeUnit.MILLISECONDS);

        hoveredCellX = (int) fieldHoveredTowerx.get(arenaUI);
        hoveredCellY = (int) fieldHoveredTowery.get(arenaUI);
        assert(hoveredCellX==-1 && hoveredCellY == -1);
        labelHovered = (Label) fieldHovered.get(arenaUI);
        assert(labelHovered.getText().equals("Hovered Tower: None"));

        setHoveredCellMethod.invoke(arenaUI,x,y);
        hoveredCellX = (int) fieldHoveredTowerx.get(arenaUI);
        hoveredCellY = (int) fieldHoveredTowery.get(arenaUI);
        assert(hoveredCellX==x && hoveredCellY == y);

        setActiveCellMethod.invoke(arenaUI,x,y);
        upgradeActiveTowerMethod.invoke(arenaUI);
        activeCellX = (int) fieldActiveTowerx.get(arenaUI);
        activeCellY = (int) fieldActiveTowery.get(arenaUI);
        t = Arena.getTower(activeCellX,activeCellY);
        labelActive = (Label) fieldActive.get(arenaUI);
        assert t != null;
        lock.await(UPDATE_INTERVAL*2, TimeUnit.MILLISECONDS);
        assert(labelActive.getText().equals(String.format("Selected Tower: %s\n Attack: %d\n Cost: %d\n Range: %d",
                t.getType(), t.getAttackPower(), t.getBuildingCost(), t.getShootingRange())));


        deleteActiveTowerMethod.invoke(arenaUI);

        t = Arena.getTower(activeCellX,activeCellY);
        assert(t==null);
        lock.await(UPDATE_INTERVAL*2, TimeUnit.MILLISECONDS);
        labelActive = (Label) fieldActive.get(arenaUI);
        assert(labelActive.getText().equals("Selected Tower: None"));
        buttonUpgradeTower = (Button) fieldButtonUpgrade.get(arenaUI);
        buttonDeleteTower = (Button) fieldButtonDelete.get(arenaUI);
        assert(!buttonDeleteTower.isVisible());
        assert(!buttonUpgradeTower.isVisible());
        t = Arena.getTower(hoveredCellX,hoveredCellY);
        assert(t==null);
        lock.await(UPDATE_INTERVAL*2, TimeUnit.MILLISECONDS);
        labelHovered = (Label) fieldHovered.get(arenaUI);
        assert(labelHovered.getText().equals("Hovered Tower: None"));

    }
}