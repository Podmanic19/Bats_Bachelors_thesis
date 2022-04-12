package model.testing;

import model.agents.AgentParams;

import java.io.Serializable;
import java.util.ArrayList;

public class AgentResult implements Aggregable, Serializable {

    private AgentParams agentType;
    ArrayList<MapResult> mapResults = new ArrayList<>();

    public AgentResult(AgentParams agentType) {
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
    public double getMaxWorkDone() {
        return 0;
    }

    @Override
    public double getMinimumWorkDone() {
        return 0;
    }

    @Override
    public int getTakenTime() {
        return 0;
    }

    public ArrayList<MapResult> getMapResults() {
        return mapResults;
    }

    public AgentParams getAgentType() {
        return agentType;
    }

    @Override
    public String toString(){
        return this.agentType.NAME;
    }

}
