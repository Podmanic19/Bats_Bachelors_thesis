package model.testing;

import controller.test.TestRunningController;
import javafx.application.Platform;
import model.agents.AgentParams;
import model.agents.Agent;
import model.main.Main;
import model.map.Home;
import model.map.mapshell.Map;
import model.map.MapParameters;
import model.map.mapshell.MapShell;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Test {

    private String name;
    private MapParameters mapparams;
    private EnvironmentParameters envparams;
    private ArrayList<AgentParams> agentparams;
    private ArrayList<MapShell> uninitializedMaps;
    private int numMaps;
    private int numAgents;
    private int numHomes;
    private int runTime;
    private int itersPerMap;

    public Test() {

    }

    public Test(String name, MapParameters mapparams, ArrayList<AgentParams> agentparams, ArrayList<MapShell> maps,
                EnvironmentParameters envparams, int numMaps, int numAgents, int itersPerMap) {
        this.name = name;
        this.mapparams = mapparams;
        this.envparams = envparams;
        this.agentparams = agentparams;
        this.uninitializedMaps = maps;
        this.numMaps = numMaps;
        this.numAgents = numAgents;
        this.itersPerMap = itersPerMap;
        this.runTime = 10000;
    }

    private void setNameAsDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.name = "TEST " + dtf.format(now);
    }

    public void run(TestRunningController ctrlr){

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
            AgentResult agentResult = new AgentResult(currentAgentParams.getNAME());
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
                    Map testedMap = new Map(shell, true, numAgents, numHomes);
                    int j = -1;
                    Statistic s = new Statistic(String.valueOf(i));
                    Instant iterStart = Instant.now();
                    //SOLVE MAP
                    while (!terminate(testedMap, ++j)) {
                        testedMap.getAgents().parallelStream().forEach(Agent::act);
                        testedMap.getHomes().removeIf(h -> (h.getPollution() <= 0));
                        for (Home h : testedMap.getHomes()) {
                            h.incrementLifetime();
                            h.increasePollution(Main.envparams.DYNAMIC_HOME_GROWTH_SIZE);
                        }
                        if (Main.envparams.DYNAMIC_HOME_SPAWN_TIME > 0 && j % Main.envparams.DYNAMIC_HOME_SPAWN_TIME == 0)
                            testedMap.addHome(j);
                        s.updatePollution(testedMap.getHomes());
                        s.updateTimeInState(testedMap.getAgentsInState());
                    }
                    Instant iterEnd = Instant.now();
                    System.out.println("Iteration " + i + " runtime: " + Duration.between(iterStart, iterEnd) +
                            " iterations: " + j);
                    s.aggregate(j, testedMap.getAgents(), testedMap.getHomes());
                    mapResult.update(s);
                }
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

    private boolean terminate(Map current, int i){
        if(runTime > 0) {
            return i >= runTime;
        }
        else return current.getHomes().isEmpty();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMapparams(MapParameters mapparams) {
        this.mapparams = mapparams;
    }

    public void setAgentparams(ArrayList<AgentParams> agentparams) {
        this.agentparams = agentparams;
    }

    public void setUninitializedMaps(ArrayList<MapShell> uninitializedMaps) {
        this.uninitializedMaps = uninitializedMaps;
    }

    public void setNumMaps(int numMaps) {
        this.numMaps = numMaps;
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
}
