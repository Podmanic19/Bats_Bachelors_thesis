package Simulation;

import Classes.Agent;
import Classes.AgentCircle;
import Environment.EnvironmentMap;
import Environment.Home;
import Environment.LineSegment;
import Main.LoadToPane;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import java.io.IOException;
import java.util.ArrayList;

import static Main.Main.*;
import static Simulation.ThreadAdmin.semaphore;

public class MainSceneController implements LoadToPane {
    @FXML Button btnCreate;
    @FXML Button btnEnvSettings;
    @FXML Button btnAgentSettings;
    @FXML Button btnLoadEnv;
    @FXML Pane paneMain;
    @FXML Button btnPlay;
    private boolean simStarted;
    public static boolean playing = false;

    public void btnCreateEnv(){
        envMap = new EnvironmentMap();
        placeHomes();
        placeWalls(paneMain);
    }

    public void btnPlaceAgents() {
        placeAgents();
    }

    public void btnPlayOnAction(){
        btnPlay.setText(playing ? "Pause" : "Play");
        playing = !playing;
        if(simStarted) semaphore.notify();
        else{
            Visualisation.getInstance(paneMain).start();
        }
    }

    private void placeAgents(){
        double coef_h = paneMain.getHeight() / envparams.POINT_MAX;
        double coef_w = paneMain.getWidth() / envparams.POINT_MAX;
        for(Agent a : envMap.getAgents()) {
            AgentCircle cir = new AgentCircle();
            cir.setFill(Color.RED);
            cir.setStroke(Color.RED);
            cir.setRadius(3);
            cir.setCenterX(a.getPosition().getX() * coef_w);
            cir.setCenterY(a.getPosition().getY() * coef_h);
            paneMain.getChildren().add(cir);
        }
    }


    private void placeHomes(){
        int RADIUS = 6;
        double coef_h = paneMain.getHeight() / envparams.POINT_MAX;
        double coef_w = paneMain.getWidth() / envparams.POINT_MAX;
        for(Home h : envMap.getHomes()){
            Circle cir = new Circle();
            cir.setFill(Color.BLUE);
            cir.setStroke(Color.BLUE);
            cir.setRadius(RADIUS);
            cir.setCenterX(h.getCoords().getX() * coef_w);
            cir.setCenterY(h.getCoords().getY() * coef_h);
            paneMain.getChildren().add(cir);
        }
    }


    public void removeAgents(){
        ArrayList<Node> toRemove = new ArrayList<>();
        for(int i = 0; i < paneMain.getChildren().size(); i++){
            Node circle = paneMain.getChildren().get(i);
            if(circle instanceof  AgentCircle){
                toRemove.add(circle);
            }
        }
        for(Node node: toRemove){
            paneMain.getChildren().remove(node);
        }
    }


    public void removeHomes(){
        ArrayList<Node> toRemove = new ArrayList<>();
        for(int i = 0; i < paneMain.getChildren().size(); i++){
            Node circle = paneMain.getChildren().get(i);
            if(circle instanceof Circle){
                toRemove.add(circle);
            }
        }
        for(Node node: toRemove){
            paneMain.getChildren().remove(node);
        }
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
