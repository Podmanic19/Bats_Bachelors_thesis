package model.main.testing;

public interface Aggregable {

    double[] getAverageTimeInState();
    double getAverageWorkDone();
    double getBestAgentWork();
    double getWorstAgentWork();
    int getNumIters();

}
