package controller;

import model.agents.BatAgent;
import model.agents.AgentCircle;
import model.agents.State;
import model.main.Main;
import model.map.Vector;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import static model.main.Main.*;
import static model.main.Main.agentparams;

public interface PlaceAgents {
    default void placeAgents(Pane paneMain) {
        double coef_h = paneMain.getHeight() / envparams.POINT_MAX;
        double coef_w = paneMain.getWidth() / envparams.POINT_MAX;
        for (BatAgent a : envMap.getAgents()) {
            AgentCircle cir = new AgentCircle();
            if (Main.SHOW_SIGHT && a.getState() != State.working) {
                Arc arc = new Arc();
                arc.setCenterX(a.getPosition().getX() * coef_w);
                arc.setCenterY(a.getPosition().getY() * coef_h);
                arc.setRadiusX(agentparams.SIGHT * coef_w);
                arc.setRadiusY(agentparams.SIGHT * coef_h);
                double angle = new Vector(1, 0).signedAngleBetween(a.getDirection());
                arc.setStartAngle(angle - agentparams.FOV / 2);
                arc.setLength(agentparams.FOV);
                arc.setType(ArcType.ROUND);
                arc.setFill(Color.TRANSPARENT);
                arc.setStroke(Color.RED);
                paneMain.getChildren().add(arc);
            }
            if (a.getState() == State.searching) {
                cir.setFill(Color.DARKSEAGREEN);
                cir.setStroke(Color.DARKSEAGREEN);
            }
            if (a.getState() == State.traveling) {
                cir.setFill(Color.DARKRED);
                cir.setStroke(Color.DARKRED);
            }
            cir.setRadius(3);
            cir.setCenterX(a.getPosition().getX() * coef_w);
            cir.setCenterY(a.getPosition().getY() * coef_h);
            paneMain.getChildren().add(cir);
        }
    }
}
