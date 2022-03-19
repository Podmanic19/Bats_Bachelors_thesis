package model.testing;

import java.io.Serializable;
import java.util.ArrayList;

public class AgentResult implements Aggregable, Serializable {

    ArrayList<MapResult> mapResults = new ArrayList<>();

    public void update(MapResult m) {

        mapResults.add(m);

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

    public ArrayList<MapResult> getMapResults() {
        return mapResults;
    }
}
