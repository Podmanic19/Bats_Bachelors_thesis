package controller;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.shape.Line;
import model.agents.BatAgent;
import model.gui.ChangeScene;
import model.map.Home;
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
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Semaphore;


import static java.lang.Thread.sleep;
import static model.main.Main.*;

public class VisualisationController implements LoadToPane, PlaceAgents, PlaceHomes, PlaceWalls, Popup, ChangeScene {

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
    @FXML CheckBox singleStart;
    @FXML Label lblTicks;
    @FXML Button btnEnv;

    private MapShell mShell;
    private final Visualisation visualisation = new Visualisation();
    private Map shownMap;

    public static boolean playing = false;

    public void btnMainMenuOnAction() {
        try {
            visualisation.cancel();
            sceneChanger("startingscene");
        } catch (IOException e) {
            popup("Couldn't load file view/startingscene.fxml");
            e.printStackTrace();
        }
    }

    public void btnEnvSettingsOnAction() {
        try {
            load(paneMain, "environmentsettings");
        } catch (IOException e) {
            popup("Couldn't load file view/startingscene.fxml");
            e.printStackTrace();
        }
    }

    public void singleStartOnAction() {
        if(playing) return;
        this.shownMap = new Map(mShell, agentparams, mapparams.AGENT_NUM, singleStart.isSelected());
        showMap();
    }

    public void btnCreateEnv() {
        lblLoading.setText("GENERATING MAP...");
        btnCreate.setDefaultButton(false);
        new Thread(() -> {
            disableButtons(true);
            mShell = new MapShell();
            shownMap = new Map(mShell, agentparams, mapparams.AGENT_NUM, singleStart.isSelected());
            Platform.runLater(() -> {
                showMap();
                disableButtons(false);
                lblLoading.setVisible(false);
            });
        }).start();
    }

    private void showMap() {
        paneMain.getChildren().clear();
        placeHomes(shownMap.getHomes(), paneMain);
        placeWalls(shownMap.getWalls(), paneMain);
        placeAgents(shownMap.getAgents(), paneMain);
    }

    public void btnPlayOnAction(){
        lblTicks.setAlignment(Pos.CENTER);
        if(playing) return;
        try {
            new Thread(() -> {
                try {
                    visualisation.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        shownMap = new Map(mShell, agentparams, mapparams.AGENT_NUM, singleStart.isSelected());
        btnPlay.setDisable(false);
        showMap();
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
        btnEnv.setDisable(disable);
    }

    class Visualisation extends Task {

        @Override
        protected Object call() throws Exception {
            return simulate();
        }

        public int simulate() {
            Semaphore semaphore = new Semaphore(0);
            Instant start = Instant.now();
            int runtime = 0;

            while(runtime < 14000 && !this.isCancelled()) {
                int finalI = runtime;
                shownMap.getAgents().parallelStream().forEach(BatAgent::act);
                shownMap.getHomes().removeIf(h -> (h.getPollution() <= 0));
                for(Home h : shownMap.getHomes()){
                    h.incrementLifetime();
                    h.increasePollution(envparams.DYNAMIC_HOME_GROWTH_SIZE);
                }
                Platform.runLater(() -> {
                    refresh();
                    placeHomes(shownMap.getHomes(), paneMain);
                    placeAgents(shownMap.getAgents(), paneMain);
                    Platform.runLater(() ->  updateTicks(finalI));
                    semaphore.release();
                });
                try {
                    semaphore.acquire();
                    sleep(10);  // sleep to make the visualisation observable by human eye
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (shownMap.getHomes().isEmpty()) {
                    System.out.println("Pocet iteracii: " + runtime);
                    break;
                }
                if(envparams.DYNAMIC_HOME_SPAWN_TIME > 0 && runtime % envparams.DYNAMIC_HOME_SPAWN_TIME == 0) shownMap.addHome();
                runtime++;
            }

            Instant end = Instant.now();
            System.out.println(Duration.between(start, end));
            return runtime;

        }

        private void refresh() {
            paneMain.getChildren().removeIf(node -> (!(node instanceof Line)));
        }


        public void updateTicks(int i){
            lblTicks.setText("Second: " + i);
        }

    }

}
