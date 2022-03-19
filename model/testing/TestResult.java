package model.testing;

import java.io.Serializable;
import java.util.ArrayList;

public class TestResult implements Aggregable, Serializable {

    ArrayList<AgentResult> agentResults = new ArrayList<>();

    public void update(AgentResult a) {

        agentResults.add(a);

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

    public ArrayList<AgentResult> getAgentResults() {
        return agentResults;
    }
}
