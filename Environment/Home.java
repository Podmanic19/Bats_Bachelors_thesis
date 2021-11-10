package Environment;

import Classes.Agent;

import java.io.Serializable;
import java.util.ArrayList;

import static Main.Main.*;

public class Home implements Serializable {
    private final int id;
    private final Coordinate coords;
    private ArrayList<Agent> agents = new ArrayList<Agent>();
    private double pollution;
    private double attraction_distance = envparams.ATTRACTION_DISTANCE;
    private boolean attracting;


    public Home(int id, double pollution, Coordinate coords) {
        this.id = id;
        this.pollution = pollution;
        this.coords = coords;
    }

    public boolean isAttracting() {
        return attracting;
    }

    public double getAttraction_distance() {
        return attraction_distance;
    }

    public void addAgent(Agent a){
        agents.add(a);
    }

    public int getId() {
        return id;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public boolean decreasePollution(Agent a){

        if(pollution > 0){
            pollution -= a.getWorkRate();
            return true;
        }
        else{
            ArrayList<Home> homes = envMap.getHomes();
            homes.remove(this);
            return false;
        }
    }

    public double getPollution() {
        return pollution;
    }

    public void setAttracting(boolean attracting) {
        this.attracting = attracting;
    }

    public Coordinate getCoords() {
        return coords;
    }
}
