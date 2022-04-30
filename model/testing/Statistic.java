package model.testing;

import model.agents.BatAgent;
import model.map.Home;

import java.io.Serializable;
import java.util.ArrayList;

public class Statistic implements Serializable {

    private String agentName;
    private String mapName;
    private int numAgents;
    private int takenTime;
    private int timeSearching;
    private int timeTravelling;
    private int timeWorking;
    private int hearingDistance;
    private double[] workDone;
    private ArrayList<Double> totalPollution;

    public Statistic(String agentName, String mapName, int numAgents, int hearingDistance) {
        this.agentName = agentName;
        this.mapName = mapName;
        this.numAgents = numAgents;
        this.hearingDistance = hearingDistance;
        this.totalPollution = new ArrayList<>();
    }

    public void aggregate(int iters, ArrayList<BatAgent> agents) {

        takenTime = iters;
        workDone  = new double[agents.size()];

        aggregateAgents(agents);

    }

    private void aggregateAgents(ArrayList<BatAgent> agents) {

        int i = 0;
        for(BatAgent a : agents) {
            workDone[i] = a.getTotalWork();
            ++i;
        }

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

    public int getTakenTime() {
        return takenTime;
    }

    public String exportData(int maxTakenTime) {
        StringBuilder line = new StringBuilder();
        line.append(agentName).append(",");
        line.append(mapName).append(",");
        line.append(hearingDistance).append(",");
        line.append(numAgents).append(",");
        line.append(takenTime).append(",");
        line.append(timeSearching).append(",");
        line.append(timeTravelling).append(",");
        line.append(timeWorking).append(",");

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

}
