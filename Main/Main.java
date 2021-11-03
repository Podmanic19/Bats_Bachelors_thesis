package Main;
import Classes.Agent;
import Environment.BatEnvironment;
import Environment.Home;
import Simulation.Simulation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {
    public static BatEnvironment e;
    public static Simulation s;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bat simulation");
        Pane canvas = new Pane();
        canvas.setPrefSize(1000,1000);
        primaryStage.setScene(new Scene(canvas));
        simulate(canvas, primaryStage);
    }



    private void simulate(Pane canvas, Stage primaryStage) {
        e = BatEnvironment.getInstance();
        s = Simulation.getInstance();
        while(!e.getHomes().isEmpty()) {
            System.out.println("SEARCHING: " + e.searching());
            System.out.println("TRAVELLING: " + e.traveling());
            System.out.println("WORKING: " + e.working());
            System.out.println("HOMES:" + e.getHomes().size());
            for (Agent a : e.getAgents()) {
                a.act();
            }
        }
        System.out.println("SEARCHING: " + e.searching());
        System.out.println("TRAVELLING: " + e.traveling());
        System.out.println("WORKING: " + e.working());
        System.out.println("HOMES:" + e.getHomes().size());
    }

    private void placeAgents(Pane canvas){
        for(Agent a : e.getAgents()){
            Circle cir = new Circle();
            cir.setFill(Color.RED);
            cir.setStroke(Color.RED);
            cir.setRadius(3);
            cir.relocate(a.getPosition().getX(), a.getPosition().getY());
            canvas.getChildren().add(cir);
        }
    }

    private void placeHomes(Pane canvas){
        for(Home h : e.getHomes()){
           Circle cir = new Circle();
           cir.setFill(Color.BLUE);
           cir.setStroke(Color.BLUE);
           cir.setRadius(8);
           cir.relocate(h.getCoords().getX(), h.getCoords().getY());
           canvas.getChildren().add(cir);
        }
    }

}