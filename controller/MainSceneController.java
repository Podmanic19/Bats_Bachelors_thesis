package controller;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import model.agents.AgentParams;
import model.gui.ChangeScene;
import model.gui.Visualisation;
import model.main.testing.Test;
import model.map.Map;
import model.map.LineSegment;
import model.gui.Popup;
import model.gui.LoadToPane;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import static model.main.Main.*;

public class MainSceneController implements LoadToPane, PlaceAgents, PlaceHomes, Popup, ChangeScene {
    @FXML Button btnCreate;
    @FXML Button btnEnvSettings;
    @FXML Button btnAgentSettings;
    @FXML Button btnLoadEnv;
    @FXML Button btnPlay;
    @FXML Button btnSave;
    @FXML Button btnHelp;
    @FXML Button btnCreateTest;
    @FXML Pane paneMain;
    @FXML Pane paneBtn;
    @FXML Label lblLoading;
    @FXML CheckBox cbShowVision;
    @FXML CheckBox cbShowCalls;
    @FXML Label lblTicks;


    public static boolean playing = false;

    public void btnCreateEnv() {
        lblLoading.setText("GENERATING MAP...");
        btnCreate.setDefaultButton(false);
        new Thread(() -> {
            disableButtons(true);
            loadedMap = new Map();
            Platform.runLater(() -> {
                showMap(paneMain);
                disableButtons(false);
                lblLoading.setVisible(false);
            });
        }).start();
    }

    private void showMap(Pane paneMain) {
        paneMain.getChildren().clear();
        placeHomes(paneMain);
        placeWalls(paneMain);
        placeAgents(paneMain);
    }

    public void btnPlayOnAction(){
        lblTicks.setAlignment(Pos.CENTER);
        if(playing) return;
        Visualisation.getInstance(paneMain, lblTicks).start();
        btnPlay.setDisable(true);
    }

    private void placeWalls(Pane canvas) {
        double coef_h = paneMain.getHeight() / mapparams.POINT_MAX;
        double coef_w = paneMain.getWidth() / mapparams.POINT_MAX;
        for (LineSegment w : loadedMap.getWalls()) {
            Line l = new Line();
            l.setFill((Color.BLACK));
            l.setStartX(w.getA().getX() * coef_w);
            l.setEndX(w.getB().getX() * coef_w);
            l.setStartY(w.getA().getY() * coef_h);
            l.setEndY(w.getB().getY() * coef_h);
            l.setStrokeWidth(1);
            canvas.getChildren().add(l);
        }
    }

    public void btnEnvSettings() {
        try {
            load(paneMain, "envsettings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnAgentSettings() {
        try {
            load(paneMain, "agentsettings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnLoad() {
        FileChooser fch = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Maps", "*.emap");
        fch.getExtensionFilters().add(fileExtensions);
        File file = fch.showOpenDialog(new Stage());

        loadedMap = (Map) loadedMap.load(file);
        btnPlay.setDisable(false);
        showMap(paneMain);
    }

    public void btnSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Maps", "*.emap"));
        File file = fileChooser.showSaveDialog(new Stage());
        loadedMap.save(loadedMap, file);

    }

    public void showCallsOnAction(){
        SHOW_ATTRACTION = cbShowCalls.isSelected();
    }

    public void showVisionOnAction(){
        SHOW_SIGHT = cbShowVision.isSelected();
    }

    public void btnCreateTest() {

        try {
            sceneChanger("choosetestparams");
        } catch (IOException e) {
            popup("Couldn't load 'view/choosetestparams.fxml'");
            e.printStackTrace();
        }
//        ArrayList<AgentParams> agenttypes = new ArrayList<>();
//        ArrayList<Map> maps = new ArrayList<>();
//        agenttypes.add(agentparams);
//        maps.add(new Map());
//
//
//        new Test("Timetest", mapparams, agenttypes, maps, envparams, 1, 20, 100 ).run();

    }

    private void disableButtons(boolean disable){
        btnPlay.setDisable(disable);
        btnAgentSettings.setDisable(disable);
        btnCreate.setDisable(disable);
        btnLoadEnv.setDisable(disable);
        btnSave.setDisable(disable);
        btnEnvSettings.setDisable(disable);
        btnCreateTest.setDisable(disable);
    }

}
