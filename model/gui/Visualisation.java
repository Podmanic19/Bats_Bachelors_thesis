package model.gui;

import controller.PlaceAgents;
import controller.PlaceHomes;
import model.agents.BatAgent;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import model.map.Home;

import java.time.Duration;
import java.time.Instant;

import static model.main.Main.*;

public class Visualisation extends Thread implements PlaceHomes, PlaceAgents {

    private final Pane paneMain;

    private Visualisation(Pane pane) {
        paneMain = pane;
    }

    public static Visualisation getInstance(Pane pane) {
        return new Visualisation(pane);
    }

    @Override
    public void run() {

        Instant start = Instant.now();
        for (int i = 0; i < 10000; i++) {
            envMap.getAgents().parallelStream().forEach(BatAgent::act);
            envMap.getHomes().removeIf(h -> (h.getPollution() <= 0));
            for(Home h : envMap.getHomes()){
                h.incrementLifetime();
                h.increasePollution(envparams.DYNAMIC_HOME_GROWTH_SIZE);
            }
            Platform.runLater(this::refresh);
            Platform.runLater(() -> placeHomes(paneMain));
            Platform.runLater(() -> placeAgents(paneMain));
            try {
                sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (envMap.getHomes().isEmpty()) {
                System.out.println("Pocet iteracii: " + i);
                break;
            }
            if(envparams.DYNAMIC_HOME_CREATION && i % envparams.DYNAMIC_HOME_SPAWN_TIME == 0) envMap.addHome(i);
        }
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));

    }

    private void refresh() {
        paneMain.getChildren().removeIf(node -> (!(node instanceof Line)));
    }

}
