package model.map.mapshell;

import model.agents.Agent;
import model.agents.BatAgent;
import model.agents.ExplorerAgent;
import model.map.Coordinate;
import model.map.Home;
import model.map.LineSegment;
import model.map.Vector;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static model.main.Main.mapparams;

public class Map implements Serializable{

    private final ArrayList<LineSegment> walls;     //initial walls existing on the map
    private final ArrayList<Agent> agents;          //agents existing on the map
    private ArrayList<Home> homes;               //homes existing on the map

    public Map(MapShell shell, boolean bats, int numAgents, int numHomes) {
        this.agents = new ArrayList<>();
        this.homes = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.walls.addAll(shell.getWalls());

        if(bats) fillWithBats(numAgents, shell.getInitialAgentPositions());
        else fillWithExplorers(numAgents, shell.getInitialAgentPositions());

        fillWithHomes(numHomes, shell.getInitialHomePositions(), shell.getInitialPollutions());
        for(Agent a : agents) a.giveMap(this);
    }

    private void fillWithHomes(int numHomes, Coordinate[] positions, double[] pollutions) {

        for(int i = 0; i < numHomes; i++) {
            homes.add(new Home(Home.ID++, pollutions[i], 0, positions[i]));
        }

    }

    private void fillWithExplorers(int numAgents, Coordinate[] positions) {

        for(int i = 0; i < numAgents; i++) {
            ExplorerAgent e = new ExplorerAgent(Agent.ID++, positions[i]);
            agents.add(e);
            e.setHorizontalDirection(new model.map.Vector(1,0));
            e.setDirection(new model.map.Vector(0,1));
            e.setVerticalDirection(new Vector(0,1));
        }


    }

    private void fillWithBats(int numAgents, Coordinate[] positions) {

        for(int i = 0; i < numAgents; i++) {
            agents.add(new BatAgent(Agent.ID++, positions[i]));
        }

    }

    public void addHome(int spawn_time){

        Coordinate c = new Coordinate(
                ThreadLocalRandom.current().nextInt(mapparams.POINT_MIN, mapparams.POINT_MAX + 1),
                ThreadLocalRandom.current().nextInt(mapparams.POINT_MIN, mapparams.POINT_MAX + 1)
        );

        while(liesOnWall(c) || alreadyHome(c)){
                c.setX(ThreadLocalRandom.current().nextInt(mapparams.POINT_MIN, mapparams.POINT_MAX + 1));
                c.setY(ThreadLocalRandom.current().nextInt(mapparams.POINT_MIN, mapparams.POINT_MAX + 1));
        }

        createHome(c,Home.ID, spawn_time);

    }

    private boolean alreadyHome(Coordinate c){
        for(Home h : homes){
            if(c.getX() == h.getCoords().getX() && c.getY() == h.getCoords().getY()) return true;
        }
        return false;
    }

    private boolean liesOnWall(Coordinate c){
        for(LineSegment w: walls){
            if(w.liesOnLine(c)) return true;
        }
        return false;
    }

    private void createHome(Coordinate coord, int id, int spawn_time) {

        int workNeeded = ThreadLocalRandom.current().nextInt(mapparams.MIN_WORK, mapparams.MAX_WORK + 1);
        homes.add(new Home(id, workNeeded, spawn_time, coord));

    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public ArrayList<LineSegment> getWalls() {
        return walls;
    }

    public ArrayList<Home> getHomes() {
        return homes;
    }

    public Integer[] getAgentsInState() {

        Integer[] numInState = new Integer[3];

        for(int i = 0; i < 3; i++) numInState[i] = 0;

        for (Agent a : agents) {
            switch (a.getState()) {
                case searching:
                    numInState[0]++;
                    break;
                case traveling:
                    numInState[1]++;
                    break;
                case working:
                    numInState[2]++;
                    break;
            }
        }

        return numInState;

    }

}
