package Simulation;

import Environment.EnvironmentMap;
import Environment.LineSegment;
import Main.LoadToPane;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.io.IOException;
import static Main.Main.*;

public class MainSceneController implements LoadToPane, PlaceAgents, PlaceHomes {
    @FXML Button btnCreate;
    @FXML Button btnEnvSettings;
    @FXML Button btnAgentSettings;
    @FXML Button btnLoadEnv;
    @FXML Pane paneMain;
    @FXML Button btnPlay;
    public static boolean playing = false;

    public void btnCreateEnv(){
        envMap = new EnvironmentMap();
        paneMain.getChildren().clear();
        placeHomes(paneMain);
        placeWalls(paneMain);
        placeAgents(paneMain);
    }

    public void btnPlaceAgents() {
        placeAgents(paneMain);
    }

    public void btnPlayOnAction(){
        btnPlay.setText(playing ? "Pause" : "Play");
        playing = !playing;
        Visualisation.getInstance(paneMain).start();
    }

    private void placeWalls(Pane canvas){
        double coef_h = paneMain.getHeight() / envparams.POINT_MAX;
        double coef_w = paneMain.getWidth() / envparams.POINT_MAX;
        for(LineSegment w: envMap.getWalls()){
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

    public void btnAgentSettings(){
        try {
            load(paneMain, "agentsettings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnLoad(){

    }

}
