package model.testing;

import java.io.Serializable;
import java.util.ArrayList;

public class AgentResult implements Aggregable, Serializable {

    private String agentType;
    ArrayList<MapResult> mapResults = new ArrayList<>();

    public AgentResult(String agentType) {
        this.agentType = agentType;
    }

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
    public int getNumSeconds() {
        return 0;
    }

    public ArrayList<MapResult> getMapResults() {
        return mapResults;
    }

    public String getAgentType() {
        return agentType;
    }

    @Override
    public String toString(){
        return this.agentType;
    }

}
