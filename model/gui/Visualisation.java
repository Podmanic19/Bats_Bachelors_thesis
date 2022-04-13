package model.gui;

import controller.PlaceAgents;
import controller.PlaceHomes;
import javafx.scene.control.Label;
import model.agents.BatAgent;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import model.map.Home;
import model.map.mapshell.Map;

import java.time.Duration;
import java.time.Instant;

import static model.main.Main.*;

public class Visualisation extends Thread implements PlaceHomes, PlaceAgents {

    private final Pane paneMain;
    private final Label lblTicks;
    private final Map visualisedMap;

    private Visualisation(Map visualisedMap, Pane pane, Label lblTicks) {
        this.lblTicks = lblTicks;
        this.paneMain = pane;
        this.visualisedMap = visualisedMap;
    }

    public static Visualisation getInstance(Map visualisedMap, Pane pane, Label lblTicks) {
        return new Visualisation(visualisedMap, pane, lblTicks);
    }

    @Override
    public void run() {

        Instant start = Instant.now();
        for (int i = 0; i < 14000; i++) {
            int finalI = i;
            visualisedMap.getAgents().parallelStream().forEach(BatAgent::act);
            visualisedMap.getHomes().removeIf(h -> (h.getPollution() <= 0));
            for(Home h : visualisedMap.getHomes()){
                h.incrementLifetime();
                h.increasePollution(envparams.DYNAMIC_HOME_GROWTH_SIZE);
            }
            Platform.runLater(this::refresh);
            Platform.runLater(() -> placeHomes(visualisedMap.getHomes(), paneMain));
            Platform.runLater(() -> placeAgents(visualisedMap.getAgents(), paneMain));
            Platform.runLater(() ->  updateTicks(finalI));
            try {
                sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (visualisedMap.getHomes().isEmpty()) {
                System.out.println("Pocet iteracii: " + i);
                break;
            }
            if(envparams.DYNAMIC_HOME_SPAWN_TIME > 0 && i % envparams.DYNAMIC_HOME_SPAWN_TIME == 0) visualisedMap.addHome();
        }
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));

    }

    private void refresh() {
        paneMain.getChildren().removeIf(node -> (!(node instanceof Line)));
    }


    public void updateTicks(int i){

        lblTicks.setText("Second: " + i);

    }


}
