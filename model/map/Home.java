package model.map;

import model.agents.BatAgent;
import model.serialization.Save;

import java.util.ArrayList;

import static model.main.Main.*;

public class Home extends Save {
    public static int ID = 1;
    private final int id;
    private int spawnTime = 0;
    private int lifeTime = 0;
    private final Coordinate coords;
    private ArrayList<BatAgent> agents = new ArrayList<>();
    private double pollution;
    private double attraction_distance = envparams.ATTRACTION_DISTANCE;
    private CallType call;

    public Home(int id, double pollution, int spawn_time, Coordinate coords) {
        this.id = id;
        this.spawnTime = spawn_time;
        this.pollution = pollution;
        this.coords = coords;
        this.call = CallType.NONE;
    }

    public synchronized CallType getCall() {
        return call;
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
