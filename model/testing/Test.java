package model.testing;

import controller.test.TestRunningController;
import javafx.application.Platform;
import model.agents.AgentParams;
import model.agents.Agent;
import model.main.Main;
import model.map.Home;
import model.map.Map;
import model.map.MapParameters;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static model.main.Main.loadedMap;

public class Test {

    private String name;
    private MapParameters mapparams;
    private EnvironmentParameters envparams;
    private ArrayList<AgentParams> agentparams;
    private ArrayList<Map> maps;
    private int numMaps;
    private int numAgents;
    private int numHomes;
    private int runTime;
    private int itersPerMap;


    public Test() {

    }

    public Test(String name, MapParameters mapparams, ArrayList<AgentParams> agentparams, ArrayList<Map> maps,
                EnvironmentParameters envparams, int numMaps, int numAgents, int itersPerMap) {
        this.name = name;
        this.mapparams = mapparams;
        this.envparams = envparams;
        this.agentparams = agentparams;
        this.maps = maps;
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

        for(AgentParams currentAgentParams : agentparams) {
            ++agentIter;
            int finalAgentIter = agentIter;
            Platform.runLater(() -> {
                ctrlr.getAgentLbl().setText(currentAgentParams.getNAME());
                ctrlr.getTotalProgressPb().setProgress((double) finalAgentIter /agentparams.size());
            });
            mapIter = -1;
            AgentResult agentResult = new AgentResult();
            for (Map m : maps) {
                ++mapIter;
                int finalMapIter = mapIter;
                Platform.runLater(() -> {
                    ctrlr.getMapLbl().setText(m.getName());
                    ctrlr.getAgentProgressPb().setProgress((double) finalMapIter /maps.size());
                });
                currentIter = -1;
                MapResult mapResult = new MapResult();
                for (int i = 1; i <= itersPerMap; i++) {
                    System.out.println("Number of agents: " + m.getAgents().size());
                    ++currentIter;
                    int finalCurrentIter = currentIter;
                    Platform.runLater(() -> {
                        ctrlr.getIterLbl().setText(String.valueOf(finalCurrentIter));
                        ctrlr.getMapProgressPb().setProgress((double) finalCurrentIter / itersPerMap);
                    });
                    loadedMap = new Map(m);
                    loadedMap.fillWithBats(this.numAgents);
                    int j = -1;
                    Statistic s = new Statistic();
                    Instant iterStart = Instant.now();
                    while (!terminate(loadedMap, ++j)) {
                        loadedMap.getAgents().parallelStream().forEach(Agent::act);
                        loadedMap.getHomes().removeIf(h -> (h.getPollution() <= 0));
                        for (Home h : loadedMap.getHomes()) {
                            h.incrementLifetime();
                            h.increasePollution(Main.envparams.DYNAMIC_HOME_GROWTH_SIZE);
                        }
                        if (Main.envparams.DYNAMIC_HOME_SPAWN_TIME > 0 && j % Main.envparams.DYNAMIC_HOME_SPAWN_TIME == 0)
                            loadedMap.addHome(j);
                        s.updatePollution(loadedMap.getHomes());
                        s.updateTimeInState(loadedMap.getAgentsInState());
                    }
                    Instant iterEnd = Instant.now();
                    System.out.println("Iteration " + (i+1) + " runtime: " + Duration.between(iterStart, iterEnd) +
                            " iterations: " + j);
                    s.aggregate(j, loadedMap.getAgents(), loadedMap.getHomes());
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
            ctrlr.getMapLbl().setText(maps.get(maps.size()-1).getName());
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

    public void setEnvparams(EnvironmentParameters envparams) {
        this.envparams = envparams;
    }

    public void setAgentparams(ArrayList<AgentParams> agentparams) {
        this.agentparams = agentparams;
    }

    public void setMaps(ArrayList<Map> maps) {
        this.maps = maps;
    }

    public void setNumMaps(int numMaps) {
        this.numMaps = numMaps;
    }

    public void setNumAgents(int numAgents) {
        this.numAgents = numAgents;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public void setItersPerMap(int itersPerMap) {
        this.itersPerMap = itersPerMap;
    }

    public void setNumHomes(int numHomes) {
        this.numHomes = numHomes;
    }
}
