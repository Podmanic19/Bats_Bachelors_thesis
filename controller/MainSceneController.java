package controller;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import model.gui.ChangeScene;
import model.gui.Visualisation;
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


import static model.main.Main.*;

public class MainSceneController implements LoadToPane, PlaceAgents, PlaceHomes, PlaceWalls, Popup, ChangeScene {
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
            loadedMap.fillWithElements(mapparams.AGENT_NUM, mapparams.NUMBER_HOME);
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

    public void btnEnvSettings() {
        try {
            load(paneMain, "mapsettings");
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
        loadedMap = Map.load(file);
        btnPlay.setDisable(false);
        showMap(paneMain);
    }

    public void btnSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Maps", "*.emap"));
        File file = fileChooser.showSaveDialog(new Stage());
        loadedMap.save(file);
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
