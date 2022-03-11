package model.testing;

import model.agents.BatAgent;
import model.map.Home;

import java.util.ArrayList;
import java.util.Arrays;

public class Statistic implements Aggregable {

    int numIters;
    int[][] totalTimeInState;
    int[] lifeTimes;
    int[] spawnTimes;
    ArrayList<Double> totalPollution;
    double[] homeSizes;
    double[] workDone;

    public Statistic(){
        totalPollution = new ArrayList<>();
    }

    public void aggregate(int iters, ArrayList<BatAgent> agents, ArrayList<Home> homes) {

        numIters = iters;
        totalPollution = new ArrayList<>();
        totalTimeInState = new int[agents.size()][3];
        lifeTimes = new int[homes.size()];
        workDone = new double[agents.size()];
        spawnTimes = new int[homes.size()];
        homeSizes = new double[homes.size()];

        new Thread(() -> {
            aggregateAgents(agents);
        }).start();

        new Thread(() -> {
            aggregateHomes(homes);
        }).start();

    }

    private void aggregateHomes(ArrayList<Home> homes){

        int i = 0;
        for(Home h : homes) {
            spawnTimes[i] = h.getSpawnTime();
            lifeTimes[i] = h.getLifeTime();
            homeSizes[i] = h.getPollution();
            ++i;
        }

    }

    private void aggregateAgents(ArrayList<BatAgent> agents) {

        int i = 0;
        for(BatAgent a : agents) {
            workDone[i] = a.getTotalWork();
            for(int j = 0; j < 3; j++) {
                totalTimeInState[i][j] = a.getTimeSpentInState()[j];
            }
            ++i;
        }

    }

    public double getAverageLifeTime() {

        return Arrays.stream(lifeTimes).average().orElse(Double.NaN);

    }

    public double getAverageSpawnTimes() {

        return Arrays.stream(spawnTimes).average().orElse(Double.NaN);


    }

    public double getAverageHomeSize() {

        return Arrays.stream(homeSizes).average().orElse(Double.NaN);

    }

    public double getAverageWorkDone() {

        return Arrays.stream(workDone).average().orElse(Double.NaN);

    }

    public double getBestAgentWork() {

        return Arrays.stream(workDone).max().orElse(Double.NaN);

    }

    public double getWorstAgentWork() {

        return Arrays.stream(workDone).min().orElse(Double.NaN);

    }

    public int getNumIters(){
        return numIters;
    }

    public void updatePollution(ArrayList<Home> homes){

        double sum = 0;
        for(Home h : homes){
            sum += h.getPollution();
        }
        this.totalPollution.add(sum);
    }

}
