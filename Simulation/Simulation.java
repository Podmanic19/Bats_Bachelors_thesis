package Simulation;

import Environment.Coordinate;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.ArrayList;

public class Simulation implements Serializable {
    private ArrayList<SimulationStep> steps;
    private static Simulation instance = null;

    private Simulation()
    {
        this.steps = new ArrayList<SimulationStep>();
    }

    public static Simulation getInstance()
    {
        if (instance == null)
            instance = new Simulation();

        return instance;
    }

    public void addStep(SimulationStep step){
        steps.add(step);
    }

    public void run(Stage stage, Pane canvas){
        setup(canvas);
        stage.show();
    }

    private void setup(Pane pane){
        placeAgents(pane);
        placeHomes(pane);
    }

    private void placeAgents(Pane canvas){
        for(Coordinate a : steps.get(0).getAgent_coords()){
            Circle cir = new Circle();
            cir.setFill(Color.RED);
            cir.setStroke(Color.RED);
            cir.setRadius(3);
            cir.relocate(a.getX(), a.getY());
            canvas.getChildren().add(cir);
        }
    }

    private void placeHomes(Pane canvas){
        for(Coordinate h : steps.get(0).getHome_coords()){
            Circle cir = new Circle();
            cir.setFill(Color.BLUE);
            cir.setStroke(Color.BLUE);
            cir.setRadius(8);
            cir.relocate(h.getX(), h.getY());
            canvas.getChildren().add(cir);
        }
    }

    public ArrayList<SimulationStep> getSteps() {
        return steps;
    }
}
