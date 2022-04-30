package model.map.mapshell;

import model.agents.BatAgent;
import model.agents.AgentParams;
import model.map.Coordinate;
import model.map.Home;
import model.map.LineSegment;

import java.io.*;
import java.util.*;

import static model.main.Main.mapparams;

public class Map implements Serializable{

    private final ArrayList<LineSegment> walls;     //initial walls existing on the map
    private final ArrayList<BatAgent> agents;          //agents existing on the map
    private ArrayList<Home> homes;               //homes existing on the map
    private Stack<Coordinate> homes_to_add;

    public Map(MapShell shell, AgentParams ap, int numAgents, boolean singleStart) {
        this.agents = new ArrayList<>();
        this.homes = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.homes_to_add = new Stack<>();

        for(Coordinate c : shell.getFutureHomePositions()) {
            homes_to_add.push(c);
        }

        this.walls.addAll(shell.getWalls());

        fillWithBats(numAgents, shell.getInitialAgentPositions(), ap, singleStart);

        fillWithHomes(shell.getInitialHomePositions(), shell.getInitialPollutions());
        for(BatAgent a : agents) a.giveMap(this);
    }

    private void fillWithHomes(Coordinate[] positions, double[] pollutions) {

        for(int i = 0; i < positions.length; i++) {
            homes.add(new Home(pollutions[i], positions[i]));
        }

    }

    private void fillWithBats(int numAgents, Coordinate[] positions, AgentParams ap, boolean singleStart) {

        for(int i = 0; i < numAgents; i++) {
            agents.add(new BatAgent(positions[singleStart ? 0 : i], ap));
        }

    }

    public void addHome(){

        double pollution = (double)(mapparams.MIN_WORK + mapparams.MAX_WORK) / 2;
        homes.add(new Home(pollution, homes_to_add.pop()));

    }

    public ArrayList<BatAgent> getAgents() {
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

        for (BatAgent a : agents) {
            switch (a.getState()) {
                case searching:
                    numInState[0]++;
                    break;
                case travelling:
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
