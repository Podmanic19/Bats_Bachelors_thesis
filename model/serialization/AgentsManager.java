package model.serialization;

import model.agents.AgentParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AgentsManager {

    private ArrayList<AgentParams> agents = new ArrayList<>();

    private void load() throws IOException {
        agents = new DatabaseAdmin<AgentParams>().download("agents" + System.getProperty("file.separator") + "agent_array");
    }

    public void update(AgentParams ap) {
        agents.add(ap);
    }

    public void updateAndSave(AgentParams ap) {
        update(ap);
        new DatabaseAdmin<AgentParams>().upload("agents" + System.getProperty("file.separator") + "agent_array", agents);
    }

    public void delete(AgentParams ap) {
        agents.remove(ap);
        new DatabaseAdmin<AgentParams>().upload("agents" + System.getProperty("file.separator") + "agent_array", agents);
    }

    public ArrayList<AgentParams> getAgents() {
        return agents;
    }

    public AgentsManager() throws IOException {
        load();
    }


}
