package model.main.testing;


import model.agents.BatAgent;
import model.map.Home;

import java.util.ArrayList;

public class MapResult implements Aggregable{

    private ArrayList<Statistic> iterations;

    public MapResult(){
        iterations = new ArrayList<>();
    }

    public void update(int iters, ArrayList<BatAgent> agents, ArrayList<Home> homes) {

        iterations.add(new Statistic(iters, agents, homes));

    }

    @Override
    public double getAverageWorkDone() {
        return 0;
    }

    @Override
    public double getBestAgentWork() {
        return 0;
    }

    @Override
    public double getWorstAgentWork() {
        return 0;
    }

    @Override
    public int getNumIters() {
        return 0;
    }
}
