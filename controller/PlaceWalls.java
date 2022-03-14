package controller;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import model.agents.AgentCircle;
import model.agents.BatAgent;
import model.agents.State;
import model.main.Main;
import model.map.LineSegment;
import model.map.Vector;

import static model.main.Main.*;
import static model.main.Main.agentparams;

public interface PlaceWalls {

    default void placeWalls(Pane canvas) {
        double coef_h = canvas.getHeight() / mapparams.POINT_MAX;
        double coef_w = canvas.getWidth() / mapparams.POINT_MAX;
        for (LineSegment w : loadedMap.getWalls()) {
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

}
