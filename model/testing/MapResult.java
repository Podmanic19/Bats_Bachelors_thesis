package model.testing;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Float.NaN;

public class MapResult implements Aggregable, Serializable {

    private String mapName;
    private int[] timesSpent;
    private double[] workDone;
    private ArrayList<Statistic> iterations;

    public MapResult(String mapName){
        this.mapName = mapName;
        iterations = new ArrayList<>();
    }

    public void update(Statistic s) {

        iterations.add(s);

    }

    public void aggregate() {
        this.timesSpent = new int[iterations.size()];
        this.workDone = new double[iterations.size()];
        int i = 0;

        for(Statistic s: iterations) {
            timesSpent[i] = s.takenTime;
            workDone[i++] = s.getTotalWorkDone();
        }
    }

    public int getTotalTimeSpent() {

        return Arrays.stream(timesSpent).sum();

    }

    public double getMedianTimeSpent() {

        int len = iterations.size();
        Arrays.sort(timesSpent);

        return len % 2  == 0 ?  (double) (timesSpent[len/2] + timesSpent[len/2 - 1])/2 : timesSpent[len/2];

    }

    public double getAverageTimeSpent() {
        return Arrays.stream(timesSpent).average().orElse(NaN);
    }

    public double getTotalWorkDone() {
        return Arrays.stream(workDone).sum();
    }


    public int getMaximumTimeSpent() {
        return Arrays.stream(timesSpent).max().orElse(0);
    }

    public int getMinimumTimeSpent() {
        return Arrays.stream(timesSpent).min().orElse(0);
    }

    public double getMedianWorkDone() {
        int len = iterations.size();
        Arrays.sort(timesSpent);

        return len % 2  == 0 ?  (workDone[len/2] + workDone[len/2 - 1])/2 : workDone[len/2];
    }

    @Override
    public double getAverageWorkDone() {
        return Arrays.stream(workDone).average().orElse(0);
    }

    @Override
    public double getMaxWorkDone() {
        return Arrays.stream(workDone).max().orElse(0);
    }

    @Override
    public double getMinimumWorkDone() {
        return Arrays.stream(workDone).min().orElse(0);
    }

    @Override
    public int getTakenTime() {
        return 0;
    }

    public ArrayList<Statistic> getIterations() {
        return iterations;
    }

    public String getMapName() {
        return mapName;
    }

    @Override
    public String toString(){
        return this.mapName;
    }
}
