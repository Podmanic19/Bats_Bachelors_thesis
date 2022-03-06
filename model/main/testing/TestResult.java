package model.main.testing;

import java.util.ArrayList;

public class TestResult implements Aggregable {

    ArrayList<AgentResult> agentResults = new ArrayList<>();

    public void update(AgentResult a) {

        agentResults.add(a);

    }

    @Override
    public double[] getAverageTimeInState() {
        return new double[0];
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
