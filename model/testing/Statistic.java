package model.testing;

import model.agents.BatAgent;
import model.map.Home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Statistic implements Aggregable, Serializable {

    private String iterationNumber;
    private int takenTime;
    private int[] lifeTimes;
    private ArrayList<Double> totalPollution;
    private int timeSearching;
    private int timeTravelling;
    private int timeWorking;
    private double[] homeSizes;
    private double[] workDone;

    public Statistic(String iterationNumber) {
        this.iterationNumber = iterationNumber;
        totalPollution = new ArrayList<>();
    }

    public void aggregate(int iters, ArrayList<BatAgent> agents, HashSet<Home> homes) {

        takenTime = iters;
        lifeTimes = new int[homes.size()];
        homeSizes = new double[homes.size()];
        workDone  = new double[agents.size()];

        aggregateHomes(homes);
        aggregateAgents(agents);

    }

    private void aggregateHomes(HashSet<Home> homes){

        int i = 0;
        for(Home h : homes) {
            lifeTimes[i] = h.getLifeTime();
            homeSizes[i] = h.getPollution();
            ++i;
        }

    }

    public double getTotalWorkDone() {

        return Arrays.stream(workDone).sum();

    }
    private void aggregateAgents(ArrayList<BatAgent> agents) {

        int i = 0;
        for(BatAgent a : agents) {
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

    public String exportData(String agentName, String mapName, String hearingDistance, int maxTakenTime) {
        StringBuilder line = new StringBuilder();
        String timeTaken = String.valueOf(takenTime);
        String numAgents = String.valueOf(workDone.length);
        line.append(agentName).append(",");
        line.append(mapName).append(",");
        line.append(numAgents).append(",");
        line.append(timeTaken).append(",");
        line.append(timeSearching).append(",");
        line.append(timeTravelling).append(",");
        line.append(timeWorking).append(",");
        line.append(hearingDistance).append(",");
        for(double work : workDone) {
            line.append("\"").append(String.format("%2f", work)).append("\"").append(",");
        }
        for(int i = 0; i < maxTakenTime; i++){
            double pollution = 0;
            try {
                pollution = totalPollution.get(i);
            }
            catch(IndexOutOfBoundsException e){
                pollution = 0;
            }
            finally{
                line.append("\"").append(String.format("%2f", pollution)).append("\"").append(",");
            }
        }
        line.deleteCharAt(line.length()-1);
        line.append("\n");
        return new String(line);
    }

    @Override
    public String toString(){
        return this.iterationNumber;
    }
}
