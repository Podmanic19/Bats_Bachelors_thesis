package model.map;

import model.agents.BatAgent;

import java.io.Serializable;
import java.util.ArrayList;

public class Home implements Serializable {
    public static int ID = 1;
    private final int id;
    private int lifeTime = 0;
    private final Coordinate coords;
    private ArrayList<BatAgent> agents = new ArrayList<>();
    private double pollution;
    private CallType call;

    public Home(int id, double pollution, Coordinate coords) {
        this.id = id;
        this.pollution = pollution;
        this.coords = coords;
        this.call = CallType.NONE;
    }

    public Home(Home h){
        this.id = h.id;
        this.pollution = h.pollution;
        this.coords = h.coords;
        this.call = h.call;
    }

    public synchronized CallType getCall() {
        return call;
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
        }

        return false;

    }

    public void incrementLifetime(){
        lifeTime++;
    }

    public void increasePollution(double pollution){
        this.pollution += pollution;
    }

    public synchronized double getPollution() {
        return pollution;
    }

    public synchronized void setCall(CallType call) {
        this.call = call;
    }

    public synchronized Coordinate getCoords() {
        return coords;
    }

    public int getLifeTime() {
        return lifeTime;
    }

}
