package model.serialization;

import model.agents.AgentParams;

import java.io.IOException;
import java.util.ArrayList;

public class AgentsManager {

    private ArrayList<AgentParams> agents = new ArrayList<>();

    private void load() throws IOException {
        agents = new DatabaseAdmin<AgentParams>().download("Agents\\agent_array");
    }

    public void update(AgentParams ap) {
        agents.add(ap);
    }

    public void updateAndSave(AgentParams ap) {
        update(ap);
        new DatabaseAdmin<AgentParams>().upload("Agents\\agent_array", agents);
    }

    public ArrayList<AgentParams> getAgents() {
        return agents;
    }

    public AgentsManager() throws IOException {
        load();
    }


}
