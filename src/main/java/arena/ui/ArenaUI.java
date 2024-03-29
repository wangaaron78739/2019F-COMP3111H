package arena.ui;

import arena.logic.Arena;
import arena.logic.GameData;
import arena.logic.Resource;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import monster.Fox;
import monster.Monster;
import static arena.logic.ArenaConstants.*;

import monster.Penguin;
import monster.Unicorn;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import tower.*;

import javax.persistence.metamodel.EntityType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to handle the UI of the Arena
 * @author Aaron WANG
 */
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
    private AnchorPane paneInfo;

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
    private Label labelHovered;

    @FXML
    private Label labelActive;

    @FXML
    private Label labelFrameCount;

    @FXML
    private Label labelEndZone;

    @FXML
    private Label labelStartZone;

    @FXML
    private static ArrayList<Label> labelProjectiles = new ArrayList<Label>();

    @FXML
    private static ArrayList<Label> labelMonsters = new ArrayList<Label>();

    @FXML
    private static Circle hoveredRangeUI = new Circle(0, new Color(0.5,0.7,1,0.5));

    @FXML
    private static Circle activeRangeUI = new Circle(0, new Color(1,0.5,0,0.5));


    @FXML
    private ArrayList<Line> LaserAttackTraceUI = new ArrayList<Line>();

    private static Label grids[][] = new Label[MAX_V_NUM_GRID][MAX_H_NUM_GRID]; //the grids on arena
    private static int activeCellX = -1;
    private static int activeCellY = -1;
    private static int hoveredCellX = -1;
    private static int hoveredCellY = -1;

    private static boolean enableBuildTowers = true;
	private boolean gameOverShown = false;
    private Tooltip tooltip = new Tooltip();

    private static final SessionFactory factory;
    static {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Decfault constructor
     */
    public ArenaUI() {

    }

    /**
     * A function that create the Arena
     */
    @FXML
    public void createArena() {
        Arena.initArena();
        final Session session = factory.openSession();
        List monsters = new LinkedList<Monster>();
        List towers = new LinkedList<Tower>();
        List gamedata;

        try {
//            System.out.println("querying all the managed entities...");
            monsters = session.createQuery("from Monster").list();
            towers = session.createQuery("from Tower").list();
            gamedata = session.createQuery("from GameData").list();
        } finally {
            session.close();
        }
        GameData data = gamedata.size()>0?(GameData) gamedata.get(0):null;
        if (data != null && !data.getGameState().equals("ended")) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Load From Previous Game");
            alert.setHeaderText("Previous Game Data Detected");
            alert.setContentText("Load from Previous Game or Start New?");

            ButtonType NewGame = new ButtonType("New Game");
            ButtonType Continue = new ButtonType("Continue");

            alert.getButtonTypes().setAll(NewGame, Continue);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == Continue) {
                Arena.setFrameCount(data.getFrameCount());
                Resource.setResourceAmount(data.getResourceAmt());
                if (data.getGameState().equals("simulate")) {
                    Arena.startGame();
                    enableBuildTowers = false;
                }else if (data.getGameState().equals("play")) {
                    Arena.startGame();
                }
                for (Object obj: towers) {
                    Tower t = (Tower) obj;
                    Tower newTower;
                    switch (t.getType()) {
                        case "Basic":
                            newTower = new BasicTower(t.getX(),t.getY());
                            break;
                        case "Catapult":
                            newTower = new Catapult(t.getX(),t.getY());
                            break;
                        case "Ice":
                            newTower = new IceTower(t.getX(),t.getY());
                            break;
                        case "Laser":
                            newTower = new LaserTower(t.getX(),t.getY());
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + t.getType());
                    }
                    newTower.setAttackPower(t.getAttackPower());
                    newTower.setBuildingCost(t.getBuildingCost());
                    newTower.setShootingRange(t.getShootingRange());
                    newTower.setUpgradeCost(t.getUpgradeCost());
                    Arena.getTowers().add(newTower);
                    Arena.setTowerBuilt(t.getX(),t.getY(),t.getType());
                }
                for (Object obj: monsters) {
                    Monster m = (Monster) obj;
                    Monster newMonster;
                    switch (m.getType()) {
                        case "Fox":
                            newMonster = new Fox(m);
                            break;
                        case "Penguin":
                            newMonster = new Penguin(m);
                            break;
                        case "Unicorn":
                            newMonster = new Unicorn(m);
                            break;
                        default:
                            throw new IllegalStateException("Unexpected value: " + m.getType());
                    }
                    Arena.getMonsters().add(newMonster);
                }
            }
        }

        for (int j = 0; j < MAX_V_NUM_GRID; j++)
            for (int i = 0; i < MAX_H_NUM_GRID; i++) {
                Label newLabel = new Label();
                newLabel.setLayoutX(j * GRID_WIDTH);
                newLabel.setLayoutY(i * GRID_HEIGHT);
                newLabel.setMinWidth(GRID_WIDTH);
                newLabel.setMaxWidth(GRID_WIDTH);
                newLabel.setMinHeight(GRID_HEIGHT);
                newLabel.setMaxHeight(GRID_HEIGHT);
                newLabel.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                newLabel.setStyle("-fx-border-color: black;");
                newLabel.setId(String.format("label x:%d,y:%d", j, i));
                grids[i][j] = newLabel;
                paneArena.getChildren().addAll(newLabel);
            }
        labelStartZone = new Label();
        labelStartZone.setLayoutX(GRID_WIDTH*2);
        labelStartZone.setLayoutY(0);
        labelStartZone.setMinWidth(GRID_WIDTH);
        labelStartZone.setMaxWidth(GRID_WIDTH);
        labelStartZone.setMinHeight(GRID_HEIGHT);
        labelStartZone.setMaxHeight(GRID_HEIGHT);
        labelStartZone.setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/startGrid.png")));
        paneArena.getChildren().addAll(labelStartZone);
        labelEndZone = new Label();
        labelEndZone.setLayoutX((MAX_V_NUM_GRID-1) * GRID_WIDTH);
        labelEndZone.setLayoutY((MAX_H_NUM_GRID-1) * GRID_HEIGHT);
        labelEndZone.setMinWidth(GRID_WIDTH);
        labelEndZone.setMaxWidth(GRID_WIDTH);
        labelEndZone.setMinHeight(GRID_HEIGHT);
        labelEndZone.setMaxHeight(GRID_HEIGHT);
        labelEndZone.setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/endZone.png")));
        paneArena.getChildren().addAll(labelEndZone);
        setHoveredRangeUI(0,0,0,"");
        hoveredRangeUI.setMouseTransparent(true);
        paneArena.getChildren().addAll(hoveredRangeUI);
        setActiveRangeUI(0,0,0,"");
        activeRangeUI.setMouseTransparent(true);
        paneArena.getChildren().addAll(activeRangeUI);
        setDragAndDrop();
        startUpdateUILoop();
        startUpdateArenaData();
    }

    /**
     * Check if we are allowed to build towers
     * @return true if allowed to build, false otherwise
     */
    public static boolean isEnableBuildTowers() {
        return enableBuildTowers;
    }

    /**
     * Set whether we are allowed to build towers
     * @param enableBuildTowers new value of enableBuildTowers
     */
    public static void setEnableBuildTowers(boolean enableBuildTowers) {
        ArenaUI.enableBuildTowers = enableBuildTowers;
    }

    private void startUpdateArenaData() {

        Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateArenaData();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateArenaData() {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.createQuery("delete from Tower").executeUpdate();
            session.createQuery("delete from Monster").executeUpdate();
            session.createQuery("delete from GameData").executeUpdate();
            String state = "";
            if (!Arena.isGameStarted()) {
                state = "not_started";
            } else if (gameOverShown) {
                state = "ended";
            } else if (enableBuildTowers) {
                state = "play";
            } else {
                state = "simulate";
            }
            GameData data = new GameData(Arena.getFrameCount(),Resource.getResourceAmount(),state);
            session.save(data);
            for(Tower t: Arena.getTowers()) {
                Tower temp = new Tower(t);
                session.save(temp);
            }
            for(Monster m: Arena.getMonsters()) {
                if (m.getType().equals("Death")) continue;
                Monster temp = new Monster(m);
                if (temp.getxPx()<0) temp.setxPx(-temp.getxPx());
                if (temp.getyPx()<0) temp.setyPx(-temp.getyPx());
                session.save(temp);
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private String getHighScoreRequest(int newScore) throws IOException {
        String url = "http://localhost:8080/highscores?newScore="+ newScore;
        URL urlForGetRequest = new URL(url);
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        int responseCode = conection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in.close();
            // print result
            String result = response.toString();
            System.out.println("JSON String Result " + result);
            return response.toString().substring(1,result.length()-1);
            //GetAndPost.POSTRequest(response.toString());
        } else {
            System.out.println("GET NOT WORKED");
        }
        return "";
    }

    private void startUpdateUILoop() {

    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("Game Over!");
    	alert.setHeaderText(null);
        Timeline timer = new Timeline(new KeyFrame(Duration.millis(UPDATE_INTERVAL), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateUI();
                if (!Monster.gameEnds()) Arena.nextFrame();
                else if (!gameOverShown) {
                    int score = Math.max(Arena.getFrameCount(),0);
                    StringBuilder topScores = new StringBuilder("Leaderboard:\n");
                    try {
                        String[] rawInput = getHighScoreRequest(score).split(",");
                        for(int i=0;i<rawInput.length;i++) {
                            topScores.append(String.format("%d: %s\n", i+1, rawInput[i]));
                        }
                        if(rawInput.length != 10) {
                            topScores = new StringBuilder("Cannot Reach Leaderboard Server");
                        }
                    } catch (IOException e) {
                        topScores = new StringBuilder("Cannot Reach Leaderboard Server");
                        e.printStackTrace();
                    }
                    alert.setContentText(String.format("Your Score: %d \n%s",score,topScores.toString()));
                    alert.show();
                	gameOverShown = true;
                }
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    @FXML
    private void updateUI() {
        //TODO:
        labelFrameCount.setText(String.format("FrameCount: %d",Arena.getFrameCount()));
        //TOWER
        for (int j = 0;j<MAX_V_NUM_GRID;j++) {
            for (int i=0;i<MAX_H_NUM_GRID;i++) {
                String shot = "";
                if (Arena.towerShot(i,j)) shot = "Shot";
                switch (Arena.towerBuiltType(i,j)) {
                    case "Basic":
                        grids[j][i].setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/basicTower"+shot+".png")));
                        break;
                    case "Catapult":
                        grids[j][i].setGraphic(ArenaUIUtils.setIcon((ArenaUIUtils.getImage("/catapult"+shot+".png"))));
                        break;
                    case "Ice":
                        grids[j][i].setGraphic(ArenaUIUtils.setIcon((ArenaUIUtils.getImage("/iceTower"+shot+".png"))));
                        break;
                    case "Laser":
                        grids[j][i].setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/laserTower"+shot+".png")));
                        break;
                    default:
                        grids[j][i].setGraphic(null);
                }
            }
        }
        //PROJECTILE TODO:
        for(Line pLine : LaserAttackTraceUI){
        	paneArena.getChildren().remove(pLine);
        }
        LaserAttackTraceUI.clear();
        for(Tower t : Arena.getTowers()){
        	if(t.getType() == "Laser"){
        		LaserTower lt = (LaserTower) t;
        		if(Arena.getMonsterNum() > 0){
        			if(lt.getAttackTrace()!= null){
        				LaserAttackTraceUI.add(lt.getAttackTrace());
        				paneArena.getChildren().add(lt.getAttackTrace());
        			}
        		}
        	}
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
            l.setLayoutX(m.getxPx()-MONSTER_WIDTH/2);
            l.setLayoutY(m.getyPx()-MONSTER_HEIGHT/2);

            l.setOnMouseEntered(new EventHandler<MouseEvent>(){
        		@Override  
        		public void handle(MouseEvent event) {
                    tooltip.setText(Integer.toString(m.getHP()));
        			tooltip.setAnchorX(event.getScreenX());
        			tooltip.setAnchorY(event.getScreenY() + 15);
        			tooltip.setShowDuration(Duration.millis(1));
        			tooltip.show(l, event.getScreenX(), event.getScreenY() + 15);
        		}
        	}); 
            l.setOnMouseExited(new EventHandler<MouseEvent>(){
        		@Override
        		public void handle(MouseEvent event){
        			tooltip.hide();
        		}
        	});
            String hit = "";
            if (m.getHit()) hit = "Hit";
            switch (m.getType()) {
                case "Fox":
                    l.setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/fox"+hit+".png"),MONSTER_HEIGHT,MONSTER_WIDTH));
                    break;
                case "Penguin":
                    l.setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/penguin"+hit+".png"),MONSTER_HEIGHT,MONSTER_WIDTH));
                    break;
                case "Unicorn":
                    l.setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/unicorn"+hit+".png"),MONSTER_HEIGHT,MONSTER_WIDTH));
                    break;
                case "Death":
                	l.setGraphic(ArenaUIUtils.setIcon(ArenaUIUtils.getImage("/collision.png"),MONSTER_HEIGHT,MONSTER_WIDTH));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + m);
            }
        }
        //RESOURCE
        labelResource.setText(String.format("Money: %d", Resource.getResourceAmount()));
        Tower t = Arena.getTower(activeCellX,activeCellY);
        if (t != null) {
            labelActive.setText(String.format("Selected Tower: %s\n Attack: %d\n Cost: %d\n Range: %d",
                    t.getType(),t.getAttackPower(),t.getBuildingCost(),t.getShootingRange()));
            buttonUpgradeTower.setVisible(true);
            buttonDeleteTower.setVisible(true);
        }else {
            labelActive.setText("Selected Tower: None");
            buttonUpgradeTower.setVisible(false);
            buttonDeleteTower.setVisible(false);
        }
        t = Arena.getTower(hoveredCellX,hoveredCellY);
        if (t != null) {
            labelHovered.setText(String.format("Hovered Tower: %s\n Attack: %d\n Cost: %d\n Range: %d",
                    t.getType(),t.getAttackPower(),t.getBuildingCost(),t.getShootingRange()));
        }else {
            labelHovered.setText("Hovered Tower: None");
        }
    }


    @FXML
    private void playStart() {
//        System.out.println("Play button clicked");
        if (Arena.isGameStarted()) return;
        Arena.startGame();
    }

    @FXML
    private void simulateStart() {
        //TODO:
        if (Arena.isGameStarted()) return;
        Arena.startGame();
        setEnableBuildTowers(false);
    }

    @FXML
    private void deleteActiveTower() {
        if (activeCellX != -1 && activeCellY != -1 && enableBuildTowers) {
            Arena.deleteTowerAt(activeCellX,activeCellY);
            setActiveCell(-1,-1);
        }
    }

    @FXML
    private void upgradeActiveTower() {
        if (activeCellX != -1 && activeCellY != -1 && enableBuildTowers) {
            Arena.upgradeTowerAt(activeCellX,activeCellY);
        }
    }

    private void setActiveCell(int x,int y) {
        if (x == -1 && y == -1) {
            setActiveRangeUI(0,0,0,"");
            if (activeCellX != -1 && activeCellY != -1)
                grids[activeCellY][activeCellX].setStyle("-fx-border-color: black;");
            activeCellX = x;
            activeCellY = y;
            return;
        }
        if (activeCellX != -1 && activeCellY != -1) {
            grids[activeCellY][activeCellX].setStyle("-fx-border-color: black;");
        }
        Tower t = Arena.getTower(x, y);
        if (t != null) {
            setActiveRangeUI(x,y,t.getShootingRange(),t.getType());
        } else {
            setActiveRangeUI(0,0,0,"");
        }
        activeCellX = x;
        activeCellY = y;
        grids[y][x].setStyle("-fx-border-color: orange;");
    }

    private void setHoveredCell(int x,int y) {
        if (x == -1 && y == -1) {
            setHoveredRangeUI(0,0,0,"");
            if (hoveredCellX != -1 && hoveredCellY != -1)
                grids[hoveredCellY][hoveredCellX].setStyle("-fx-border-color: black;");
            hoveredCellX = x;
            hoveredCellY = y;
            return;
        }
        if (hoveredCellX != -1 && hoveredCellY != -1) {
            if (hoveredCellX != activeCellX || hoveredCellY != activeCellY)
                grids[hoveredCellY][hoveredCellX].setStyle("-fx-border-color: black;");
        }
        Tower t = Arena.getTower(x, y);
        if (t != null) {
            setHoveredRangeUI(x,y,t.getShootingRange(),t.getType());
        } else {
            setHoveredRangeUI(0,0,0,"");
        }
        hoveredCellX = x;
        hoveredCellY = y;
        if (x != activeCellX || y != activeCellY) {
            grids[y][x].setStyle("-fx-border-color: blue;");
        }
    }

    private void setHoveredRangeUI(int x, int y, int r, String towerType) {
        if (towerType.equals("Laser")) {
            hoveredRangeUI.setCenterX(0);
            hoveredRangeUI.setCenterY(0);
            hoveredRangeUI.setRadius(0);
            hoveredRangeUI.setStrokeWidth(0);
            return;
        }
        if (towerType.equals("Catapult")) {
            hoveredRangeUI.setCenterX(x * GRID_WIDTH + GRID_WIDTH/2);
            hoveredRangeUI.setCenterY(y * GRID_HEIGHT + GRID_HEIGHT/2);
            hoveredRangeUI.setRadius(r-50);
            hoveredRangeUI.setStroke(new Color(0.5,0.7,1,0.5));
            hoveredRangeUI.setStrokeWidth(100);
            hoveredRangeUI.setFill(null);
            return;
        }
        hoveredRangeUI.setFill(new Color(0.5,0.7,1,0.5));
        hoveredRangeUI.setCenterX(x * GRID_WIDTH + GRID_WIDTH/2);
        hoveredRangeUI.setCenterY(y * GRID_HEIGHT + GRID_HEIGHT/2);
        hoveredRangeUI.setRadius(r);
        hoveredRangeUI.setStrokeWidth(0);
    }

    private void setActiveRangeUI(int x, int y, int r, String towerType) {
        if (towerType.equals("Laser")) {
            activeRangeUI.setCenterX(0);
            activeRangeUI.setCenterY(0);
            activeRangeUI.setRadius(0);
            activeRangeUI.setStrokeWidth(0);
            return;
        }
        if (towerType.equals("Catapult")) {
            activeRangeUI.setCenterX(x * GRID_WIDTH + GRID_WIDTH/2);
            activeRangeUI.setCenterY(y * GRID_HEIGHT + GRID_HEIGHT/2);
            activeRangeUI.setRadius(r-50);
            activeRangeUI.setStroke(new Color(1,0.5,0,0.5));
            activeRangeUI.setStrokeWidth(100);
            activeRangeUI.setFill(null);
            return;
        }
        activeRangeUI.setFill(new Color(1,0.5,0,0.5));
        activeRangeUI.setCenterX(x * GRID_WIDTH + GRID_WIDTH/2);
        activeRangeUI.setCenterY(y * GRID_HEIGHT + GRID_HEIGHT/2);
        activeRangeUI.setRadius(r);
        activeRangeUI.setStrokeWidth(0);
    }

    private void setDragAndDrop() {
        Label[] tower = {labelBasicTower, labelIceTower, labelCatapult, labelLaserTower};
        for (Label label : tower) {
            label.setOnDragDetected(new DragEventHandler(label));
        }
        paneInfo.setOnMouseEntered(event->setHoveredCell(-1,-1));
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
                int finalI = i;
                int finalJ = j;
                cell.setOnMouseEntered(event -> {
                    setHoveredCell(finalI,finalJ);
                });
                cell.setOnDragEntered((event) -> {
                    event.consume();
                });

                cell.setOnMouseClicked((event) -> {
                    setActiveCell(finalI, finalJ);
                    event.consume();
                });
            }
        }
    }
}

/**
 * Class to handle the dragging events
 * @author Aaron WANG
 */
class DragEventHandler implements EventHandler<MouseEvent> {
    private Label source;

    /**
     * Constructor class
     * @param e source label for drag event
     */
    public DragEventHandler(Label e) {
        source = e;
    }

    /**
     * Handles the dragging event
     * @param event drag event trigger
     */
    @Override
    public void handle(MouseEvent event) {
        if (ArenaUI.isEnableBuildTowers()) {
            Dragboard db = source.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(source.getText());
            switch (source.getId()) {
                case "labelBasicTower":
                    content.putString("Basic");
                    db.setDragView(ArenaUIUtils.getImage("/basicTower.png"));
                    break;
                case "labelCatapult":
                    content.putString("Catapult");
                    db.setDragView(ArenaUIUtils.getImage("/catapult.png"));
                    break;
                case "labelIceTower":
                    content.putString("Ice");
                    db.setDragView(ArenaUIUtils.getImage("/iceTower.png"));
                    break;
                case "labelLaserTower":
                    content.putString("Laser");
                    db.setDragView(ArenaUIUtils.getImage("/laserTower.png"));
                    break;
            }
            db.setContent(content);
        }
        event.consume();
    }
}

/**
 * Class for handling dropping event
 * @author Aaron WANG
 */
class DragDroppedEventHandler implements EventHandler<DragEvent> {
    /**
     * Regex to get match label for cells
     */
    Pattern gridRegex = Pattern.compile("label x:(\\d+),y:(\\d+)");

    /**
     * Handler for the drag and drop
     * @param event drag even to be handled
     */
    @Override
    public void handle(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        Label target = (Label) event.getGestureTarget();
//        System.out.println(target.getId());
        Matcher m = gridRegex.matcher(target.getId());
        m.find();
        int x = Integer.parseInt(m.group(1));
        int y = Integer.parseInt(m.group(2));
        if (Arena.buildTower(x, y, db.getString())) {
//            System.out.println(db.getString());
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();

    }
}
