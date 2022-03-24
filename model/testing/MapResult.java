package model.testing;


import java.io.Serializable;
import java.util.ArrayList;

public class MapResult implements Aggregable, Serializable {

    private String mapName;
    private ArrayList<Statistic> iterations;

    public MapResult(){
        iterations = new ArrayList<>();
    }

    public void update(Statistic s) {

        iterations.add(s);

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
    public int getNumSeconds() {
        return 0;
    }

    public ArrayList<Statistic> getIterations() {
        return iterations;
    }

    public String getMapName() {
        return mapName;
    }

    @Override
    public String toString(){
        return this.mapName;
    }
}
