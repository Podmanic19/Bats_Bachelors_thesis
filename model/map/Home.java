package model.map;

import model.agents.Agent;

import java.io.Serializable;
import java.util.ArrayList;

import static model.main.Main.*;

public class Home implements Serializable {
    public static int ID = 1;
    private final int id;
    private int spawnTime = 0;
    private int lifeTime = 0;
    private final Coordinate coords;
    private ArrayList<Agent> agents = new ArrayList<>();
    private double pollution;
    private double attraction_distance = mapparams.ATTRACTION_DISTANCE;
    private CallType call;

    public Home(int id, double pollution, int spawn_time, Coordinate coords) {
        this.id = id;
        this.spawnTime = spawn_time;
        this.pollution = pollution;
        this.coords = coords;
        this.call = CallType.NONE;
    }

    public Home(Home h){
        this.id = h.id;
        this.spawnTime = h.spawnTime;
        this.pollution = h.pollution;
        this.coords = h.coords;
        this.call = h.call;
    }

    public synchronized CallType getCall() {
        return call;
    }

    public double getAttraction_distance() {
        return attraction_distance;
    }

    public synchronized void addAgent(Agent a) {
        agents.add(a);
    }

    public int getId() {
        return id;
    }

    public synchronized ArrayList<Agent> getAgents() {
        return agents;
    }

    public synchronized boolean decreasePollution(Agent a) {

        if (pollution > 0) {
            pollution -= a.getWorkRate();
            return true;
        }

        return false;

    }

    public void incrementLifetime(){
        lifeTime++;
    }

    public void increasePollution(int pollution){
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

    public int getSpawnTime() {
        return spawnTime;
    }

    public int getLifeTime() {
        return lifeTime;
    }

}
