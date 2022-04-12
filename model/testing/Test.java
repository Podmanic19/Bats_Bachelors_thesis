package model.testing;

import controller.test.TestRunningController;
import javafx.application.Platform;
import model.agents.AgentParams;
import model.agents.BatAgent;
import model.main.Main;
import model.map.Home;
import model.map.mapshell.Map;
import model.map.mapshell.MapShell;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;


public class Test {

    private ArrayList<AgentParams> agentparams;
    private ArrayList<MapShell> uninitializedMaps;
    private EnvironmentParameters envparams;
    private int numAgents;
    private int numHomes;
    private int runTime;
    private int itersPerMap;
    private boolean singleStart;

    public Test() {

    }

    public void run(TestRunningController ctrlr){

        Main.envparams = this.envparams;
        TestResult result = new TestResult();
        Instant start = Instant.now();
        int agentIter = -1, mapIter, currentIter;

        //ITERATE OVER ALL AGENT TYPES
        for(AgentParams currentAgentParams : agentparams) {
            ++agentIter;
            int finalAgentIter = agentIter;
            Platform.runLater(() -> {
                ctrlr.getAgentLbl().setText(currentAgentParams.getNAME());
                ctrlr.getTotalProgressPb().setProgress((double) finalAgentIter /agentparams.size());
            });
            mapIter = -1;
            AgentResult agentResult = new AgentResult(currentAgentParams);
            //ITERATE OVER MAPS
            for (MapShell shell : uninitializedMaps) {
                ++mapIter;
                int finalMapIter = mapIter;
                Platform.runLater(() -> {
                    ctrlr.getMapLbl().setText(shell.getName());
                    ctrlr.getAgentProgressPb().setProgress((double) finalMapIter / uninitializedMaps.size());
                });
                currentIter = -1;
                MapResult mapResult = new MapResult(shell.getName());
                //TEST MAP NUMBER OF ITERATIONS PER MAP TIMES
                for (int i = 1; i <= itersPerMap; i++) {
                    ++currentIter;
                    int finalCurrentIter = currentIter;
                    Platform.runLater(() -> {
                        ctrlr.getIterLbl().setText(String.valueOf(finalCurrentIter));
                        ctrlr.getMapProgressPb().setProgress((double) finalCurrentIter / itersPerMap);
                    });
                    Map testedMap = new Map(shell, currentAgentParams, numAgents, singleStart);
                    int j = -1;
                    Statistic s = new Statistic(String.valueOf(i));
                    Instant iterStart = Instant.now();
                    HashSet<Home> allHomes = new HashSet<>();
                    for(Home h : testedMap.getHomes()) {
                        if(h.getPollution() >= 0) allHomes.add(h);
                    }
                    //SOLVE MAP
                    while (++j < runTime) {
                        testedMap.getAgents().parallelStream().forEach(BatAgent::act);
                        testedMap.getHomes().removeIf(h -> (h.getPollution() <= 0));
                        for (Home h : testedMap.getHomes()) {
                            h.incrementLifetime();
                            h.increasePollution(Main.envparams.DYNAMIC_HOME_GROWTH_SIZE);
                        }
                        if (Main.envparams.DYNAMIC_HOME_SPAWN_TIME > 0 && j % Main.envparams.DYNAMIC_HOME_SPAWN_TIME == 0)
                            testedMap.addHome();
                        s.updatePollution(testedMap.getHomes());
                        s.updateTimeInState(testedMap.getAgentsInState());
                    }
                    allHomes.addAll(testedMap.getHomes());
                    Instant iterEnd = Instant.now();
                    System.out.println("Iteration " + i + " runtime: " + Duration.between(iterStart, iterEnd) +
                            " iterations: " + j);
                    s.aggregate(j, testedMap.getAgents(), allHomes);
                    mapResult.update(s);
                }
                mapResult.aggregate();
                agentResult.update(mapResult);
            }
            result.update(agentResult);
        }

        Platform.runLater(() -> {
            ctrlr.getTotalProgressPb().setProgress(100);
            ctrlr.getAgentProgressPb().setProgress(100);
            ctrlr.getMapProgressPb().setProgress(100);
            ctrlr.getIterLbl().setText("100");
            ctrlr.getMapLbl().setText(uninitializedMaps.get(uninitializedMaps.size()-1).getName());
            ctrlr.getAgentLbl().setText(agentparams.get(agentparams.size()-1).getNAME());
        });

        Instant end = Instant.now();
        System.out.println("Total runtime: " + Duration.between(start, end));

        ctrlr.setTestResult(result);
        ctrlr.setShow(true);

    }

    public void setAgentparams(ArrayList<AgentParams> agentparams) {
        this.agentparams = agentparams;
    }

    public void setUninitializedMaps(ArrayList<MapShell> uninitializedMaps) {
        this.uninitializedMaps = uninitializedMaps;
    }

    public void setNumAgents(int numAgents) {
        this.numAgents = numAgents;
    }

    public void setItersPerMap(int itersPerMap) {
        this.itersPerMap = itersPerMap;
    }

    public void setNumHomes(int numHomes) {
        this.numHomes = numHomes;
    }

    public void setEnvparams(EnvironmentParameters envparams) {
        this.envparams = envparams;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public void setSingleStart(boolean singleStart) {
        this.singleStart = singleStart;
    }
}
