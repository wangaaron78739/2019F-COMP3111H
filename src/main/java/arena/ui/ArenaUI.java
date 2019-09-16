package arena.ui;

import arena.logic.Arena;
import arena.logic.Resource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import monster.Monster;
import tower.Tower;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArenaUI {

    @FXML
    private Button buttonSimulate;

    @FXML
    private Button buttonPlay;

    @FXML
    private Button buttonUpgradeTower;

    @FXML
    private Button buttonDeleteTower;

    @FXML
    private AnchorPane paneArena;

    @FXML
    private Label labelBasicTower;

    @FXML
    private Label labelIceTower;

    @FXML
    private Label labelCatapult;

    @FXML
    private Label labelLaserTower;

    @FXML
    private Label labelResource;

    @FXML
    private static ArrayList<Label> labelProjectiles = new ArrayList<Label>();

    @FXML
    private static ArrayList<Label> labelMonsters = new ArrayList<Label>();

    static final int ARENA_WIDTH = 480;
    static final int ARENA_HEIGHT = 480;
    static final int GRID_WIDTH = 40;
    static final int GRID_HEIGHT = 40;
    static final int MAX_H_NUM_GRID = 12;
    static final int MAX_V_NUM_GRID = 12;
    static final int INITIAL_RESOURCE_NUM = 400;
    static final int UPDATE_INTERVAL = 50;

    static final int MONSTER_HEIGHT = 15;
    static final int MONSTER_WIDTH = 15;

    private static Arena arena = null;
    private static Label grids[][] = new Label[MAX_V_NUM_GRID][MAX_H_NUM_GRID]; //the grids on arena
    static int x = 100;
    static int y = 100;
    static int activeCellX = -1;
    static int activeCellY = -1;
    /**
     * A dummy function to show how button click works
     */
    @FXML
    private void play() {
        //TODO:
        System.out.println("Play button clicked");

        arena.addMonster(x+= 10,y+= 10, "Fox");
    }

    /**
     * A function that create the Arena
     */
    @FXML
    public void createArena() {
        if (arena != null)
            return;
        arena = new Arena(ARENA_WIDTH, ARENA_HEIGHT, MAX_H_NUM_GRID, MAX_V_NUM_GRID,
                GRID_WIDTH, GRID_HEIGHT, INITIAL_RESOURCE_NUM, UPDATE_INTERVAL);
        arena.initArena();
        for (int j = 0; j < MAX_V_NUM_GRID; j++)
            for (int i = 0; i < MAX_H_NUM_GRID; i++) {
                Label newLabel = new Label();
                newLabel.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                newLabel.setLayoutX(j * GRID_WIDTH);
                newLabel.setLayoutY(i * GRID_HEIGHT);
                newLabel.setMinWidth(GRID_WIDTH);
                newLabel.setMaxWidth(GRID_WIDTH);
                newLabel.setMinHeight(GRID_HEIGHT);
                newLabel.setMaxHeight(GRID_HEIGHT);
                newLabel.setId(String.format("label x:%d,y:%d", j, i));
                newLabel.setStyle("-fx-border-color: black;");
                grids[i][j] = newLabel;
                paneArena.getChildren().addAll(newLabel);
            }
        setDragAndDrop();
        startUpdateUILoop();
    }

    public static Arena getArena() {
        return arena;
    }

    private void startUpdateUILoop() {
        Timeline timer = new Timeline(new KeyFrame(Duration.millis(UPDATE_INTERVAL), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateUI();
                Arena.nextFrame();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    @FXML
    private void updateUI() {
        //TODO:
        //TOWER
        for (int j = 0; j < MAX_V_NUM_GRID; j++)
            for (int i = 0; i < MAX_H_NUM_GRID; i++) {
                Tower tower = Arena.getTower(i,j);
                if (tower != null) {
                                switch (tower.getType()) {
                case "BasicTower":
                    grids[j][i].setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/basicTower.png")));
                    break;
                case "Catapult":
                    grids[j][i].setGraphic(ArenaUIUtils.setIcon((ArenaUIUtils.getImage("/catapult.png"))));
                    break;
                case "IceTower":
                    grids[j][i].setGraphic(ArenaUIUtils.setIcon((ArenaUIUtils.getImage("/iceTower.png"))));
                    break;
                case "LaserTower":
                    grids[j][i].setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/laserTower.png")));
                    break;
            }
                } else {
                    grids[j][i].setGraphic(null);
                }
            }
        //PROJECTILE TODO:
//        if (Arena.getProjectileNum() < labelProjectiles.size()) {
//            while (Arena.getProjectileNum() < labelProjectiles.size()) {
//                paneArena.getChildren().remove(labelProjectiles.get(0));
//                labelProjectiles.remove(0);
//            }
//        } else if (Arena.getProjectileNum() > labelProjectiles.size()) {
//            while (Arena.getProjectileNum() > labelProjectiles.size()) {
//                Label l = new Label("Proj");
//                labelProjectiles.add(l);
//                paneArena.getChildren().add(l);
//            }
//        }
        for (int i=0;i<labelProjectiles.size();i++) {
//            labelProjectiles.get(i).setLayoutX(Arena.getProjectiles().get(i).getXPos());
//            labelProjectiles.get(i).setLayoutY(Arena.getProjectiles().get(i).getXPos());
        }
        //MONSTER
        if (Arena.getMonsterNum() < labelMonsters.size()) {
            while (Arena.getMonsterNum() < labelMonsters.size()) {
                paneArena.getChildren().remove(labelMonsters.get(0));
                labelMonsters.remove(0);
            }
        } else if (Arena.getMonsterNum() > labelMonsters.size()) {
            while (Arena.getMonsterNum() > labelMonsters.size()) {
                Label l = new Label();
                l.setMinWidth(MONSTER_WIDTH);
                l.setMaxWidth(MONSTER_WIDTH);
                l.setMinHeight(MONSTER_HEIGHT);
                l.setMaxHeight(MONSTER_HEIGHT);
                labelMonsters.add(l);
                paneArena.getChildren().add(l);
            }
        }
        for (int i=0;i<labelMonsters.size();i++) {
            Monster m = Arena.getMonsters().get(i);
            Label l = labelMonsters.get(i);
            l.setLayoutX(m.getXPos()-MONSTER_WIDTH/2);
            l.setLayoutY(m.getYPos()-MONSTER_HEIGHT/2);
            switch (m.getType()) {
                case "Fox":
                    l.setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/fox.png"),MONSTER_HEIGHT,MONSTER_WIDTH));
                    break;
                case "Penguin":
                    l.setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/penguin.png"),MONSTER_HEIGHT,MONSTER_WIDTH));
                    break;
                case "Unicorn":
                    l.setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/unicorn.png"),MONSTER_HEIGHT,MONSTER_WIDTH));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + m);
            }
        }
        //RESOURCE
        labelResource.setText(String.format("Money: %d", Resource.getResourceAmount()));
    }

    @FXML
    private void simulateStart() {
        //TODO:
        arena.getMonsters().remove(0);
    }

    @FXML
    private void deleteActiveTower() {
        if (activeCellX != -1 && activeCellY != -1) {
            Arena.deleteTowerAt(activeCellX,activeCellY);
        }
    }

    @FXML
    private void upgradeActiveTower() {
        if (activeCellX != -1 && activeCellY != -1) {
            Arena.upgradeTowerAt(activeCellX,activeCellY);
        }
    }

    private void setActiveCell(int x,int y) {
        if (activeCellX != -1 && activeCellY != -1) {
            grids[activeCellY][activeCellX].setStyle("-fx-border-color: black;");
        }
        activeCellX = x;
        activeCellY = y;
        grids[y][x].setStyle("-fx-border-color: orange;");
    }
    /**
     * A function that demo how drag and drop works
     */
    private void setDragAndDrop() {
        Label[] tower = {labelBasicTower, labelIceTower, labelCatapult, labelLaserTower};
        for (Label label : tower) {
            label.setOnDragDetected(new DragEventHandler(label));
        }
        for (int i=0;i<grids.length;i++) {
            for (int j=0;j<grids.length;j++) {
                Label cell = grids[j][i];
                cell.setOnDragDropped(new DragDroppedEventHandler());

                cell.setOnDragOver((event) -> {
                    /* data is dragged over the cell */
//                System.out.println("onDragOver");
                    /* accept it only if it is  not dragged from the same node
                     * and if it has a string data */
                    if (event.getGestureSource() != cell &&
                            event.getDragboard().hasString()) {
                        /* allow for both copying and moving, whatever user chooses */
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                });

                cell.setOnDragEntered((event) -> {
                    /* the drag-and-drop gesture entered the cell */
//                    System.out.println("onDragEntered");
                    /* show to the user that it is an actual gesture cell */
                    if (event.getGestureSource() != cell &&
                            event.getDragboard().hasString()) {
                        cell.setStyle("-fx-border-color: blue;");
                    }
                    event.consume();
                });
                cell.setOnDragExited((event) -> {
                    cell.setStyle("-fx-border-color: black;");
                    event.consume();
                });

                int finalI = i;
                int finalJ = j;
                cell.setOnMouseClicked((event) -> {
                    setActiveCell(finalI, finalJ);
                    event.consume();
                });
            }
        }

    }
}


class DragEventHandler implements EventHandler<MouseEvent> {
    private Label source;

    public DragEventHandler(Label e) {
        source = e;
    }

    @Override
    public void handle(MouseEvent event) {
        Dragboard db = source.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(source.getText());
        switch (source.getId()) {
            case "labelBasicTower":
                content.putString("BasicTower");
                db.setDragView(ArenaUIUtils.getImage("/basicTower.png"));
                break;
            case "labelCatapult":
                content.putString("Catapult");
                db.setDragView(ArenaUIUtils.getImage("/catapult.png"));
                break;
            case "labelIceTower":
                content.putString("IceTower");
                db.setDragView(ArenaUIUtils.getImage("/iceTower.png"));
                break;
            case "labelLaserTower":
                content.putString("LaserTower");
                db.setDragView(ArenaUIUtils.getImage("/laserTower.png"));
                break;
        }
        db.setContent(content);

        event.consume();
    }
}

class DragDroppedEventHandler implements EventHandler<DragEvent> {
    Pattern gridRegex = Pattern.compile("label x:(\\d+),y:(\\d+)");

    @Override
    public void handle(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        Label target = (Label) event.getGestureTarget();
        System.out.println(target.getId());
        Matcher m = gridRegex.matcher(target.getId());
        m.find();
        int x = Integer.parseInt(m.group(1));
        int y = Integer.parseInt(m.group(2));
        if (ArenaUI.getArena().buildTower(x, y, db.getString())) {
            System.out.println(db.getString());
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();

    }
}
