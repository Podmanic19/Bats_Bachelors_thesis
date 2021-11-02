package Main;

import Classes.Agent;
import Environment.Coordinate;
import Environment.Home;
import Environment.Point;

import java.io.Serializable;
import java.util.ArrayList;

public class SimulationStep implements Serializable {
    private ArrayList<Coordinate> agent_coords = new ArrayList<>();
    private ArrayList<Coordinate> home_coords = new ArrayList<>();

    public SimulationStep(ArrayList<Agent> agents, ArrayList<Point> homes) {
        for(Agent a: agents) {
            this.agent_coords.add(a.getPosition());
        }
        for(Point h: homes) {
            this.home_coords.add(h.getCoords());
        }
    }

    public ArrayList<Coordinate> getAgent_coords() {
        return agent_coords;
    }

    public ArrayList<Coordinate> getHome_coords() {
        return home_coords;
    }
}
