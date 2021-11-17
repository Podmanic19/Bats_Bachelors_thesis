package Simulation;

import Classes.Agent;
import Classes.AgentCircle;
import Classes.State;
import Environment.Home;
import Environment.Vector;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

import static Main.Main.*;
import static java.lang.Double.NaN;

public class Visualisation extends Thread implements PlaceHomes, PlaceAgents{

    private static Visualisation instance = null;
    private Pane paneMain;

    private Visualisation(Pane pane){
        paneMain = pane;
    }

    public static Visualisation getInstance(Pane pane){
        return new Visualisation(pane);
    }

    @Override
    public void run(){
        while(!envMap.getHomes().isEmpty()) {
//            System.out.println("SEARCHING: " + envMap.searching());
//            System.out.println("TRAVELLING: " + envMap.traveling());
//            System.out.println("WORKING: " + envMap.working());
//            System.out.println("HOMES:" + envMap.getHomes().size());
            for (Agent a : envMap.getAgents()) {
                a.act();
            }
            Platform.runLater(this::refresh);
            Platform.runLater(() -> placeHomes(paneMain));
            Platform.runLater(() -> placeAgents(paneMain));
            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void refresh(){
        ArrayList<Node> toRemove = new ArrayList<>();
        for(int i = 0; i < paneMain.getChildren().size(); i++){
            Node node = paneMain.getChildren().get(i);
            if(!(node instanceof Line)){
                toRemove.add(node);
            }
        }
        for(Node node: toRemove){
            paneMain.getChildren().remove(node);
        }
    }

}
