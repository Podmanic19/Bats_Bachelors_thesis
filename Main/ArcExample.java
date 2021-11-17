package Main;

import Environment.Vector;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

import static Main.Main.agentparams;

public class ArcExample extends Application {
    @Override
    public void start(Stage stage) {
////Drawing an arc
//        Arc arc = new Arc();
////Setting the properties of the arc
//        arc.setCenterX(300.0f);
//        arc.setCenterY(150.0f);
//        arc.setRadiusX(90.0f);
//        arc.setRadiusY(90.0f);
//        arc.setStartAngle(90.0f);
//        arc.setLength(239.0f);
////Setting the type of the arc
        Arc arc = new Arc();
        arc.setCenterX(300.0f);
        arc.setCenterY(150.0f);
        arc.setRadiusX(agentparams.SIGHT);
        arc.setRadiusY(agentparams.SIGHT);
        arc.setStartAngle(Math.toDegrees(new Vector(1,1).angleBetween(new Vector(0,1))) -
                agentparams.FOV/.2);
        arc.setLength(agentparams.FOV);
        //arc.setLength(Math.toDegrees(2 * Math.PI * agentparams.SIGHT/380 * agentparams.FOV));
        arc.setType(ArcType.ROUND);
        arc.setFill(Color.TRANSPARENT);
        arc.setStroke(Color.YELLOW);
        arc.setType(ArcType.ROUND);
//Creating a Group object
        Group root = new Group(arc);
//Creating a scene object
        Scene scene = new Scene(root, 600, 300);
//Setting title to the Stage
        stage.setTitle("Drawing an Arc");
//Adding scene to the stage
        stage.setScene(scene);
//Displaying the contents of the stage
        stage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}
