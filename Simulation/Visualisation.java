package Simulation;

import Classes.Agent;
import Classes.AgentCircle;
import Environment.Home;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

import static Main.Main.envMap;
import static Main.Main.envparams;
import static Simulation.MainSceneController.playing;
import static Simulation.ThreadAdmin.*;

public class Visualisation extends Thread{

    private static Visualisation instance = null;
    private Pane paneMain;

    private Visualisation(Pane pane){
        paneMain = pane;
    }

    public static Visualisation getInstance(Pane pane){
        return new Visualisation(pane);
    }

    public static Visualisation getInstance(){
        return instance;
    }

    @Override
    public void run(){
        while(!envMap.getHomes().isEmpty()) {
            System.out.println("SEARCHING: " + envMap.searching());
            System.out.println("TRAVELLING: " + envMap.traveling());
            System.out.println("WORKING: " + envMap.working());
            System.out.println("HOMES:" + envMap.getHomes().size());
            for (Agent a : envMap.getAgents()) {
                a.act();
            }
            Platform.runLater(this::removeAgents);
            Platform.runLater(this::removeHomes);
            Platform.runLater(this::placeAgents);
            Platform.runLater(this::placeHomes);
            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            if(h.isAttracting() == false) cir.setFill(Color.BLUE);
            else cir.setFill(Color.GREEN);
            if(h.isAttracting() == false) cir.setStroke(Color.BLUE);
            else cir.setStroke(Color.GREEN);
            cir.setRadius(RADIUS);
            cir.setCenterX(h.getCoords().getX() * coef_w);
            cir.setCenterY(h.getCoords().getY() * coef_h);
            paneMain.getChildren().add(cir);
        }
    }

    private void removeAgents(){
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


    private void removeHomes(){
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

}
