package model.testing;

import model.agents.Agent;
import model.map.Home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Statistic implements Aggregable, Serializable {

    String iterationNumber;
    int takenTime;
    int[] lifeTimes;
    int[] spawnTimes;
    ArrayList<Double> totalPollution;
    int timeSearching;
    int timeTravelling;
    int timeWorking;
    double[] homeSizes;
    double[] workDone;

    public Statistic(String iterationNumber) {
        this.iterationNumber = iterationNumber;
        totalPollution = new ArrayList<>();
    }

    public void aggregate(int iters, ArrayList<Agent> agents, HashSet<Home> homes) {

        takenTime = iters;
        lifeTimes = new int[homes.size()];
        workDone = new double[agents.size()];
        spawnTimes = new int[homes.size()];
        homeSizes = new double[homes.size()];

        aggregateHomes(homes);
        aggregateAgents(agents);

    }

    private void aggregateHomes(HashSet<Home> homes){

        int i = 0;
        for(Home h : homes) {
            spawnTimes[i] = h.getSpawnTime();
            lifeTimes[i] = h.getLifeTime();
            homeSizes[i] = h.getPollution();
            ++i;
        }

    }

    public double getTotalWorkDone() {

        return Arrays.stream(workDone).sum();

    }
    private void aggregateAgents(ArrayList<Agent> agents) {

        int i = 0;
        for(Agent a : agents) {
            workDone[i] = a.getTotalWork();
            ++i;
        }

    }

    public double getAverageLifeTime() {

        return Arrays.stream(lifeTimes).average().orElse(Double.NaN);

    }

    public double getAverageWorkDone() {

        return Arrays.stream(workDone).average().orElse(Double.NaN);

    }

    public double getMedianWorkDone() {

        Arrays.sort(workDone);
        int len = workDone.length;

        return len % 2  == 0 ?  (workDone[len/2] + workDone[len/2 - 1])/2 : workDone[len/2];

    }

    public double getMaxWorkDone() {

        return Arrays.stream(workDone).max().orElse(Double.NaN);

    }

    public double getMinimumWorkDone() {

        return Arrays.stream(workDone).min().orElse(Double.NaN);

    }

    public int getTakenTime(){
        return takenTime;
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
