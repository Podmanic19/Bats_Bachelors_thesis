package model.testing;

import model.agents.Agent;
import model.map.Home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Statistic implements Aggregable, Serializable {

    String iterationNumber;
    int numSeconds;
    int[] lifeTimes;
    int[] spawnTimes;
    ArrayList<Double> totalPollution;
    int timeSearching;
    int timeTravelling;
    int timeWorking;
    double[] homeSizes;
    double[] workDone;

    public Statistic(){
        totalPollution = new ArrayList<>();
    }

    public void aggregate(int iters, ArrayList<Agent> agents, ArrayList<Home> homes) {

        numSeconds = iters;
        lifeTimes = new int[homes.size()];
        workDone = new double[agents.size()];
        spawnTimes = new int[homes.size()];
        homeSizes = new double[homes.size()];

        aggregateHomes(homes);

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

    public int getNumSeconds(){
        return numSeconds;
    }

    public void updatePollution(ArrayList<Home> homes){

        double sum = 0;
        for(Home h : homes){
            sum += h.getPollution();
        }
        this.totalPollution.add(sum);

    }

    public void updateTimeInState(Integer[] numInState) {
        timeSearching += numInState[0];
        timeTravelling += numInState[1];
        timeWorking += numInState[2];
    }

    public int[] getLifeTimes() {
        return lifeTimes;
    }

    public int[] getSpawnTimes() {
        return spawnTimes;
    }

    public ArrayList<Double> getTotalPollution() {
        return totalPollution;
    }

    public int getTimeSearching() {
        return timeSearching;
    }

    public int getTimeTravelling() {
        return timeTravelling;
    }

    public int getTimeWorking() {
        return timeWorking;
    }

    public double[] getHomeSizes() {
        return homeSizes;
    }

    public double[] getWorkDone() {
        return workDone;
    }

    public String getIterationNumber() {
        return iterationNumber;
    }

    @Override
    public String toString(){
        return this.iterationNumber;
    }
}
