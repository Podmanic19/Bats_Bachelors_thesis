package model.map;

import model.agents.BatAgent;

import java.io.Serializable;
import java.util.ArrayList;

import static model.Main.*;

public class Home implements Serializable {
    private final int id;
    private final Coordinate coords;
    private ArrayList<BatAgent> agents = new ArrayList<BatAgent>();
    private double pollution;
    private double attraction_distance = envparams.ATTRACTION_DISTANCE;
    private boolean attracting;

    public Home(int id, double pollution, Coordinate coords) {
        this.id = id;
        this.pollution = pollution;
        this.coords = coords;
    }

    public synchronized boolean isAttracting() {
        return attracting;
    }

    public double getAttraction_distance() {
        return attraction_distance;
    }

    public synchronized void addAgent(BatAgent a) {
        agents.add(a);
    }

    public int getId() {
        return id;
    }

    public synchronized ArrayList<BatAgent> getAgents() {
        return agents;
    }

    public synchronized boolean decreasePollution(BatAgent a) {

        if (pollution > 0) {
            pollution -= a.getWorkRate();
            return true;
        } else {
            ArrayList<Home> homes = envMap.getHomes();
            return false;
        }
    }

    public synchronized double getPollution() {
        return pollution;
    }

    public synchronized void setAttracting(boolean attracting) {
        this.attracting = attracting;
    }

    public synchronized Coordinate getCoords() {
        return coords;
    }
}
