package model.testing;

import java.io.*;
import java.util.ArrayList;

public class TestResult implements Aggregable, Serializable {

    ArrayList<AgentResult> agentResults = new ArrayList<>();

    public void update(AgentResult a) {

        agentResults.add(a);

    }

    public void save(File f) throws IOException {

        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
        fos.close();

    }

    @Override
    public double getAverageWorkDone() {
        return 0;
    }

    @Override
    public double getMaxWorkDone() {
        return 0;
    }

    @Override
    public double getMinimumWorkDone() {
        return 0;
    }

    @Override
    public int getTakenTime() {
        return 0;
    }

    public ArrayList<AgentResult> getAgentResults() {
        return agentResults;
    }
}
