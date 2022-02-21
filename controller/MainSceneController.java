package controller;

import model.map.Map;
import model.map.LineSegment;
import model.gui.IAlert;
import model.gui.LoadToPane;
import model.serialization.Serialization;
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
import static model.Main.*;

public class MainSceneController implements LoadToPane, PlaceAgents, PlaceHomes, IAlert {
    @FXML
    Button btnCreate;
    @FXML
    Button btnEnvSettings;
    @FXML
    Button btnAgentSettings;
    @FXML
    Button btnLoadEnv;
    @FXML
    Button btnPlay;
    @FXML
    Button btnSave;
    @FXML
    Button btnPlace;
    @FXML
    Pane paneMain;
    @FXML
    Label lblLoading;

    public static boolean playing = false;

    public void btnCreateEnv() {
        lblLoading.setText("GENERATING MAP...");
        btnCreate.setDefaultButton(false);
        new Thread(() -> {
            disableButtons(true);
            envMap = new Map();
            Platform.runLater(() -> {
                showMap(paneMain);
                disableButtons(false);
                lblLoading.setVisible(false);
                btnPlay.setDefaultButton(true);
            });
        }).start();
    }

    private void showMap(Pane paneMain) {
        paneMain.getChildren().clear();
        placeHomes(paneMain);
        placeWalls(paneMain);
        placeAgents(paneMain);
    }

    public void btnPlaceAgents() {
        placeAgents(paneMain);
    }

    public void btnPlayOnAction(){
        if(playing) return;
        Visualisation.getInstance(paneMain).start();
    }

    private void placeWalls(Pane canvas) {
        double coef_h = paneMain.getHeight() / envparams.POINT_MAX;
        double coef_w = paneMain.getWidth() / envparams.POINT_MAX;
        for (LineSegment w : envMap.getWalls()) {
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

    public void btnLoad() throws IOException {
        FileChooser fch = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("KKT", "*.emap");
        fch.getExtensionFilters().add(fileExtensions);
        File file = fch.showOpenDialog(new Stage());

        envMap = Serialization.getInstance().loadMap(file);
        showMap(paneMain);
    }

    public void btnSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Maps", "*.emap"));
        File file = fileChooser.showSaveDialog(new Stage());
        Serialization.getInstance().saveMap(envMap, file);
    }

    private void disableButtons(boolean disable){
        btnPlay.setDisable(disable);
        btnAgentSettings.setDisable(disable);
        btnCreate.setDisable(disable);
        btnLoadEnv.setDisable(disable);
        btnSave.setDisable(disable);
        btnEnvSettings.setDisable(disable);
        btnPlace.setDisable(disable);
    }

}
