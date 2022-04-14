package model.testing;

import model.agents.AgentParams;
import model.map.mapshell.MapShell;

import java.util.ArrayList;

public class TestParams {
    private ArrayList<AgentParams> agentparams;
    private ArrayList<MapShell> uninitializedMaps;
    private EnvironmentParameters envparams;
    private int numAgents;
    private int numHomes;
    private int runTime;
    private int itersPerMap;
    private boolean singleStart;

    public ArrayList<AgentParams> getAgentparams() {
        return agentparams;
    }

    public ArrayList<MapShell> getUninitializedMaps() {
        return uninitializedMaps;
    }

    public EnvironmentParameters getEnvparams() {
        return envparams;
    }



    public int getNumAgents() {
        return numAgents;
    }

    public int getNumHomes() {
        return numHomes;
    }

    public int getRunTime() {
        return runTime;
    }

    public int getItersPerMap() {
        return itersPerMap;
    }

    public boolean isSingleStart() {
        return singleStart;
    }

    public void setAgentparams(ArrayList<AgentParams> agentparams) {
        this.agentparams = agentparams;
    }

    public void setUninitializedMaps(ArrayList<MapShell> uninitializedMaps) {
        this.uninitializedMaps = uninitializedMaps;
    }

    public void setEnvparams(EnvironmentParameters envparams) {
        this.envparams = envparams;
    }

    public void setNumAgents(int numAgents) {
        this.numAgents = numAgents;
    }

    public void setNumHomes(int numHomes) {
        this.numHomes = numHomes;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public void setItersPerMap(int itersPerMap) {
        this.itersPerMap = itersPerMap;
    }

    public void setSingleStart(boolean singleStart) {
        this.singleStart = singleStart;
    }
}
