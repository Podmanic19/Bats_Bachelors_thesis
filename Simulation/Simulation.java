package Simulation;

import Classes.Agent;
import Environment.EnvironmentMap;

public class Simulation {

    private void simulate() {
        EnvironmentMap e = EnvironmentMap.getInstance();
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

//    private void placeAgents(Pane canvas){
//        for(Coordinate a : steps.get(0).getAgent_coords()){
//            Circle cir = new Circle();
//            cir.setFill(Color.RED);
//            cir.setStroke(Color.RED);
//            cir.setRadius(3);
//            cir.relocate(a.getX(), a.getY());
//            canvas.getChildren().add(cir);
//        }
//    }
//
//    private void placeHomes(Pane canvas){
//        for(Coordinate h : steps.get(0).getHome_coords()){
//            Circle cir = new Circle();
//            cir.setFill(Color.BLUE);
//            cir.setStroke(Color.BLUE);
//            cir.setRadius(8);
//            cir.relocate(h.getX(), h.getY());
//            canvas.getChildren().add(cir);
//        }
//    }
}
