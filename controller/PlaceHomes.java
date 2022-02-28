package controller;

import model.main.Main;
import model.map.CallType;
import model.map.Home;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static model.main.Main.envMap;
import static model.main.Main.envparams;

public interface PlaceHomes {

    default void placeHomes(Pane paneMain){
        int RADIUS = 6;
        double coef_h = paneMain.getHeight() / envparams.POINT_MAX;
        double coef_w = paneMain.getWidth() / envparams.POINT_MAX;
        for(Home h : envMap.getHomes()){
            Circle cir = new Circle();
            if(h.getCall() == CallType.NONE) {
                cir.setFill(Color.BLUE);
            }
            else {
                cir.setFill(Color.GREEN);
                if(Main.SHOW_ATTRACTION){
                    Circle att = new Circle();
                    att.setFill(Color.TRANSPARENT);
                    att.setRadius(envparams.ATTRACTION_DISTANCE * coef_w);
                    att.setCenterX(h.getCoords().getX() * coef_w);
                    att.setCenterY(h.getCoords().getY() * coef_h);
                    att.setStroke(Color.GREEN);
                    paneMain.getChildren().add(att);
                }
            }
            if(h.getCall() == CallType.NONE) cir.setStroke(Color.BLUE);
            else cir.setStroke(Color.GREEN);
            cir.setRadius(RADIUS);
            cir.setCenterX(h.getCoords().getX() * coef_w);
            cir.setCenterY(h.getCoords().getY() * coef_h);
            paneMain.getChildren().add(cir);
        }
    }

}
