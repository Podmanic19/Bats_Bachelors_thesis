package model.main.testing;

import model.agents.AgentParams;
import model.agents.BatAgent;
import model.map.Home;
import model.map.Map;
import model.map.MapParameters;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static model.main.Main.envMap;
import static model.main.Main.envparams;

public class Test implements Serializable {

    private String name;
    private MapParameters mapparams;
    private ArrayList<AgentParams> agentparams;
    private ArrayList<Map> maps;
    private ArrayList<Home> allHomes;
    private int numMaps;
    private int numAgents;
    private int runTime;
    private int itersPerMap;

    private void setNameAsDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        this.name = "TEST " + dtf.format(now);
    }

    public TestResult runTest(){

        TestResult result = new TestResult();
        Instant start = Instant.now();

        for(AgentParams currentAgentParams : agentparams) {
            AgentResult agentResult = new AgentResult();
            for (Map m : maps) {
                MapResult mapResult = new MapResult();
                replaceAgents(m, currentAgentParams);
                for (int i = 0; i < itersPerMap; i++) {
                    Map current = new Map(m);
                    int j = -1;
                    while (!terminate(current, ++j)) {
                        envMap.getAgents().parallelStream().forEach(BatAgent::act);
                        envMap.getHomes().removeIf(h -> (h.getPollution() <= 0));
                        for (Home h : envMap.getHomes()) {
                            h.incrementLifetime();
                            h.increasePollution(envparams.DYNAMIC_HOME_GROWTH_SIZE);
                        }
                        if (envparams.DYNAMIC_HOME_CREATION && j % envparams.DYNAMIC_HOME_SPAWN_TIME == 0)
                            envMap.addHome(j);
                    }
                    mapResult.update(j, current.getAgents(), current.getHomes());
                }
                agentResult.update(mapResult);
            }
            result.update(agentResult);
        }

        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));

        return result;

    }

    private boolean terminate(Map current, int i){
        if(runTime > 0 && i >= runTime) return true;
        return current.getHomes().isEmpty();
    }

    public void setName(String name) {
        this.name = name;
    }

    private void replaceAgents(Map m, AgentParams params) {

        for(BatAgent a : m.getAgents()){
            a.remake(params);
        }

    }

}
