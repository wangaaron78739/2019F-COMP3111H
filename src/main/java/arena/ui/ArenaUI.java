package arena.ui;

import arena.logic.Arena;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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

import java.io.FileInputStream;
import java.io.InputStream;

public class ArenaUI {

    @FXML
    private Button buttonSimulate;

    @FXML
    private Button buttonPlay;

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

    static final int ARENA_WIDTH = 480;
    static final int ARENA_HEIGHT = 480;
    static final int GRID_WIDTH = 40;
    static final int GRID_HEIGHT = 40;
    static final int MAX_H_NUM_GRID = 12;
    static final int MAX_V_NUM_GRID = 12;
    static final int INITIAL_RESOURCE_NUM = 400;

    private static Arena arena = null;
    private Label grids[][] = new Label[MAX_V_NUM_GRID][MAX_H_NUM_GRID]; //the grids on arena
    /**
     * A dummy function to show how button click works
     */
    @FXML
    private void play() {
        //TODO:
        System.out.println("Play button clicked");
    }

    /**
     * A function that create the Arena
     */
    @FXML
    public void createArena() {
        if (arena != null)
            return;
        arena = new Arena( ARENA_WIDTH,  ARENA_HEIGHT,  MAX_H_NUM_GRID,  MAX_V_NUM_GRID,  GRID_WIDTH,  GRID_HEIGHT,  INITIAL_RESOURCE_NUM);
        arena.initArena();
        for (int i = 0; i < MAX_V_NUM_GRID; i++)
            for (int j = 0; j < MAX_H_NUM_GRID; j++) {
                Label newLabel = new Label();
                newLabel.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                newLabel.setLayoutX(j * GRID_WIDTH);
                newLabel.setLayoutY(i * GRID_HEIGHT);
                newLabel.setMinWidth(GRID_WIDTH);
                newLabel.setMaxWidth(GRID_WIDTH);
                newLabel.setMinHeight(GRID_HEIGHT);
                newLabel.setMaxHeight(GRID_HEIGHT);
                newLabel.setStyle("-fx-border-color: black;");
                grids[i][j] = newLabel;
                paneArena.getChildren().addAll(newLabel);
            }
        setDragAndDrop();
    }

    private void updateUI() {
        //TODO:
    }

    @FXML
    private void simulateStart() {
        //TODO:
        System.out.println("Simulate button clicked");
        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("this is called every 5 seconds on UI thread");
            }
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }

    /**
     * A function that demo how drag and drop works
     */
    private void setDragAndDrop() {
        Label[] tower = {labelBasicTower,labelIceTower,labelCatapult,labelLaserTower};
        for (Label label : tower) {
            label.setOnDragDetected(new DragEventHandler(label));
        }
        for (Label[] row: grids) {
            for (Label cell: row) {
                cell.setOnDragDropped(new DragDroppedEventHandler());

                cell.setOnDragOver((event)-> {
                    /* data is dragged over the cell */
//                System.out.println("onDragOver");
                    /* accept it only if it is  not dragged from the same node
                     * and if it has a string data */
                    if (event.getGestureSource() != cell &&
                            event.getDragboard().hasImage()) {
                        /* allow for both copying and moving, whatever user chooses */
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                });

                cell.setOnDragEntered((event)-> {
                    /* the drag-and-drop gesture entered the cell */
//                    System.out.println("onDragEntered");
                    /* show to the user that it is an actual gesture cell */
                    if (event.getGestureSource() != cell &&
                            event.getDragboard().hasImage()) {
                        cell.setStyle("-fx-border-color: blue;");
                    }
                    event.consume();
                });
                cell.setOnDragExited((event) -> {
                    /* mouse moved away, remove the graphical cues */
                    cell.setStyle("-fx-border-color: black;");
//                    System.out.println("Exit");
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
    public void handle (MouseEvent event) {
        Dragboard db = source.startDragAndDrop(TransferMode.ANY);
        switch(source.getId()) {
            case "labelBasicTower":
                db.setContent(ArenaUIUtils.copyToClipboardImageFromFile("/basicTower.png"));
                break;
            case "labelCatapult":
                db.setContent(ArenaUIUtils.copyToClipboardImageFromFile("/catapult.png"));
                break;
            case "labelIceTower":
                db.setContent(ArenaUIUtils.copyToClipboardImageFromFile("/iceTower.png"));
                break;
            case "labelLaserTower":
                db.setContent(ArenaUIUtils.copyToClipboardImageFromFile("/laserTower.png"));
                break;
        }

        event.consume();
    }
}

class DragDroppedEventHandler implements EventHandler<DragEvent> {
    @Override
    public void handle(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasImage()) {
            success = true;
            ((Label)event.getGestureTarget()).setGraphic(ArenaUIUtils.setIcon(db.getImage()));
        }
        event.setDropCompleted(success);
        event.consume();

    }
}
