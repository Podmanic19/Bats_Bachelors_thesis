package controller;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import model.gui.ChangeScene;
import model.gui.Visualisation;
import model.map.mapshell.Map;
import model.gui.Popup;
import model.gui.LoadToPane;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.map.mapshell.MapShell;

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
    @FXML Pane paneMain;
    @FXML Pane paneBtn;
    @FXML Label lblLoading;
    @FXML CheckBox cbShowVision;
    @FXML CheckBox cbShowCalls;
    @FXML Label lblTicks;

    private MapShell mShell;
    private Map shownMap;

    public static boolean playing = false;

    public void btnCreateEnv() {
        lblLoading.setText("GENERATING MAP...");
        btnCreate.setDefaultButton(false);
        new Thread(() -> {
            disableButtons(true);
            mShell = new MapShell();
            shownMap = new Map(mShell, true, mapparams.AGENT_NUM,  mapparams.NUMBER_HOME);
            Platform.runLater(() -> {
                showMap(paneMain);
                disableButtons(false);
                lblLoading.setVisible(false);
            });
        }).start();
    }

    private void showMap(Pane paneMain) {
        paneMain.getChildren().clear();
        placeHomes(shownMap.getHomes(), paneMain);
        placeWalls(shownMap.getWalls(), paneMain);
        placeAgents(shownMap.getAgents(), paneMain);
    }

    public void btnPlayOnAction(){
        lblTicks.setAlignment(Pos.CENTER);
        if(playing) return;
        Visualisation.getInstance(shownMap, paneMain, lblTicks).start();
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
        mShell = MapShell.load(file);
        if(file == null || mShell == null) {
            popup("No map selected");
            return;
        }
        shownMap = new Map(mShell, true, mapparams.AGENT_NUM,  mapparams.NUMBER_HOME);
        btnPlay.setDisable(false);
        showMap(paneMain);
    }

    public void btnSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Maps", "*.emap"));
        File file = fileChooser.showSaveDialog(new Stage());
        mShell.save(file);
    }

    public void showCallsOnAction(){
        SHOW_ATTRACTION = cbShowCalls.isSelected();
    }

    public void showVisionOnAction(){
        SHOW_SIGHT = cbShowVision.isSelected();
    }

    private void disableButtons(boolean disable){
        btnPlay.setDisable(disable);
        btnAgentSettings.setDisable(disable);
        btnCreate.setDisable(disable);
        btnLoadEnv.setDisable(disable);
        btnSave.setDisable(disable);
        btnEnvSettings.setDisable(disable);
    }

}
