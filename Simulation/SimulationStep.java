package Simulation;

import Classes.Agent;
import Environment.Coordinate;
import Environment.Home;

import java.io.Serializable;
import java.util.ArrayList;

public class SimulationStep implements Serializable {
    private ArrayList<Coordinate> agent_coords = new ArrayList<>();
    private ArrayList<Coordinate> home_coords = new ArrayList<>();

    public SimulationStep(ArrayList<Agent> agents, ArrayList<Home> homes) {
        for(Agent a: agents) {
            this.agent_coords.add(new Coordinate(a.getPosition()));
        }
        for(Home h: homes) {
            this.home_coords.add(new Coordinate(h.getCoords()));
        }
    }

    public ArrayList<Coordinate> getAgent_coords() {
        return agent_coords;
    }

    public ArrayList<Coordinate> getHome_coords() {
        return home_coords;
    }
}
