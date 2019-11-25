package arena.ui;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import arena.logic.Arena;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ArenaUITest extends ApplicationTest {
    private ArenaUI arenaUI = null;

    @Override
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/arena.fxml"));
        Parent root = loader.load();
        stage.setTitle("Tower Defence");
        stage.setScene(new Scene(root, 680, 480));
        stage.show();
        arenaUI = loader.getController();
        arenaUI.createArena();
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

//    @Test
//    public void isEnableBuildTowers_and_setEnableBuildTowers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        arenaUI.getArena().buildTower(1,1,"Catapult");
//        Method setActiveCellMethod = ArenaUI.class.getDeclaredMethod("setActiveCell",int.class,int.class);
//        Method setHoveredCellMethod = ArenaUI.class.getDeclaredMethod("setHoveredCell",int.class,int.class);
//        Method upgradeActiveTowerMethod = ArenaUI.class.getDeclaredMethod("upgradeActiveTower");
//        Method deleteActiveTowerMethod = ArenaUI.class.getDeclaredMethod("deleteActiveTower");
//        setActiveCellMethod.setAccessible(true);
//        setHoveredCellMethod.setAccessible(true);
//        upgradeActiveTowerMethod.setAccessible(true);
//        deleteActiveTowerMethod.setAccessible(true);
//
//        setActiveCellMethod.invoke(arenaUI,1,1);
//        setActiveCellMethod.invoke(arenaUI,-1,-1);
//        setActiveCellMethod.invoke(arenaUI,1,1);
//        setHoveredCellMethod.invoke(arenaUI,1,1);
//        setHoveredCellMethod.invoke(arenaUI,-1,-1);
//        setHoveredCellMethod.invoke(arenaUI,1,1);
//        upgradeActiveTowerMethod.invoke(arenaUI);
//        deleteActiveTowerMethod.invoke(arenaUI);
//        setActiveCellMethod.invoke(arenaUI,1,1);
//        setHoveredCellMethod.invoke(arenaUI,1,1);
//
//
//        assert(ArenaUI.isEnableBuildTowers());
//        ArenaUI.setEnableBuildTowers(false);
//        assert(!ArenaUI.isEnableBuildTowers());
//    }
}