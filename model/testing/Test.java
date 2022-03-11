package model.testing;

import model.agents.AgentParams;
import model.agents.BatAgent;
import model.main.Main;
import model.map.Home;
import model.map.Map;
import model.map.MapParameters;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static model.main.Main.loadedMap;

public class Test implements Serializable {

    private String name;
    private MapParameters mapparams;
    private EnvironmentParameters envparams;
    private ArrayList<AgentParams> agentparams;
    private ArrayList<Map> maps;
    private ArrayList<Home> allHomes;
    private int numMaps;
    private int numAgents;
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

    public TestResult run(){

        TestResult result = new TestResult();
        Instant start = Instant.now();
        int agentIter = -1, mapIter, currentIter;

        for(AgentParams currentAgentParams : agentparams) {
            ++agentIter;
            mapIter = -1;
            AgentResult agentResult = new AgentResult();
            for (Map m : maps) {
                ++mapIter;
                currentIter = -1;
                MapResult mapResult = new MapResult();
                replaceAgents(m, currentAgentParams);
                for (int i = 0; i < itersPerMap; i++) {
                    ++currentIter;
                    loadedMap = new Map(m);
                    int j = -1;
                    Statistic s = new Statistic();
                    Instant iterStart = Instant.now();
                    while (!terminate(loadedMap, ++j)) {
                        loadedMap.getAgents().parallelStream().forEach(BatAgent::act);
                        loadedMap.getHomes().removeIf(h -> (h.getPollution() <= 0));
                        for (Home h : loadedMap.getHomes()) {
                            h.incrementLifetime();
                            h.increasePollution(Main.envparams.DYNAMIC_HOME_GROWTH_SIZE);
                        }
                        if (Main.envparams.DYNAMIC_HOME_SPAWN_TIME > 0 && j % Main.envparams.DYNAMIC_HOME_SPAWN_TIME == 0)
                            loadedMap.addHome(j);
                        s.updatePollution(loadedMap.getHomes());
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

        Instant end = Instant.now();
        System.out.println("Total runtime: " + Duration.between(start, end));

        return result;

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

    private void replaceAgents(Map m, AgentParams params) {

        for(BatAgent a : m.getAgents()){
            a.remake(params);
        }

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

    public void setAllHomes(ArrayList<Home> allHomes) {
        this.allHomes = allHomes;
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
}
